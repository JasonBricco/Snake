import javafx.geometry.Point2D;

/**
 * Snake.java
 * @Author: Jason Bricco
 */

public class Level1 extends Level
{
    @Override
    public void begin(Grid grid, Snake snake)
    {
        grid.clearAll();
        addBorder(grid);

        snake.spawn(new Point2Di(12, 13), new Point2D(0.0f, -32.0f));
    }
}
