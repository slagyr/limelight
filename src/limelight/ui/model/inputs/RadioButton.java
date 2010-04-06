//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.RadioButtonGroup;
import limelight.ui.RadioButtonGroupMember;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RadioButton extends JRadioButton implements RadioButtonGroupMember, ActionListener
{
  private final RadioButtonPanel panel;
  private RadioButtonGroup radioButtonGroup;

  public RadioButton(RadioButtonPanel panel)
  {
    this.panel = panel;
    this.addActionListener(this);
  }

  public void repaint()
  {
    if(panel != null && panel.getRoot() != null)
      panel.getRoot().addDirtyRegion(panel.getAbsoluteBounds());
  }

  public void repaint(long tm, int x, int y, int width, int height)
  {
    repaint(new Rectangle(x, y, width, height));
  }

  public void repaint(Rectangle dirtyBounds)
  {
    if(panel != null && panel.getRoot() != null)
    {
      Point location = panel.getAbsoluteLocation();
      dirtyBounds.translate(location.x, location.y);
      panel.getRoot().addDirtyRegion(dirtyBounds);
    }
  }

  public boolean isShowing()
  {
    return true;
  }

  public void setGroup(RadioButtonGroup radioButtonGroup)
  {
    this.radioButtonGroup = radioButtonGroup;
  }

  public void actionPerformed(ActionEvent e)
  {
    if(radioButtonGroup != null && isSelected())
      radioButtonGroup.buttonSelected(this);
  }
}
