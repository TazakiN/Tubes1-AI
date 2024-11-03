package com.tubesai;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Menu menu = new Menu();

        MagicCube cube = new MagicCube(5);

        boolean isInputFromJSON = menu.isInputFromJSON();
        if (isInputFromJSON) {
            // TODO: handle input from JSON
        }

        IAlgorithm solver;
        int algoChoice = menu.chooseAlgoMenu();
        // TODO: handle algorithm choice
        if (algoChoice == 1) {
            solver = new HillClimbingSideMove(10); // Change max_side_moves as needed
        } else if (algoChoice == 2) {
            solver = new SimulatedAnnealing(10, 0.000001);
        } else if (algoChoice == 3) {
            solver = new GeneticAlgorithm(1, 1, 1);
        } else {
            System.out.println("Error in algorithm choice.");
            return;
        }

        // Solve the cube
        MagicCube solvedCube = solver.getSolvedCube(cube);
        System.out.println("Fitness before solving: " + cube.getFitness());
        System.out.println("Fitness after solving: " + solvedCube.getFitness());
        System.out.println();

        // Visualize the solved cube
        boolean visualize = menu.isVisualize();
        if (visualize) {
            CubeVisualizer.visualize(solvedCube, GraphData.createRealisticDummyData(true));
        }

        menu.closeScanner();
    }

    // public static void main(String[] args) {
    // // This just a test to see if the SimulatedAnnealing works
    // MagicCube cube = new MagicCube(5);

    // SimulatedAnnealing solver = new SimulatedAnnealing(10, 0.000001);

    // System.out.println("Starter Cube:");
    // System.out.println("Fitness: " + cube.getFitness());
    // // cube.printCube();
    // System.out.println();
    // MagicCube solvedCube = solver.getSolvedCube(cube);
    // System.out.println("Solved Fitness (re-evaluate): " +
    // solvedCube.evaluateObjFunc2());
    // System.out.println("Solved Fitness (fitness attribute): " +
    // solvedCube.getFitness());
    // System.out.println();

    // CubeVisualizer.visualize(solvedCube);
    // }
}