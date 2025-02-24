package com.tucil1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.utils.Piece;
import com.utils.Solver;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

public class MainController {
    @FXML
    private TextArea dataInputField;

    @FXML
    private Text errorText;

    @FXML
    private Text casesText;

    @FXML
    private Text timeText;

    @FXML
    private GridPane outputGrid;

    private Color getColorForcharacter(char character) {
        double hue = (character - 'A') * (360.0 / 26.0);
        return Color.hsb(hue, 0.7, 0.65);
    }

    @FXML
    public void initialize() {
        dataInputField.setText("");
    }

    @FXML
    public void handleFileUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showOpenDialog(dataInputField.getScene().getWindow());
        if (file != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                dataInputField.setText(content.toString());
            } catch (IOException e) {
                casesText.setText("");
                timeText.setText("");
                outputGrid.getChildren().clear();
                errorText.setText("Error reading file: " + e.getMessage());
            }
        }
    }

    @FXML
    public void handleSolve() {
        String input = dataInputField.getText();
        if (input.isEmpty()) {
            errorText.setText("Input empty");
            casesText.setText("");
            timeText.setText("");
            outputGrid.getChildren().clear();
            return;
        }
        int Rows, Columns;
        int piecesCount = 0;
        List<List<Piece>> allPieces = new ArrayList<>();
        char[][] board = new char[0][];
        try (BufferedReader reader = new BufferedReader(new StringReader(input))) {
            String line = reader.readLine();
            if (line != null) {
                String[] parts = line.split(" ");
                if (parts.length != 3) {
                    errorText
                            .setText("First line must contain exactly three numbers (rows, columns, number of pieces)");
                    casesText.setText("");
                    timeText.setText("");
                    outputGrid.getChildren().clear();
                    return;
                }
                try {
                    Rows = Integer.parseInt(parts[0]);
                    Columns = Integer.parseInt(parts[1]);
                    piecesCount = Integer.parseInt(parts[2]);
                } catch (NumberFormatException e) {
                    errorText.setText("First line must contain only numbers");
                    casesText.setText("");
                    timeText.setText("");
                    outputGrid.getChildren().clear();
                    return;
                }
                if (Rows <= 0 || Columns <= 0 || piecesCount > 26) {
                    errorText.setText("Rows and Columns must be above 0, Number of pieces must be below 27");
                    casesText.setText("");
                    timeText.setText("");
                    outputGrid.getChildren().clear();
                    return;
                }
                board = new char[Rows][Columns];
                for (int i = 0; i < Rows; i++) {
                    for (int j = 0; j < Columns; j++) {
                        board[i][j] = ' ';
                    }
                }
            }
            line = reader.readLine();
            if (!line.equals("DEFAULT")) {
                errorText.setText("Only DEFAULT available");
                casesText.setText("");
                timeText.setText("");
                outputGrid.getChildren().clear();
                return;
            }
            int inputPiecesCount = 0;
            Piece currentPiece = null;
            char previousChar = '*';
            char[] usedChar = new char[26];
            while ((line = reader.readLine()) != null) {
                line = line.stripTrailing();
                char currentChar = ' ';
                for (char c : line.toCharArray()) {
                    if (c != ' ') {
                        currentChar = c;
                        break;
                    }
                }
                if (currentChar != previousChar) {
                    for (int i = 0; i < 26; i++) {
                        if (currentChar == usedChar[i]) {
                            errorText.setText("Letter " + currentChar + " has been used before!");
                            casesText.setText("");
                            timeText.setText("");
                            outputGrid.getChildren().clear();
                            return;
                        }
                    }
                    if (currentPiece != null && (currentPiece.getLength() > 0)) {
                        usedChar[inputPiecesCount] = previousChar;
                        inputPiecesCount++;
                        if (currentPiece.validatePiece()) {
                            List<Piece> newList = new ArrayList<>();
                            newList.add(currentPiece);
                            newList.add(currentPiece.rotate90());
                            newList.add(currentPiece.rotate90().rotate90());
                            newList.add(currentPiece.rotate90().rotate90().rotate90());
                            newList.add(currentPiece.rotate90().rotate90().rotate90().flipHorizontal());
                            newList.add(currentPiece.rotate90().rotate90().flipHorizontal());
                            newList.add(currentPiece.rotate90().flipHorizontal());
                            newList.add(currentPiece.flipHorizontal());
                            allPieces.add(newList);
                        } else {
                            errorText.setText("Piece " + previousChar + " is not valid!");
                            casesText.setText("");
                            timeText.setText("");
                            outputGrid.getChildren().clear();
                            return;
                        }
                    }
                    currentPiece = new Piece();
                    previousChar = currentChar;
                }
                if (currentPiece != null & !line.isEmpty()) {
                    currentPiece.addLineBelow(line.toCharArray());
                }
            }
            if (currentPiece != null) {
                inputPiecesCount++;
                if (currentPiece.validatePiece()) {
                    List<Piece> newList = new ArrayList<>();
                    newList.add(currentPiece);
                    newList.add(currentPiece.rotate90());
                    newList.add(currentPiece.rotate90().rotate90());
                    newList.add(currentPiece.rotate90().rotate90().rotate90());
                    newList.add(currentPiece.rotate90().rotate90().rotate90().flipHorizontal());
                    newList.add(currentPiece.rotate90().rotate90().flipHorizontal());
                    newList.add(currentPiece.rotate90().flipHorizontal());
                    newList.add(currentPiece.flipHorizontal());
                    allPieces.add(newList);
                } else {
                    errorText.setText("Piece " + previousChar + " is not valid!");
                    casesText.setText("");
                    timeText.setText("");
                    outputGrid.getChildren().clear();
                    return;
                }
            }
            if (inputPiecesCount != piecesCount) {
                errorText.setText("Number of pieces input not the same with input from first line!");
                casesText.setText("");
                timeText.setText("");
                outputGrid.getChildren().clear();
                return;
            }
            errorText.setText("");
        } catch (IOException e) {
            errorText.setText("Invalid input");
        }
        long startTime = System.nanoTime();
        Solver solver = new Solver(board, 0);
        Solver result = solver.solveBoard(board, allPieces);
        long endTime = System.nanoTime();
        char[][] solution = result.board;
        long cases = result.casesTried;
        if (Arrays.deepEquals(solution, board)) {
            errorText.setText("Solution not found!");
            casesText.setText("");
            timeText.setText("");
            outputGrid.getChildren().clear();
            return;
        }
        for (char[] row : solution) {
            for (char cell : row) {
                if (cell == ' ') {
                    errorText.setText("Solution contains empty cells!");
                    casesText.setText("");
                    timeText.setText("");
                    outputGrid.getChildren().clear();
                    return;
                }
            }
        }
        updateOutputDisplay(solution);
        casesText.setText(String.valueOf(cases));
        long duration = endTime - startTime;
        double durationInMilliseconds = duration / 1_000_000.0;
        timeText.setText(String.format("Time: %.3f ms", durationInMilliseconds));
        StringBuilder solutionString = new StringBuilder();
        for (char[] row : solution) {
            for (char cell : row) {
                solutionString.append(cell);
            }
            solutionString.append("\n");
        }
        this.solutionText = solutionString.toString();
    }

    private String solutionText;

    private void updateOutputDisplay(char[][] solution) {
        outputGrid.getChildren().clear();
        outputGrid.getStyleClass().add("output-grid");

        for (int row = 0; row < solution.length; row++) {
            for (int col = 0; col < solution[row].length; col++) {
                char character = solution[row][col];

                StackPane cell = new StackPane();

                Rectangle background = new Rectangle(40, 40);
                background.setFill(getColorForcharacter(character));
                background.setArcHeight(10);
                background.setArcWidth(10);

                Label characterLabel = new Label(String.valueOf(character));
                characterLabel.setTextFill(Color.WHITE);
                characterLabel.setStyle("-fx-font-weight: bold;");

                cell.getChildren().addAll(background, characterLabel);

                outputGrid.add(cell, col, row);
            }
        }
    }

    @FXML
    public void handleDownload() {
        String result = solutionText;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Solution");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try (FileWriter fileWriter = new FileWriter(fileChooser.getSelectedFile() +
                    ".txt")) {
                fileWriter.write(result);
                errorText.setText("Solution downloaded successfully.");
            } catch (IOException e) {
                errorText.setText("Error: Unable to save the file.");
            }
        }
    }
}