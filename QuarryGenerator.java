import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * A class that randomly generates quarries that the player will hunt.
 * @author Connor Beckett-Lemus 009215893
 */
public class QuarryGenerator
{
	/**
	 * An ArrayList containg all possible types of quarries.
	 */
	private ArrayList<Quarry> quarry;
	/**
	 * Creates a new quarry generator, reading in the possible quarries from a file named "QuarryList.txt".
	 */
	public QuarryGenerator()
	{
		quarry = new ArrayList<Quarry>();
		try
		{
			Scanner fileIn = new Scanner(new File("QuarryList.txt"));
			while (fileIn.hasNextLine())
			{
				String quarryLine = fileIn.nextLine();
				String[] data = quarryLine.split(",");
				int weight = Integer.parseInt(data[1]);
				int hp = Integer.parseInt(data[2]);
				int speed = Integer.parseInt(data[3]);
				Quarry newQuarry = new Quarry(new Point(0,0), weight / 2 + 20, 
								weight / 2 + 20, hp, speed, data[0], weight);
				quarry.add(newQuarry);
			}
			fileIn.close();
		}
		catch (FileNotFoundException e)
		{
			System.out.println("QuarryList.txt not found.");
		}
	}
	/**
	 * Generates a random quarry with a random starting point and direction.
	 * @return The generated quarry.
	 */
	public Quarry generateQuarry()
	{
		int randIndex = (int) (Math.random() * quarry.size());
		boolean fromLeft = ((int) (Math.random() * 2)) == 0;
		int randX = (int) (Math.random() * 790);
		int randY = (int) (Math.random() * 590);
		int randDirection = (int) (Math.random() * 4);
		Quarry generated = quarry.get(randIndex);
		if (fromLeft)
		{
			generated.setLocation(new Point(0, randY));
			generated.setDirection(randDirection + 1);
		}
		else
		{
			generated.setLocation(new Point(randX, 0));
			generated.setDirection(randDirection + 3);
		}
		return new Quarry(generated);
	}
}