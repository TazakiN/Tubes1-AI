package com.tubesai;

import java.util.Random;
import java.util.HashMap;
import java.util.Map;

public class HillClimbingSideMove implements IAlgorithm {
    private int max_side_moves;

    public HillClimbingSideMove(int max_side_moves) {
        this.max_side_moves = max_side_moves;
    }

    @Override
    public MagicCube getSolvedCube(MagicCube cube) {
        
        Map<String, Boolean> cubemap = new HashMap<>();

        MagicCube prev = new MagicCube(cube);
        cubemap.put(cube.sequence, true);

        while(true){
            MagicCube next = new MagicCube(getNextNeighbour(prev, cubemap));
            if(next.sequence.compareTo(prev.sequence) == 0){
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
                            for (int c = (z == i && x == j) ? k + 1 : 0; c < dimension; c++) {

                                MagicCube tempCube = new MagicCube(cube);
                                tempCube.moveToNeighbour(new Position(i, j, k), new Position(z, x, c));
                                if (tempCube.evaluateObjFunc2() >= result.evaluateObjFunc2() && !cubemap.containsKey(tempCube.sequence)) {
                                
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

    public static void main(String args[]){
        HillClimbingSideMove a = new HillClimbingSideMove(0);
        MagicCube test = new MagicCube(5);
        // System.out.println(test.evaluateObjFunc2());

        // MagicCube baru = a.getNextNeighbour(test);
        // System.out.println(baru.evaluateObjFunc2());

        // test.printCube();
        // baru.printCube();
        
        MagicCube apa = new MagicCube(a.getSolvedCube(test));

        test.printCube();
        apa.printCube();

        System.out.println(test.evaluateObjFunc2());
        System.out.println(apa.evaluateObjFunc2());
    }
}
