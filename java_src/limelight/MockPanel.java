package limelight;

import limelight.ui.Rectangle;

public class MockPanel extends Panel
{
  public Rectangle rectangleInsideMargin;
  public Rectangle rectangleInsidePadding;

  public MockPanel()
	{
		super(new MockBlock());
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
