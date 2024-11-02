package com.tubesai;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        // This just a test to see if the SimulatedAnnealing works
        MagicCube cube = new MagicCube(5);

        System.out.print(cube.sequence);

        // CubeVisualizer.visualize(cube);
    }
}