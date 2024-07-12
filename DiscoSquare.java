import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

/**
 * DiscoSquare is a class that represents a square in the DiscoPane grid.
 * It extends the JavaFX Rectangle class and implements the Runnable interface.
 */
public class DiscoSquare extends Rectangle implements Runnable {
    private final int x; // The x-coordinate of the square
    private final int y; // The y-coordinate of the square
    private final DiscoPane pane; // The DiscoPane object that the square belongs to
    private Color color; // The color of the square
    private boolean threadSuspended; // A flag indicating whether the thread is suspended

    Thread thread; // The thread that runs the square

    /**
     * Constructs a DiscoSquare object with the given parameters.
     * @param pane The DiscoPane object that the square belongs to.
     * @param x The x-coordinate of the square.
     * @param y The y-coordinate of the square.
     */
    public DiscoSquare(DiscoPane pane, int x, int y) {
        super();
        this.x = x;
        this.y = y;
        this.pane = pane;

        setWidth(50);
        setHeight(50);
        color = pane.getRandomColor(); // Set the initial color to a random color
        setFill(color);

        setStroke(Color.CORAL);
        setStrokeType(StrokeType.INSIDE);
        setStrokeWidth(0);

        // Add a click event listener to the square
        this.setOnMouseClicked(e -> {
            toggleSuspension();
            if (threadSuspended) {
                setStrokeWidth(5);
                System.out.println(x + "x" + y + ": Suspended");
            } else {
                setStrokeWidth(0);
            }
        });
    }

    /**
     * Toggles the suspension of the thread.
     */
    private void toggleSuspension() {
        if (threadSuspended) {
            start();
        } else {
            stop();
        }
    }

    /**
     * Starts the thread.
     */
    public void start() {
        threadSuspended = false;
        thread = new Thread(this);
        thread.start();
    }

    /**
     * Stops the thread.
     */
    public void stop() {
        threadSuspended = true;
    }

    /**
     * Returns whether the thread is suspended.
     * @return A boolean indicating whether the thread is suspended.
     */
    synchronized public boolean isSuspended() {
        return threadSuspended;
    }

    /**
     * Sets the color of the square.
     * @param color The color to set.
     */
    synchronized private void setColor(Color color) {
        Platform.runLater(() -> setFill(color));
    }

    /**
     * Returns the color of the square.
     * @return A Color object representing the color of the square.
     */
    synchronized public Color getColor() {
        return color;
    }

    /**
     * The run method for the Runnable interface.
     * It continuously updates the color of the square randomly or based on the colors of its neighbors.
     */
    @Override
    public void run() {
        System.out.println(x + "x" + y + ": Running");

        DiscoSquare[] neighbors = pane.getNeighbors(x, y); // Get the neighbors of the square

        while (true) {
            try {
                Thread.sleep(pane.getRandomDelay());

                if (threadSuspended) { // If the thread is suspended, break
                    break;
                }

                synchronized (pane) { // Synchronize on the DiscoPane object to avoid concurrent modification
                    System.out.println(x + "x" + y + ": Start");
                    if (pane.getProbability() > pane.getRandomProbability()) { // If the probability is greater than the random probability, set a random color
                        color = pane.getRandomColor();
                        setColor(color);
                    } else { // Otherwise, calculate the average color of active neighbors
                        double[] rgb = {0, 0, 0};
                        int neighborCount = 0;
                        for (DiscoSquare neighbor : neighbors) { // For each neighbor, get the color and add it to the sum
                            if (!neighbor.isSuspended()) {
                                Color neighborColor = neighbor.getColor();
                                rgb[0] += neighborColor.getRed();
                                rgb[1] += neighborColor.getGreen();
                                rgb[2] += neighborColor.getBlue();
                                neighborCount++;
                            }
                        }
                        if (neighborCount != 0) { // If there are neighbors, calculate the average color and set it
                            color = Color.color(rgb[0] / neighborCount, rgb[1] / neighborCount, rgb[2] / neighborCount);
                            Platform.runLater(() -> setFill(color));
                        }
                    }
                    System.out.println(x + "x" + y + ": End");
                }

            } catch (InterruptedException e) { // If the thread is interrupted, break
                System.out.println(x + "x" + y + ": Interrupted");
                break;
            }
        }
    }
}