import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * DiscoApp is the main class for the application.
 * It extends the JavaFX Application class and contains the main method to launch the application.
 */
public class DiscoApp extends Application {
    /**
     * The main method to launch the application.
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        Application.launch();
    }

    /**
     * The start method to initialize the application.
     * It creates a configuration popup for the user to input the parameters for the DiscoPane.
     * @param stage The primary stage for the application.
     */
    @Override
    public void start(Stage stage) {
        // Create a new stage for the configuration popup
        Stage popupStage = new Stage();
        popupStage.setTitle("Disco Configuration");

        // Create text fields for the user to input the parameters
        TextField widthField = new TextField();
        widthField.setPromptText("Width");
        TextField heightField = new TextField();
        heightField.setPromptText("Height");
        TextField delayField = new TextField();
        delayField.setPromptText("Delay");
        TextField probabilityField = new TextField();
        probabilityField.setPromptText("Probability");

        // Create a start button that creates the DiscoPane with the input parameters when clicked
        Button startButton = new Button("Start");
        startButton.setOnAction(e -> {
            try {
                // Parse the input parameters
                int width = Integer.parseInt(widthField.getText());
                int height = Integer.parseInt(heightField.getText());
                int delay = Integer.parseInt(delayField.getText());
                double probability = Double.parseDouble(probabilityField.getText());

                // Validate the input parameters
                if (width <= 0) {
                    throw new IllegalArgumentException("Invalid width value");
                }
                if (height <= 0) {
                    throw new IllegalArgumentException("Invalid height value");
                }
                if (delay <= 0) {
                    throw new IllegalArgumentException("Invalid delay value");
                }
                if (probability < 0 || probability > 1) {
                    throw new IllegalArgumentException("Invalid probability value");
                }

                // Create the DiscoPane with the input parameters
                DiscoPane pane = new DiscoPane(width, height, delay, probability);
                Scene scene = new Scene(pane);
                stage.setTitle("Disco");
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
                stage.setOnCloseRequest(event -> pane.stop());

                // Close the configuration popup
                popupStage.close();

            } catch (IllegalArgumentException ex ) {
                System.out.println("Error: " + ex.getMessage());
            }
        });

        // Create a VBox to hold the text fields and the start button
        VBox vbox = new VBox(10, widthField, heightField, delayField, probabilityField, startButton);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(javafx.geometry.Pos.CENTER);

        // Set the scene for the configuration popup
        Scene popupScene = new Scene(vbox);
        popupStage.setScene(popupScene);
        popupStage.show();
    }
}
