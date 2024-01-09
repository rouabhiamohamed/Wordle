package frontend;

import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.effect.Reflection;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class wordleMain extends Application {

    private Stage mainMenuStage;
    private Label elapsedTimeLabel;
    private Button[][] gameGridButtons;

    private Stage gameStage;
    private long startTime;
    private final char[] letters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y'};
    private int nextLetterIndex = 0;
    private VirtualKeyboard virtualKeyboard; // Ajout d'une instance de VirtualKeyboard


    // I added those so we could change the difficulty at will...
    private int CellsCount;
    // Contains the word to find, somehow this wasn't here already lol
    private String wordToFind;
    // This boolean is used so the program knows if it can add new letters or not
    // it effectively blocks the input until the word gets registred
    private boolean canAddLetters;
    private String wordHint;
    private Partie game;

    @Override
    public void start(Stage primaryStage) {
        mainMenuStage = primaryStage;
        mainMenuStage.setTitle("Menu Principal");

        // Start Game Button
        Button easyMode = new Button("Start a Worlde game in a 5x5 grid");
        easyMode.setStyle("-fx-background-color: green;");
        easyMode.setOnAction(e -> createGameUI(5));
        // Added two new difficulties pick
        Button medium = new Button("Start a Worlde game in a 6x5 grid");
        medium.setStyle("-fx-background-color: orange;");
        medium.setOnAction(e -> createGameUI(6));

        Button hard = new Button("Start a Worlde game in a 8x5 grid");
        hard.setStyle("-fx-background-color: red;");
        hard.setOnAction(e -> createGameUI(8));


        VBox mainMenuLayout = new VBox(30);
        mainMenuLayout.setAlignment(Pos.CENTER);
        mainMenuLayout.getChildren().add(easyMode);
        mainMenuLayout.getChildren().add(medium);
        mainMenuLayout.getChildren().add(hard);

        Scene mainMenuScene = new Scene(mainMenuLayout, 800, 768);
        mainMenuStage.setScene(mainMenuScene);
        mainMenuStage.show();

        // I made it so the time updates every seconds instead of whenever the player clicks on OK lmao
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateElapsedTime()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

    }

    /*
        So this function simply set some initial variables that are going to be used (hopefully)
        several times within the runtime of this program
     */
    public void initializeSomeVariables()
    {
        wordToFind = game.getExample();
        wordHint = Hint.getOneHint(wordToFind);
    }
    private void createGameUI(int Cells) {
        startTime = System.currentTimeMillis();
        gameStage = new Stage();
        gameStage.setTitle("Mon menu Wordle");

        //I put the initialization of the grid here since I can't do it earlier...
        //Cells means how wide, as for the 5 it means you have 5 chance to find the word
        CellsCount = Cells;
        gameGridButtons = new Button[5][CellsCount];

        // Now that I think about it, we could put the CellsCount on the constructor instead
        game = new Partie();
        game.initialization(CellsCount);
        initializeSomeVariables();

        // Allow the user to input new letters
        canAddLetters = true;

        BorderPane gameLayout = new BorderPane();
        Scene gameScene = new Scene(gameLayout, 800, 768);

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

        //int cellSize = Math.min((int) (gameScene.getWidth() - 100) / CellsCount, (int) (gameScene.getHeight() - 100) / CellsCount);
        //Since everything is hardcoded, I didn't bother and just put values so it kinda looks okayish
        int cellSize = Math.min((int) (gameScene.getWidth() - 100) / 8, (int) (gameScene.getHeight() - 100) / 8);

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < CellsCount; col++) {
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
        helpButton.getStyleClass().addAll("button-64");

        helpButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Wordle Help");
            alert.setHeaderText(null);
            alert.setContentText("Bienvenue dans Wordle! \nLe but du jeu est de deviner le mot caché en un nombre limité de tentatives.\nUtilisez le clavier virtuel pour proposer des mots.\nLes lettres correctes seront affichées en vert, les lettres correctes mais mal placées en jaune.\nBonne chance!");
            alert.showAndWait();
        });

        // Restart Button
        Button restartButton = new Button("Restart");
        restartButton.getStyleClass().addAll("button-64");

        restartButton.setOnAction(e -> restartGame());

        Button hintButton = new Button("Get Hint");
        hintButton.getStyleClass().addAll("button-64");

        // Back to Menu Button
        Button backButton = new Button("Back to Menu");
        backButton.getStyleClass().addAll("button-64");

        backButton.setOnAction(e -> {
            mainMenuStage.show();
            gameStage.close();
        });

        Button hintButton = new Button("Get Hint");
        hintButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Wordle Hint");
            alert.setHeaderText(null);
            if (null == wordHint)
                alert.setContentText("Please boot the python server in order to get the hint");
            else
                alert.setContentText("Here's your hint mate : " + wordHint);

            alert.showAndWait();
        });


        scorePanel.getChildren().addAll(helpButton, restartButton, hintButton, backButton);

        gameLayout.setBottom(scorePanel);

        // Scores Panel
        HBox scoreInfoPanel = new HBox(10);
        scoreInfoPanel.setAlignment(Pos.CENTER);

        elapsedTimeLabel = new Label("Elapsed Time: 0s");
        elapsedTimeLabel.getStyleClass().add("text");
        scoreInfoPanel.getChildren().add(elapsedTimeLabel);

        Button highScore = new Button("High Scores");
        highScore.getStyleClass().addAll("button-64");

        Button saveScore = new Button("Save High Scores");
        saveScore.getStyleClass().addAll("button-64");

        Label winStreak = new Label("Win Streak:");
        winStreak.getStyleClass().addAll("text");

        scoreInfoPanel.getChildren().addAll(winStreak,highScore,saveScore );
        gameLayout.setTop(scoreInfoPanel);

        gameStage.setScene(gameScene);
        mainMenuStage.hide();
        gameStage.show();

        // Initialisez l'instance de VirtualKeyboard
        virtualKeyboard = new VirtualKeyboard();
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
                    letterButton.getStyleClass().add("button-keyboard");
                    letterButton.setOnAction(e -> handleButtonClick(letterButton));
                    rowBox.getChildren().add(letterButton);
                    currentChar++;
                }
            }

            // Ajouter le bouton OK après Z
            if (i == 2) {
                Button okButton = new Button("OK");
                okButton.setMinSize(40, 40);
                okButton.getStyleClass().add("ok-button");
                okButton.setOnAction(e -> virtualKeyboard.handleOkButtonClick());
                rowBox.getChildren().add(okButton);
            }

            keyboardBox.getChildren().add(rowBox);
        }

        // Ajout du bouton Supprimer
        HBox additionalButtonsBox = new HBox(5);
        additionalButtonsBox.setAlignment(Pos.CENTER);

        Button deleteButton = new Button("Supprimer");
        deleteButton.setMinSize(40, 40);
        deleteButton.getStyleClass().add("delete-button");
        deleteButton.setOnAction(e -> virtualKeyboard.handleDeleteButtonClick());

        additionalButtonsBox.getChildren().addAll(deleteButton);

        keyboardBox.getChildren().add(additionalButtonsBox);

        return keyboardBox;
    }
    private void animateCellFlip(Button cellButton, int delay) {
        Timeline timeline = new Timeline();
        KeyFrame keyFrame = new KeyFrame(
                Duration.millis(delay * 200), // Délai multiplié par 100 pour créer un délai progressif
                new KeyValue(cellButton.styleProperty(), "-fx-background-color: #90ee90;", Interpolator.LINEAR)
        );

        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    //From what I understand, this code checks if we have finished a row or not, if so then it automatically
    //Register the word...
    private void handleButtonClick(Button button) {
        // If the word hasn't been validated then it can no longer register new words
        if (!canAddLetters)
            return;
        if (virtualKeyboard.currentRow < 5) { // Permet la saisie uniquement dans la première ligne
            for (int row = 0; row < 5; row++) {
                for (int col = 0; col < CellsCount; col++) {
                    if (gameGridButtons[row][col].getText().isEmpty()) {
                        gameGridButtons[row][col].setText(button.getText());
                        virtualKeyboard.lastLetterButton = gameGridButtons[row][col];
                        virtualKeyboard.lettersAddedToRow++;

                        // Mettez à jour lastLetterButton correctement
                        if (virtualKeyboard.lettersAddedToRow < CellsCount) {
                            virtualKeyboard.lastLetterButton = gameGridButtons[row][col + 1];
                        }

                        // Si la ligne actuelle est complète:
                        if (virtualKeyboard.lettersAddedToRow == CellsCount) {
                            virtualKeyboard.handleOkButtonClick();  // Validez automatiquement la ligne
                            for (int i = 0; i != CellsCount; i++) {
                                final int index = i;
                                Platform.runLater(() -> animateCellFlip(gameGridButtons[0][index], index + 1));
                            }
                        }

                        return;
                    }
                }
            }
        } else {
            System.out.println("Vous ne pouvez plus ajouter de lettres après avoir validé la première ligne.");
        }
    }


    public class VirtualKeyboard {
        private int currentRow = 0;
        private int lettersAddedToRow = 0;
        public Button lastLetterButton;

        public void handleDeleteButtonClick() {
            if (lastLetterButton != null && lettersAddedToRow > 0) {
                lettersAddedToRow--;
                lastLetterButton.setText("");  // Effacez la lettre du dernier bouton ajouté

                // Mettez à jour lastLetterButton correctement
                if (lettersAddedToRow > 0) {
                    lastLetterButton = gameGridButtons[currentRow][lettersAddedToRow - 1];
                } else {
                    // Si la ligne est vide après la suppression, mettez à jour lastLetterButton pour pointer vers la première colonne
                    lastLetterButton = gameGridButtons[currentRow][0];
                }
            }
        }

        public void handleKeyPress(String keyPressed) {
            System.out.println( KeyCode.getKeyCode(keyPressed));
            if (virtualKeyboard.currentRow < 5) {
                for (int row = 0; row < 5; row++) {
                    for (int col = 0; col < CellsCount; col++) {
                        if (gameGridButtons[row][col].getText().isEmpty()) {

                                gameGridButtons[row][col].setText(keyPressed);
                                virtualKeyboard.lastLetterButton = gameGridButtons[row][col];
                                virtualKeyboard.lettersAddedToRow++;

                                if (virtualKeyboard.lettersAddedToRow < CellsCount) {
                                    virtualKeyboard.lastLetterButton = gameGridButtons[row][col + 1];
                                }

                                if (virtualKeyboard.lettersAddedToRow == CellsCount) {
                                    virtualKeyboard.handleOkButtonClick();  // Validez automatiquement la ligne
                                    for (int i = 0; i != CellsCount; i++) {
                                        final int index = i;
                                        Platform.runLater(() -> animateCellFlip(gameGridButtons[0][index], index + 1));
                                    }
                                }

                            return;
                        }
                    }
                }
            } else {
                System.out.println("Vous ne pouvez plus ajouter de lettres après avoir validé la première ligne.");
            }
        }

        public void handleOkButtonClick() {
            StringBuilder word = new StringBuilder();
            for (int col = 0; col < lettersAddedToRow; col++) {
                word.append(gameGridButtons[currentRow][col].getText());
            }
            System.out.println("Mot validé : " + word.toString());
            int stateOfWord = game.jouer(word.toString());

            // The word has been refused by the dictionnary
            // Make stuff red or some shit
            if (stateOfWord == -1)
            {
                // Do stuff
                return;
            }

            // Player has lost, technically this is handled here as well afaik
            else if (stateOfWord == -2)
            {
                // Do stuff
                return;
            }

            // Word has been accepted, thus it can do the rest of the function below :)
            else if (stateOfWord == 0)
            {
                // Do stuff
                for (int i = 0; game.getCouleurMot().length > i; i++)
                {
                    System.out.println(game.getCouleurMot()[i]);
                }

            }

            // Right answer has been found
            else if (stateOfWord == 1)
            {
                // Do stuff
            }

            // Unlock the user input
            canAddLetters = true;

            // Vérifiez si la ligne actuelle est complète et peut être validée
            if (lettersAddedToRow == CellsCount) {

                // Passez à la ligne suivante
                currentRow++;

                // Si toutes les lignes ont été remplies
                if (currentRow == 5) {
                    System.out.println("Toutes les lignes ont été remplies. Fin du jeu !");
                } else {
                    // Effacez les lettres de la grille après avoir validé la ligne
                    clearGridLetters();

                    // Réinitialisez les lettres ajoutées à la nouvelle ligne
                    lettersAddedToRow = 0;
                    // Mettez à jour le dernier bouton pour la nouvelle ligne
                    lastLetterButton = gameGridButtons[currentRow][lettersAddedToRow];
                }
            } else {
                // Affichez un message informant l'utilisateur de compléter la ligne actuelle
                System.out.println("Veuillez compléter la ligne avant de passer à la suivante.");
            }
        }


        
       
    }

    private void clearGridLetters() {
        for (int col = 0; col < CellsCount; col++) {
            gameGridButtons[virtualKeyboard.currentRow][col].setText("");
        }

        // Réinitialisez les valeurs dans la classe VirtualKeyboard également
        virtualKeyboard.lettersAddedToRow = 0;
        virtualKeyboard.lastLetterButton = gameGridButtons[virtualKeyboard.currentRow][0];
    }


    private void restartGame() {
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < CellsCount; col++) {
                gameGridButtons[row][col].setText("");
            }
        }

        virtualKeyboard.lettersAddedToRow = 0;
        updateElapsedTime();
    }

    // Always check if shit is null, otherwise you get a crash :(
    private void updateElapsedTime() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = (currentTime - startTime) / 1000;
        if (null != elapsedTimeLabel)
            elapsedTimeLabel.setText("Elapsed Time: " + elapsedTime + "s");
    }

    public static void main(String[] args) {
        launch(args);
    }

}
