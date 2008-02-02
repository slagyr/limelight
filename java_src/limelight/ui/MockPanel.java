package limelight.ui;

import java.awt.*;

public class MockPanel extends RootBlockPanel
{
  public Graphics2D paintedGraphics;
  public limelight.Rectangle rectangleInsideMargin;
  public limelight.Rectangle rectangleInsidePadding;

  public MockPanel()
  {
    super(new MockBlock(), new MockFrame());
  }

  public MockPanel(Block block)
  {
    super(block, new MockFrame());
  }

  public void paint(Graphics2D graphics)
  {
    this.paintedGraphics = graphics;  
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
}

class MockFrame extends Frame
{
  public MockFrame()
  {
    super(new MockBlock());
  }
}
