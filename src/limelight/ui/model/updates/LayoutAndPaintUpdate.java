//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.updates;

import limelight.ui.Panel;

public class LayoutAndPaintUpdate extends PaintUpdate
{
  protected LayoutAndPaintUpdate(int severity)
  {
    super(severity);
  }

  public void performUpdate(Panel panel)
  {
    panel.doLayout();
    paintPanel(panel);
  }
}
