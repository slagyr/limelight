//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.ui.Panel;

public class BasePanelLayout implements Layout
{
  public static BasePanelLayout instance = new BasePanelLayout();
  public Panel lastPanelProcessed;

  public void doLayout(Panel thePanel)
  {
    PanelBase panel = (PanelBase) thePanel;
    panel.resetLayout();
    panel.wasLaidOut();
    lastPanelProcessed = thePanel;
  }

  public boolean overides(Layout other)
  {
    return false;
  }

  public void doLayout(Panel panel, boolean topLevel)
  {
    doLayout(panel);
  }

}
