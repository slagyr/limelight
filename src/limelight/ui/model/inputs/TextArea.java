//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

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
}
