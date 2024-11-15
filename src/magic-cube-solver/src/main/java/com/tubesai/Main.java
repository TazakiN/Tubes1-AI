package com.tubesai;

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
        if (algoChoice == 1) {
            solver = new HillClimbingSideMove(500);
        } else if (algoChoice == 2) {
            solver = new SimulatedAnnealing(10, 0.000001);
        } else if (algoChoice == 3) {
            solver = new GeneticAlgorithm(1000, 1000, 0.2);
        } else {
            System.out.println("Error in algorithm choice.");
            return;
        }

        long startTime = System.currentTimeMillis();
        MagicCube solvedCube = solver.getSolvedCube(cube);
        long endTime = System.currentTimeMillis();
        GraphData graphData = solver.getGraphData();
        System.out.println("Fitness before solving: " + cube.getFitness());
        System.out.println("Fitness after solving: " + solvedCube.getFitness());
        System.out.println("iterasi: " + graphData.getAllData().size());
        System.out.println("Waktu eksekusi: " + (endTime - startTime) + " ms");
        graphData.setExecutionTime(endTime - startTime);
        System.out.println();

        menu.isVisualize(cube, solvedCube, graphData);

        menu.closeScanner();

        System.out.println("Program has ended.");
    }
}