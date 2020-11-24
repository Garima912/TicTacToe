import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Main extends Application implements  EventHandler {
    Stage primaryStage;
    Scene welcomeScene,gameScene,resultScene;
    VBox welcomeSceneBox, gameSceneBox;
    GameSceneController gameSceneController;
    Text ticTacToeTxt;
    PauseTransition showGameScene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        setUpWelcomeScene();
        primaryStage.setScene(welcomeScene);
        gameSceneBox =  new VBox();
        gameSceneController = new GameSceneController(gameSceneBox);
        gameScene =  new Scene(gameSceneBox,800,750);
        showGameScene = new PauseTransition(Duration.seconds(5));
        showGameScene.play();
        showGameScene.setOnFinished(this);
        primaryStage.show();

    }

    public void setUpWelcomeScene(){
        welcomeSceneBox =  new VBox();
        Text playTxt = new Text("Let's Play..");
        playTxt.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 30));
        playTxt.setFill(Color.RED);
        ticTacToeTxt = new Text("TIC-TAC-TOE");
        ticTacToeTxt.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 70));
        ticTacToeTxt.setFill(Color.MIDNIGHTBLUE);
        ticTacToeTxt.setStrokeWidth(2);
        ticTacToeTxt.setStroke(Color.CYAN);
        Image gamePic = new Image("/ticTacToe2.png",true);
        ImageView gamePicView1 = new ImageView(gamePic);
        gamePicView1.setFitHeight(300);
        gamePicView1.setFitWidth(600);
        welcomeSceneBox.getChildren().addAll(playTxt, ticTacToeTxt, gamePicView1);
        welcomeSceneBox.setPadding(new Insets(50,50,0,50));
        welcomeSceneBox.setBackground(new Background(new BackgroundFill(Color.rgb(252, 157, 3), CornerRadii.EMPTY, Insets.EMPTY)));
        welcomeScene =  new Scene(welcomeSceneBox,800,750);

    }

    @Override
    public void handle(Event event) {
        if(event.getSource().equals(showGameScene)){
            primaryStage.setScene(gameScene);
        }
    }
}
