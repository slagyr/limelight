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
		loadPage(page);
		frame.setVisible(true);
		frame.repaint();
	}

	public void close()
	{
		frame.setVisible(false);
		frame.dispose();
	}

	public void loadPage(Page page)
	{
		frame.getContentPane().removeAll();
		frame.add(page.getPanel());
		page.setBook(this);
		page.getPanel().setSize(frame.getSize());
	}

	public JFrame getFrame()
	{
		return frame;
	}
}
