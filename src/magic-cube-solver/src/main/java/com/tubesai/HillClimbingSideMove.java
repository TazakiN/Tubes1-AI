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

        int side_moves = 0; // Tambahkan counter untuk side moves
        int count = 0;

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
                            for (int c = (z == i && x == j) ? k + 1 : 0; c < dimension; c++) { // loop pembngkitan next state yang mungkin
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
}