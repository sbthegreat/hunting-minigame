import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 * The obstacles that impede the player's progress in the game.
 * @author Connor Beckett-Lemus 009215893
 */
public class Obstacle extends Entity
{
	/**
	 * The type of obstacle this is. Valid types are 2, 6, 7, 8, 9, and 12.
	 */
	private int type;
	/**
	 * Creates a new obstacle and assigns fields based on the parameters.
	 * @param p The obstacle's starting point.
	 * @param t The type of obstacle this is.
	 */
	public Obstacle(Point p, int t)
	{
		super(p, t * 10, t * 10, 1, 0, 0);
		type = t;
	}
	/**
	 * Draws the obstacle.
	 * @param g The Graphics object to draw with.
	 * @param p The obstacle's starting point.
	 * @param w The obstacle's width.
	 * @param h The obstacle's height.
	 * @param dir The obstacle's direction.
	 */
	@Override
	public void draw(Graphics g, Point p, int w, int h, int dir)
	{
		g.setColor(Color.GRAY);
		switch (type)
		{
		case 2: // Fallen squirrels/snakes
			g.setColor(new Color(0x65, 0xDB, 0x18));
			break;
		case 6: // Fallen bears/deers
			g.setColor(new Color(0xEF, 0xD8, 0x13));
			break;
		case 7:
		case 8:
		case 9: // Default obstacles of varying sizes
			g.setColor(Color.GRAY);
			break;
		case 12: // Fallen bison
			g.setColor(new Color(0x46, 0xEF, 0xE5));
			break;
		}
		g.fillRect(getLocation().x, getLocation().y, getWidth(), getHeight());
	}
}