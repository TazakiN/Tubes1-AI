package com.tubesai;

import java.util.HashMap;
import java.util.Map;

public class HillClimbingSideMove implements IAlgorithm {
    private int max_side_moves;
    private GraphData graphData;

    public HillClimbingSideMove(int max_side_moves) {
        this.max_side_moves = max_side_moves;
        this.graphData = new GraphData(false);
    }

    @Override
    public MagicCube getSolvedCube(MagicCube cube) {
        Map<String, Boolean> cubemap = new HashMap<>();
        graphData.addData(cube.getFitness());

        MagicCube prev = new MagicCube(cube);
        cubemap.put(cube.sequence, true);

        int side_moves = 0; // Counter untuk menghitung sideways moves

        while (true) {
            graphData.finishIteration();
            MagicCube next = new MagicCube(getNextNeighbour(prev, cubemap));
            graphData.addData(next.getFitness());
            
            if (next.getFitness() > prev.getFitness()) {
                    side_moves = 0; // Reset jika ada peningkatan fitness
                } else if (next.getFitness() == prev.getFitness()) {
                        side_moves++; // Increment jika fitness sama
                        if (side_moves >= max_side_moves) {
                                return prev; // Hentikan pencarian
                            }
            }

            if (next.sequence.equals(prev.sequence)) {
                return next;
            }
                
            cubemap.put(next.sequence, true);
            prev = new MagicCube(next);
        }
    }

    public MagicCube getNextNeighbour(MagicCube cube, Map<String, Boolean> cubemap) {
        MagicCube result = new MagicCube(cube);
        int dimension = cube.getSize();

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                for (int k = 0; k < dimension; k++) {

                    for (int z = i; z < dimension; z++) {
                        for (int x = (z == i) ? j : 0; x < dimension; x++) {
                            for (int c = (z == i && x == j) ? k + 1 : 0; c < dimension; c++) { // loop pembangkitan next state yang mungkin
                                MagicCube tempCube = new MagicCube(cube);

                                tempCube.moveToNeighbour(new Position(i, j, k), new Position(z, x, c));
                                graphData.addData(tempCube.getFitness());
                                
                                if (tempCube.getFitness() >= result.getFitness()
                                && !cubemap.containsKey(tempCube.sequence)) {
                                    result = new MagicCube(tempCube); // Memilih tetangga dengan nilai fungsi yang lebih tinggi
                                }
                            }
                        }
                    }

                }
            }
        }
        return result;
    }

    public int getMaxSideMoves() {
        return max_side_moves;
    }

    @Override
    public GraphData getGraphData() {
        return graphData;
    }

    // public static void main(String args[]){
    //     MagicCube obj = new MagicCube(5);
        
    //     HillClimbingSideMove hill100 = new HillClimbingSideMove(100);
    //     HillClimbingSideMove hill500 = new HillClimbingSideMove(500);
    //     HillClimbingSideMove hill1000 = new HillClimbingSideMove(1000);
    //     HillClimbingSideMove hill5000 = new HillClimbingSideMove(5000);

    //     long start = System.currentTimeMillis();
    //     MagicCube solved = hill100.getSolvedCube(obj);
    //     long end = System.currentTimeMillis();
    //     long res = end-start;
    //     System.out.println("100: "+ res + " " + solved.getFitness());


    //     start = System.currentTimeMillis();
    //     solved = hill500.getSolvedCube(obj);
    //     end = System.currentTimeMillis();
    //     res = end-start;
    //     System.out.println("500: "+ res + " " + solved.getFitness());

    //     start = System.currentTimeMillis();
    //     solved = hill1000.getSolvedCube(obj);
    //     end = System.currentTimeMillis();
    //     res = end-start;
    //     System.out.println("1000: "+ res + " " + solved.getFitness());

    //     start = System.currentTimeMillis();
    //     solved = hill5000.getSolvedCube(obj);
    //     end = System.currentTimeMillis();
    //     res = end-start;
    //     System.out.println("5000: "+ res + " " + solved.getFitness());
    // }
}