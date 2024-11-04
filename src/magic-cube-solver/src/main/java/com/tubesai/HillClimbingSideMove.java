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
        graphData.addData(cube.evaluateObjFunc());

        MagicCube prev = new MagicCube(cube);
        cubemap.put(cube.sequence, true);

        int side_moves = 0; // Tambahkan counter untuk side moves
        int count = 0;

        while (true) {
            graphData.finishIteration();
            MagicCube next = new MagicCube(getNextNeighbour(prev, cubemap));
            // System.out.println(side_moves);
            graphData.addData(next.evaluateObjFunc());
            
            if (next.getFitness() > prev.getFitness()) {
                    side_moves = 0; // Reset jika ada peningkatan fitness
                } else if (next.getFitness() == prev.getFitness()) {
                        side_moves++; // Increment jika fitness sama
                        if (side_moves >= max_side_moves) {
                                System.out.println(count);
                                return prev; // Hentikan pencarian
                            }
            }

            if (next.sequence.equals(prev.sequence)) {
                System.out.println(count);
                System.out.println("Tidak ada yang mengalahkan saya");
                return next;
            }
                
            count++;
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
                            for (int c = (z == i && x == j) ? k + 1 : 0; c < dimension; c++) {
                                MagicCube tempCube = new MagicCube(cube);
                                tempCube.moveToNeighbour(new Position(i, j, k), new Position(z, x, c));

                                graphData.addData(tempCube.getFitness());
                                
                                if (tempCube.getFitness() >= result.getFitness()
                                        && !cubemap.containsKey(tempCube.sequence)) {
                                    result = new MagicCube(tempCube);
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

    public static void main(String args[]) {
        HillClimbingSideMove a = new HillClimbingSideMove(5000);
        MagicCube test = new MagicCube(5);
        // System.out.println(test.evaluateObjFunc2());

        // MagicCube baru = a.getNextNeighbour(test);
        // System.out.println(baru.evaluateObjFunc2());

        // test.printCube();
        // baru.printCube();

        MagicCube apa = new MagicCube(a.getSolvedCube(test));

        // test.printCube();
        // apa.printCube();

        System.out.println(test.getFitness());
        System.out.println(apa.getFitness());
    }

    @Override
    public GraphData getGraphData() {
        return graphData;
    }
}