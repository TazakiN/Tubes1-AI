package com.tubesai;

public class HillClimbingSideMove implements IAlgorithm {
    private int max_side_moves;

    public HillClimbingSideMove(int max_side_moves) {
        this.max_side_moves = max_side_moves;
    }

    @Override
    public void solve(MagicCube cube) {
        for (int i = 0; i < max_side_moves; i++) {
            int currentFitness = cube.evaluate();
            cube.swapRandomCells();
            int newFitness = cube.evaluate();
            if (newFitness < currentFitness) {
                // Revert to previous state
                cube.swapRandomCells();
            }
        }
    }
}
