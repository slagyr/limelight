package limelight.ui;

import javax.swing.*;
import java.awt.*;

public class Frame extends JPanel
{
  private RootBlockPanel panel;

  public Frame(Block block)
  {
    super();
    panel = new RootBlockPanel(block, this);
  }

  public Panel getPanel()
  {
    return panel;
  }

  public void setPanel(RootBlockPanel panel)
  {
    this.panel = panel;
  }

  public void setSize(int width, int height)
  {
    super.setSize(width, height);
    panel.setSize(width, height);
  }

  public void paint(Graphics g)
  {
    panel.paint((Graphics2D)g);
  }
}
