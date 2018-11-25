/**
 * SnakeGame.java
 * @Author: Jason Bricco
 */

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The main game class. Manages the interface to JavaFX as well as core components
 * of the game, such as the game loop and screen switching.
 */
public class SnakeGame extends Application
{
    private Grid grid = new Grid();
    private Image[] gridItemImages = new Image[GridItemID.values().length];

    private Snake snake;

    private Text scoreText;
    private int score;
    private int foodGoal;

    private Level[] levels =
    {
        new Level1(),
        new Level2(),
        new Level3(),
        new Level4(),
        new Level5()
    };

    private Text levelText;
    private int levelID;
    private Level level;

    private long lastTime;

    private Timer timer = new Timer();

    private Scene scene;
    private BorderPane gamePane;
    private BorderPane gameOverPane;

    private Sound sound;

    private AnimationTimer gameLoop;

    public static void main(String[] args)
    {
        launch(args);
    }

    /**
     * Change the level to the level of the given id. The snake will be
     * respawned according to the level properties.
     */
    private void changeLevel(int id)
    {
        levelID = id;
        level = levels[id - 1];
        levelText.setText("Level: " + id);
        level.begin(grid, snake);
    }

    /**
     * Sets the score to the given value, updating the displayed text in the process.
     */
    private void setScore(int value)
    {
        score = value;
        scoreText.setText("Score: " + value);
    }

    /**
     * Adds the given amount of score. If the score exceeds the level threshold,
     * the level will be changed.
     */
    public void addScore(int value)
    {
        setScore(score + value);

        if (score == foodGoal)
        {
            TimerTask task = new TimerTask()
            {
                @Override
                public void run()
                {
                    foodGoal = (int)(5.0 + (foodGoal * 1.25));
                    levelID++;

                    if (levelID > levels.length)
                    {
                        levelID = 1;
                        snake.increaseSpeed();
                    }

                    changeLevel(levelID);
                }
            };

            snake.setLevelEnded();
            timer.schedule(task, 3000);
        }
    }

    /**
     * Fill the obstacle images array with images using the name as part of the path
     * to the image on disk.
     */
    private void setObstacleImages()
    {
        GridItemID[] values = GridItemID.values();

        // Skip the first IDs (Empty and Snake) as they don't load images here.
        for (int i = 2; i < gridItemImages.length; i++)
            gridItemImages[i] = new Image("Assets/" + values[i].name() + ".png");
    }

    @Override
    public void start(Stage stage)
    {
        sound = new Sound();
        stage.setTitle("SnakeGame");

        gamePane = new BorderPane();

        Canvas canvas = new Canvas(768, 576);
        gamePane.setBottom(canvas);

        GraphicsContext context = canvas.getGraphicsContext2D();

        Image background = new Image("Assets/Background.png");

        setObstacleImages();

        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);

        scoreText = new Text("Score: 0");
        scoreText.setFill(Color.LIGHTGREEN);
        scoreText.setFont(Font.font("Times New Roman", FontWeight.BOLD, 24));

        levelText = new Text();
        levelText.setFill(Color.LIGHTGREEN);
        levelText.setFont(Font.font("Times New Roman", FontWeight.BOLD, 24));

        hbox.getChildren().addAll(levelText, scoreText);
        hbox.setSpacing(50.0);
        gamePane.setTop(hbox);

        Image snakeHead = new Image("Assets/SnakeHeadUp.png");
        Image snakeBody = new Image("Assets/SnakeBody.png");
        Image invHead = new Image("Assets/SnakeHeadInv.png");
        Image invBody = new Image("Assets/SnakeBodyInv.png");

        snake = new Snake(this, snakeHead, snakeBody, invHead, invBody, grid, sound);

        lastTime = System.nanoTime();

        // Implement the game loop.
        gameLoop = new AnimationTimer()
        {
            public void handle(long time)
            {
                // Convert nanoseconds to seconds to get the frame time.
                double deltaTime = (time - lastTime) / 1000000000.0;
                lastTime = System.nanoTime();

                grid.update(timer, deltaTime);

                context.drawImage(background, 0.0, 0.0);
                grid.draw(context, gridItemImages);

                snake.update(context, deltaTime);
            }
        };

        scene = new Scene(gamePane, 768, 600, Color.BLACK);

        scene.setOnKeyPressed(e ->
        {
            KeyCode code = e.getCode();
            Point2D dir = null;

            switch (code)
            {
                case UP:
                case W:
                    dir = new Point2D(0.0, -32.0);
                    break;

                case DOWN:
                case S:
                    dir = new Point2D(0.0, 32.0);
                    break;

                case LEFT:
                case A:
                    dir = new Point2D(-32.0, 0.0);
                    break;

                case RIGHT:
                case D:
                    dir = new Point2D(32.0, 0.0);
                    break;
            }

            if (dir != null)
                snake.trySetNextDir(dir);
        });

        // Ensure the game closes properly when the red X is pressed.
        stage.setOnCloseRequest(e -> onQuit());

        stage.setScene(scene);
        stage.show();

        startGame();
    }

    /**
     * Ensures the game properly quits.
     */
    private void onQuit()
    {
        sound.stop();
        gameLoop.stop();
        System.exit(0);
    }

    /**
     * Begins the game from the start. Resets the level, score, and food goal and begins
     * the game loop.
     */
    private void startGame()
    {
        scene.setRoot(gamePane);
        gamePane.setBackground(Background.EMPTY);
        setScore(0);
        foodGoal = 5;
        gameLoop.start();
        changeLevel(1);
        sound.start();
    }

    /**
     * Schedules the game over screen to appear after a certain amount of time.
     */
    public void scheduleGameOver()
    {
        TimerTask task = new TimerTask()
        {
            @Override
            public void run()
            {
                gameOver();
            }
        };

        timer.schedule(task, 3000);
    }

    /**
     * Creates a game over screen to display after losing.
     */
    private void gameOver()
    {
        sound.stop();
        gameLoop.stop();

        gameOverPane = new BorderPane();
        gameOverPane.setBackground(Background.EMPTY);

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(20.0);

        HBox textBox = new HBox();
        textBox.setAlignment(Pos.CENTER);
        Text text = new Text("Game Over");
        text.setFill(Color.LIGHTGREEN);
        text.setFont(Font.font("Times New Roman", FontWeight.BOLD, 36));
        textBox.getChildren().add(text);

        HBox finalScoreBox = new HBox();
        finalScoreBox.setAlignment(Pos.CENTER);
        Text finalScore = new Text("Final Score: " + score);
        finalScore.setFont(Font.font("Times New Roman", FontWeight.BOLD, 24));
        finalScore.setFill(Color.LIGHTGREEN);
        finalScoreBox.getChildren().add(finalScore);

        Button retryButton = new Button("Play Again");
        Button quitButton = new Button("Quit");

        retryButton.setOnAction(e -> startGame());
        quitButton.setOnAction(e -> onQuit());

        HBox buttons = new HBox();
        buttons.setAlignment(Pos.CENTER);
        buttons.setFillHeight(true);
        buttons.setSpacing(15.0);
        buttons.getChildren().addAll(retryButton, quitButton);

        vBox.getChildren().addAll(text, finalScore, buttons);
        gameOverPane.setCenter(vBox);

        VBox bottom = new VBox();
        bottom.setAlignment(Pos.CENTER);

        Text credits = new Text("Credits");
        credits.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 18));
        credits.setFill(Color.LIGHTGREEN);

        Text author = new Text("Programmer: Jason Bricco");
        author.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 12));
        author.setFill(Color.WHITE);

        Text musicCredit = new Text("Music by: rezoner (opengameart.org)");
        musicCredit.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 12));
        musicCredit.setFill(Color.WHITE);

        Text soundCredit = new Text("Sound effects by: Little Robot Sound Factory (opengameart.org)");
        soundCredit.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 12));
        soundCredit.setFill(Color.WHITE);

        Text artCredit = new Text("Art by: Cosme (itch.io)");
        artCredit.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 12));
        artCredit.setFill(Color.WHITE);

        bottom.getChildren().addAll(credits, author, musicCredit, soundCredit, artCredit);
        gameOverPane.setBottom(bottom);

        scene.setRoot(gameOverPane);
    }
}
