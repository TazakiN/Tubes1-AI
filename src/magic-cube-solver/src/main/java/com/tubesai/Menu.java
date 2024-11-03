package com.tubesai;

import java.util.Scanner;

public class Menu {
    // Create a single Scanner instance that will be used throughout the class
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Prompts the user to decide whether to input the cube from a JSON file.
     * 
     * @return true if the user wants to input the cube from a JSON file, false
     *         otherwise.
     */
    public boolean isInputFromJSON() {
        System.out.print("Do you want to input the cube from a JSON file? (y/n): ");
        String input = scanner.nextLine().trim().toLowerCase();
        System.out.println();

        if (input.equals("y")) {
            return true;
        } else if (!input.equals("n")) {
            System.out.println("Invalid input. Please input 'y' or 'n'.\n");
            return isInputFromJSON();
        }

        return false;
    }

    /**
     * Displays a menu for selecting an algorithm to solve the cube.
     * 
     * @return an integer representing the chosen algorithm
     */
    public int chooseAlgoMenu() {
        System.out.println("Algorithms to solve the cube:");
        System.out.println("1. Hill Climbing Side Move");
        System.out.println("2. Simulated Annealing");
        System.out.println("3. Genetic Algorithm");
        System.out.print("Choose the algorithm: ");

        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume the remaining newline
            System.out.println();

            switch (choice) {
                case 1:
                case 2:
                case 3:
                    return choice;
                default:
                    System.out.println("Invalid choice. Please choose a valid algorithm.\n");
                    return chooseAlgoMenu();
            }
        } catch (Exception e) {
            scanner.nextLine(); // clear the invalid input
            System.out.println("Invalid input. Please enter a number.\n");
            return chooseAlgoMenu();
        }
    }

    /**
     * Prompts the user to decide whether to visualize the cube.
     * 
     * @return true if the user wants to visualize the cube, false otherwise.
     */
    public boolean isVisualize() {
        System.out.print("Do you want to visualize the cube? (y/n): ");
        String input = scanner.nextLine().trim().toLowerCase();
        System.out.println();

        if (input.equals("y")) {
            return true;
        } else if (!input.equals("n")) {
            System.out.println("Invalid input. Please input 'y' or 'n'.\n");
            return isVisualize();
        }
        return false;
    }

    /**
     * Prompts the user to decide whether to save the cube to a JSON file.
     *
     * @return true if the user inputs 'y', false if the user inputs 'n'.
     */
    public boolean isSaveToJSON() {
        System.out.print("Do you want to save the cube to a JSON file? (y/n): ");
        String input = scanner.nextLine().trim().toLowerCase();
        System.out.println();

        if (input.equals("y")) {
            return true;
        } else if (!input.equals("n")) {
            System.out.println("Invalid input. Please input 'y' or 'n'.\n");
            return isSaveToJSON();
        }
        return false;
    }

    /**
     * Closes the scanner when it's no longer needed.
     * Call this method when you're done with all menu operations.
     */
    public void closeScanner() {
        scanner.close();
    }
}