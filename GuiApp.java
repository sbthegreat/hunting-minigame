import javax.swing.*;
/**
 * A frame to hold the drawing panel.
 * @author Connor Beckett-Lemus 009215893
 */
public class GuiApp extends JFrame
{
	/**
	 * The drawing panel the game is drawn on.
	 */
	private DrawingPanel panel;
	/**
	 * Makes a new frame and adds the panel to it after settings its boundaries.
	 */
	public GuiApp()
	{
		setBounds(100, 100, 800, 640);
		panel = new DrawingPanel();
		getContentPane().add(panel);
	}
	public static void main(String [] args)
	{
		GuiApp f = new GuiApp();
		f.setTitle("Oregon Trail: Hunting");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}