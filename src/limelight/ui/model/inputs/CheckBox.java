package limelight.ui.model.inputs;

import limelight.ui.model.updates.Updates;

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
      panel.setNeededUpdate(Updates.shallowPaintUpdate);
  }

  public void repaint(long tm, int x, int y, int width, int height)
  {
    if(panel != null)
      panel.setNeededUpdate(Updates.shallowPaintUpdate);
  }

  public void repaint(Rectangle r)
  {
    if(panel != null)
      panel.setNeededUpdate(Updates.shallowPaintUpdate);
  }

  public boolean isShowing()
  {
    return true;
  }
}
