package limelight.ui;

public class MockLayout extends PanelLayout
{
  public boolean layoutPerformed;

  public MockLayout()
  {
    super(null);
  }

  public void doLayout()
  {
    layoutPerformed = true;
  }
}
