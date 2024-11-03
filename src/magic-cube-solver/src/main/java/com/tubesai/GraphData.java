package com.tubesai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GraphData {
    private final Map<Integer, IterationStats> iterationData;
    private int currentIteration;
    private InnerGraphData bestValue;
    private boolean isSimulatedAnnealing;

    public GraphData(boolean isSimulatedAnnealing) {
        this.iterationData = new HashMap<>();
        this.currentIteration = 0;
        this.bestValue = null;
        this.isSimulatedAnnealing = isSimulatedAnnealing;
    }

    public GraphData(GraphData other) {
        this.iterationData = new HashMap<>(other.iterationData);
        this.currentIteration = other.currentIteration;
        this.bestValue = other.bestValue != null ? new InnerGraphData(other.bestValue) : null;
        this.isSimulatedAnnealing = other.isSimulatedAnnealing;
    }

    public void addData(int objFuncValue) {
        addData(objFuncValue, 0.0);
    }

    public void addData(int objFuncValue, double temperature) {
        InnerGraphData newData = new InnerGraphData(objFuncValue, currentIteration, temperature);

        if (bestValue == null || objFuncValue < bestValue.objFuncValue) {
            bestValue = newData;
        }

        IterationStats stats = iterationData.computeIfAbsent(currentIteration,
                k -> new IterationStats(isSimulatedAnnealing));

        stats.addValue(objFuncValue, temperature);
    }

    public void finishIteration() {
        currentIteration++;
    }

    public double getAvgObjFuncValue(int iteration) {
        IterationStats stats = iterationData.get(iteration);
        return stats != null ? stats.getAverage() : 0.0;
    }

    public double getAvgTemperature(int iteration) {
        if (!isSimulatedAnnealing)
            return 0.0;
        IterationStats stats = iterationData.get(iteration);
        return stats != null ? stats.getAverageTemperature() : 0.0;
    }

    public InnerGraphData getBestObjFuncValue() {
        return bestValue;
    }

    public int getIteration() {
        return currentIteration;
    }

    public Map<Integer, IterationStats> getAllData() {
        return new HashMap<>(iterationData);
    }

    public boolean isSimulatedAnnealing() {
        return isSimulatedAnnealing;
    }

    public static class InnerGraphData {
        private final int objFuncValue;
        private final int iterationNum;
        private final double temperature;

        public InnerGraphData(int objFuncValue, int iterationNum) {
            this(objFuncValue, iterationNum, 0.0);
        }

        public InnerGraphData(int objFuncValue, int iterationNum, double temperature) {
            this.objFuncValue = objFuncValue;
            this.iterationNum = iterationNum;
            this.temperature = temperature;
        }

        protected InnerGraphData(InnerGraphData other) {
            this.objFuncValue = other.objFuncValue;
            this.iterationNum = other.iterationNum;
            this.temperature = other.temperature;
        }

        public int getObjFuncValue() {
            return objFuncValue;
        }

        public int getIterationNum() {
            return iterationNum;
        }

        public double getTemperature() {
            return temperature;
        }
    }

    static class IterationStats {
        private int count;
        private double sum;
        private int min;
        private int max;
        private final ArrayList<Integer> values;
        private double temperatureSum;
        private final ArrayList<Double> temperatures;
        private final boolean trackTemperature;

        public IterationStats(boolean trackTemperature) {
            this.count = 0;
            this.sum = 0;
            this.min = Integer.MAX_VALUE;
            this.max = Integer.MIN_VALUE;
            this.values = new ArrayList<>();
            this.temperatureSum = 0;
            this.temperatures = new ArrayList<>();
            this.trackTemperature = trackTemperature;
        }

        public void addValue(int value, double temperature) {
            count++;
            sum += value;
            min = Math.min(min, value);
            max = Math.max(max, value);
            values.add(value);

            if (trackTemperature) {
                temperatureSum += temperature;
                temperatures.add(temperature);
            }
        }

        public double getAverage() {
            return count > 0 ? sum / count : 0;
        }

        public double getAverageTemperature() {
            return count > 0 && trackTemperature ? temperatureSum / count : 0;
        }

        public int getMin() {
            return min;
        }

        public int getMax() {
            return max;
        }

        public ArrayList<Integer> getValues() {
            return new ArrayList<>(values);
        }

        public ArrayList<Double> getTemperatures() {
            return trackTemperature ? new ArrayList<>(temperatures) : new ArrayList<>();
        }
    }

    public static GraphData createDummyData(boolean isSimulatedAnnealing) {
        return createDummyData(isSimulatedAnnealing, 50, 5);
    }

    public static GraphData createDummyData(boolean isSimulatedAnnealing, int iterations, int samplesPerIteration) {
        GraphData dummyData = new GraphData(isSimulatedAnnealing);
        Random random = new Random();

        // Parameters for generating realistic-looking data
        int baseValue = 1000; // Starting objective function value
        double improvement = 0.95; // Improvement factor per iteration
        double noise = 0.1; // Noise factor for random variation

        // Simulated Annealing specific parameters
        double initialTemp = 100.0;
        double coolingRate = 0.95;
        double temperature = initialTemp;

        for (int iter = 0; iter < iterations; iter++) {
            // Calculate target value for this iteration (generally decreasing)
            double targetValue = baseValue * Math.pow(improvement, iter);

            for (int sample = 0; sample < samplesPerIteration; sample++) {
                // Add some random noise to the target value
                double noise_factor = 1.0 + (random.nextDouble() - 0.5) * noise;
                int objFuncValue = (int) (targetValue * noise_factor);

                // Ensure minimum value of 0
                objFuncValue = Math.max(0, objFuncValue);

                if (isSimulatedAnnealing) {
                    dummyData.addData(objFuncValue, temperature);
                } else {
                    dummyData.addData(objFuncValue);
                }
            }

            // Update temperature for SA
            if (isSimulatedAnnealing) {
                temperature *= coolingRate;
            }

            dummyData.finishIteration();
        }

        return dummyData;
    }

    public static GraphData createRealisticDummyData(boolean isSimulatedAnnealing) {
        GraphData dummyData = new GraphData(isSimulatedAnnealing);
        Random random = new Random();

        // More sophisticated parameters for realistic magic cube optimization
        int cubeSize = 3; // Assuming 3x3x3 cube
        int maxPossibleError = cubeSize * cubeSize * cubeSize * 100; // Maximum possible error
        double initialErrorRate = 0.7; // Start with 70% of max error
        int iterations = 100;
        int samplesPerIteration = 8;

        // SA parameters
        double initialTemp = maxPossibleError * 0.1; // Temperature scaled to problem size
        double finalTemp = 0.1;
        double temperature = initialTemp;
        double coolingRate = Math.pow(finalTemp / initialTemp, 1.0 / iterations);

        int initialError = (int) (maxPossibleError * initialErrorRate);

        for (int iter = 0; iter < iterations; iter++) {
            // Calculate expected improvement curve
            // Using a combination of exponential and logarithmic decay
            double progress = (double) iter / iterations;
            double exponentialFactor = Math.exp(-4 * progress);
            double logarithmicFactor = Math.log10(iter + 10) / Math.log10(iterations + 10);
            double errorRate = initialError * exponentialFactor * (1 - logarithmicFactor * 0.5);

            for (int sample = 0; sample < samplesPerIteration; sample++) {
                // Add realistic variation
                double variation = random.nextGaussian() * (errorRate * 0.1);
                int objFuncValue = (int) Math.max(0, errorRate + variation);

                // Sometimes add "lucky" improvements
                if (random.nextDouble() < 0.1) { // 10% chance
                    objFuncValue = (int) (objFuncValue * 0.8); // 20% better
                }

                if (isSimulatedAnnealing) {
                    // Calculate acceptance probability for this temperature
                    double acceptanceProb = Math.exp(-variation / temperature);
                    // Sometimes reject improvements based on temperature
                    if (acceptanceProb < random.nextDouble()) {
                        objFuncValue = (int) (objFuncValue * 1.1); // 10% worse
                    }
                    dummyData.addData(objFuncValue, temperature);
                } else {
                    // Hill Climbing only accepts improvements
                    dummyData.addData(objFuncValue);
                }
            }

            // Update temperature for SA
            if (isSimulatedAnnealing) {
                temperature *= coolingRate;
            }

            dummyData.finishIteration();
        }

        return dummyData;
    }
}