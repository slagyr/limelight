package limelight;

public class MockPanel extends Panel
{
  public Rectangle rectangleInsideMargin;

  public MockPanel()
	{
		super(new MockBlock());
	}

	public Rectangle getRectangleInsidePadding()
	{
		return new Rectangle(0, 0, 999, 999);
	}

  public Rectangle getRectangleInsideMargins()
  {
    if(rectangleInsideMargin != null)
      return rectangleInsideMargin;
    return super.getRectangleInsideMargins();
  }
}
