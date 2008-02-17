package limelight.ui;

import java.awt.*;
import java.awt.event.MouseWheelEvent;

public class RootBlockPanel extends BlockPanel
{
  private Frame frame;

  public RootBlockPanel(Block block, Frame frame)
  {
    super(block);
    this.frame = frame;
  }

  public Frame getFrame()
  {
    return frame;
  }

  public void snapToSize()
  {
    Style style = getBlock().getStyle();
    width = style.asInt(style.getWidth());
    height = style.asInt(style.getHeight());
  }

  public void mouseWheelMoved(MouseWheelEvent e)
  {
    //ignore
  }

  public Graphics2D getClippedGraphics()
  {
    return (Graphics2D)frame.getGraphics();
  }
}
