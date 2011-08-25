//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

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

  public void doLayout(Panel panel, boolean topLevel)
  {
    doLayout(panel);
  }
}
