package limelight.ui.model.inputs;

import limelight.ui.model.updates.Updates;
import limelight.ui.model.updates.BoundedPaintUpdate;
import limelight.util.*;

import javax.swing.*;
import java.awt.*;

public class TextArea extends JTextArea
{
  private TextAreaPanel panel;

  public TextArea(TextAreaPanel panel)
  {
    this.panel = panel;
    setLineWrap(true);
    setBorder(BorderFactory.createEtchedBorder(Color.lightGray, Color.gray));
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
