import javafx.geometry.Point2D;

/**
 * Snake.java
 * @Author: Jason Bricco
 */

public class Level4 extends Level
{
    @Override
    public void begin(Grid grid, Snake snake)
    {
        grid.clearAll();

        addBorder(grid);

        for (int x = 4; x <= 19; x++)
        {
            grid.set(x, 3, new GridItem(GridItemID.Wall, true));
            grid.set(x, 14, new GridItem(GridItemID.Wall, true));
        }

        for (int y = 4; y < 14; y++)
        {
            grid.set(4, y, new GridItem(GridItemID.Wall, true));
            grid.set(19, y, new GridItem(GridItemID.Wall, true));
        }

        grid.set(12, 3, new GridItem(GridItemID.Empty, false));
        grid.set(12, 14, new GridItem(GridItemID.Empty, false));
        grid.set(4, 9, new GridItem(GridItemID.Empty, false));
        grid.set(19, 9, new GridItem(GridItemID.Empty, false));

        snake.spawn(new Point2Di(12, 11), new Point2D(0.0, -32.0));
    }
}
