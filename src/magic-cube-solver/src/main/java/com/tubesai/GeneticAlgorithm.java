package com.tubesai;

import java.util.*;

public class GeneticAlgorithm implements IAlgorithm {
    private List<MagicCube> populations;
    private int population_size;
    private double mutation_rate;
    private int max_generations;
    private MagicCube bestCube;
    private GraphData graphData;

    public GeneticAlgorithm(int population_size, int max_generations, double mutation_rate) {
        this.population_size = population_size;
        this.max_generations = max_generations;
        this.mutation_rate = mutation_rate;
        this.bestCube = new MagicCube(5);
        this.populations = new ArrayList<>();
        this.generateInitialPopulation();
        this.graphData = new GraphData(false);
    }

    @Override
    public MagicCube getSolvedCube(MagicCube cube) {
        // Find best solution from population
        this.populations.set(0, cube);
        int i = 0;
        int best_eval;
        do {
            List<MagicCube> new_mcs = new ArrayList<>();
            for (int j = 0; j < this.population_size / 2; j++) {
                MagicCube cube1 = this.randomMagicCube();
                MagicCube cube2 = this.randomMagicCube();
                List<MagicCube> children = this.crossover(cube1, cube2);
                for (MagicCube child : children) {
                    MagicCube mutated_child = this.mutate(child);
                    graphData.addData(mutated_child.getFitness());
                    new_mcs.add(mutated_child);
                }
            }
            this.populations = new_mcs;
            i++;
            best_eval = this.getBestFitness();
            graphData.finishIteration();
            this.mutation_rate -= 0.005;
        } while (i < this.max_generations && best_eval < 90);
        return this.bestCube;
    }

    private void generateInitialPopulation() {
        // Generate random initial population of MagicCubes
        for (int i = 0; i < this.population_size; i++) {
            MagicCube mc = new MagicCube(5);
            // mc.printCube();
            this.populations.add(mc);
        }
    }

    private MagicCube randomMagicCube() {
        Random rand = new Random();
        int sum = 0;
        for (MagicCube mc : this.populations) {
            sum += mc.getFitness()+1;
        }
        double select = rand.nextDouble();
        select *= sum;
        int temp = 0;
        for (MagicCube mc : this.populations) {
            temp += mc.getFitness()+1;
            if (select <= temp) {
                return mc;
            }
        }
        return null;
    }

    private int[] translateCubetoArray(MagicCube mc) {
        int size = mc.getSize();
        int[] ret = new int[125];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    Position pos = new Position(i, j, k);
                    int index = i * 25 + j * 5 + k;
                    ret[index] = mc.getCubeElement(pos);
                }
            }
        }
        return ret;
    }

    private MagicCube translateArraytoCube(int[] arr) {
        MagicCube ret = new MagicCube(5);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 5; k++) {
                    Position pos = new Position(i, j, k);
                    int index = i * 25 + j * 5 + k;
                    ret.setCubeElement(pos, arr[index]);
                }
            }
        }
        return ret;
    }

    private List<MagicCube> crossover(MagicCube parent1, MagicCube parent2) {
        // Combine two MagicCubes to create a new MagicCube
        Random rand = new Random();
        int[] arr_parent1 = this.translateCubetoArray(parent1);
        int[] arr_parent2 = this.translateCubetoArray(parent2);

        int[] child1 = new int[125];
        int[] child2 = new int[125];
        Arrays.fill(child1, -1);
        Arrays.fill(child2, -1);

        int crossover_point1 = rand.nextInt(0, 124);
        int crossover_point2 = rand.nextInt(0, 124);
        if (crossover_point1 > crossover_point2) {
            int temp = crossover_point1;
            crossover_point1 = crossover_point2;
            crossover_point2 = temp;
        }

        for (int i = crossover_point1; i <= crossover_point2; i++) {
            child1[i] = arr_parent1[i];
            child2[i] = arr_parent2[i];
        }

        Map<Integer, Integer> mapping1 = new HashMap<>();
        Map<Integer, Integer> mapping2 = new HashMap<>();
        for (int i = crossover_point1; i <= crossover_point2; i++) {
            mapping1.put(arr_parent2[i], arr_parent1[i]);
            mapping2.put(arr_parent1[i], arr_parent2[i]);
        }

        for (int i = 0; i < 125; i++) {
            if (child1[i] == -1) {
                int gene = arr_parent2[i];
                while (mapping1.containsKey(gene)) {
                    gene = mapping1.get(gene);
                }
                child1[i] = gene;
            }
        }

        for (int i = 0; i < 125; i++) {
            if (child2[i] == -1) {
                int gene = arr_parent1[i];
                while (mapping2.containsKey(gene)) {
                    gene = mapping2.get(gene);
                }
                child2[i] = gene;
            }
        }

        MagicCube mc_child1 = this.translateArraytoCube(arr_parent1);
        MagicCube mc_child2 = this.translateArraytoCube(arr_parent2);

        List<MagicCube> ret = new ArrayList<>();
        if (mc_child1.getFitness() > parent1.getFitness()) {
            ret.add(mc_child1);
        } else {
            ret.add(parent1);
        }
        if (mc_child2.getFitness() > parent2.getFitness()) {
            ret.add(mc_child2);
        } else {
            ret.add(parent2);
        }

        return ret;
    }

    private MagicCube mutate(MagicCube cube) {
        // Mutate the cube based on mutation rate
        Random rand = new Random();

        int[] arr_chromosome = this.translateCubetoArray(cube);

        if (rand.nextDouble() < mutation_rate) {
            int pos1 = rand.nextInt(arr_chromosome.length);
            int pos2 = rand.nextInt(arr_chromosome.length);
            while (pos1 == pos2) {
                pos2 = rand.nextInt(arr_chromosome.length);
            }
            int temp = arr_chromosome[pos1];
            arr_chromosome[pos1] = arr_chromosome[pos2];
            arr_chromosome[pos2] = temp;
        }
        MagicCube mutated = this.translateArraytoCube(arr_chromosome);
        return mutated;
    }

    private int getBestFitness() {
        int ret = 0;
        for (MagicCube mc : this.populations) {
            if (mc.getFitness() > ret) {
                ret = mc.getFitness();
                this.bestCube.copyFrom(mc);
            }
        }
        return ret;
    }

    public int getPopulationSize() {
        return population_size;
    }

    public void setPopulationSize(int population_size) {
        this.population_size = population_size;
    }

    public int getMaxGenerations() {
        return max_generations;
    }

    public void setMaxGenerations(int max_generations) {
        this.max_generations = max_generations;
    }

    @Override
    public GraphData getGraphData() {
        return graphData;
    }
}
