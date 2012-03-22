//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import limelight.ui.Panel;

public class FakeLayout implements Layout
{
  public static FakeLayout instance = new FakeLayout(false);
  public static FakeLayout alwaysOverides = new FakeLayout(true);

  public Panel lastPanelProcessed;
  private boolean overide;

  public FakeLayout(boolean overide)
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

  public void doLayout(Panel panel, boolean topLevel)
  {
    doLayout(panel);
  }
}
