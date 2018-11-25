import javafx.geometry.Point2D;

/**
 * Snake.java
 * @Author: Jason Bricco
 */

public class Level5 extends Level
{
    @Override
    public void begin(Grid grid, Snake snake)
    {
        grid.clearAll();

        addBorder(grid);

        for (int y = 2; y < grid.HEIGHT - 2; y += 2)
        {
            for (int x = 2; x < grid.WIDTH - 2; x += 2)
                grid.set(x, y, new GridItem(GridItemID.Wall, true));
        }

        grid.set(1, grid.HEIGHT - 2, new GridItem(GridItemID.Wall, true));
        grid.set(grid.WIDTH - 2, grid.HEIGHT - 2, new GridItem(GridItemID.Wall, true));
        grid.set(grid.WIDTH - 2, 1, new GridItem(GridItemID.Wall, true));

        snake.spawn(new Point2Di(grid.WIDTH - 4, grid.HEIGHT - 2), new Point2D(-32.0, 0.0));
    }
}
