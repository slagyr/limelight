package limelight;

import limelight.ui.Panel;

public class MockAnimation extends Animation
{
  public int updates;

  public MockAnimation(int updatesPerSecond, Panel panel)
  {
    super(updatesPerSecond);
  }

  protected void doUpdate()
  {
    updates++;
  }

}
