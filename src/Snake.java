/**
 * Snake.java
 * @Author: Jason Bricco
 */

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;

public class Snake
{
    // Direction to move in.
    private Point2D nextDir = new Point2D(0.0, -32.0);

    // Stores all segments of the snake.
    private ArrayList<Segment> segments = new ArrayList<>();

    private Image bodyImage;
    private double speed = 5.0;

    private Point2D growPos = null;
    private boolean moveFinished;

    private SnakeGame game;
    private Grid grid;

    public Snake(SnakeGame game, Image head, Image body, Grid grid)
    {
        this.game = game;
        this.grid = grid;

        segments.add(new Segment(head, Utils.tileToScreenPos(12, 13)));

        for (int i = 0; i < 2; i++)
            segments.add(new Segment(body, Utils.tileToScreenPos(12, 14 + i)));

        bodyImage = body;
        setTargets();
    }

    public void setMoveFinished()
    {
        moveFinished = true;
    }

    private void grow()
    {
        Segment segment = new Segment(bodyImage, growPos);
        segments.add(segment);
        growPos = null;
        grid.setRandom(new GridItem(GridItemID.Apple, false));
    }

    private void snakePosToGrid()
    {
        grid.clearSpecific(GridItemID.Snake);

        for (int i = 0; i < segments.size(); i++)
        {
            Point2Di tileP = Utils.screenToTilePos(segments.get(i).getPosition());

            GridItem item = grid.get(tileP);

            if (item.getID() == GridItemID.Apple)
            {
                game.addScore(1);
                growPos = segments.get(segments.size() - 1).getPosition();
            }

            grid.set(tileP, new GridItem(GridItemID.Snake, true));
        }
    }

    private void setTargets()
    {
        if (growPos != null)
            grow();

        Segment head = segments.get(0);

        Point2D headTarget = head.getPosition().add(nextDir);
        Point2Di tileP = Utils.screenToTilePos(headTarget);

        if (grid.get(tileP).isObstacle())
        {
            segments.clear();
            return;
        }

        snakePosToGrid();

        head.setTarget(headTarget, nextDir);
        Point2D target = head.getPosition();

        for (int i = 1; i < segments.size(); i++)
        {
            Segment body = segments.get(i);
            Point2D dir = target.subtract(body.getPosition());
            body.setTarget(target, dir);
            target = body.getPosition();
        }
    }

    public void update(GraphicsContext context, double deltaTime)
    {
        if (moveFinished)
        {
            setTargets();
            moveFinished = false;
        }

        for (int i = 0; i < segments.size(); i++)
        {
            Segment segment = segments.get(i);
            segment.move(this, deltaTime, speed);
            Point2D pos = segment.getPosition();
            context.drawImage(segment.getImage(), pos.getX(), pos.getY());
        }
    }

    public void trySetNextDir(Point2D dir)
    {
        Point2D currentDir = segments.get(0).getDirection();

        double negX = -currentDir.getX();
        double negY = -currentDir.getY();

        Point2D negDir = new Point2D(negX, negY);

        if (!Utils.approx(dir, negDir))
            nextDir = dir;
    }
}
