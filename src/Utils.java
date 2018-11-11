/**
 * Utils.java
 * @Author: Jason Bricco
 */

import javafx.geometry.Point2D;
import java.util.Random;

public class Utils
{
    /**
     * Returns the screen position for the given tile coordinates.
     */
    public static Point2D tileToScreenPos(int tileX, int tileY)
    {
        return new Point2D(tileX * 32.0, tileY * 32.0);
    }

    public static Point2Di screenToTilePos(Point2D pos)
    {
        int tileX = (int)Math.floor(pos.getX() / 32.0);
        int tileY = (int)Math.floor(pos.getY() / 32.0);
        return new Point2Di(tileX, tileY);
    }

    public static double lerp(double a, double b, double t)
    {
        return a + t * (b - a);
    }
    public static Point2D lerp(Point2D a, Point2D b, double t)
    {
        double x = lerp(a.getX(), b.getX(), t);
        double y = lerp(a.getY(), b.getY(), t);
        return new Point2D(x, y);
    }

    public static boolean approx(double a, double b)
    {
        return Math.abs(a - b) < 1E-7;
    }

    public static boolean approx(Point2D a, Point2D b)
    {
        boolean approxA = approx(a.getX(), b.getX());
        boolean approxB = approx(a.getY(), b.getY());
        return approxA && approxB;
    }

    // Returns a random number between min and max.
    public static int randomRange(Random rand, int min, int max)
    {
        return Math.abs(min + rand.nextInt() % ((max + 1) - min));
    }
}
