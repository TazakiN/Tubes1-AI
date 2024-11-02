package com.tubesai;

import java.util.Random;
// import java.math.BigDecimal;

public class SimulatedAnnealing implements IAlgorithm {
    private double initial_temperature;     // set to 100
    private double cooling_rate;            // set to 0.00001
    // private int counter = 0;

    public SimulatedAnnealing(double initial_temperature, double cooling_rate) {
        this.initial_temperature = initial_temperature;
        this.cooling_rate = cooling_rate;
    }

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

    @Override
    public MagicCube getSolvedCube(MagicCube cube) {
        // TODO: Fix this logic algorithm to matches the best technique to solve MagicCube (Liat catetan ucup)
        double temperature = initial_temperature;
        MagicCube currentCube = new MagicCube(cube);
        MagicCube bestCube = new MagicCube(cube);

        // int movedWorseNeigbourCount = 0;

        while (temperature > 1) {
            MagicCube neighbour = getRandomNeighbour(currentCube);

            int currentFitness = currentCube.getFitness();
            int neighbourFitness = neighbour.getFitness();

            if (acceptanceProbability(currentFitness, neighbourFitness, temperature) > 0.95) {
                if (!(neighbourFitness > currentFitness)) {
                    // movedWorseNeigbourCount++;
                }
                currentCube = new MagicCube(neighbour);
            }

            if (currentFitness > bestCube.getFitness()) {
                bestCube = new MagicCube(currentCube);
                // System.out.println("Best Fitness: " + bestCube.getFitness());
                // System.out.println("Temperature: " + temperature);
            }

            temperature *= 1 - cooling_rate;
        }

        return bestCube;
    }

    private double acceptanceProbability(int currentFitness, int neighbourFitness, double temperature) {
        if (neighbourFitness > currentFitness) {
            // counter++;
            return 1.0;
        }
        return Math.exp((neighbourFitness - currentFitness) / temperature);
    }
}
