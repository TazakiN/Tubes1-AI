package com.tubesai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GraphData {
    private final Map<Integer, IterationStats> iterationData;
    private int currentIteration;
    private InnerGraphData bestValue;

    public GraphData() {
        this.iterationData = new HashMap<>();
        this.currentIteration = 0;
        this.bestValue = null;
    }

    public GraphData(GraphData other) {
        this.iterationData = new HashMap<>(other.iterationData);
        this.currentIteration = other.currentIteration;
        this.bestValue = other.bestValue != null ? new InnerGraphData(other.bestValue) : null;
    }

    public void addData(int objFuncValue) {
        InnerGraphData newData = new InnerGraphData(objFuncValue, currentIteration);

        if (bestValue == null || objFuncValue < bestValue.objFuncValue) {
            bestValue = newData;
        }

        IterationStats stats = iterationData.computeIfAbsent(currentIteration,
                k -> new IterationStats());

        stats.addValue(objFuncValue);
    }

    public void finishIteration() {
        currentIteration++;
    }

    public double getAvgObjFuncValue(int iteration) {
        IterationStats stats = iterationData.get(iteration);
        return stats != null ? stats.getAverage() : 0.0;
    }

    public InnerGraphData getBestObjFuncValue() {
        return bestValue;
    }

    public int getIteration() {
        return currentIteration;
    }

    public Map<Integer, IterationStats> getAllData() {
        return new HashMap<>(iterationData);
    }

    public static class InnerGraphData {
        private final int objFuncValue;
        private final int iterationNum;

        public InnerGraphData(int objFuncValue, int iterationNum) {
            this.objFuncValue = objFuncValue;
            this.iterationNum = iterationNum;
        }

        // Copy constructor
        private InnerGraphData(InnerGraphData other) {
            this.objFuncValue = other.objFuncValue;
            this.iterationNum = other.iterationNum;
        }

        public int getObjFuncValue() {
            return objFuncValue;
        }

        public int getIterationNum() {
            return iterationNum;
        }
    }

    static class IterationStats {
        private int count;
        private double sum;
        private int min;
        private int max;
        private final ArrayList<Integer> values;

        public IterationStats() {
            this.count = 0;
            this.sum = 0;
            this.min = Integer.MAX_VALUE;
            this.max = Integer.MIN_VALUE;
            this.values = new ArrayList<>();
        }

        public void addValue(int value) {
            count++;
            sum += value;
            min = Math.min(min, value);
            max = Math.max(max, value);
            values.add(value);
        }

        public double getAverage() {
            return count > 0 ? sum / count : 0;
        }

        public int getMin() {
            return min;
        }

        public int getMax() {
            return max;
        }

        public ArrayList<Integer> getValues() {
            return new ArrayList<>(values);
        }
    }
}