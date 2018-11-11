/**
 * GridItem.java
 * @Author: Jason Bricco
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

    public GridItemID getID()
    {
        return id;
    }

    public boolean isObstacle()
    {
        return obstacle;
    }
}
