package frontend.Wordlemenu2.src;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class MenuWordle extends Application {

    private static Stage mainMenuStage;
    private static Label elapsedTimeLabel;
    private static final Button[][] gameGridButtons = new Button[5][5];

    private static Stage gameStage;
    private static long startTime;
    private static final char[] letters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y'};
    private static int nextLetterIndex = 0;

   // public static void main(String[] args) {
       // launch(args);
    //}

    @Override
    public void start(Stage primaryStage) {
        mainMenuStage = primaryStage;
        mainMenuStage.setTitle("Menu Principal");

        // Start Game Button
        Button startGameButton = new Button("Start WordleGame");
        startGameButton.setStyle("-fx-background-color: orange;");
        startGameButton.setOnAction(e -> createGameUI());
        VBox mainMenuLayout = new VBox(10);
        mainMenuLayout.setAlignment(Pos.CENTER);
        mainMenuLayout.getChildren().add(startGameButton);

        Scene mainMenuScene = new Scene(mainMenuLayout, 400, 300);
        mainMenuStage.setScene(mainMenuScene);
        mainMenuStage.show();
    }

    private void createGameUI() {
        startTime = System.currentTimeMillis();
        gameStage = new Stage();
        gameStage.setTitle("Mon menu Wordle");

        BorderPane gameLayout = new BorderPane();
        Scene gameScene = new Scene(gameLayout, 800, 600);

        // Build the game UI components using JavaFX controls
        MenuBar myMenuBar = new MenuBar();
        Menu gameMenu = new Menu("Game");

        // Start Game MenuItem
        MenuItem startGameMenuItem = new MenuItem("Start Game");

        // Restart MenuItem
        MenuItem restartMenuItem = new MenuItem("Restart");
        restartMenuItem.setOnAction(e -> restartGame());
        MenuItem helpMenuItem = new MenuItem("Help");

        MenuItem highScoresMenuItem = new MenuItem("High Scores");

        gameMenu.getItems().addAll(startGameMenuItem, restartMenuItem, helpMenuItem, highScoresMenuItem);
        myMenuBar.getMenus().add(gameMenu);

        gameLayout.setTop(myMenuBar);

        VBox gamePanel = new VBox(10);
        gamePanel.setAlignment(Pos.CENTER);

        // Create the Grid
        GridPane gameGrid = new GridPane();
        gameGrid.setAlignment(Pos.CENTER);

        int cellSize = Math.min((int) (gameScene.getWidth() - 100) / 5, (int) (gameScene.getHeight() - 100) / 5);

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                Button cellButton = new Button();
                cellButton.setMinSize(cellSize, cellSize);
                gameGridButtons[row][col] = cellButton;
                gameGrid.add(cellButton, col, row);

                nextLetterIndex = (nextLetterIndex + 1) % letters.length;
            }
        }

        gamePanel.getChildren().addAll(gameGrid, createVirtualKeyboard());

        gameLayout.setCenter(gamePanel);

        HBox scorePanel = new HBox(10);
        scorePanel.setAlignment(Pos.CENTER);

        // Help Button
        Button helpButton = new Button("Help");
        helpButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Wordle Help");
            alert.setHeaderText(null);
            alert.setContentText("Bienvenue dans Wordle! \nLe but du jeu est de deviner le mot caché en un nombre limité de tentatives.\nUtilisez le clavier virtuel pour proposer des mots.\nLes lettres correctes seront affichées en vert, les lettres correctes mais mal placées en jaune.\nBonne chance!");
            alert.showAndWait();
        });

        // Restart Button
        Button restartButton = new Button("Restart");
        restartButton.setOnAction(e -> restartGame());

        // Back to Menu Button
        Button backButton = new Button("Back to Menu");
        backButton.setOnAction(e -> {
            mainMenuStage.show();
            gameStage.close();
        });

        scorePanel.getChildren().addAll(helpButton, restartButton, new Button("Get Hint"), backButton);
        gameLayout.setBottom(scorePanel);

        // Scores Panel
        HBox scoreInfoPanel = new HBox(10);
        scoreInfoPanel.setAlignment(Pos.CENTER);

        elapsedTimeLabel = new Label("Elapsed Time: 0s");
        scoreInfoPanel.getChildren().add(elapsedTimeLabel);

        scoreInfoPanel.getChildren().addAll(new Label("Win Streak:"), new Button("High Scores"), new Button("Save High Scores"));
        gameLayout.setTop(scoreInfoPanel);

        gameStage.setScene(gameScene);
        mainMenuStage.hide();
        gameStage.show();
    }

    private VBox createVirtualKeyboard() {
        VBox keyboardBox = new VBox(5);
        keyboardBox.setAlignment(Pos.CENTER);

        for (int i = 0; i < 3; i++) {
            HBox rowBox = new HBox(5);
            rowBox.setAlignment(Pos.CENTER);

            char currentChar = (char) ('A' + i * 9);

            for (int j = 0; j < 9; j++) {
                if (currentChar <= 'Z') {
                    Button letterButton = new Button(String.valueOf(currentChar));
                    letterButton.setMinSize(40, 40);
                    letterButton.setOnAction(e -> handleButtonClick(letterButton));
                    rowBox.getChildren().add(letterButton);
                    currentChar++;
                }
            }

            keyboardBox.getChildren().add(rowBox);
        }

        return keyboardBox;
    }

    private void handleButtonClick(Button button) {
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                if (gameGridButtons[row][col].getText().isEmpty()) {
                    gameGridButtons[row][col].setText(button.getText());
                    return;
                }
            }
        }
    }

    private void restartGame() {
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                gameGridButtons[row][col].setText("");
            }
        }

        VirtualKeyboard.lettersAddedToRow = 0;
        updateElapsedTime();
    }

    private static void updateElapsedTime() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = (currentTime - startTime) / 1000;
        elapsedTimeLabel.setText("Elapsed Time: " + elapsedTime + "s");
    }

    public static class VirtualKeyboard {
        private static List<String> wordList = new ArrayList<>();
        private static int currentRow = 0;
        private static int lettersAddedToRow = 0;
        private static Button lastLetterButton;

        public static void handleDeleteButtonClick() {
            if (lastLetterButton != null && lettersAddedToRow > 0) {
                lettersAddedToRow--;
                gameGridButtons[currentRow][lettersAddedToRow].setText("");
                lastLetterButton = (lettersAddedToRow > 0) ? gameGridButtons[currentRow][lettersAddedToRow - 1] : null;
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

