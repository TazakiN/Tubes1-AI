package com.tubesai;

// import java.util.Random;

public class Main {
    public static void main(String[] args) {
        // This just a test to see if the SimulatedAnnealing works
        // MagicCube cube = new MagicCube(5);

        // SimulatedAnnealing solver = new SimulatedAnnealing(100, 1);
        // MagicCube solvedCube = solver.getSolvedCube(cube);

        // System.out.println("Starter Cube:");
        // System.out.println("Fitness: " + cube.getFitness());
        // cube.printCube();
        // System.out.println();
        // System.out.println("Solved Cube:");
        // System.out.println("Fitness: " + solvedCube.getFitness());
        // solvedCube.printCube();

        // This is just a test to see if the GeneticAlgorithm Works
        MagicCube mc = new MagicCube(5);

        GeneticAlgorithm ga = new GeneticAlgorithm(4, 3, 0.1);
        MagicCube solved_mc = ga.getSolvedCube(mc);

        System.out.println("Initial Cube: ");
        mc.printCube();
        System.out.println("Fitness: " + mc.getFitness());
        System.out.println();
        System.out.println("Solved Cube:");
        solved_mc.printCube();
        System.out.println("Fitness: " + solved_mc.getFitness());

        // This is just a test to see if the Visualize MagicCube works
        // MagicCube magicCube = new MagicCube(5);
        // int ans = magicCube.evaluateObjFunc();
        // System.out.println(ans);

        // CubeVisualizer.visualize(magicCube);
    }
}