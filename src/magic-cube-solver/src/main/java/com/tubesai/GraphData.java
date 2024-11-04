package com.tubesai;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GraphData {
    private final Map<Integer, IterationStats> iterationData;
    private int currentIteration;
    private InnerGraphData bestValue;
    private boolean isSimulatedAnnealing;
    private double executionTime;
    private int localOptimumFrequency;

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

    /**
     * Adds data to the graph with the specified objective function value.
     *
     * @param objFuncValue the objective function value to be added to the graph
     */
    public void addData(int objFuncValue) {
        addData(objFuncValue, 0.0);
    }

    /**
     * Adds data for the current iteration, including the objective function value
     * and temperature.
     * Updates the best value if the new objective function value is better.
     * 
     * @param objFuncValue the objective function value to be added
     * @param temperature  the temperature value to be added
     */
    public void addData(int objFuncValue, double temperature) {
        InnerGraphData newData = new InnerGraphData(objFuncValue, currentIteration, temperature);

        if (bestValue == null || objFuncValue < bestValue.objFuncValue) {
            bestValue = newData;
        }

        IterationStats stats = iterationData.computeIfAbsent(currentIteration,
                k -> new IterationStats(isSimulatedAnnealing));

        stats.addValue(objFuncValue, temperature);
    }

    /**
     * Increments the current iteration count by one.
     */
    public void finishIteration() {
        currentIteration++;
    }

    /**
     * Retrieves the average objective function value for a given iteration.
     *
     * @param iteration the iteration number for which the average objective
     *                  function value is requested
     * @return the average objective function value for the specified iteration, or
     *         0.0 if the iteration data is not available
     */
    public double getAvgObjFuncValue(int iteration) {
        IterationStats stats = iterationData.get(iteration);
        return stats != null ? stats.getAverage() : 0.0;
    }

    /**
     * Retrieves the average temperature for a given iteration during the simulated
     * annealing process.
     *
     * @param iteration the iteration number for which the average temperature is
     *                  requested
     * @return the average temperature for the specified iteration if simulated
     *         annealing is enabled,
     *         otherwise returns 0.0
     */
    public double getAvgTemperature(int iteration) {
        if (!isSimulatedAnnealing)
            return 0.0;
        IterationStats stats = iterationData.get(iteration);
        return stats != null ? stats.getAverageTemperature() : 0.0;
    }

    /**
     * Retrieves the best objective function value.
     *
     * @return the best objective function value encapsulated in an InnerGraphData
     *         object.
     */
    public InnerGraphData getBestObjFuncValue() {
        return bestValue;
    }

    /**
     * Returns the current iteration number.
     *
     * @return the current iteration number.
     */
    public int getIteration() {
        return currentIteration;
    }

    /**
     * Retrieves all iteration data.
     *
     * @return a map containing all iteration statistics, where the key is the
     *         iteration number
     *         and the value is the corresponding {@link IterationStats} object.
     */
    public Map<Integer, IterationStats> getAllData() {
        return new HashMap<>(iterationData);
    }

    /**
     * Checks if the current algorithm being used is Simulated Annealing.
     *
     * @return true if Simulated Annealing is being used, false otherwise.
     */
    public boolean isSimulatedAnnealing() {
        return isSimulatedAnnealing;
    }

    /**
     * InnerGraphData is a static inner class that holds data related to graph
     * operations.
     * It contains information about the objective function value, iteration number,
     * and temperature.
     */
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

    /**
     * The IterationStats class is used to track statistical data over multiple
     * iterations.
     * It keeps track of the count, sum, minimum, and maximum values, as well as the
     * sum of temperatures if required.
     */
    static class IterationStats {
        private int count;
        private double sum;
        private int min;
        private int max;
        private double temperatureSum;
        private final boolean trackTemperature;

        public IterationStats(boolean trackTemperature) {
            this.count = 0;
            this.sum = 0;
            this.min = Integer.MAX_VALUE;
            this.max = Integer.MIN_VALUE;
            this.temperatureSum = 0;
            this.trackTemperature = trackTemperature;
        }

        /**
         * Adds a value to the graph data and updates the statistical metrics.
         *
         * @param value       the value to be added
         * @param temperature the temperature associated with the value, used if
         *                    tracking temperature
         */
        public void addValue(int value, double temperature) {
            count++;
            sum += value;
            min = Math.min(min, value);
            max = Math.max(max, value);

            if (trackTemperature) {
                temperatureSum += temperature;
            }
        }

        /**
         * Calculates the average value.
         *
         * @return the average value if count is greater than 0, otherwise returns 0.
         */
        public double getAverage() {
            return count > 0 ? sum / count : 0;
        }

        /**
         * Calculates the average temperature.
         *
         * @return the average temperature if the count is greater than 0 and
         *         trackTemperature is true,
         *         otherwise returns 0.
         */
        public double getAverageTemperature() {
            return count > 0 && trackTemperature ? temperatureSum / count : 0;
        }

        public int getMin() {
            return min;
        }

        public int getMax() {
            return max;
        }

        // Removed getValues() and getTemperatures() methods
    }

    /**
     * Creates a dummy GraphData object.
     *
     * @param isSimulatedAnnealing a boolean indicating whether to use simulated
     *                             annealing.
     * @return a dummy GraphData object.
     */
    public static GraphData createDummyData(boolean isSimulatedAnnealing) {
        return createDummyData(isSimulatedAnnealing, 50, 5);
    }

    /**
     * Creates dummy data for testing purposes, simulating the behavior of an
     * optimization algorithm.
     *
     * @param isSimulatedAnnealing A boolean indicating whether to simulate data for
     *                             Simulated Annealing (true) or another algorithm
     *                             (false).
     * @param iterations           The number of iterations to simulate.
     * @param samplesPerIteration  The number of samples to generate per iteration.
     * @return A GraphData object containing the generated dummy data.
     */
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

    /**
     * Creates a realistic dummy data set for magic cube optimization using either
     * Simulated Annealing (SA) or Hill Climbing.
     *
     * @param isSimulatedAnnealing A boolean flag indicating whether to use
     *                             Simulated Annealing (true) or Hill Climbing
     *                             (false).
     * @return A GraphData object containing the generated dummy data.
     *
     *         <p>
     *         The method generates data based on the following parameters:
     *         - Cube size: 3x3x3
     *         - Maximum possible error: cubeSize^3 * 100
     *         - Initial error rate: 70% of the maximum possible error
     *         - Iterations: 100
     *         - Samples per iteration: 8
     *
     *         <p>
     *         For Simulated Annealing:
     *         - Initial temperature: 10% of the maximum possible error
     *         - Final temperature: 0.1
     *         - Cooling rate: calculated based on the initial and final
     *         temperatures over the number of iterations
     *
     *         <p>
     *         The method simulates the optimization process by iterating through a
     *         number of iterations and samples per iteration.
     *         It calculates an error rate using a combination of exponential and
     *         logarithmic decay.
     *         Random variations and "lucky" improvements are added to simulate
     *         realistic optimization behavior.
     *
     *         <p>
     *         For Simulated Annealing, the acceptance probability is calculated
     *         based on the current temperature, and sometimes
     *         improvements are rejected based on this probability.
     *
     *         <p>
     *         For Hill Climbing, only improvements are accepted.
     *
     *         <p>
     *         The temperature is updated for each iteration in the case of
     *         Simulated Annealing.
     */
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

    /**
     * Sets the execution time for the graph data.
     *
     * @param executionTime the execution time to set, in seconds
     */
    public void setExecutionTime(double executionTime) {
        this.executionTime = executionTime;
    }

    /**
     * Returns the execution time.
     *
     * @return the execution time as a double.
     */
    public double getExecutionTime() {
        return executionTime;
    }

    /**
     * Sets the frequency of local optimum occurrences.
     *
     * @param localOptimumFrequency the frequency of local optimum occurrences to
     *                              set
     */
    public void setLocalOptimumFrequency(int localOptimumFrequency) {
        this.localOptimumFrequency = localOptimumFrequency;
    }

    /**
     * Returns the frequency of local optimum occurrences.
     *
     * @return the frequency of local optimum occurrences.
     */
    public int getLocalOptimumFrequency() {
        return localOptimumFrequency;
    }
}