//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.Panel;
import limelight.ui.model.PanelBase;
import limelight.ui.model.Layout;
import limelight.util.Box;

public class InputPanelLayout implements Layout
{
  public static InputPanelLayout instance = new InputPanelLayout();

  public void doLayout(Panel thePanel)
  { 
    PanelBase panel = (PanelBase)thePanel;
    panel.resetLayout();
    Box bounds = panel.getParent().getChildConsumableBounds();
    panel.setLocation(bounds.x, bounds.y);
    panel.setSize(bounds.width, bounds.height);
    panel.markAsDirty();
  }

  public boolean overides(Layout other)
  {
    return true;
  }

  public void doLayout(Panel panel, boolean topLevel)
  {
    doLayout(panel);
  }
}
