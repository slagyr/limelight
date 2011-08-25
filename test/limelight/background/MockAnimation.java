//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.background;

import limelight.ui.Panel;
import limelight.background.Animation;

public class MockAnimation extends Animation
{
  public int updates;

  public MockAnimation(double updatesPerSecond, Panel panel)
  {
    super(updatesPerSecond);
  }

  protected void doUpdate()
  {
    updates++;
  }

}
