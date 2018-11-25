/**
 * Grid.java
 * @Author: Jason Bricco
 */

/**
 * Base level class. All levels derive from it.
 */
public class Level
{
    /**
     * Called on level start. Allows the level to place all required grid items.
     */
    public void begin(Grid grid, Snake snake) {}

    /**
     * Adds a border of bricks around the edge of the map.
     */
    protected void addBorder(Grid grid)
    {
        int limX = grid.WIDTH - 1, limY = grid.HEIGHT - 1;

        for (int x = 1; x < grid.WIDTH - 1; x++)
        {
            grid.set(x, 0, new GridItem(GridItemID.WallUp, true));
            grid.set(x, limY, new GridItem(GridItemID.WallDown, true));
        }

        for (int y = 1; y < grid.HEIGHT - 1; y++)
        {
            grid.set(0, y, new GridItem(GridItemID.WallLeft, true));
            grid.set(limX, y, new GridItem(GridItemID.WallRight, true));
        }

        grid.set(0, 0, new GridItem(GridItemID.WallLeftUp, true));
        grid.set(0, limY, new GridItem(GridItemID.WallLeftDown, true));
        grid.set(limX, 0, new GridItem(GridItemID.WallRightUp, true));
        grid.set(limX, limY, new GridItem(GridItemID.WallRightDown, true));
    }
}
