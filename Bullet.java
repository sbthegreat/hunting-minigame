import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 * The bullet that the player fires to hunt quarries.
 * @author Connor Beckett-Lemus 009215893
 */
public class Bullet extends Entity
{
	/**
	 * Creates a new bullet and assigns parameters to its fields.
	 * @param p The bullet's starting point.
	 * @param width The bullet's width.
	 * @param height The bullet's height.
	 * @param h The bullet's HP.
	 * @param sp The bullet's speed.
	 * @param dir The bullet's direction.
	 */
	public Bullet(Point p, int width, int height, int h, int sp, int dir)
	{
		super(p, width, height, h, sp, dir);
	}
	/**
	 * Draws the bullet.
	 * @param g The Graphics object to draw with.
	 * @param p The bullet's starting point.
	 * @param w The bullet's width.
	 * @param h The bullet's height.
	 * @param dir The bullet's direction.
	 */
	@Override
	public void draw(Graphics g, Point p, int w, int h, int dir)
	{
		int xLocation = p.x;
		int yLocation = p.y;
		int startX = 0;
		int startY = 0;
		int endX = 0;
		int endY = 0;
		switch (dir)
		{
		case 0:
			startX = endX = xLocation + w / 2;
			startY = yLocation + h;
			endY = yLocation;
			break;
		case 1:
			startX = xLocation;
			startY = yLocation + h;
			endX = xLocation + w;
			endY = yLocation;
			break;
		case 2:
			startX = xLocation;
			startY = endY = yLocation + h / 2;
			endX = xLocation + w;
			break;
		case 3:
			startX = xLocation;
			startY = yLocation;
			endX = xLocation + w;
			endY = yLocation + h;
			break;
		case 4: // down
			startX = endX = xLocation + w / 2;
			startY = yLocation;
			endY = yLocation + h;
			break;
		case 5:
			startX = xLocation + w;
			startY = yLocation;
			endX = xLocation;
			endY = yLocation + h;
			break;
		case 6:
			startX = xLocation + w;
			startY = endY = yLocation + h / 2;
			endX = xLocation;
			break;
		case 7:
			startX = xLocation + w;
			startY = yLocation + h;
			endX = xLocation;
			endY = yLocation;
			break;
		}
		g.setColor(Color.GRAY);
		g.drawLine(startX, startY, endX, endY);
	}
}