package com.tubesai;

import java.util.Scanner;

public class Menu {
    /**
     * Prompts the user to decide whether to input the cube from a JSON file.
     * 
     * This method asks the user for input via the console, expecting a 'y' or 'n' response.
     * If the user inputs 'y', the method returns true, indicating that the cube should be
     * input from a JSON file. Any other input will result in the method returning false.
     * 
     * @return true if the user wants to input the cube from a JSON file, false otherwise.
     */
    public boolean isInputFromJSON() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Do you want to input the cube from a JSON file? (y/n): ");
        String input = scanner.nextLine().trim().toLowerCase();
        System.out.println();
        scanner.close();

        if (input.equals("y")) {
            return true;
        } else if (!input.equals("n")) {
            // handle invalid input
            System.out.println("Invalid input. Please input 'y' or 'n'.\n");
            return isInputFromJSON();
        }

        return false;
    }

    /**
     * Displays a menu for selecting an algorithm to solve the cube and returns the user's choice.
     * The available algorithms are:
     * 1. Hill Climbing Side Move
     * 2. Simulated Annealing
     * 3. Genetic Algorithm
     * 
     * @return an integer representing the chosen algorithm:
     *         1 for Hill Climbing Side Move,
     *         2 for Simulated Annealing,
     *         3 for Genetic Algorithm.
     *         If an invalid choice is made, the menu is displayed again.
     */
    public int chooseAlgoMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Algorithms to solve the cube:");
        System.out.println("1. Hill Climbing Side Move");
        System.out.println("2. Simulated Annealing");
        System.out.println("3. Genetic Algorithm");
        System.out.print("Choose the algorithm: ");
        int choice = scanner.nextInt();
        System.out.println();
        scanner.close();

        switch (choice) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            default:
                System.out.println("Invalid choice. Please choose a valid algorithm.\n");
                return chooseAlgoMenu();
        }
    }

    /**
     * Prompts the user to decide whether to visualize the cube.
     * 
     * This method asks the user for input via the console, expecting 'y' for yes
     * or 'n' for no. If the input is 'y', the method returns true. If the input is 'n',
     * the method returns false. If the input is invalid, it prompts the user again
     * until a valid input is provided.
     * 
     * @return true if the user wants to visualize the cube, false otherwise.
     */
    public boolean isVisualize() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Do you want to visualize the cube? (y/n): ");
        String input = scanner.nextLine().trim().toLowerCase();
        System.out.println();
        scanner.close();

        if (input.equals("y")) {
            return true;
        } else if (!input.equals("n")) {
            // handle invalid input
            System.out.println("Invalid input. Please input 'y' or 'n'.\n");
            return isVisualize();
        }
        return false;
    }

    /**
     * Prompts the user to decide whether to save the cube to a JSON file.
     * The method will repeatedly ask the user for input until a valid response ('y' or 'n') is provided.
     *
     * @return true if the user inputs 'y', false if the user inputs 'n'.
     */
    public boolean isSaveToJSON() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Do you want to save the cube to a JSON file? (y/n): ");
        String input = scanner.nextLine().trim().toLowerCase();
        System.out.println();
        scanner.close();

        if (input.equals("y")) {
            return true;
        } else if (!input.equals("n")) {
            // handle invalid input
            System.out.println("Invalid input. Please input 'y' or 'n'.\n");
            return isSaveToJSON();
        }
        return false;
    }
}
