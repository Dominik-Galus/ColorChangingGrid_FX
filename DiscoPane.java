import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.Random;

/**
 * DiscoPane is a class that represents a grid of DiscoSquare objects.
 * It extends the JavaFX Pane class and contains methods for managing the grid and its squares.
 */
public class DiscoPane extends Pane {
    private final int width; // The width of the grid
    private final int height; // The height of the grid
    private final int delay; // The delay for the color change
    private final double probability; // The probability for choosing the color randomly

    GridPane gridPane = new GridPane(); // The GridPane object that represents the grid

    private final Random random = new Random(); // A Random object for generating random values

    /**
     * Returns a random delay within a range.
     * @return A long representing the random delay.
     */
    public long getRandomDelay() {
        return random.nextLong(1/2 * delay, 3/2 * delay);
    }

    /**
     * Returns a random probability.
     * @return A double representing the random probability.
     */
    public double getRandomProbability() {
        return random.nextDouble();
    }

    /**
     * Returns the probability for choosing the color randomly.
     * @return A double representing the probability.
     */
    public double getProbability() {
        return probability;
    }

    /**
     * Returns a random color.
     * @return A Color object representing the random color.
     */
    public Color getRandomColor() {
        return Color.color(random.nextDouble(), random.nextDouble(), random.nextDouble());
    }

    /**
     * Returns a DiscoSquare object at a given position in the grid.
     * @param x The x-coordinate of the square.
     * @param y The y-coordinate of the square.
     * @return A DiscoSquare object at the given position.
     */
    private DiscoSquare getSquare(int x, int y) {
        x = (x + width) % width;
        y = (y + height) % height;
        return (DiscoSquare) gridPane.getChildren().get(y + width * x);
    }

    /**
     * Returns the neighbors of a given square in the grid.
     * @param x The x-coordinate of the square.
     * @param y The y-coordinate of the square.
     * @return An array of DiscoSquare objects representing the neighbors of the square.
     */
    public DiscoSquare[] getNeighbors (int x, int y) {
        return new DiscoSquare[] {
                getSquare(x, y+1),
                getSquare(x+1, y),
                getSquare(x, y-1),
                getSquare(x-1, y)
        };
    }

    /**
     * Constructs a DiscoPane object with the given parameters.
     * @param width The width of the grid.
     * @param height The height of the grid.
     * @param delay The delay for the color change.
     * @param probability The probability for choosing the color randomly.
     */
    public DiscoPane(int width, int height, int delay, double probability) {
        this.width = width;
        this.height = height;
        this.delay = delay;
        this.probability = probability;

        // Initialize the grid and its squares
        gridPane.setPadding(new Insets(5));
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                DiscoSquare square = new DiscoSquare(this, x, y);
                gridPane.add(square, x, y);
            }
        }

        // Start all squares
        for (int i = 0; i < gridPane.getChildren().size(); i++) {
            DiscoSquare square = (DiscoSquare) gridPane.getChildren().get(i);
            square.start();
        }

        getChildren().add(gridPane);
    }

    /**
     * Stops all squares in the grid.
     */
    public void stop() {
        for (int i = 0; i < gridPane.getChildren().size(); i++) {
            DiscoSquare square = (DiscoSquare) gridPane.getChildren().get(i);
            square.stop();
        }
    }
}