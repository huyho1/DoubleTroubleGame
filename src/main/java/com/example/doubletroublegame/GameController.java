package com.example.doubletroublegame;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Iterator;

public class GameController {
    private final String greenColor = "0x1fff35ff";
    private final String yellowColor = "0xfdff1fff";
    private final String redColor = "0xff1f1fff";

    public AnchorPane anchorPane;
    private String selectedColor;
    private String selectedNumber;

    @FXML
    private TextArea textArea;

    @FXML
    private Button playAgainButton;

    @FXML
    private ComboBox<String> colorComboBox;

    @FXML
    private ComboBox<String> numberComboBox;

    @FXML
    private Button endTurnButton;

    // activates player's turn based on the choices they chose
    @FXML
    protected void onEndButtonClick() throws IOException {
        int num = Integer.parseInt(selectedNumber);
        if (selectedColor.equals("Green")) {
            removeCircles(num,greenColor);
        }
        else if (selectedColor.equals("Yellow")) {
            removeCircles(num,yellowColor);
        }
        else {
            removeCircles(num,redColor);
        }
        textArea.appendText("Your turn: Took " + selectedNumber + " " + selectedColor + " circle(s)\n");
        colorComboBox.getSelectionModel().clearSelection();
        numberComboBox.getSelectionModel().clearSelection();
        if (countCircles(greenColor) == 0 && countCircles(yellowColor) == 0 && countCircles(redColor) == 0) {
            playAgain();
            textArea.appendText("Congrats you won!");
        }
        else {
            play();
        }
    }

    // checks for number of options based on color selected
    @FXML
    private void onColorSelected() {
        String selectedColor = colorComboBox.getValue();

        // Define options for numberComboBox based on selected color
        ObservableList<String> numbers = FXCollections.observableArrayList();
        if ("Green".equals(selectedColor)) {
            int numGreen = countCircles(greenColor);
            for (int i = 1; i <= numGreen; i++) {
                numbers.addAll(String.valueOf(i));
            }
        }
        else if ("Yellow".equals(selectedColor)) {
            int numGreen = countCircles(yellowColor);
            for (int i = 1; i <= numGreen; i++) {
                numbers.addAll(String.valueOf(i));
            }
        }
        else if ("Red".equals(selectedColor)) {
            int numGreen = countCircles(redColor);
            for (int i = 1; i <= numGreen; i++) {
                numbers.addAll(String.valueOf(i));
            }
        }

        // Update options in numberComboBox
        numberComboBox.setItems(numbers);
    }

    //loads next window if button is pressed
    @FXML
    protected void onPlayAgainButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("turn.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) playAgainButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void initialize() {
        // Disable the button initially until the changes in the two ComboBox are made
        endTurnButton.setDisable(true);

        // Un-disable the button after changes are made in the two ComboBox
        colorComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedColor = newValue;
            updateButtonDisableState();
        });
        numberComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedNumber = newValue;
            updateButtonDisableState();
        });
    }

    // computer simulation
    public void play() {
        int greenCircles = countCircles(greenColor);
        int yellowCircles = countCircles(yellowColor);
        int redCircles = countCircles(redColor);

        String greenBinary = Integer.toBinaryString(greenCircles);
        String yellowBinary = Integer.toBinaryString(yellowCircles);
        String redBinary = Integer.toBinaryString(redCircles);

        int GY = xorSum(greenBinary, yellowBinary); // green & yellow
        int GR = xorSum(greenBinary, redBinary); // green & red
        int YR = xorSum(yellowBinary, redBinary); // yellow & red
        int totalSum = xorSum(Integer.toBinaryString(GY), redBinary); // all together

        double randomNum = Math.random(); // random number

        if (totalSum == 0) { //if you're already in a losing situation, just choose a random circle
            double prob = 1.00 / (yellowCircles + greenCircles + redCircles);
            double chance = prob;
            for (Node node : anchorPane.getChildren()) {
                if (node instanceof Circle circle) {
                    if (chance >= randomNum) {
                        String color = circle.getFill().toString();
                        anchorPane.getChildren().remove(circle);
                        if (color.equals(greenColor)) {
                            textArea.appendText("Computer's Turn: Took 1 green circle(s)\n");
                        }
                        else if (color.equals(yellowColor)) {
                            textArea.appendText("Computer's Turn: Took 1 yellow circle(s)\n");
                        }
                        else {
                            textArea.appendText("Computer's Turn: Took 1 red circle(s)\n");
                        }
                        break;
                    } else {
                        chance += prob;
                    }
                }
            }
        }
        else if (greenCircles == 0 && yellowCircles == 0) { //if only red is left
            removeCircles(redCircles, redColor);
            textArea.appendText("Computer's Turn: Took " + redCircles + " red circle(s)\n");
        }
        else if (greenCircles == 0 && redCircles == 0) { //if only yellow is left
            removeCircles(yellowCircles, yellowColor);
            textArea.appendText("Computer's Turn: Took " + yellowCircles + " yellow circle(s)\n");
        }
        else if (yellowCircles == 0 && redCircles == 0) {//if only green is left
            removeCircles(greenCircles, greenColor);
            textArea.appendText("Computer's Turn: Took " + greenCircles + " green circle(s)\n");
        }
        else if (greenCircles == 0) {// if it's only yellow and red left
            if (yellowCircles > redCircles) {
                removeCircles((yellowCircles - redCircles), yellowColor);
                textArea.appendText("Computer's Turn: Took " + (yellowCircles - redCircles) + " yellow circle(s)\n");
            }
            else {
                removeCircles((redCircles - yellowCircles), redColor);
                textArea.appendText("Computer's Turn: Took " + (redCircles - yellowCircles) + " red circle(s)\n");
            }
        }
        else if (yellowCircles == 0) {// if it's only green and red left
            if (greenCircles > redCircles) {
                removeCircles((greenCircles - redCircles), greenColor);
                textArea.appendText("Computer's Turn: Took " + (greenCircles - redCircles) + " green circle(s)\n");
            }
            else {
                removeCircles((redCircles - greenCircles), redColor);
                textArea.appendText("Computer's Turn: Took " + (redCircles - greenCircles) + " red circle(s)\n");
            }
        }
        else if (redCircles == 0) {//if it's only green and yellow left
            if (greenCircles > yellowCircles) {
                removeCircles((greenCircles - yellowCircles), greenColor);
                textArea.appendText("Computer's Turn: Took " + (greenCircles - yellowCircles) + " green circle(s)\n");
            }
            else {
                removeCircles((yellowCircles - greenCircles), yellowColor);
                textArea.appendText("Computer's Turn: Took " + (yellowCircles - greenCircles) + " yellow circle(s)\n");
            }
        }
        else if (redCircles > GY) { //non zero strategy #1
            removeCircles((redCircles-GY),redColor);
            textArea.appendText("Computer's Turn: Took " + (redCircles - GY) + " red circle(s)\n");
        }
        else if (yellowCircles > GR) { //non zero strategy #2
            removeCircles((yellowCircles-GR),yellowColor);
            textArea.appendText("Computer's Turn: Took " + (yellowCircles - GR) + " yellow circle(s)\n");
        }
        else if (greenCircles > YR) { //non zero strategy #3
            removeCircles((greenCircles-YR),greenColor);
            textArea.appendText("Computer's Turn: Took " + (greenCircles - YR) + " green circle(s)\n");
        }
        else { //if nothing applies, just take away one random circle
            for (Node node : anchorPane.getChildren()) {
                if (node instanceof Circle circle) {
                    String color = circle.getFill().toString();
                    removeCircles(1,color);
                    textArea.appendText("Computer's Turn: Took 1 " + color + " circle(s)\n");
                }
            }
        }

        // if there's no circles left, the computer won!
        if (countCircles(greenColor) == 0 && countCircles(yellowColor) == 0 && countCircles(redColor) == 0) {
            try {
                playAgain();
                textArea.appendText("Computer Won! Better luck next time you simpleton human!");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // Used to spawn in playAgain button
    public void playAgain() throws IOException {
        playAgainButton.setVisible(true);
    }

    // Used to un-disable the end turn button
    private void updateButtonDisableState() {
        // Enable the button only if both ComboBoxes have selected values
        endTurnButton.setDisable(colorComboBox.getValue() == null || numberComboBox.getValue() == null);
    }

    // Used to count the # of each colored circle
    private int countCircles(String fillColor) {
        int count = 0;
        for (Node node : anchorPane.getChildren()) {
            if (node instanceof Circle circle) {
                if (circle.getFill().toString().equals(fillColor)) {
                    count++;
                }
            }
        }
        return count;
    }

    // Used to calculate the xorSum of two binary numbers
    public static int xorSum(String binary1, String binary2) {
        // Convert binary strings to integers
        int num1 = Integer.parseInt(binary1, 2);
        int num2 = Integer.parseInt(binary2, 2);

        return num1 ^ num2;
    }

    // Used to remove circles
    public void removeCircles (int count, String color) {
        Iterator<Node> iterator = anchorPane.getChildren().iterator();
        int counter = count;
        while (iterator.hasNext()) {
            Node node = iterator.next();
            if (counter == 0) {
                break;
            }
            if (node instanceof Circle circle && counter > 0) {
                String circleColor = circle.getFill().toString();
                if (circleColor.equals(color)) {
                    iterator.remove(); // Remove the circle using the iterator
                    counter--;
                }
            }
        }
    }
}