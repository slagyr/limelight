package limelight.ui;

import limelight.Rectangle;

public class RootBlockPanel extends Panel
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
    height = style.asInt(style.getHeight());;
  }

}
