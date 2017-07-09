import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 * The hunter that the player controls.
 * @author Connor Beckett-Lemus 009215893
 */
public class Hunter extends Entity
{
	/**
	 * The bullet that the hunter fires.
	 */
	private Bullet bullet;
	/**
	 * The speed of the bullet.
	 */
	private final int BULLET_SPEED = 40;
	/**
	 * The size of the bullet.
	 */
	private final int BULLET_SIZE = 20;
	/**
	 * Creates a new hunter and assigns parameters to fields.
	 * @param p The hunter's starting point.
	 * @param w The hunter's width.
	 * @param h The hunter's height.
	 * @param hp The hunter's HP.
	 * @param sp The hunter's speed.
	 */
	public Hunter(Point p, int w, int h, int hp, int sp)
	{
		super(p, w, h, hp, sp, 0);
		bullet = new Bullet(p, BULLET_SIZE, BULLET_SIZE, 1, BULLET_SPEED, 0);
	}
	/**
	 * Causes the hunter's bullet to be fire in the direction they're currently facing.
	 */
	public void fireBullet()
	{
		Point offset = new Point(getLocation().x + 40, getLocation().y + 40);
		bullet.setLocation(offset);
		bullet.setDirection(getDirection());
		bullet.startMoving();
	}
	/**
	 * Tests to see if their bullet has hit an entity.
	 * @param e The entity being checked.
	 * @return True if the entity was hit, false otherwise.
	 */
	public boolean testHit(Entity e)
	{
		if (bullet.testCollision(e))
		{
			bullet.stopMoving();
			return true;
		}
		else
		{
			return false;
		}
	}
	/**
	 * Draws the hunter, and also updates its bullet.
	 * @param g The Graphics object to draw with.
	 * @param p The hunter's starting point.
	 * @param w The hunter's width.
	 * @param h The hunter's height.
	 * @param dir The hunter's direction.
	 */
	@Override
	public void draw(Graphics g, Point p, int w, int h, int dir)
	{
		int hunterX = p.x;
		int hunterY = p.y;
		// When the bullet isn't moving, i.e. inactive, it follows the hunter around obscured.
		if (!bullet.isMoving())
		{
			Point offset = new Point(hunterX + 40, hunterY + 40);
			bullet.setLocation(offset);
		}
		bullet.update(g);
		g.setColor(Color.BLUE);
		g.fillRect(hunterX, hunterY, w, h);
		// The head of the hunter that denotes which direction they're facing.
		g.setColor(Color.CYAN);
		int headWidth = w / 3;
		int headHeight = h / 3;
		switch (dir)
		{
		case 0:
			g.fillOval(hunterX + headWidth, hunterY, headWidth, headHeight);
			break;
		case 1:
			g.fillOval(hunterX + 2 * headWidth, hunterY, headWidth, headHeight);
			break;
		case 2:
			g.fillOval(hunterX + 2 * headWidth, hunterY + headHeight, headWidth, headHeight);
			break;
		case 3:
			g.fillOval(hunterX + 2 * headWidth, hunterY + 2 * headHeight, headWidth, headHeight);
			break;
		case 4:
			g.fillOval(hunterX + headWidth, hunterY + 2 * headHeight, headWidth, headHeight);
			break;
		case 5:
			g.fillOval(hunterX, hunterY + 2 * headHeight, headWidth, headHeight);
			break;
		case 6:
			g.fillOval(hunterX, hunterY + headHeight, headWidth, headHeight);
			break;
		case 7:
			g.fillOval(hunterX, hunterY, headWidth, headHeight);
			break;
		}
	}
}