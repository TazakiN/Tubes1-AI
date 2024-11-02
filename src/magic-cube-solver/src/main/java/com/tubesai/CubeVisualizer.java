package com.tubesai;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

public class CubeVisualizer {
    private static JLabel[][][] labelGrid;
    private static int size;
    private static int[][][] cubeValues;
    private static Color[] highlightColors = {
            Color.YELLOW,
            Color.CYAN,
            Color.PINK,
            Color.ORANGE,
            new Color(255, 182, 193), // Light Pink
            new Color(173, 216, 230), // Light Blue
            new Color(144, 238, 144), // Light Green
            new Color(255, 228, 181), // Moccasin
            new Color(255, 250, 205), // Lemon Chiffon
            new Color(255, 239, 213), // Papaya Whip
            Color.WHITE
    };
    private static JLabel[] summaryLabels;
    private static JLabel hoveredCellLabel;
    private static ChartPanel chartPanel;
    private static GraphData graphData;

    /**
     * Visualizes the given MagicCube in a graphical user interface.
     * The cube is displayed in a JFrame with a grid layout, where each cell
     * represents a value in the cube.
     * The frame also includes a summary panel that shows the count of each
     * highlighted color.
     *
     * @param magicCube the MagicCube object to be visualized
     */
    public static void visualize(MagicCube magicCube, GraphData graphData) {
        size = magicCube.getSize();
        cubeValues = magicCube.getCube();
        labelGrid = new JLabel[size][size][size];

        JFrame frame = new JFrame("Visualisasi Magic Cube");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel mainPanel = new JPanel(new BorderLayout());
        frame.add(mainPanel);

        hoveredCellLabel = new JLabel("Hover mouse over a cell to highlight related cells.", SwingConstants.CENTER);
        hoveredCellLabel.setPreferredSize(new Dimension(400, 30));
        mainPanel.add(hoveredCellLabel, BorderLayout.NORTH);

        JPanel cubePanel = new JPanel(new GridLayout(1, size));
        JPanel cubePanel2 = new JPanel(new BorderLayout(2, 1));
        cubePanel2.add(cubePanel);
        JScrollPane scrollPane = new JScrollPane(cubePanel2);

        for (int z = 0; z < size; z++) {
            JPanel layerPanel = new JPanel(new GridLayout(size, size));
            layerPanel.setBorder(BorderFactory.createTitledBorder("Layer " + (z + 1)));
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    int value = cubeValues[z][y][x];
                    JLabel label = new JLabel(String.valueOf(value), SwingConstants.CENTER);
                    label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    label.setOpaque(true);
                    labelGrid[z][y][x] = label;

                    final int fx = x, fy = y, fz = z;
                    label.addMouseListener(new CubeMouseListener(fx, fy, fz));
                    layerPanel.add(label);
                }
            }
            cubePanel.add(layerPanel);
        }

        chartPanel = new ChartPanel(null);
        chartPanel.setPreferredSize(new Dimension(800, 400));

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollPane, chartPanel);
        splitPane.setResizeWeight(0.7);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        summaryLabels = new JLabel[highlightColors.length];
        for (int i = 0; i < highlightColors.length; i++) {
            summaryLabels[i] = new JLabel("Jumlah : 0", SwingConstants.CENTER);
            summaryLabels[i].setOpaque(true);
            summaryLabels[i].setBackground(highlightColors[i]);
            summaryLabels[i].setPreferredSize(new Dimension(100, 30));
            summaryPanel.add(summaryLabels[i]);
        }
        cubePanel2.add(summaryPanel, BorderLayout.SOUTH);

        CubeVisualizer.graphData = graphData;

        updateChart();

        frame.setVisible(true);
    }

    /**
     * Updates the summary labels with the sum of cube values for each highlight
     * color and updates the chart.
     */
    private static void updateSummaryLabels() {
        int[] colorSums = new int[highlightColors.length];

        for (int z = 0; z < size; z++) {
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    Color bgColor = labelGrid[z][y][x].getBackground();
                    if (bgColor != null) {
                        for (int i = 0; i < highlightColors.length; i++) {
                            if (bgColor.equals(highlightColors[i])) {
                                colorSums[i] += cubeValues[z][y][x];
                                break;
                            }
                        }
                    }
                }
            }
        }

        for (int i = 0; i < highlightColors.length; i++) {
            summaryLabels[i].setText("Jumlah : " + colorSums[i]);
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < colorSums.length; i++) {
            dataset.addValue(colorSums[i], "Jumlah", "Warna " + (i + 1));
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Chart Avg & Max ObjFunc Values Over Iterations",
                "Iteration",
                "Objective Function Value",
                dataset);

        chartPanel.setChart(chart);

        updateChart();
    }

    public static GraphData createDummyGraphData() {
        GraphData dummyData = new GraphData();
        for (int iter = 0; iter < 50; iter++) {
            for (int i = 0; i < 5; i++) {
                int objFuncValue = (int) (Math.random() * 100) + iter * 10;
                dummyData.addData(objFuncValue);
            }
            dummyData.finishIteration();
        }
        return dummyData;
    }

    private static void updateChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        Map<Integer, GraphData.IterationStats> allData = graphData.getAllData();
        for (Map.Entry<Integer, GraphData.IterationStats> entry : allData.entrySet()) {
            int iteration = entry.getKey();
            GraphData.IterationStats stats = entry.getValue();
            double avgValue = stats.getAverage();
            int maxValue = stats.getMax();

            dataset.addValue(avgValue, "Average ObjFunc", String.valueOf(iteration + 1));
            dataset.addValue(maxValue, "Max ObjFunc", String.valueOf(iteration + 1));
        }

        JFreeChart chart = ChartFactory.createLineChart(
                "Objective Function Values Over Iterations",
                "Iteration",
                "Objective Function Value",
                dataset);

        chartPanel.setChart(chart);
    }

    /**
     * CubeMouseListener is a MouseAdapter that highlights related cells in a 3D
     * grid when the mouse enters or exits a cell. It highlights cells in the same
     * row,
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
            hoveredCellLabel.setText(String.format("Position: x = %d, y = %d, z = %d", z + 1, y + 1, x + 1));
            highlightRelatedCells();
            updateSummaryLabels();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            hoveredCellLabel.setText("Hover mouse over a cell to highlight related cells.");
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
}
