import javafx.geometry.Point2D;

/**
 * Snake.java
 * @Author: Jason Bricco
 */

public class Level3 extends Level
{
    @Override
    public void begin(Grid grid, Snake snake)
    {
        grid.clearAll();

        addBorder(grid);

        for (int x = 1; x < 23; x++)
        {
            grid.set(x, 5, new GridItem(GridItemID.Wall, true));
            grid.set(x, 10, new GridItem(GridItemID.Wall, true));
        }

        for (int i = 0; i < 3; i++)
        {
            grid.set(5 + i, 5, new GridItem(GridItemID.Empty, false));
            grid.set(15 + i, 10, new GridItem(GridItemID.Empty, false));
        }

        snake.spawn(new Point2Di(20, 15), new Point2D(-32.0, 0.0));
    }
}
