package limelight.ui;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame
{
  public Frame()
  {
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setLayout(null);
  }

  public void close()
  {
    setVisible(false);
    dispose();
  }

  public void open()
  {
    setVisible(true);
    repaint();
  }

  public void load(Component child)
  {
    getContentPane().removeAll();
    add(child);
  }


}
