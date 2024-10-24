package com.tubesai;

import java.util.Random;
import java.io.FileReader;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class MagicCube {
    private int size;
    private int[][][] cube;
    private int magic_number;
    private int fitness; // int or float, decide later

    /**
     * Constructs a MagicCube object with the specified size.
     * Initializes the cube with the given size, sets the magic number to 315,
     * evaluates the fitness using the objective function, and initializes the cube.
     *
     * @param size the size of the magic cube
     */
    public MagicCube(int size) {
        this.size = size;
        this.cube = new int[size][size][size];
        this.magic_number = 315;
        this.fitness = evaluateObjFunc();
        initializeCube();
    }

    /**
     * Constructs a MagicCube object by loading its state from a JSON file.
     *
     * @param jsonFilePath the path to the JSON file containing the cube's state.
     *                      The JSON file is expected to have a "cube" key with a 3D array of integers.
     * @throws IOException if an I/O error occurs while reading the file.
     */
    public MagicCube(String jsonFilePath) {
        // Load state cube from json file and assuming its size is 5
        try (FileReader reader = new FileReader(jsonFilePath)) {
            // TODO: Resolved Gson and JsonObject import
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            int[][][] loadedCube = gson.fromJson(jsonObject.get("cube"), int[][][].class);
            this.size = loadedCube.length;
            this.cube = loadedCube;
            this.magic_number = 315;
            this.fitness = evaluateObjFunc();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructs a MagicCube object by copying the state of another MagicCube object.
     * 
     * @param cube the MagicCube object to be copied
     */
    public MagicCube(MagicCube cube) {
        this.size = cube.getSize();
        this.cube = cube.getCube();
        this.magic_number = cube.getMagicNumber();
        this.fitness = cube.getFitness();
    }

    /**
     * Initializes the 5x5x5 magic cube with sequential values from 1 to 125 and then shuffles the cube.
     * 
     * The cube is first initialized with sequential values in a 3D array. 
     * These values are then copied into a 1D array to facilitate shuffling using the Fisher-Yates algorithm.
     * After shuffling, the values are copied back into the 3D array.
     * 
     * The shuffling ensures that the cube is randomized each time it is initialized.
     */
    private void initializeCube() {
        // Initialize cube with random or sequential values

        // make 5x5x5 cube
        this.cube = new int[][][]
        {
            {
                {1, 2, 3, 4, 5},
                {6, 7, 8, 9, 10},
                {11, 12, 13, 14, 15},
                {16, 17, 18, 19, 20},
                {21, 22, 23, 24, 25}
            },
            {
                {26, 27, 28, 29, 30},
                {31, 32, 33, 34, 35},
                {36, 37, 38, 39, 40},
                {41, 42, 43, 44, 45},
                {46, 47, 48, 49, 50}
            },
            {
                {51, 52, 53, 54, 55},
                {56, 57, 58, 59, 60},
                {61, 62, 63, 64, 65},
                {66, 67, 68, 69, 70},
                {71, 72, 73, 74, 75}
            },
            {
                {76, 77, 78, 79, 80},
                {81, 82, 83, 84, 85},
                {86, 87, 88, 89, 90},
                {91, 92, 93, 94, 95},
                {96, 97, 98, 99, 100}
            },
            {
                {101, 102, 103, 104, 105},
                {106, 107, 108, 109, 110},
                {111, 112, 113, 114, 115},
                {116, 117, 118, 119, 120},
                {121, 122, 123, 124, 125}
            }
        };

        // Shuffle the cube
        Random rand = new Random();

        // Ubah menjadi array 1D untuk mempermudah pengacakan
        int[] tempArray = new int[125];
        int index = 0;

        // Salin elemen-elemen dari cube 3D ke array 1D
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 5; k++) {
                    tempArray[index++] = this.cube[i][j][k];
                }
            }
        }

        // Fisher-Yates shuffle untuk mengacak array 1D
        for (int i = tempArray.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            // Tukar elemen tempArray[i] dengan tempArray[j]
            int temp = tempArray[i];
            tempArray[i] = tempArray[j];
            tempArray[j] = temp;
        }

        // Salin elemen-elemen dari array 1D yang sudah diacak kembali ke cube 3D
        index = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 5; k++) {
                    this.cube[i][j][k] = tempArray[index++];
                }
            }
        }
    }

    // Belom ada docs/comment nya karena belom jadi (kalo udh jadi, tambahin nanti pake copilot "Generate docs")
    public int evaluateObjFunc() { // int or float, decide later
        // TODO: Evaluate the cube's fitness based on the magic cube conditions

        // Option 1, how many rows, columns, and diagonals (and other aspects if any) that sum up to magic number

        // Option 2, how many rows, columns, and diagonals (and other aspects if any) that NOT sum up to magic number (minus value, 0 is global optimum)

        // Option 3, decide one solution to achieve and calculate how many number that is not in position according to the solution that have been decided
        return 0;
    }

    /**
     * Swaps the elements at the specified positions in the cube and automatically updates the fitness value.
     *
     * @param el1 the position of the first element to be swapped
     * @param el2 the position of the second element to be swapped
     */
    public void moveToNeighbour(Position el1, Position el2) {
        // Swap two number/element in the cube
        int temp = cube[el1.getX()][el1.getY()][el1.getZ()];
        cube[el1.getX()][el1.getY()][el1.getZ()] = cube[el2.getX()][el2.getY()][el2.getZ()];
        cube[el2.getX()][el2.getY()][el2.getZ()] = temp;

        // Update fitness after swapping
        this.fitness = evaluateObjFunc();
    }

    public MagicCube getNeighbour(Position el1, Position el2) {
        MagicCube neighbour = new MagicCube(this);
        neighbour.moveToNeighbour(el1, el2);
        return neighbour;
    }

    /**
     * Prints the current state of the magic cube to the console.
     * The cube is represented as a 3x3x3 array, and this method
     * prints each layer of the cube in a readable format.
     * 
     * Each layer is printed with its corresponding layer number,
     * followed by the rows and columns of that layer.
     */
    public void printCube() {
        for (int i = 0; i < 3; i++) {
            System.out.println("Layer " + (i + 1) + ":");
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    System.out.print(this.cube[i][j][k] + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }
    
    /**
     * Saves the current state of the cube to a JSON file.
     *
     * @param jsonFilePath the path to the JSON file where the cube state will be saved
     */
    public void saveCube(String jsonFilePath) {
        // Save state cube to json file
        try {
            // TODO: Resolved Gson and JsonObject import
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("cube", gson.toJsonTree(this.cube));
            try (FileWriter writer = new FileWriter(jsonFilePath)) {
                gson.toJson(jsonObject, writer);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the element from the magic cube at the specified position.
     *
     * @param pos the position in the cube from which to retrieve the element
     * @return the element at the specified position in the cube
     */
    public int getCubeElement(Position pos) {
        return cube[pos.getX()][pos.getY()][pos.getZ()];
    }

    public int getSize() {
        return size;
    }

    public int[][][] getCube() {
        return cube;
    }

    public int getMagicNumber() {
        return magic_number;
    }

    public int getFitness() {
        return fitness;
    }
}
