/**
 * GridItem.java
 * @Author: Jason Bricco
 */

/**
 * Represents an item that can be stored on the game's grid.
 */
public class GridItem
{
    private GridItemID id;
    private boolean obstacle;

    public GridItem()
    {
        id = GridItemID.Empty;
        obstacle = false;
    }

    public GridItem(GridItemID id, boolean obstacle)
    {
        this.id = id;
        this.obstacle = obstacle;
    }

    /**
     * Returns the id for this item.
     */
    public GridItemID getID()
    {
        return id;
    }

    /**
     * Returns true if this item will destroy the snake upon contact.
     */
    public boolean isObstacle()
    {
        return obstacle;
    }
}
