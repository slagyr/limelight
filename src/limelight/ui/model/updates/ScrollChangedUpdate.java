//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.updates;

import limelight.ui.Panel;
import limelight.ui.model.PropPanel;
import limelight.ui.model.PropPanelLayout;

public class ScrollChangedUpdate extends PaintUpdate
{
  protected ScrollChangedUpdate(int severity)
  {
    super(severity);
  }

  public void performUpdate(Panel panel)
  {
    PropPanel propPanel = (PropPanel)panel;
    PropPanelLayout layout = propPanel.getLayout();
    layout.layoutRows();
    paintPanel(panel);
  }
}
