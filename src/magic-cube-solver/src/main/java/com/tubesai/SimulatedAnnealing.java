package com.tubesai;

public class SimulatedAnnealing implements IAlgorithm {
    private double initial_temperature;
    private double cooling_rate;

    public SimulatedAnnealing(double initial_temperature, double cooling_rate) {
        this.initial_temperature = initial_temperature;
        this.cooling_rate = cooling_rate;
    }

    @Override
    public void solve(MagicCube cube) {
        double temperature = initial_temperature;
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
            temperature *= cooling_rate;
        }
    }
}
