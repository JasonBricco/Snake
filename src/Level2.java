import javafx.geometry.Point2D;

/**
 * Snake.java
 * @Author: Jason Bricco
 */

public class Level2 extends Level
{
    @Override
    public void begin(Grid grid, Snake snake)
    {
        grid.clearAll();
        addBorder(grid);

        grid.set(4, 4, new GridItem(GridItemID.Wall, true));
        grid.set(3, 9, new GridItem(GridItemID.Wall, true));
        grid.set(4, 14, new GridItem(GridItemID.Wall, true));

        grid.set(18, 4, new GridItem(GridItemID.Wall, true));
        grid.set(20, 9, new GridItem(GridItemID.Wall, true));
        grid.set(19, 14, new GridItem(GridItemID.Wall, true));

        grid.set(12, 3, new GridItem(GridItemID.Wall, true));

        snake.spawn(new Point2Di(12, 13), new Point2D(0.0f, -32.0f));
    }
}
