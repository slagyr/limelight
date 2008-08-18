package limelight.ui.model.inputs;

import limelight.util.Box;
import limelight.ui.model.updates.Updates;
import limelight.ui.model.updates.BoundedPaintUpdate;
import limelight.ui.model.updates.BoundedShallowPaintUpdate;

import javax.swing.*;
import java.awt.*;

public class TextBox extends JTextField
{
  private TextBoxPanel panel;

  public TextBox(TextBoxPanel panel)
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
