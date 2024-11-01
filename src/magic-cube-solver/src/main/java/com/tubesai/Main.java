package com.tubesai;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        // This just a test to see if the SimulatedAnnealing works
        MagicCube cube = new MagicCube(5);

        SimulatedAnnealing solver = new SimulatedAnnealing(1000000, 0.003);
        
        System.out.println("Starter Cube:");
        System.out.println("Fitness: " + cube.getFitness());
        cube.printCube();
        System.out.println();
        MagicCube solvedCube = solver.getSolvedCube(cube);
        System.out.println("Solved Fitness: " + solvedCube.getFitness());
        System.out.println();

        // CubeVisualizer.visualize(cube);
    }
}