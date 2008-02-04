package limelight.ui;

import java.awt.*;

public class MockPanel extends RootBlockPanel
{
  public limelight.Rectangle rectangleInsideMargin;
  public limelight.Rectangle rectangleInsidePadding;
  public Rectangle paintedClip;

  public MockPanel()
  {
    super(new MockBlock(), new MockFrame());
  }

  public MockPanel(Block block)
  {
    super(block, new MockFrame());
  }

  public limelight.Rectangle getRectangleInsideMargins()
  {
    if(rectangleInsideMargin != null)
      return rectangleInsideMargin;
    return super.getRectangleInsideMargins();
  }

  public limelight.Rectangle getRectangleInsidePadding()
  {
    if(rectangleInsidePadding != null)
      return rectangleInsidePadding;
    return super.getRectangleInsidePadding();
  }

  public void paint(Rectangle clip)
  {
    paintedClip = clip;
  }
}

class MockFrame extends Frame
{
  public Graphics graphics;

  public MockFrame()
  {
    super(new MockBlock());
    graphics = new MockGraphics();
  }

  public Graphics getGraphics()
  {
    return graphics;
  }
}
