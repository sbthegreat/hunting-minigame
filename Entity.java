import java.awt.*;
/**
 * The masterclass that all the objects in the game derive from.
 * @author Connor Beckett-Lemus
 */
public abstract class Entity
{
	/**
	 * The rectangular location that encapsulates the entity.
	 */
	private Rectangle location;
	/**
	 * The amount of HP the entity has.
	 */
	private int hp;
	/**
	 * The speed the entity moves at.
	 */
	private int speed;
	/**
	 * The direction the entity is moving in. The 8 cardinal directions are
	 * represented by 0-7, with 0 being north, moving clockwise as the integer
	 * representation is increased.
	 */
	private int direction;
	/**
	 * True is the entity is currently moving, false otherwise.
	 */
	private boolean moving;
	/**
	 * Creates a new entity and assigns parameters to fields.
	 * @param p The starting point of the entity.
	 * @param width The width of the entity.
	 * @param height The height of the entity.
	 * @param h The HP of the entity.
	 * @param sp The speed of the entity.
	 * @param dir The direction of the entity.
	 */
	public Entity(Point p, int width, int height, int h, int sp, int dir)
	{
		location = new Rectangle(p.x, p.y, width, height);
		hp = h;
		speed = sp;
		direction = dir;
		moving = false;
	}
	/**
	 * Returns the starting point of the entity.
	 * @return The starting point of the entity.
	 */
	public Point getLocation()
	{
		return new Point(location.x, location.y);
	}
	/**
	 * Returns the entity's width.
	 * @return The entity's width.
	 */
	public int getWidth()
	{
		return location.width;
	}
	/**
	 * Returns the entity's height.
	 * @return The entity's height.
	 */
	public int getHeight()
	{
		return location.height;
	}
	/**
	 * Returns the entity's HP.
	 * @return The entity's HP.
	 */
	public int getHp()
	{
		return hp;
	}
	/**
	 * Returns the entity's speed.
	 * @return The entity's speed.
	 */
	public int getSpeed()
	{
		return speed;
	}
	/**
	 * Returns the entity's direction.
	 * @return The entity's direction.
	 */
	public int getDirection()
	{
		return direction;
	}
	/**
	 * Determines whether or not the entity is dead.
	 * @return True if dead, false otherwise.
	 */
	public boolean isDead()
	{
		return hp <= 0;
	}
	/**
	 * Depletes the entity's HP.
	 */
	public void takeHit()
	{
		hp = 0;
	}
	/**
	 * Rotates the entity clockwise once.
	 */
	public void spinCW()
	{
		if (direction == 7)
		{
			direction = 0;
		}
		else
		{
			direction++;
		}
	}
	/**
	 * Rotates the entity counterclockwise once.
	 */
	public void spinCCW()
	{
		if (direction == 0)
		{
			direction = 7;
		}
		else
		{
			direction--;
		}
	}
	/**
	 * Sets the entity's direction
	 * @param d The new direction.
	 */
	public void setDirection(int d)
	{
		direction = d;
	}
	/**
	 * Sets the entity's starting point.
	 * @param p The new starting point.
	 */
	public void setLocation(Point p)
	{
		location.x = p.x;
		location.y = p.y;
	}
	/**
	 * Causes the entity to start moving.
	 */
	public void startMoving()
	{
		moving = true;
	}
	/**
	 * Toggles the entity's movement on and off.
	 */
	public void toggleMoving()
	{
		moving = !moving;
	}
	/**
	 * Causes the entity to stop moving.
	 */
	public void stopMoving()
	{
		moving = false;
	}
	/**
	 * Determines whether or not the entity is moving.
	 * @return True if moving, false otherwise.
	 */
	public boolean isMoving()
	{
		return moving;
	}
	/**
	 * Updates the entity's position and corresponding appearance on the panel.
	 * @param g A Graphics object used to draw the entity.
	 */
	public void update(Graphics g)
	{
		move();
		draw(g, new Point(location.x, location.y), location.width, 
													location.height, direction);
	}
	/**
	 * Moves the entity based on its direction and speed (if it's moving at all).
	 */
	public void move()
	{
		if (moving)
		{
			switch (direction)
			{
			case 0:
				location.y -= speed;
				break;
			case 1:
				// Diagonal cases go up speed/sqrt(2) along xy axes because
				// that gives a length speed diagonal, so it's in line with
				// the straight directions.
				location.x += (int) (speed / Math.sqrt(2));
				location.y -= (int) (speed / Math.sqrt(2));
				break;
			case 2:
				location.x += speed;
				break;
			case 3:
				location.x += (int) (speed / Math.sqrt(2));
				location.y += (int) (speed / Math.sqrt(2));
				break;
			case 4:
				location.y += speed;
				break;
			case 5:
				location.x -= (int) (speed / Math.sqrt(2));
				location.y += (int) (speed / Math.sqrt(2));
				break;
			case 6:
				location.x -= speed;
				break;
			case 7:
				location.x -= (int) (speed / Math.sqrt(2));
				location.y -= (int) (speed / Math.sqrt(2));
				break;
			}
		}
	}
	/**
	 * Determines whether or not the two entities have collided.
	 * @param e The entity to be compared against.
	 * @return True is the two entities are colliding, false otherwise.
	 */
	public boolean testCollision(Entity e)
	{
		return location.intersects(e.location);
	}
	/**
	 * Reverses the entity's direction and goes back slightly.
	 */
	public void rebound()
	{
		direction = (direction + 4) % 8;
		move();
	}
	/**
	 * Draws the entity according to its parameters.
	 * @param g The Graphics object to draw with.
	 * @param p The entity's starting point.
	 * @param w The entity's width.
	 * @param h The entity's height.
	 * @param dir The entity's direction.
	 */
	public abstract void draw(Graphics g, Point p, int w, int h, int dir);
}