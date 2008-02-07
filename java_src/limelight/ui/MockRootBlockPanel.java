package limelight.ui;

import java.awt.*;

public class MockRootBlockPanel extends RootBlockPanel
{
  public Rectangle rectangleInsideMargin;
  public Rectangle rectangleInsidePadding;
  public Rectangle paintedClip;

  public MockRootBlockPanel()
  {
    super(new MockBlock(), new MockFrame());
  }

  public MockRootBlockPanel(Block block)
  {
    super(block, new MockFrame());
  }

  public Rectangle getRectangleInsideMargins()
  {
    if(rectangleInsideMargin != null)
      return rectangleInsideMargin;
    return super.getRectangleInsideMargins();
  }

  public Rectangle getRectangleInsidePadding()
  {
    if(rectangleInsidePadding != null)
      return rectangleInsidePadding;
    return super.getRectangleInsidePadding();
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
