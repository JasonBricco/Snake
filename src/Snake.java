/**
 * Snake.java
 * @Author: Jason Bricco
 */

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;

/**
 * The main snake class - manages properties and behavior of the snake.
 */
public class Snake
{
    /**
     * Determines what happens when the snake gets destroyed.
     */
    private enum RemoveState
    {
        None,
        LevelEnded,
        SnakeDied
    }

    // Direction to move in.
    private Point2D nextDir;

    // Stores all segments of the snake.
    private ArrayList<Segment> segments = new ArrayList<>();

    private double speed = 5.0;

    private Point2D growPos = null;
    private boolean moveFinished;

    private SnakeGame game;
    private Grid grid;
    private Image head, body;
    private Image invHead, invBody;

    private RemoveState removeState;
    private int breakSteps;

    private Sound sound;

    public Snake(SnakeGame game, Image head, Image body, Image invHead, Image invBody, Grid grid, Sound sound)
    {
        this.game = game;
        this.grid = grid;
        this.head = head;
        this.body = body;
        this.invHead = invHead;
        this.invBody = invBody;
        this.sound = sound;
    }

    public void increaseSpeed()
    {
        speed *= 1.5f;
    }

    /**
     * Spawns the snake using the level's spawn position.
     * Resets the snake to its default size.
     */
    public void spawn(Point2Di startPos, Point2D startDir)
    {
        breakSteps = 0;
        nextDir = startDir;
        removeState = RemoveState.None;

        segments.add(new Segment(head, invHead, Utils.tileToScreenPos(startPos.getX(), startPos.getY())));

        Point2D normDir = startDir.normalize();

        for (int i = 0; i < 2; i++)
        {
            Point2Di bodyP = startPos.add(new Point2D(-normDir.getX(), -normDir.getY()));
            segments.add(new Segment(body, invBody, Utils.tileToScreenPos(bodyP.getX(), bodyP.getY())));
        }

        grid.setRandom(new GridItem(GridItemID.Apple, false));
        setTargets();
    }

    public void setLevelEnded()
    {
        removeState = RemoveState.LevelEnded;
    }

    public void setMoveFinished()
    {
        moveFinished = true;
    }

    /**
     * Adds another segment to the snake and respawns the food item.
     */
    private void grow()
    {
        Segment segment = new Segment(body, invBody, growPos);
        segments.add(segment);
        growPos = null;
        grid.setRandom(new GridItem(GridItemID.Apple, false));
    }

    /**
     * If the snake collided with a power up, triggers its effect.
     */
    private void tryApplyPowerup(GridItemID id)
    {
        switch (id)
        {
            case Apple:
                game.addScore(1);
                growPos = segments.get(segments.size() - 1).getPosition();
                sound.play(SoundEffect.Pickup0);
                break;

            case Cherry:
                if (segments.size() > 1) {
                    segments.remove(segments.size() - 1);
                    sound.play(SoundEffect.Pickup1);
                }
                break;

            case GoldenApple:
                sound.play(SoundEffect.Pickup2);
                breakSteps = 30;
                break;
        }
    }

    /**
     * Sets the snake's segments as items into the grid based on its position.
     */
    private void snakePosToGrid()
    {
        grid.clearSpecific(GridItemID.Snake);

        for (int i = 0; i < segments.size(); i++)
        {
            Point2Di tileP = Utils.screenToTilePos(segments.get(i).getPosition());

            GridItem item = grid.get(tileP);
            tryApplyPowerup(item.getID());

            grid.set(tileP, new GridItem(GridItemID.Snake, true));
        }
    }

    /**
     * Using the direction the snake is moving in, sets the targets for all
     * segments to be 1 cell over in that direction. Non-head segments
     * can simply set their targets to the targets of the segment before them.
     */
    private void setTargets()
    {
        if (growPos != null)
            grow();

        Segment head = segments.get(0);

        Point2D headTarget = head.getPosition().add(nextDir);
        Point2Di tileP = Utils.screenToTilePos(headTarget);

        GridItem item = grid.get(tileP);
        snakePosToGrid();

        if (item.isObstacle())
        {
            if (item.getID() == GridItemID.Wall && breakSteps > 0)
            {
                grid.set(tileP.getX(), tileP.getY(), new GridItem(GridItemID.Empty, false));
                sound.play(SoundEffect.Break);
            }
            else
            {
                removeState = RemoveState.SnakeDied;
                sound.play(SoundEffect.Death);
            }
        }

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
        if (removeState != RemoveState.None)
        {
            remove();

            if (removeState == RemoveState.SnakeDied)
                game.scheduleGameOver();

            removeState = RemoveState.None;
            return;
        }

        if (segments.size() == 0)
            return;

        if (moveFinished)
        {
            setTargets();
            moveFinished = false;
            breakSteps--;
        }

        for (int i = 0; i < segments.size(); i++)
        {
            Segment segment = segments.get(i);
            segment.move(this, deltaTime, speed);
            Point2D pos = segment.getPosition();
            Image image = breakSteps > 0 ? segment.getInvincibleImage() : segment.getImage();
            context.drawImage(image, pos.getX(), pos.getY());
        }
    }

    /**
     * Sets the snake's direction to the given direction if the direction is valid.
     */
    public void trySetNextDir(Point2D dir)
    {
        if (segments.size() == 0)
            return;

        Point2D currentDir = segments.get(0).getDirection();

        double negX = -currentDir.getX();
        double negY = -currentDir.getY();

        Point2D negDir = new Point2D(negX, negY);

        if (!Utils.approx(dir, negDir))
            nextDir = dir;
    }

    /**
     * Removes the snake from the grid.
     */
    private void remove()
    {
        segments.clear();
        growPos = null;
        moveFinished = true;
    }
}
