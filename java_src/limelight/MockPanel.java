package limelight;

public class MockPanel extends Panel
{
	public MockPanel()
	{
		super(new MockBlock());
	}

	public Rectangle getRectangleInsidePadding()
	{
		return new Rectangle(0, 0, 999, 999);
	}
}
