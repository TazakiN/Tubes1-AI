package com.tubesai;

import java.util.ArrayList;
import java.util.Random;
// import java.math.BigDecimal;

public class SimulatedAnnealing implements IAlgorithm {
    private double initial_temperature; // set to 10
    private double cooling_rate; // set to 0.000001
    private ArrayList<Double> probabilityHistory; // <index + 1> represents the iteration, <value> represents the
                                                  // probability
    // private int counter = 0;
    private GraphData graphData;

    public SimulatedAnnealing(double initial_temperature, double cooling_rate) {
        this.initial_temperature = initial_temperature;
        this.cooling_rate = cooling_rate;
        this.probabilityHistory = new ArrayList<Double>();
        this.graphData = new GraphData(true);
    }

    /**
     * Generates a random neighboring state of the given MagicCube by swapping two
     * random positions.
     *
     * @param cube the current state of the MagicCube
     * @return a new MagicCube instance with two elements swapped
     */
    public MagicCube getRandomNeighbour(MagicCube cube) {
        MagicCube neighbor = new MagicCube(cube);

        // Get random two Positions
        Random rand = new Random();
        int x1 = rand.nextInt(cube.getSize());
        int y1 = rand.nextInt(cube.getSize());
        int z1 = rand.nextInt(cube.getSize());
        Position el1 = new Position(x1, y1, z1);

        int x2 = rand.nextInt(cube.getSize());
        int y2 = rand.nextInt(cube.getSize());
        int z2 = rand.nextInt(cube.getSize());
        Position el2 = new Position(x2, y2, z2);

        // Swap the two elements
        neighbor.moveToNeighbour(el1, el2);
        return neighbor;
    }

    /**
     * Solves the given MagicCube using the Simulated Annealing algorithm. Resets
     * and updates the probabilityHistory attribute. With HEURISTIC to update the
     * best cube if the current cube has a better fitness.
     *
     * @param cube the initial MagicCube to be solved
     * @return the solved MagicCube with the best fitness found
     */
    @Override
    public MagicCube getSolvedCube(MagicCube cube) {
        double temperature = initial_temperature;
        MagicCube currentCube = new MagicCube(cube);
        MagicCube bestCube = new MagicCube(cube);
        int iteration = 0;

        probabilityHistory.clear();

        int localOptimumFrequency = 0;

        while (temperature > 1) {
            iteration++;

            MagicCube neighbour = getRandomNeighbour(currentCube);
            int currentFitness = currentCube.getFitness();
            int neighbourFitness = neighbour.getFitness();

            double accProbability = acceptanceProbability(currentFitness, neighbourFitness, temperature);

            graphData.addData(neighbourFitness, temperature);
            // probabilityHistory.add(accProbability);
            
            // Hitung frekuensi stuck local optimum
            if (!(neighbourFitness > currentFitness)) {
                localOptimumFrequency++;
            }

            if (accProbability > 0.95) {
                graphData.finishIteration();
                currentCube = new MagicCube(neighbour);
            }

            // HEURISTIC: Update the best cube if the current cube has a better fitness
            if (currentFitness > bestCube.getFitness()) {
                bestCube = new MagicCube(currentCube);
                // System.out.println("Best Fitness: " + bestCube.getFitness());
                // System.out.println("Temperature: " + temperature);
            }
            temperature *= 1 - cooling_rate;
        }

        System.out.println("Number of iterations: " + iteration);
        System.out.println("Local Optimum Stuck Frequency: " + localOptimumFrequency);
        return bestCube;
    }

    /**
     * Calculates the acceptance probability of moving to a neighboring solution
     * in the simulated annealing algorithm.
     *
     * @param currentFitness   the fitness value of the current solution
     * @param neighbourFitness the fitness value of the neighboring solution
     * @param temperature      the current temperature in the simulated annealing
     *                         process
     * @return the acceptance probability of moving to the neighboring solution
     */
    private double acceptanceProbability(int currentFitness, int neighbourFitness, double temperature) {
        if (neighbourFitness > currentFitness) {
            // counter++;
            return 1.0;
        }
        return Math.exp((neighbourFitness - currentFitness) / temperature);
    }

    /**
     * Retrieves the history of probabilities recorded during the simulated
     * annealing process.
     *
     * @return An ArrayList of Double values representing the probability history
     *         and the <index + 1> as the iteration
     */
    public ArrayList<Double> getProbabilityHistory() {
        return probabilityHistory;
    }

    @Override
    public GraphData getGraphData() {
        return graphData;
    }
}
