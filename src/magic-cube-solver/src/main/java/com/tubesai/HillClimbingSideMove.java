package com.tubesai;

import java.util.Random;

public class HillClimbingSideMove implements IAlgorithm {
    private int max_side_moves;

    public HillClimbingSideMove(int max_side_moves) {
        this.max_side_moves = max_side_moves;
    }

    @Override
    public MagicCube getSolvedCube(MagicCube cube) {
        // TODO: Implement Hill Climbing Side Move algorithm
        return null;
    }

    public int getMaxSideMoves() {
        return max_side_moves;
    }
}
