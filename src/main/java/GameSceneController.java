import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.util.Duration;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.*;

// In the Game scene GUI, I have used Tooltips for every node to communicate with the user on what to do.
// this class switches back and forth between game screen and results screen.
public class GameSceneController implements EventHandler {

    public VBox gameScene,resultSceneBox;
    private HBox headerRow, inputsRow,playersRow ,playArea, controlsRow;
    public Text trumpScore, bidenScore, roundNum;
    private Button startGame, instructionsBtn, exitBtn;
    ComboBox trumpLevel, bidenLevel, roundsChoices;
    String trumpPlay, bidenPlay;
    int totalRounds;
    int currentRound;
    GridPane ticTacToeBoard;
    ArrayList<String> finalGameBoard;
    Label square;
    StartGamePlay startGamePlay;
    PauseTransition endResultScene;
    PauseTransition showResultsScene;
    String bidenWins = "BIDEN WON \n  TRUMP LOST";
    String trumpWins = "TRUMP WON \n  BIDEN LOST";
    String tie = "IT'S A TIE";


    public GameSceneController(VBox gameScene) {
        this.gameScene = gameScene;
        trumpPlay = "";
        bidenPlay = "";
        totalRounds = 0;
        currentRound = 0;
        gameScene.setPadding(new Insets(20,20,20,20));
        createHeaderRow();
        createInputsRow();
        createPlayersRow();
        createPlayArea();
        createControlsRow();
        gameScene.setSpacing(50);
        gameScene.setStyle("-fx-background-color: linear-gradient(to bottom,  #6a5acd, #03fcec)");
        gameScene.getChildren().addAll(headerRow, inputsRow, playersRow, playArea, controlsRow);
    }

    public void createHeaderRow(){
        headerRow =  new HBox();
        Tooltip instructionsBtnTip, exitBtnTip;
        instructionsBtnTip =  new Tooltip("Learn how to play the game here");
        exitBtnTip =  new Tooltip("Exit the game");
        instructionsBtn = new Button("How to play");
        instructionsBtn.setStyle("-fx-font: 20 arial;");
        CornerRadii corner = new CornerRadii(15);
        instructionsBtn.setPrefSize(150,40);
        instructionsBtn.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, corner, Insets.EMPTY)));
        Tooltip.install(instructionsBtn,instructionsBtnTip);

        Text ticTacToeTxt = new Text("TIC-TAC-TOE");
        ticTacToeTxt.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 40));
        ticTacToeTxt.setFill(Color.MIDNIGHTBLUE);
        ticTacToeTxt.setStrokeWidth(2);
        ticTacToeTxt.setStroke(Color.CYAN);

        exitBtn = new Button("Exit");
        exitBtn.setStyle("-fx-font: 18 arial;");
        exitBtn.setPrefSize(80,40);
        Tooltip.install(exitBtn, exitBtnTip);
        headerRow.getChildren().addAll(instructionsBtn, ticTacToeTxt, exitBtn);
        headerRow.setSpacing(80);
        headerRow.setAlignment(Pos.CENTER);
        instructionsBtn.setOnAction(this);
        exitBtn.setOnAction(this);
    }

    public void createInputsRow(){
        Tooltip trumpLevelTip, bidenLevelTip, roundsChoicesTip;
        trumpLevelTip = new Tooltip("Choose the game expertise Level for Trump");
        bidenLevelTip = new Tooltip("Choose the game expertise Level for Biden");
        roundsChoicesTip = new Tooltip("Select number of game rounds here");

        inputsRow =  new HBox();
        HBox rounds = new HBox();
        VBox roundDetails = new VBox();
        trumpLevel = new ComboBox();
        bidenLevel = new ComboBox();
        Tooltip.install(trumpLevel, trumpLevelTip);
        Tooltip.install(bidenLevel, bidenLevelTip);
        trumpLevel.getItems().addAll("Novice","Advanced", "Expert");
        bidenLevel.getItems().addAll("Novice","Advanced", "Expert");
        trumpLevel.setPromptText("Trump: Play Level");
        bidenLevel.setPromptText("Biden: Play Level");

        Text selectRoundsTxt = new Text("Select Rounds:");
        selectRoundsTxt.setStyle("-fx-font: 20 arial;");
        selectRoundsTxt.setFill(Color.MIDNIGHTBLUE);

        ObservableList<Integer> roundsList = FXCollections.observableArrayList();
        for(int i=1; i<11; i++){
            roundsList.add(i);
        }
        roundsChoices = new ComboBox(roundsList);
        Tooltip.install(roundsChoices, roundsChoicesTip);
        rounds.getChildren().addAll(selectRoundsTxt, roundsChoices);
        rounds.setSpacing(10);
        Text roundTxt = new Text("ROUND: ");
        roundTxt.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 30));
        roundTxt.setFill(Color.MIDNIGHTBLUE);
        roundTxt.setUnderline(true);
        roundNum = new Text("0");
        roundNum.setUnderline(true);
        roundNum.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 30));
        roundNum.setFill(Color.MIDNIGHTBLUE);
        HBox roundBox = new HBox();
        roundBox.getChildren().addAll(roundTxt, roundNum);
        roundDetails.getChildren().addAll(rounds, roundBox);
        roundDetails.setAlignment(Pos.CENTER_RIGHT);
        roundDetails.setSpacing(30);
        inputsRow.getChildren().addAll(trumpLevel,roundDetails, bidenLevel);
        inputsRow.setSpacing(50);
        inputsRow.setAlignment(Pos.TOP_CENTER);
        trumpLevel.setOnAction(this);
        bidenLevel.setOnAction(this);
        roundsChoices.setOnAction(this);
    }
    public void createPlayersRow(){
        playersRow = new HBox();
        Text trumpTxt = new Text("TRUMP");
        trumpTxt.setFont(Font.font("verdana", FontWeight.SEMI_BOLD, 30));
        trumpTxt.setFill(Color.DARKRED);
        trumpTxt.setUnderline(true);
        Text bidenTxt= new Text("BIDEN");
        bidenTxt.setFont(Font.font("verdana", FontWeight.SEMI_BOLD, 30));
        bidenTxt.setFill(Color.DARKBLUE);
        bidenTxt.setUnderline(true);
        Image vsLoad = new Image("/vs.jpg", true);
        ImageView vsPicture = new ImageView(vsLoad);
        vsPicture.setFitHeight(70);
        vsPicture.setFitWidth(200);
        playersRow.getChildren().addAll(trumpTxt, vsPicture, bidenTxt);
        playersRow.setAlignment(Pos.CENTER);
        playersRow.setSpacing(80);

    }
    public void createPlayArea(){
        Image crossPic = new Image("/cross.jpg");
        ImageView cross = new ImageView(crossPic);
        Image circlePic = new Image("/circle.jpg");
        ImageView circle = new ImageView(circlePic);

        playArea = new HBox();
        VBox trumpSide = new VBox();
        Text trumpScoreTxt = new Text("Score");
        trumpScoreTxt.setFill(Color.BLACK);
        trumpScoreTxt.setFont(Font.font("verdana", FontWeight.MEDIUM, 25));
        trumpScore = new Text("0");
        trumpScore.setFill(Color.DARKRED);
        trumpScore.setFont(Font.font("verdana", FontWeight.SEMI_BOLD, 30));
        trumpSide.getChildren().addAll(trumpScoreTxt, trumpScore);
        trumpSide.setSpacing(20);
        trumpSide.setAlignment(Pos.TOP_RIGHT);

        VBox bidenSide = new VBox();
        Text bidenScoreTxt = new Text("Score");
        bidenScoreTxt.setFill(Color.BLACK);
        bidenScoreTxt.setFont(Font.font("verdana", FontWeight.MEDIUM, 25));
        bidenScore = new Text("0");
        bidenScore.setFill(Color.DARKBLUE);
        bidenScore.setFont(Font.font("verdana", FontWeight.SEMI_BOLD, 30));
        bidenSide.getChildren().addAll(bidenScoreTxt, bidenScore);
        bidenSide.setSpacing(20);
        bidenSide.setAlignment(Pos.TOP_LEFT);
        HBox trumpCross = new HBox();
        trumpCross.getChildren().add(cross);
        trumpCross.setAlignment(Pos.TOP_LEFT);

        HBox bidenCircle = new HBox();
        bidenCircle.getChildren().add(circle);
        bidenCircle.setAlignment(Pos.TOP_LEFT);

        GridPane gameBoard = createTicTacToeBoard();
        playArea.getChildren().addAll(trumpCross,trumpSide, gameBoard, bidenSide, bidenCircle);

        playArea.setSpacing(20);
        playArea.setAlignment(Pos.CENTER);
    }

    public GridPane createTicTacToeBoard(){
        ticTacToeBoard = new GridPane();
        ticTacToeBoard.setAlignment(Pos.CENTER);
        ticTacToeBoard.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE );

        for(int i=0; i <3; i++){
            for(int j=0; j <3; j++){
                square =  new Label(" ");
                square.setBackground(new Background(new BackgroundFill(Color.NAVAJOWHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                square.setTextAlignment(TextAlignment.CENTER);
                square.setPrefSize(90, 90);
                square.setAlignment(Pos.CENTER);
                square.setFont(Font.font("verdana", FontWeight.BOLD, 50));
                ticTacToeBoard.add(square,i,j);
            }
        }
        ticTacToeBoard.setHgap(10);
        ticTacToeBoard.setVgap(10);
        return ticTacToeBoard;
    }

    public void createControlsRow(){
        Tooltip startButtonTip = new Tooltip("Press start to begin a round or start new set of rounds");
        controlsRow = new HBox();
        startGame = new Button("Start Game");
        Tooltip.install(startGame,startButtonTip);
        startGame.setPrefSize(200,80);
        startGame.setStyle("-fx-background-color: MIDNIGHTBLUE");
        startGame.setBackground(new Background( new BackgroundFill(Color.MIDNIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        startGame.setStyle("-fx-font: 25 verdana");
        startGame.setTextFill(Color.LIGHTCYAN);
        controlsRow.getChildren().add(startGame);
        controlsRow.setAlignment(Pos.CENTER);
        startGame.setOnAction(this);
        startGame.setDisable(true);
    }

    public void displayInstructions(){
        Alert instructionsAlert = new Alert(Alert.AlertType.INFORMATION);
        instructionsAlert.setTitle("Instructions for the Game:");
        String rules = "1. Choose game expertise levels for both the players.\n " +
                "2. Choose the number of rounds for the game to continue with the selected player levels. \n"+
                "3. After making your selections, start the game and watch either of them win or the game being a tie! \n" +
                "4. Hover over the buttons, if in a doubt what it is for! \n" +
                "5. After every round finishes, the result for the particular round will be displayed. \n"+
                "Finally, You can start a new game with new game selections only after the the selected number of rounds finish. \n"+
                "Trump or Biden ? Choose your side! WISELY!";
        instructionsAlert.setContentText(rules);
        instructionsAlert.showAndWait();
    }

    public void setUpResultScene(int currentRound){
        resultSceneBox = new VBox();
        Image gameOverPic =  new Image("/gameOver.jpg");
        ImageView gameOver = new ImageView(gameOverPic);
        gameOver.setFitWidth(800);
        gameOver.setFitHeight(300);
        Text resultsHeading = new Text("Round");
        resultsHeading.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 50));
        resultsHeading.setFill(Color.MIDNIGHTBLUE);
        resultsHeading.setUnderline(true);
        Text roundNum = new Text();
        roundNum.setText(String.valueOf(currentRound));
        roundNum.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 50));
        roundNum.setUnderline(true);
        HBox roundDetails = new HBox();
        roundDetails.getChildren().addAll(resultsHeading,roundNum);
        roundDetails.setSpacing(20);
        roundDetails.setAlignment(Pos.CENTER);

        HBox resultsBox = new HBox();
        Image resultsPic = new Image("/resultsBoard.jpg");
        ImageView resultsBoard = new ImageView(resultsPic);
        resultsBoard.setFitWidth(200);
        resultsBoard.setFitHeight(200);
        Text winner = new Text();

        // check the game results and set the winner of the game
        if(startGamePlay.getWinner() == "trump"){
            winner.setText(trumpWins);
        }
        else if(startGamePlay.getWinner() == "biden"){
            winner.setText(bidenWins);
        }
        else{
            winner.setText(tie);
        }
        winner.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 40));
        winner.setFill(Color.MIDNIGHTBLUE);
        resultsBox.getChildren().addAll(resultsBoard,winner);
        resultsBox.setSpacing(80);
        resultsBox.setAlignment(Pos.CENTER);

        resultSceneBox.setStyle("-fx-background-color: linear-gradient(to bottom,  #6a5acd, #03fcec)");
        resultSceneBox.getChildren().addAll(gameOver, roundDetails,resultsBox);
        resultSceneBox.setSpacing(30);
        resultSceneBox.setAlignment(Pos.CENTER);

    }


    @Override
    public void handle(Event event) {

        if(event.getSource().equals(instructionsBtn)){
            displayInstructions();  // hover over the button to know what that button does
        }
        if(event.getSource().equals(exitBtn)){
            Platform.exit();
        }
        if(event.getSource().equals(trumpLevel)){

            if(trumpLevel.getValue() != null && bidenLevel.getValue() != null && roundsChoices.getValue() != null){
                startGame.setDisable(false);
            }
        }
        if(event.getSource().equals(bidenLevel)){

            if(trumpLevel.getValue() != null && bidenLevel.getValue() != null &&roundsChoices.getValue() != null){
                startGame.setDisable(false);
            }
        }
        if(event.getSource().equals(roundsChoices)){

            if(trumpLevel.getValue() != null && bidenLevel.getValue() != null && roundsChoices.getValue() != null){
                startGame.setDisable(false);
            }
        }

        if(event.getSource().equals(startGame)){
            trumpPlay = trumpLevel.getValue().toString();
            bidenPlay = bidenLevel.getValue().toString();
            totalRounds = Integer.valueOf(roundsChoices.getValue().toString());
            try {
                startGamePlay = new StartGamePlay(trumpPlay, bidenPlay);
                finalGameBoard = startGamePlay.getGameBoard();
                currentRound += 1;
                roundNum.setText(String.valueOf(currentRound));
                startGame.setDisable(true);
                roundsChoices.setDisable(true);
                bidenLevel.setDisable(true);
                trumpLevel.setDisable(true);
                displayMoves(finalGameBoard);

                int changeAfter = finalGameBoard.size()*2;
                showResultsScene = new PauseTransition(Duration.seconds(changeAfter));
                showResultsScene.play();
                showResultsScene.setOnFinished(this);

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(event.getSource().equals(showResultsScene)){
            setUpResultScene(currentRound);
            gameScene.getScene().setRoot(resultSceneBox);
            endResultScene = new PauseTransition(Duration.seconds(6));
            endResultScene.play();
            endResultScene.setOnFinished(GameSceneController.this::handle);
        }
        if(event.getSource().equals(endResultScene)){
            resultSceneBox.getScene().setRoot(gameScene);
            for(int i =0; i <9; i++){
                getSquareFromBoard(i/3, i%3).setText(" ");
            }
            roundNum.setText(String.valueOf(currentRound));
            startGame.setDisable(false);
            if(currentRound == totalRounds){
                startNewGame();
            }
        }
    }
    public void startNewGame(){

        roundsChoices.getSelectionModel().clearSelection();
        totalRounds =0;
        currentRound =0;
        trumpLevel.getSelectionModel().clearSelection();
        trumpPlay = "";
        bidenLevel.getSelectionModel().clearSelection();
        bidenPlay = "";
        bidenScore.setText("0");
        trumpScore.setText("0");
        roundNum.setText("0");

        roundsChoices.setDisable(false);
        trumpLevel.setDisable(false);
        bidenLevel.setDisable(false);
        startGame.setDisable(true);
    }

    public void updateScores(){
        if(startGamePlay.getWinner() == "trump"){
            trumpScore.setText(String.valueOf(Integer.valueOf(trumpScore.getText()) +1));
        }
        else if(startGamePlay.getWinner() == "biden"){
            bidenScore.setText(String.valueOf(Integer.valueOf(bidenScore.getText())+1));
        }
        //TODO: remove this later
        else{
            trumpScore.setText(String.valueOf(Integer.valueOf(trumpScore.getText()) +1));
            bidenScore.setText(String.valueOf(Integer.valueOf(bidenScore.getText())+1));
        }
    }
    public Label getSquareFromBoard(int col, int row) {
        for (Node node : ticTacToeBoard.getChildren()) {
            if (ticTacToeBoard.getColumnIndex(node) == col && ticTacToeBoard.getRowIndex(node) == row) {
                return (Label)node;
            }
        }
        return null;
    }

    public void displayMoves( ArrayList<String> List){

        final int[] statesCounter = {0};
        Timer timeInterval = new Timer();
        timeInterval.schedule(new TimerTask() {
            @Override
            public void run() {

                statesCounter[0] += 1;
                if(statesCounter[0] == finalGameBoard.size()-1) {
                    timeInterval.cancel();
                    updateScores();
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                            String[] gameBoardMoves = finalGameBoard.get(statesCounter[0]).split(" ");
                            for (int i = 0; i < gameBoardMoves.length; i++) {
                                int x = i / 3;
                                if (gameBoardMoves[i].equals("X") && getSquareFromBoard( i % 3, x).getText() == " " ) {
                                    getSquareFromBoard( i % 3, x).setText("X");
                                    getSquareFromBoard( i % 3, x).setTextFill(Color.RED);
                                }
                                if (gameBoardMoves[i].equals("O") && getSquareFromBoard( i % 3, x).getText()== " ") {
                                   getSquareFromBoard( i % 3, x).setText("O");
                                    getSquareFromBoard( i % 3, x).setTextFill(Color.DARKBLUE);
                                }
                            }
                        }
                });
            }
        }, 0, 2000);
        System.out.println("Round complete");
    }

} // gameSceneController class ends here
