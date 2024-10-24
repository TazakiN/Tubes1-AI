package com.tubesai;

import java.util.Random;

public class SimulatedAnnealing implements IAlgorithm {
    private double initial_temperature;
    private double cooling_rate;

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
        // TODO: Fix this algorithm logic so it matchs the best technique to solve the magic cube
        double temperature = initial_temperature;
        MagicCube currentSolution = new MagicCube(cube);
        MagicCube bestSolution = new MagicCube(cube);

        while (temperature > 1) {
            MagicCube newSolution = getRandomNeighbour(currentSolution);

            int currentEnergy = currentSolution.getEnergy();
            int neighbourEnergy = newSolution.getEnergy();

            if (acceptanceProbability(currentEnergy, neighbourEnergy, temperature) > Math.random()) {
            currentSolution = newSolution;
            }

            if (currentSolution.getEnergy() < bestSolution.getEnergy()) {
            bestSolution = currentSolution;
            }

            temperature *= 1 - cooling_rate;
        }

        cube.copyFrom(bestSolution);
    }

    private double acceptanceProbability(int currentEnergy, int neighbourEnergy, double temperature) {
        if (neighbourEnergy < currentEnergy) {
            return 1.0;
        }
        return Math.exp((currentEnergy - neighbourEnergy) / temperature);
    }
}
