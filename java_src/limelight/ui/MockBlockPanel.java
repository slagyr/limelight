package limelight.ui;

public class MockBlockPanel extends BlockPanel
{
  public MockBlockPanel(Block block)
  {
    super(block);
  }

  public MockBlockPanel()
  {
    super(new MockBlock());
  }
}
