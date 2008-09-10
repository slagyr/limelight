//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.model.updates.Updates;
import limelight.ui.model.updates.BoundedPaintUpdate;

import javax.swing.*;
import java.awt.*;

public class CheckBox extends JCheckBox
{
  private CheckBoxPanel panel;

  public CheckBox(CheckBoxPanel panel)
  {
    this.panel = panel;
  }

  public void repaint()
  {
    if(panel != null)
      panel.setNeededUpdate(Updates.paintUpdate);
  }

  public void repaint(long tm, int x, int y, int width, int height)
  {
    if(panel != null)
      panel.setNeededUpdate(new BoundedPaintUpdate(x, y, width, height));
  }

  public void repaint(Rectangle r)
  {
    if(panel != null)
      panel.setNeededUpdate(new BoundedPaintUpdate(r));
  }

  public boolean isShowing()
  {
    return true;
  }
}
