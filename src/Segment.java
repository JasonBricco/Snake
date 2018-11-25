/**
 * Segment.java
 * @Author: Jason Bricco
 */

import javafx.geometry.Point2D;
import javafx.scene.image.Image;

/**
 * Represents a segment of the snake.
 */
public class Segment
{
    private Image image, invincibleImage;
    private Point2D direction;
    private Point2D position;
    private Point2D start, target;

    // The percentage between start and target tiles.
    private double t;

    public Segment(Image image, Image invincibleImage, Point2D position)
    {
        this.image = image;
        this.invincibleImage = invincibleImage;
        this.start = position;
        this.position = position;
    }

    /**
     * Returns the image the segment uses during normal state.
     */
    public Image getImage()
    {
        return image;
    }

    /**
     * Returns the image the snake uses while under the effects of the golden apple.
     */
    public Image getInvincibleImage() { return invincibleImage; }

    /**
     * Returns the segment's position in space.
     */
    public Point2D getPosition()
    {
        return position;
    }

    /**
     * Returns the current direction the snake is moving.
     */
    public Point2D getDirection()
    {
        return direction;
    }

    /**
     * Sets a target cell to move to move to.
     */
    public void setTarget(Point2D target, Point2D direction)
    {
        this.target = target;
        this.direction = direction;
    }

    /**
     * Moves this segment closer to its target.
     */
    public void move(Snake snake, double deltaTime, double speed)
    {
        if (position.equals(target))
            return;

        t = Math.min(t + (speed * deltaTime), 1.0);
        position = Utils.lerp(start, target, t);

        if (t == 1.0)
        {
            start = target;
            snake.setMoveFinished();
            t = 0.0;
        }
    }
}
