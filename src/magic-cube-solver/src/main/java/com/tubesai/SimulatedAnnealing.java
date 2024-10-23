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
        // TODO: Implement Simulated Annealing algorithm
    }
}
