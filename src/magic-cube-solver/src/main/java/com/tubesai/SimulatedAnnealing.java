package com.tubesai;

public class SimulatedAnnealing implements IAlgorithm {
    private double initialTemperature;
    private double coolingRate;

    public SimulatedAnnealing(double initialTemperature, double coolingRate) {
        this.initialTemperature = initialTemperature;
        this.coolingRate = coolingRate;
    }

    @Override
    public void solve(MagicCube cube) {
        double temperature = initialTemperature;
        while (temperature > 1) {
            int currentFitness = cube.evaluate();
            cube.swapRandomCells();
            int newFitness = cube.evaluate();
            if (newFitness < currentFitness || Math.random() < Math.exp((currentFitness - newFitness) / temperature)) {
                // Accept new state
            } else {
                // Revert to previous state
                cube.swapRandomCells();
            }
            temperature *= coolingRate;
        }
    }
}
