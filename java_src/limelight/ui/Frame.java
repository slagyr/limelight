package limelight.ui;

import javax.swing.*;
import java.awt.*;

public class Frame extends JPanel
{
  private RootBlockPanel panel;
  private FrameListener listener;

  public Frame(Block block)
  {
    super();
    panel = new RootBlockPanel(block, this);
    listener = new FrameListener(this);
    addMouseListener(listener);
    addMouseMotionListener(listener);
    addMouseWheelListener(listener);
    addKeyListener(listener);
  }

  public ParentPanel getPanel()
  {
    return panel;
  }

  public void setPanel(RootBlockPanel panel)
  {
    this.panel = panel;
  }

  public void doLayout()
  {
    setLocation(0, 0);
    panel.snapToSize();
    panel.doLayout();
    setSize(panel.getWidth(), panel.getHeight());
  }

  public void paint(Graphics g)
  {
    PaintJob job = new PaintJob(new Rectangle(0, 0, getWidth(), getHeight()));
    job.paint(panel);
    job.applyTo(getGraphics());
  }
}
