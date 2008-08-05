package limelight.ui.model.inputs;

import limelight.ui.model.updates.Updates;
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
//    System.err.println("repaint");
    if(panel != null)
      panel.setNeededUpdate(Updates.shallowPaintUpdate);
  }

  public void repaint(long tm, int x, int y, int width, int height)
  {
//    System.err.println("repaint " + x + ", " + y + ", " + width + ", " + height);
    if(panel != null)
      panel.setNeededUpdate(new BoundedShallowPaintUpdate(new Rectangle(x, y, width, height)));
  }

  public void repaint(Rectangle r)
  {
//    System.err.println("repaint " + r);
    if(panel != null)
      panel.setNeededUpdate(new BoundedShallowPaintUpdate(r));
  }

  public boolean isShowing()
  {
    return true;
  }
}
