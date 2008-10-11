package limelight.background;

import limelight.ui.Panel;
import limelight.background.Animation;

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
