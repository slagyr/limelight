package limelight.ui;

public class MockBlockPanel extends BlockPanel
{
  public Rectangle childConsumableRectangle;

  public MockBlockPanel(Block block)
  {
    super(block);
  }

  public MockBlockPanel()
  {
    super(new MockBlock());
  }

  public Rectangle getChildConsumableArea()
  {
    if(childConsumableRectangle != null)
      return childConsumableRectangle;
    else
      return super.getChildConsumableArea();
  }
}
