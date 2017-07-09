import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.JPanel;

/**
 * A panel that updates and draws all the objects of the Hunting game.
 * @author Connor Beckett-Lemus 009215893
 */
public class DrawingPanel extends JPanel 
implements MouseListener, MouseMotionListener, KeyListener, Runnable
{
	/**
	 * A thread to refresh the screen.
	 */
	private Thread refresh;
	/**
	 * The hunter that the player controls.
	 */
	private Hunter hunter;
	/**
	 * Generates the quarries that randomly spawn throughout the game.
	 */
	private QuarryGenerator qGen;
	/**
	 * An ArrayList containing all quarries on the screen.
	 */
	private ArrayList<Quarry> quarries;
	/**
	 * An ArrayList containing all obstacles on the screen.
	 */
	private ArrayList<Obstacle> obstacles;
	/**
	 * The system time the game is started at, used to keep track of time.
	 */
	private long startTime;
	/**
	 * Denotes whether or not the hunter is spinning clockwise.
	 */
	private boolean spinningCW = false;
	/**
	 * Denotes whether or not the hunter is spinning counterclockwise.
	 */
	private boolean spinningCCW = false;
	/**
	 * The player's score, increased by shooting quarries.
	 */
	private int score = 0;
	/**
	 * Denotes whether or not the game is still being played.
	 */
	private boolean playing = true;
	/**
	 * The time at which the last quarry was introduced to the game.
	 */
	private long lastQuarryAdded = 0;
	/**
	 * A rectangle representing the area the game is played on. Does not include the score/time at the bottom.
	 */
	private final Rectangle GAMEFRAME = new Rectangle(0, 0, 790, 565);
	/**
	 * The number of obstacles that are spawned at the start of the game.
	 */
	private final int INITIAL_OBSTACLES = 4;
	/**
	 * The maximum number of quarries that can appear at the same time.
	 */
	private final int QUARRY_LIMIT = 3;
	/**
	 * The minimum number of seconds it takes for another quarry to spawn.
	 */
	private final int QUARRY_BUFFER = 2;
	/**
	 * The time, in seconds, that the game is played.
	 */
	private final int TIME_LIMIT = 60;
	/**
	 * Creates a new drawing panel that adds the relevant event listeners and initial obstacles.
	 */
	public DrawingPanel()
	{
		setBackground(Color.BLACK);
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		setFocusable(true);
		hunter = new Hunter(new Point(100, 100), 80, 80, 1, 10);
		startTime = System.currentTimeMillis();
		refresh = new Thread(this);
		qGen = new QuarryGenerator();
		quarries = new ArrayList<Quarry>();
		obstacles = new ArrayList<Obstacle>();
		for (int i = 0; i < INITIAL_OBSTACLES; i++)
		{
			boolean obstacleAdded = false;
			Obstacle newObstacle = null;
			while (!obstacleAdded)
			{
				// start off by assuming the obstacle will be added, change if any collision is found
				obstacleAdded = true;
				int randX = (int) (Math.random() * 790);
				int randY = (int) (Math.random() * 590);
				// 7 to 9
				int randType = (int) (Math.random() * 3) + 7;
				newObstacle = new Obstacle(new Point(randX, randY), randType);
				Rectangle obstacleOutline = new Rectangle(randX, randY, 
						newObstacle.getWidth(), newObstacle.getHeight());
				if (!GAMEFRAME.contains(obstacleOutline))
				{
					obstacleAdded = false;
				}
				if (newObstacle.testCollision(hunter))
				{
					obstacleAdded = false;
				}
				if (!obstacles.isEmpty())
				{
					for (Obstacle o : obstacles)
					{
						if (o.testCollision(newObstacle))
						{
							obstacleAdded = false;
						}
					}
				}
			}
			obstacles.add(newObstacle);
		}
		refresh.start();
	}
	/**
	 * Runs the game, refreshing once every 50 ms.
	 */
	@Override
	public void run()
	{
		try
		{
			while (true)
			{
				long currentTime = System.currentTimeMillis() / 1000;
				if (quarries.size() != QUARRY_LIMIT 
						&& currentTime - lastQuarryAdded > QUARRY_BUFFER)
				{
					// After the quarry buffer time, a quarry has a 1/50 chance
					// of being spawned every 50 ms, for another 2.5 seconds
					// of waiting on average.
					int random = (int) (Math.random() * 50);
					if (random == 0)
					{
						addQuarry();
						lastQuarryAdded = System.currentTimeMillis() / 1000;
					}
				}
				testCollisions();
				repaint();
				Thread.sleep(50);
			}
		}
		catch (InterruptedException e)
		{
			System.out.println("Thread interrupted.");
		}
	}
	/**
	 * Tests for collisions between all objects in the game and reacts accordingly.
	 */
	public void testCollisions()
	{
		Obstacle deadAnimal = null;
		for (Obstacle o : obstacles)
		{
			Point hStart = hunter.getLocation();
			Rectangle hunterOutline = new Rectangle(hStart.x, hStart.y, 
									hunter.getWidth(), hunter.getHeight());
			if (o.testCollision(hunter) || !GAMEFRAME.contains(hunterOutline))
			{
				hunter.rebound();
				hunter.stopMoving();
			}
			hunter.testHit(o);
			for (int i = 0; i < quarries.size(); i++)
			{
				Quarry current = quarries.get(i);
				Point start = current.getLocation();
				Rectangle quarryOutline = new Rectangle(start.x, start.y, 
									current.getWidth(), current.getHeight());
				if (!GAMEFRAME.intersects(quarryOutline))
				{
					quarries.remove(current);
				}
				for (Quarry q : quarries)
				{
					if (q.testCollision(current) && q != current)
					{
						q.rebound();
						current.rebound();
					}
				}
				if (o.testCollision(current))
				{
					// not entirely random - will not pick a direction that immediately
					// causes collision on the same object
					int random = (int) (Math.random() * 5 + 2); // [2, 6]
					current.rebound();
					// add the 4 to undo rebound before adding random
					current.setDirection((current.getDirection() + 4 + random) 
																		% 8);
				}
				if (current.testCollision(hunter))
				{
					hunter.rebound();
					current.rebound();
				}
				if (hunter.testHit(current))
				{
					current.takeHit();
					if (current.isDead())
					{
						score += current.getSpeed();
						String name = current.getName();
						int type;
						if (name.equals("Squirrel"))
						{
							type = 2;
						}
						else if (name.equals("Snake"))
						{
							type = 2;
						}
						else if (name.equals("Bear"))
						{
							type = 6;
						}
						else if (name.equals("Deer"))
						{
							type = 6;
						}
						else // Bison
						{
							type = 12;
						}
						deadAnimal = new Obstacle(current.getLocation(), type);
						quarries.remove(current);
					}
				}
			}
		}
		if (deadAnimal != null && !deadAnimal.testCollision(hunter))
		{
			obstacles.add(deadAnimal);
		}
	}
	/**
	 * Updates positional values of all objects and draws the game accordingly.
	 */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (!playing)
		{
			g.setColor(Color.WHITE);
			g.drawString("Time's up!", 350, 270);
			g.drawString("SCORE: " + score, 350, 300);
		}
		else
		{
			// Spins clockwise until facing left
			if (spinningCW)
			{
				hunter.spinCW();
				if (hunter.getDirection() == 6) // left
				{
					spinningCW = false;
				}
			}
			// Spins counterclockwise facing right
			if (spinningCCW)
			{
				hunter.spinCCW();
				if (hunter.getDirection() == 2) // right
				{
					spinningCCW = false;
				}
			}
			for (Obstacle o : obstacles)
			{
				o.update(g);
			}
			for (Quarry q : quarries)
			{
				q.update(g);
			}
			hunter.update(g);
			
			g.setColor(Color.BLACK);
			// Another black rectangle is filled on the bottom on top of the quarries
			// so that the part of them outside of the gameframe can't be seen on the
			// bottom when they're leaving.
			g.fillRect(0, 567, 790, 40);
			g.setColor(Color.WHITE);
			g.drawRect(GAMEFRAME.x, GAMEFRAME.y, GAMEFRAME.width - 7, 
													GAMEFRAME.height);
			g.drawString("SCORE: " + score, 10, 590);
			long timeElapsed = (System.currentTimeMillis() - startTime) / 1000;
			if (timeElapsed >= TIME_LIMIT)
			{
				playing = false;
			}
			g.drawString("TIME: " + (TIME_LIMIT - timeElapsed), 100, 590);
		}
	}
	/**
	 * Introduces a new quarry into the game while making sure it doesn't collide with any existing objects.
	 */
	public void addQuarry()
	{
		Quarry newQuarry = null;
		boolean quarryAdded = false;
		while (!quarryAdded)
		{
			// start off by assuming the quarry will be added, change if any collision is found
			quarryAdded = true;
			newQuarry = qGen.generateQuarry();
			Point start = newQuarry.getLocation();
			Rectangle quarryOutline = new Rectangle(start.x, start.y, 
								newQuarry.getWidth(), newQuarry.getHeight());
			if (!GAMEFRAME.contains(quarryOutline) 
					|| newQuarry.testCollision(hunter))
			{
				quarryAdded = false;
			}
			for (Obstacle o : obstacles)
			{
				if (o.testCollision(newQuarry))
				{
					quarryAdded = false;
				}
			}
			for (Quarry q : quarries)
			{
				if (q.testCollision(newQuarry))
				{
					quarryAdded = false;
				}
			}
		}
		newQuarry.startMoving();
		quarries.add(newQuarry);
	}
	/**
	 * When a click is made in the gameframe, the hunter fires a bullet.
	 * @param e The mouse click that triggers this method.
	 */
	@Override
	public void mouseClicked(MouseEvent e)
	{
		int clickX = e.getX();
		int clickY = e.getY();
		if (GAMEFRAME.contains(new Point(clickX, clickY)))
		{
			hunter.fireBullet();
		}
	}
	/**
	 * Unused.
	 * @param e The mouse enter that triggers this method.
	 */
	@Override
	public void mouseEntered(MouseEvent e)
	{
	}
	/**
	 * Unused.
	 * @param e The mouse exit that triggers this method.
	 */
	@Override
	public void mouseExited(MouseEvent e)
	{
	}
	/**
	 * Unused.
	 * @param e The mouse press that triggers this method.
	 */
	@Override
	public void mousePressed(MouseEvent e)
	{
	}
	/**
	 * Unused.
	 * @param e The mouse release that triggers this method.
	 */
	@Override
	public void mouseReleased(MouseEvent e)
	{
	}
	/**
	 * Unused.
	 * @param e The mouse drag that triggers this method.
	 */
	@Override
	public void mouseDragged(MouseEvent arg0)
	{
	}
	/**
	 * Changes the hunter's direction to wherever the mouse is moved relative to the hunter's current position.
	 * @param e The mouse movement that triggers this method.
	 */
	@Override
	public void mouseMoved(MouseEvent e)
	{
		int hoverX = e.getX();
		int hoverY = e.getY();
		Point hunterPoint = hunter.getLocation();
		int hunterWidth = hunter.getWidth();
		int hunterHeight = hunter.getHeight();
		// The areas adjacent to the hunter are considered N,S,E,W.
		// The areas between those are the remaining cardinal directions.
		if (hoverX > hunterPoint.x 
				&& hoverX <= hunterPoint.x + hunterWidth
				&& hoverY < hunterPoint.y)
		{
			hunter.setDirection(0);
		}
		else if (hoverX > hunterPoint.x + hunterWidth && hoverY < hunterPoint.y)
		{
			hunter.setDirection(1);
		}
		else if (hoverY >= hunterPoint.y
				&& hoverY < hunterPoint.y + hunterHeight
				&& hoverX > hunterPoint.x + hunterWidth)
		{
			hunter.setDirection(2);
		}
		else if (hoverX > hunterPoint.x + hunterWidth 
				&& hoverY >= hunterPoint.y + hunterHeight)
		{
			hunter.setDirection(3);
		}
		else if (hoverX > hunterPoint.x 
				&& hoverX <= hunterPoint.x + hunterWidth 
				&& hoverY > hunterPoint.y + hunterHeight)
		{
			hunter.setDirection(4);
		}
		else if (hoverX < hunterPoint.x 
				&& hoverY > hunterPoint.y + hunterHeight)
		{
			hunter.setDirection(5);
		}
		else if (hoverY >= hunterPoint.y 
				&& hoverY < hunterPoint.y + hunterHeight 
				&& hoverX < hunterPoint.x)
		{
			hunter.setDirection(6);
		}
		else
		{
			hunter.setDirection(7);
		}
	}
	/**
	 * Performs various actions when specific keys are pressed. 
	 * @param e The key press that triggers this method.
	 */
	@Override
	public void keyPressed(KeyEvent e)
	{
		int keyPress = e.getKeyCode();
		if (keyPress == KeyEvent.VK_SPACE)
		{
			hunter.fireBullet();
		}
		else if (keyPress == KeyEvent.VK_ENTER)
		{
			hunter.toggleMoving();
		}
		else if (keyPress == KeyEvent.VK_LEFT)
		{
			spinningCW = true;
		}
		else if (keyPress == KeyEvent.VK_RIGHT)
		{
			spinningCCW = true;
		}
		else if (keyPress == KeyEvent.VK_7)
		{
			hunter.setDirection(7);
		}
		else if (keyPress == KeyEvent.VK_8)
		{
			hunter.setDirection(0);
		}
		else if (keyPress == KeyEvent.VK_9)
		{
			hunter.setDirection(1);
		}
		else if (keyPress == KeyEvent.VK_4)
		{
			hunter.setDirection(6);
		}
		else if (keyPress == KeyEvent.VK_6)
		{
			hunter.setDirection(2);
		}
		else if (keyPress == KeyEvent.VK_1)
		{
			hunter.setDirection(5);
		}
		else if (keyPress == KeyEvent.VK_2)
		{
			hunter.setDirection(4);
		}
		else if (keyPress == KeyEvent.VK_3)
		{
			hunter.setDirection(3);
		}
	}
	/**
	 * Unused.
	 * @param e The key release that triggers this method.
	 */
	@Override
	public void keyReleased(KeyEvent arg0)
	{
	}
	/**
	 * Unused.
	 * @param e The key type that triggers this method.
	 */
	@Override
	public void keyTyped(KeyEvent arg0)
	{
	}
}