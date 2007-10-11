package limelight;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.swing.border.Border;
import java.awt.*;

public class Tester
{
	public static void main(String[] args)
	{
		JFrame frame = new JFrame();

//		JLabel panel = new JLabel("I'm a little teapot!");
//		panel.setSize(100, 50);
//		panel.setBackground(Color.blue);
//		panel.setLocation(25, 25);

		JTextPane panel = new JTextPane();
		panel.setText("I'm a little teapot!");
		panel.setSize(100, 50);
//		panel.setBackground(Color.blue);
		panel.setLocation(25, 25);

		frame.setLayout(null);
		frame.setSize(500, 500);
		frame.setLocation(200, 25);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.add(panel);
		frame.setVisible(true);
		frame.repaint();
	}
}


