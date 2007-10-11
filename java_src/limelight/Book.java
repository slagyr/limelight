package limelight;

import javax.swing.*;
import java.awt.*;

public class Book
{
	private JFrame frame;

	public Book()
	{
		frame = new JFrame();
	}

	public void open()
	{
		open(new DefaultPage());
	}

	public void open(Page page)
	{
		frame.setLayout(null);
		frame.setSize(900, 900);
		frame.setLocation(200, 25);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		load(page);
		frame.setVisible(true);
		frame.repaint();
	}

	public void close()
	{
		frame.setVisible(false);
		frame.dispose();
	}

	public void load(Page page)
	{
//		frame.removeAll();
		frame.add(page.getPanel());
		page.getPanel().setSize(frame.getSize());
	}

	public JFrame getFrame()
	{
		return frame;
	}
}
