package com.tubesai;

/**
 * The {@code Position} class represents a point in a 3D space with x, y, and z coordinates.
 * It provides methods to get and set the coordinates.
 * 
 * <p>Example usage:
 * <pre>
 *     Position position = new Position(1, 2, 3);
 *     int x = position.getX();
 *     position.setY(4);
 * </pre>
 * </p>
 * 
 * @author Cupas
 */
public class Position {
    private int x;
    private int y;
    private int z;

    /**
     * Constructs a new Position with the specified coordinates.
     *
     * @param x the x-coordinate of the position
     * @param y the y-coordinate of the position
     * @param z the z-coordinate of the position
     * @throws IllegalArgumentException if any coordinate is out of range (0-4)
     */
    public Position(int x, int y, int z) {
        if (!isValidCoordinate(x) || !isValidCoordinate(y) || !isValidCoordinate(z)) {
            throw new IllegalArgumentException("Coordinates must be in the range 0 to 4.");
        }
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        if (!isValidCoordinate(x)) {
            throw new IllegalArgumentException("Coordinate must be in the range 0 to 4.");
        }
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        if (!isValidCoordinate(y)) {
            throw new IllegalArgumentException("Coordinate must be in the range 0 to 4.");
        }
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        if (!isValidCoordinate(z)) {
            throw new IllegalArgumentException("Coordinate must be in the range 0 to 4.");
        }
        this.z = z;
    }

    /**
     * Returns a string representation of the Position object.
     * The string includes the values of the x, y, and z coordinates.
     *
     * @return a string representation of the Position object in the format:
     *         "Position{x=value, y=value, z=value}"
     */
    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    /**
     * Checks if the given coordinate is within the valid range (0-4).
     *
     * @param coordinate the coordinate to check
     * @return true if the coordinate is within the range 0 to 4, false otherwise
     */
    private boolean isValidCoordinate(int coordinate) {
        return coordinate >= 0 && coordinate <= 4;
    }
}
