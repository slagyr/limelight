//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model.inputs;

import limelight.ui.Panel;
import limelight.ui.model.Layout;
import limelight.ui.model.PanelBase;
import limelight.ui.model.SimpleLayout;
import limelight.util.Box;

import java.util.Map;

public class InputPanelLayout extends SimpleLayout
{
  public static InputPanelLayout instance = new InputPanelLayout();

  @Override
  public void doExpansion(Panel thePanel)
  {
    PanelBase panel = (PanelBase)thePanel;
    Box bounds = panel.getParent().getChildConsumableBounds();
    panel.setLocation(bounds.x, bounds.y);
    panel.setSize(bounds.width, bounds.height);
  }

  @Override
  public void doFinalization(Panel panel)
  {
    panel.markAsDirty();
  }

  public boolean overides(Layout other)
  {
    return true;
  }
}
