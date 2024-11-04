package com.tubesai;

// import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Menu menu = new Menu();

        MagicCube cube = new MagicCube(5);

        boolean isInputFromJSON = menu.isInputFromJSON();
        if (isInputFromJSON) {
            String filename = menu.getJSONFilename();
            cube = new MagicCube(filename);
        }

        IAlgorithm solver;
        int algoChoice = menu.chooseAlgoMenu();
        // TODO: handle algorithm choice
        if (algoChoice == 1) {
            solver = new HillClimbingSideMove(100); // Change max_side_moves as needed
        } else if (algoChoice == 2) {
            solver = new SimulatedAnnealing(10, 0.000001);
        } else if (algoChoice == 3) {
            solver = new GeneticAlgorithm(100, 100, 0.1);
        } else {
            System.out.println("Error in algorithm choice.");
            return;
        }

        // Solve the cube
        long startTime = System.currentTimeMillis();
        MagicCube solvedCube = solver.getSolvedCube(cube);
        long endTime = System.currentTimeMillis();
        GraphData graphData = solver.getGraphData();
        System.out.println("Fitness before solving: " + cube.getFitness());
        System.out.println("Fitness after solving: " + solvedCube.getFitness());
        System.out.println("iterasi: " + graphData.getIteration());
        System.out.println("Waktu eksekusi: " + (endTime - startTime) + " ms");
        System.out.println();

        // Visualize the solved cube
        boolean visualize = menu.isVisualize();
        if (visualize) {
            CubeVisualizer.visualize(solvedCube, graphData);
        }

        menu.closeScanner();
    }
}