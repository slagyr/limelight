//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs.painting;

import limelight.ui.model.inputs.TextModel;

import java.awt.*;
import java.util.ArrayList;

public class TextPanelSelectionPainter extends TextPanelPainter
{
  public static TextPanelPainter instance = new TextPanelSelectionPainter();

  private TextPanelSelectionPainter()
  {
  }

  public void paint(Graphics2D graphics, TextModel boxInfo)
  {
    if (!boxInfo.isSelectionOn())
      return;
    if (boxInfo.getText() != null && boxInfo.getText().length() > 0)
    {
      graphics.setColor(Color.cyan);
      ArrayList<Rectangle> regions = boxInfo.getSelectionRegions();
      if (regions != null)
      {
        for (Rectangle rect : regions) {
          graphics.fillRect(rect.x, rect.y, rect.width, rect.height);
        }
      }
    }
  }
}
