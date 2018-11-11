/**
 * Segment.java
 * @Author: Jason Bricco
 */

import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public class Segment
{
    private Image image;
    private Point2D direction;
    private Point2D position;
    private Point2D start, target;

    // The percentage between start and target tiles.
    private double t;

    public Segment(Image image, Point2D position)
    {
        this.image = image;
        this.start = position;
        this.position = position;
    }

    public Image getImage()
    {
        return image;
    }

    public Point2D getPosition()
    {
        return position;
    }

    public Point2D getDirection()
    {
        return direction;
    }

    public void setTarget(Point2D target, Point2D direction)
    {
        this.target = target;
        this.direction = direction;
    }

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
