/**
 * Grid.java
 * @Author: Jason Bricco
 */

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Random;

public class Grid
{
    Random rand = new Random();
    private final int WIDTH = 24, HEIGHT = 18;

    // Stores obstacles in the map.
    private GridItem[] grid = new GridItem[WIDTH * HEIGHT];

    public GridItem get(Point2Di tileP)
    {
        return grid[tileP.getY() * WIDTH + tileP.getX()];
    }

    public void set(Point2Di tileP, GridItem item)
    {
        grid[tileP.getY() * WIDTH + tileP.getX()] = item;
    }

    public void setRandom(GridItem item)
    {
        int x = Utils.randomRange(rand, 1, WIDTH - 1);
        int y = Utils.randomRange(rand, 1, HEIGHT - 1);

        if (grid[y * WIDTH + x].getID() == GridItemID.Empty)
        {
            grid[y * WIDTH + x] = item;
            return;
        }

        // Note: not the most efficient way to do this...
        setRandom(item);
    }

    public void clearAll()
    {
        for (int i = 0; i < grid.length; i++)
            grid[i] = new GridItem();
    }

    public void clearSpecific(GridItemID id)
    {
        for (int i = 0; i < grid.length; i++)
        {
            if (grid[i].getID() == id)
                grid[i] = new GridItem();
        }
    }

    public void fill()
    {
        clearAll();

        int limX = WIDTH - 1, limY = HEIGHT - 1;

        for (int x = 1; x < WIDTH - 1; x++)
        {
            grid[0 * WIDTH + x] = new GridItem(GridItemID.WallUp, true);
            grid[limY * WIDTH + x] = new GridItem(GridItemID.WallDown, true);
        }

        for (int y = 1; y < HEIGHT - 1; y++)
        {
            grid[y * WIDTH + 0] = new GridItem(GridItemID.WallLeft, true);
            grid[y * WIDTH + limX] = new GridItem(GridItemID.WallRight, true);
        }

        grid[0 * WIDTH + 0] = new GridItem(GridItemID.WallLeftUp, true);
        grid[limY * WIDTH + 0] = new GridItem(GridItemID.WallLeftDown, true);
        grid[0 * WIDTH + limX] = new GridItem(GridItemID.WallRightUp, true);
        grid[limY * WIDTH + limX] = new GridItem(GridItemID.WallRightDown, true);
    }

    public void draw(GraphicsContext context, Image[] images)
    {
        for (int y = 0; y < HEIGHT; y++)
        {
            for (int x = 0; x < WIDTH; x++)
            {
                GridItemID ob = grid[y * WIDTH + x].getID();

                if (ob != GridItemID.Empty)
                    context.drawImage(images[ob.ordinal()], x * 32.0, y * 32.0);
            }
        }
    }
}
