/**
 * Point2Di.java
 * @Author: Jason Bricco
 */

import javafx.geometry.Point2D;

/**
 * Simple integer version of Point2D.
 */
public class Point2Di
{
    private int x, y;

    public Point2Di(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public Point2Di add(Point2D other)
    {
        return new Point2Di((int)(x + other.getX()), (int)(y + other.getY()));
    }
}
