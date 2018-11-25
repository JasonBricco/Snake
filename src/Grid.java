/**
 * Grid.java
 * @Author: Jason Bricco
 */

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Manages the grid - the core component of the game within which obstacles and entities live.
 */
public class Grid
{
    private Random rand = new Random();
    private double powerupSpawn;

    public Grid()
    {
        powerupSpawn = 10.0;
    }

    public final int WIDTH = 24, HEIGHT = 18;

    // Stores obstacles in the map.
    private GridItem[] grid = new GridItem[WIDTH * HEIGHT];

    /**
     * Returns the item in the grid at the given tile position.
     */
    public GridItem get(Point2Di tileP)
    {
        return grid[tileP.getY() * WIDTH + tileP.getX()];
    }

    /**
     * Sets a new item to the grid at the given tile position.
     */
    public void set(int tileX, int tileY, GridItem item)
    {
        grid[tileY * WIDTH + tileX] = item;
    }

    /**
     * Sets a new item to the grid at the given tile position.
     */
    public void set(Point2Di tileP, GridItem item)
    {
        set(tileP.getX(), tileP.getY(), item);
    }

    /**
     * Sets the given item to a random available cell in the grid.
     */
    public void setRandom(GridItem item)
    {
        int x = Utils.randomRange(rand, 1, WIDTH - 1);
        int y = Utils.randomRange(rand, 1, HEIGHT - 1);

        if (grid[y * WIDTH + x].getID() == GridItemID.Empty)
        {
            grid[y * WIDTH + x] = item;
            return;
        }

        setRandom(item);
    }

    /**
     * Clears the entire grid.
     */
    public void clearAll()
    {
        for (int i = 0; i < grid.length; i++)
            grid[i] = new GridItem();
    }

    /**
     * Removes all occurrences of the given item from the grid.
     */
    public void clearSpecific(GridItemID id)
    {
        for (int i = 0; i < grid.length; i++)
        {
            if (grid[i].getID() == id)
                grid[i] = new GridItem();
        }
    }

    /**
     * Draws all grid cells.
     */
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

    /**
     * Spawns a random power up randomly on the grid.
     */
    private void spawnPowerup(Timer timer)
    {
        boolean b = rand.nextBoolean();
        GridItem item = new GridItem(b ? GridItemID.Cherry : GridItemID.GoldenApple, false);
        setRandom(item);

        TimerTask task = new TimerTask()
        {
            @Override
            public void run()
            {
                clearSpecific(item.getID());
            }
        };

        timer.schedule(task, 10000);
    }

    /**
     * Grid's update - called every frame. Spawns a new power up every so often.
     */
    public void update(Timer timer, double deltaTime)
    {
        powerupSpawn -= deltaTime;

        if (powerupSpawn <= 0.0)
        {
            spawnPowerup(timer);
            powerupSpawn = 15.0 + (rand.nextDouble() * 10.0);
        }
    }
}
