package limelight.ui;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame
{
  private Stage stage;

  public Frame(Stage stage)
  {
	System.setProperty("apple.laf.useScreenMenuBar", "true");
    this.stage = stage;
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setLayout(null);
  }

  public void doLayout()
  {
    super.doLayout();
    Component[] components = getContentPane().getComponents();
    for (Component component : components)
    {
      component.doLayout();
      component.repaint();
    }
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
