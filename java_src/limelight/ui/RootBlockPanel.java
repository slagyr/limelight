package limelight.ui;

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
}
