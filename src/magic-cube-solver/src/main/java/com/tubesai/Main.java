package com.tubesai;

public class Main {
    public static void main(String[] args) {
        MagicCube magicCube = new MagicCube(5);
        int ans = magicCube.evaluateObjFunc();
        System.out.println(ans);

        CubeVisualizer.visualize(magicCube);
    }
}