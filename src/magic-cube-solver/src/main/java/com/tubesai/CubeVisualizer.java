package com.tubesai;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class CubeVisualizer {
    private static JLabel[][][] labelGrid;
    private static int size;
    private static int[][][] cubeValues;
    private static Color[] highlightColors = {
            Color.YELLOW, Color.CYAN, Color.MAGENTA,
            Color.ORANGE, Color.PINK, Color.GREEN,
            Color.BLUE, Color.RED, Color.LIGHT_GRAY,
            Color.DARK_GRAY, Color.BLACK, Color.WHITE
    };
    private static JLabel[] summaryLabels;

    /**
     * Visualizes the given MagicCube in a graphical user interface.
     * The cube is displayed in a JFrame with a grid layout, where each cell
     * represents a value in the cube.
     * The frame also includes a summary panel that shows the count of each
     * highlighted color.
     *
     * @param magicCube the MagicCube object to be visualized
     */
    public static void visualize(MagicCube magicCube) {
        size = magicCube.getSize();
        cubeValues = magicCube.getCube();
        labelGrid = new JLabel[size][size][size];

        JFrame frame = new JFrame("Visualisasi Magic Cube");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 800);

        JPanel mainPanel = new JPanel(new BorderLayout());
        frame.add(mainPanel);

        JPanel cubePanel = new JPanel();
        int gridRows = (int) Math.ceil(Math.sqrt(size));
        int gridCols = (int) Math.ceil((double) size / gridRows);
        cubePanel.setLayout(new GridLayout(gridRows, gridCols));
        mainPanel.add(cubePanel, BorderLayout.CENTER);

        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new GridLayout(highlightColors.length, 1));
        mainPanel.add(summaryPanel, BorderLayout.EAST);

        summaryLabels = new JLabel[highlightColors.length];
        for (int i = 0; i < highlightColors.length; i++) {
            summaryLabels[i] = new JLabel("Jumlah angka warna " + (i + 1) + ": 0", SwingConstants.CENTER);
            summaryLabels[i].setOpaque(true);
            summaryLabels[i].setBackground(highlightColors[i]);
            summaryPanel.add(summaryLabels[i]);
        }

        for (int z = 0; z < size; z++) {
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(size, size));
            panel.setBorder(BorderFactory.createTitledBorder("Bidang di Z = " + (z + 1)));

            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    int value = cubeValues[z][y][x];
                    JLabel label = new JLabel(String.valueOf(value), SwingConstants.CENTER);
                    label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    label.setOpaque(true);
                    labelGrid[z][y][x] = label;

                    final int fx = x, fy = y, fz = z;
                    label.addMouseListener(new CubeMouseListener(fx, fy, fz));
                    panel.add(label);
                }
            }

            cubePanel.add(panel);
        }

        frame.setVisible(true);
    }

    /**
     * CubeMouseListener is a MouseAdapter that highlights related cells in a 3D
     * grid
     * when the mouse enters or exits a cell. It highlights cells in the same row,
     * column, and pillar, as well as various diagonals within the 3D space.
     */
    private static class CubeMouseListener extends MouseAdapter {
        private final int x, y, z;
        private final List<JLabel> highlightedLabels = new ArrayList<>();
        private final List<Integer> colorIndices = new ArrayList<>();

        public CubeMouseListener(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            highlightRelatedCells();
            updateSummaryLabels();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            resetHighlightedCells();
            updateSummaryLabels();
        }

        private void highlightRelatedCells() {
            int colorIndex = 0;

            // Highlight baris
            for (int i = 0; i < size; i++) {
                highlightLabel(labelGrid[z][y][i], highlightColors[colorIndex % highlightColors.length], colorIndex);
            }
            colorIndex++;

            // Highlight kolom
            for (int i = 0; i < size; i++) {
                highlightLabel(labelGrid[z][i][x], highlightColors[colorIndex % highlightColors.length], colorIndex);
            }
            colorIndex++;

            // Highlight tiang
            for (int i = 0; i < size; i++) {
                highlightLabel(labelGrid[i][y][x], highlightColors[colorIndex % highlightColors.length], colorIndex);
            }
            colorIndex++;

            // Diagonal bidang
            if (x == y) {
                for (int i = 0; i < size; i++) {
                    highlightLabel(labelGrid[z][i][i], highlightColors[colorIndex % highlightColors.length],
                            colorIndex);
                }
            }
            colorIndex++;

            if (x + y == size - 1) {
                for (int i = 0; i < size; i++) {
                    highlightLabel(labelGrid[z][i][size - 1 - i], highlightColors[colorIndex % highlightColors.length],
                            colorIndex);
                }
            }
            colorIndex++;

            // Diagonal kolom
            if (x == z) {
                for (int i = 0; i < size; i++) {
                    highlightLabel(labelGrid[i][y][i], highlightColors[colorIndex % highlightColors.length],
                            colorIndex);
                }
            }
            colorIndex++;

            if (x + z == size - 1) {
                for (int i = 0; i < size; i++) {
                    highlightLabel(labelGrid[i][y][size - 1 - i], highlightColors[colorIndex % highlightColors.length],
                            colorIndex);
                }
            }
            colorIndex++;

            // Diagonal baris
            if (y == z) {
                for (int i = 0; i < size; i++) {
                    highlightLabel(labelGrid[i][i][x], highlightColors[colorIndex % highlightColors.length],
                            colorIndex);
                }
            }
            colorIndex++;

            if (y + z == size - 1) {
                for (int i = 0; i < size; i++) {
                    highlightLabel(labelGrid[i][size - 1 - i][x], highlightColors[colorIndex % highlightColors.length],
                            colorIndex);
                }
            }
            colorIndex++;

            // Diagonal ruang utama dan sekunder
            if (x == y && y == z) {
                for (int i = 0; i < size; i++) {
                    highlightLabel(labelGrid[i][i][i], highlightColors[colorIndex % highlightColors.length],
                            colorIndex);
                }
            }
            colorIndex++;

            if (x == y && z == size - 1 - x) {
                for (int i = 0; i < size; i++) {
                    highlightLabel(labelGrid[i][i][size - 1 - i], highlightColors[colorIndex % highlightColors.length],
                            colorIndex);
                }
            }
            colorIndex++;

            // Diagonal ruang silang
            if (x == size - 1 - y && y == z) {
                for (int i = 0; i < size; i++) {
                    highlightLabel(labelGrid[size - 1 - i][i][i], highlightColors[colorIndex % highlightColors.length],
                            colorIndex);
                }
            }
            colorIndex++;

            if (x == y && y == size - 1 - z) {
                for (int i = 0; i < size; i++) {
                    highlightLabel(labelGrid[i][size - 1 - i][i], highlightColors[colorIndex % highlightColors.length],
                            colorIndex);
                }
            }
            colorIndex++;

            if (x == size - 1 - y && z == size - 1 - x) {
                for (int i = 0; i < size; i++) {
                    highlightLabel(labelGrid[size - 1 - i][size - 1 - i][i],
                            highlightColors[colorIndex % highlightColors.length], colorIndex);
                }
            }
            colorIndex++;

            if (x == size - 1 - y && y == z) {
                for (int i = 0; i < size; i++) {
                    highlightLabel(labelGrid[size - 1 - i][i][size - 1 - i],
                            highlightColors[colorIndex % highlightColors.length], colorIndex);
                }
            }
        }

        private void highlightLabel(JLabel lbl, Color color, int colorIndex) {
            if (!highlightedLabels.contains(lbl)) {
                lbl.setBackground(color);
                highlightedLabels.add(lbl);
                colorIndices.add(colorIndex);
            }
        }

        private void resetHighlightedCells() {
            for (JLabel lbl : highlightedLabels) {
                lbl.setBackground(null);
            }
            highlightedLabels.clear();
            colorIndices.clear();
        }
    }

    /**
     * Updates the summary labels with the sum of cube values for each highlight
     * color.
     * 
     * This method iterates through a 3D grid of labels, checks the background color
     * of each label,
     * and sums the corresponding cube values for each highlight color. The results
     * are then used
     * to update the text of the summary labels.
     * 
     * The summary labels display the total sum of cube values for each highlight
     * color.
     */
    private static void updateSummaryLabels() {
        int[] colorSums = new int[highlightColors.length];

        for (int z = 0; z < size; z++) {
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    Color bgColor = labelGrid[z][y][x].getBackground();
                    if (bgColor != null) {
                        for (int i = 0; i < highlightColors.length; i++) {
                            if (highlightColors[i].equals(bgColor)) {
                                colorSums[i] += cubeValues[z][y][x];
                                break;
                            }
                        }
                    }
                }
            }
        }

        for (int i = 0; i < highlightColors.length; i++) {
            summaryLabels[i].setText("Jumlah angka warna " + (i + 1) + ": " + colorSums[i]);
        }
    }
}
