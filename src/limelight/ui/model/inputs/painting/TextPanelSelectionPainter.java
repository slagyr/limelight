//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model.inputs.painting;

import limelight.util.Box;
import limelight.util.Colors;

import java.awt.*;
import java.util.ArrayList;

public class TextPanelSelectionPainter extends TextPanelPainter
{
  public static TextPanelPainter instance = new TextPanelSelectionPainter();

  private TextPanelSelectionPainter()
  {
  }

  public void paint(Graphics2D graphics, limelight.ui.model.text.TextModel boxInfo)
  {
    if (!boxInfo.hasSelection())
      return;
    if (boxInfo.hasText())
    {
      graphics.setColor(Colors.resolve("#BBD453AA"));
      ArrayList<Box> regions = boxInfo.getSelectionRegions();
      if (regions != null)
      {
        for (Rectangle rect : regions) {
          graphics.fillRect(rect.x, rect.y, rect.width, rect.height);
        }
      }
    }
  }
}
