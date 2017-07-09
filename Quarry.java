import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 * The quarries that the player hunts in the game.
 * @author Connor Beckett-Lemus
 */
public class Quarry extends Entity
{
	/**
	 * The name of the quarry.
	 */
	private String name;
	/**
	 * The weight of the quarry.
	 */
	private int weight;
	/**
	 * Creates a new quarry and assigns parameters to fields.
	 * @param p The quarry's starting point.
	 * @param width The quarry's width.
	 * @param height The quarry's height.
	 * @param h The quarry's HP.
	 * @param sp The quarry's speed.
	 * @param n The quarry's name.
	 * @param w The quarry's weight.
	 */
	public Quarry(Point p, int width, int height, int h, int sp, String n, 
																		int w)
	{
		super(p, width, height, h, sp, 0);
		name = n;
		weight = w;
	}
	/**
	 * Creates a new quarry by copying another's quarry's parameters.
	 * @param q The quarry to be copied.
	 */
	public Quarry(Quarry q)
	{
		super(q.getLocation(), q.getWidth(), q.getHeight(), q.getHp(), 
											q.getSpeed(), q.getDirection());
		name = q.name;
		weight = q.weight;
	}
	/**
	 * Returns the quarry's name.
	 * @return The quarry's name.
	 */
	public String getName()
	{
		return name;
	}
	/**
	 * Returns the quarry's weight.
	 * @return The quarry's weight.
	 */
	public int getWeight()
	{
		return weight;
	}
	/**
	 * Draws the quarry.
	 * @param g The Graphics object to draw with.
	 * @param p The quarry's starting point.
	 * @param w The quarry's width.
	 * @param h The quarry's height.
	 * @param dir The quarry's direction.
	 */
	@Override
	public void draw(Graphics g, Point p, int w, int h, int dir)
	{
		if (name.equals("Squirrel"))
		{
			g.setColor(new Color(0xAF, 0xB7, 0x50));
		}
		else if (name.equals("Snake"))
		{
			g.setColor(new Color(0x50, 0xB7, 0x78));
		}
		else if (name.equals("Bear"))
		{
			g.setColor(new Color(0x78, 0x80, 0x50));
		}
		else if (name.equals("Deer"))
		{
			g.setColor(new Color(0xDD, 0x69, 0x06));
		}
		else // Bison
		{
			g.setColor(new Color(0x06, 0x84, 0xDD));
		}
		g.fillRect(getLocation().x, getLocation().y, getWidth(), getHeight());
	}
}