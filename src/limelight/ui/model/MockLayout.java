package limelight.ui.model;

import limelight.ui.Panel;

public class MockLayout implements Layout
{
  public static MockLayout instance = new MockLayout(false);
  public static MockLayout alwaysOverides = new MockLayout(true);

  public Panel lastPanelProcessed;
  private boolean overide;

  public MockLayout(boolean overide)
  {
    this.overide = overide;
  }

  public void doLayout(Panel panel)
  {
    lastPanelProcessed = panel;
  }

  public boolean overides(Layout other)
  {
    return overide;
  }
}
