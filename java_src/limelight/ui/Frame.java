package limelight.ui;

import javax.swing.*;
import java.awt.*;

public class Frame extends JPanel
{
  private RootBlockPanel panel;
  private FrameListener listener;
  private JPanel hiddenPanel;

  public Frame(Block block)
  {
    super();
    panel = new RootBlockPanel(block, this);
    listener = new FrameListener(this);
    addMouseListener(listener);
    addMouseMotionListener(listener);
    addMouseWheelListener(listener);
    addKeyListener(listener);

    hiddenPanel = new JPanel();
    hiddenPanel.setVisible(false);
  }

  public ParentPanel getPanel()
  {
    return panel;
  }

  public void setPanel(RootBlockPanel panel)
  {
    this.panel = panel;
  }

  public JPanel getHiddenPanel()
  {
    return hiddenPanel;
  }

  public void doLayout()
  {
    setLocation(0, 0);
//    panel.snapToSize();
    panel.doLayout();
    setSize(panel.getWidth(), panel.getHeight());
    hiddenPanel.setSize(panel.getWidth(), panel.getHeight());
  }

  public void paint(Graphics g)
  {
    PaintJob job = new PaintJob(new Rectangle(0, 0, getWidth(), getHeight()));
    job.paint(panel);
    job.applyTo(getGraphics());
  }
  
  public Graphics getGraphics()
  {
    return super.getGraphics();
  }
}
