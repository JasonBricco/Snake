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
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SnakeGame extends Application
{
    private Grid grid = new Grid();
    private Image[] gridItemImages = new Image[GridItemID.values().length];

    private Snake snake;

    private Text scoreText;
    private int score = 0;

    private long lastTime;

    public static void main(String[] args)
    {
        launch(args);
    }

    public void addScore(int value)
    {
        score += value;
        scoreText.setText("Score: " + score);
    }

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
        stage.setTitle("SnakeGame");

        BorderPane root = new BorderPane();

        Canvas canvas = new Canvas(768, 576);
        root.setBottom(canvas);

        GraphicsContext context = canvas.getGraphicsContext2D();

        Image background = new Image("Assets/Background.png");

        setObstacleImages();
        grid.fill();

        Image snakeHead = new Image("Assets/SnakeHeadUp.png");
        Image snakeBody = new Image("Assets/SnakeBody.png");

        snake = new Snake(this, snakeHead, snakeBody, grid);
        grid.setRandom(new GridItem(GridItemID.Apple, false));

        lastTime = System.nanoTime();

        // Implement the game loop.
        new AnimationTimer()
        {
            public void handle(long time)
            {
                // Convert nanoseconds to seconds to get the frame time.
                double deltaTime = (time - lastTime) / 1000000000.0;
                lastTime = System.nanoTime();

                context.drawImage(background, 0.0, 0.0);
                grid.draw(context, gridItemImages);

                snake.update(context, deltaTime);
            }
        }.start();

        Scene scene = new Scene(root, 768, 600, Color.BLACK);

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

        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);

        scoreText = new Text("Score: 0");
        scoreText.setFill(Color.LIGHTGREEN);
        scoreText.setFont(Font.font("Times New Roman", FontWeight.BOLD, 24));

        hbox.getChildren().add(scoreText);
        root.setTop(hbox);

        stage.setScene(scene);
        stage.show();
    }
}
