package game;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GUI extends Application {
    List<Label> labels = new ArrayList<Label>();
    public int score1 = 0;
    public int score2 = 0;
    private static int windowWidth = 800;
    private static int windowHeight = 600;
    Timeline loop;
    
    // Some data
    final private int originalCircleX = windowWidth/2;
    final private int originalCircleY = windowHeight/2;
    final private int originalPaddle1X = 40;
    final private int originalPaddle1Y = windowHeight/2 - 40;
    final private int originalPaddle2X = windowWidth - 40;
    final private int originalPaddle2Y = windowHeight/2 - 40;
    
    Circle myBall;
    Rectangle player1, player2;
    
    public void reset() {
    	player1.relocate(originalPaddle1X, originalPaddle1Y);
    	player2.relocate(originalPaddle2X, originalPaddle2Y);
    	myBall.relocate(originalCircleX, originalCircleY);
    	loop.pause();
    }
    
    @Override
    public void start(Stage primaryStage) {
		Pane canvas = new Pane();
		canvas.setPrefSize(800,600); 
		canvas.setFocusTraversable(true);
		canvas.getStyleClass().add("pane");

		Scene scene = new Scene(canvas);
		scene.getStylesheets().add("game/stylesheets.css");
		// HBox for scores
		HBox scoreBox = new HBox();
		scoreBox.setMinWidth(800);

		// Labels to display scores
		Label player1Score = new Label();
		player1Score.getStyleClass().add("score");
		player1Score.setMinWidth(400);
		player1Score.setText(score1 + "");
		
		Label player2Score = new Label();
		player2Score.getStyleClass().add("score");
		player2Score.setMinWidth(400);
		player2Score.setText(score2 + "");
		
		scoreBox.getChildren().addAll(player1Score, player2Score);

		// Create the ball
		myBall = new Circle();
		myBall.setRadius(10);
		myBall.relocate(originalCircleX, originalCircleY);
		myBall.getStyleClass().add("ball");

		// Create the paddles
		player1 = new Rectangle(5,100);
		player1.relocate(originalPaddle1X, originalPaddle1Y);
		player1.getStyleClass().add("paddle");
		
		player2 = new Rectangle(5,100);
		player2.relocate(originalPaddle2X, originalPaddle2Y);
		player2.getStyleClass().add("paddle");	
		
		HBox instructionBox = new HBox();
		Label instructions = new Label();
		instructions.setText("Press SPACE to start");
		instructionBox.getChildren().add(instructions);
		
		
		loop = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {

        	double deltaX = 3;
            double deltaY = 3;
            // Pick a random direction for the ball to start moving in
            double upOrDown = Math.random() * 2;
            @Override
            public void handle(final ActionEvent t) {
            	if (upOrDown == 0) {
            		deltaX = -deltaX;
            		deltaY = -deltaY;
            	}
                myBall.setLayoutX(myBall.getLayoutX() + deltaX);
                myBall.setLayoutY(myBall.getLayoutY() + deltaY);

                final Bounds bounds = canvas.getBoundsInLocal();
                final boolean atRightBorder = myBall.getLayoutX() >= (bounds.getMaxX() - myBall.getRadius());
                final boolean atLeftBorder = myBall.getLayoutX() <= (bounds.getMinX() + myBall.getRadius());
                final boolean atBottomBorder = myBall.getLayoutY() >= (bounds.getMaxY() - myBall.getRadius());
                final boolean atTopBorder = myBall.getLayoutY() <= (scoreBox.getHeight() + myBall.getRadius());
                final boolean hitPaddle1 = player1.getBoundsInParent().intersects(myBall.getBoundsInParent());
                final boolean hitPaddle2 = player2.getBoundsInParent().intersects(myBall.getBoundsInParent());
                
                if (atRightBorder) {
                	deltaX *= -1;
                    score1++;
            		player1Score.setText(score1 + "");
                    reset();  
                }
                if (atLeftBorder) {
                	deltaX *= -1;
                    score2++;
            		player2Score.setText(score2 + "");
                    reset();
                }
                if (hitPaddle1 || hitPaddle2) {
                	deltaX *= -1;
                }
                if (atBottomBorder || atTopBorder) {
                    deltaY *= -1;
                }
            }

        }));

		// Event handlers for keys
		final EventHandler<KeyEvent> e = new EventHandler<KeyEvent>() {
            @Override
			public void handle(final KeyEvent keyEvent) {
            	
            	// Key A = Move up paddle 1
                if (keyEvent.getCode() == KeyCode.A) {
                	// Don't let the paddle go out of the screen
                    if (!scoreBox.getBoundsInParent().intersects(player1.getBoundsInParent())) player1.setTranslateY(player1.getTranslateY() - 20);
                    keyEvent.consume();
                }
                // Key Z = Move down paddle 1
                else if (keyEvent.getCode() == KeyCode.Z) {
                	// Don't let the paddle go out of the screen
                	if (player1.getBoundsInParent().getMaxY() < windowHeight) player1.setTranslateY(player1.getTranslateY() + 20);
                    keyEvent.consume();
                }
                // Key UP = Move up paddle 2
                else if (keyEvent.getCode() == KeyCode.UP) {
                	// Don't let the paddle go out of the screen
                	if (!scoreBox.getBoundsInParent().intersects(player2.getBoundsInParent())) player2.setTranslateY(player2.getTranslateY() - 20);
                    keyEvent.consume();
                }
                // Key DOWN = Move down paddle 2
                else if (keyEvent.getCode() == KeyCode.DOWN) {
                	// Don't let the paddle go out of the screen
                	if (player2.getBoundsInParent().getMaxY() < windowHeight) player2.setTranslateY(player2.getTranslateY() + 20);
                    keyEvent.consume();
                }
                // Key SPACE to start 
                else if (keyEvent.getCode() == KeyCode.SPACE) {
                	loop.setCycleCount(Timeline.INDEFINITE);
                    loop.play();
                    keyEvent.consume();
                }
            }
        };
        
        scene.setOnKeyPressed(e);
        
		canvas.getChildren().addAll(scoreBox, myBall, player1, player2, instructions);
		
		primaryStage.setTitle("Pong");
		primaryStage.setScene(scene);
		primaryStage.show();

    }

    public static void main(String[] args) {
    	launch(args);
    }
}
