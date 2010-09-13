//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs.painting;

import com.android.ninepatch.NinePatch;
import limelight.ui.PaintablePanel;
import limelight.ui.Painter;
import limelight.ui.images.Images;
import limelight.ui.model.Drawable;
import limelight.ui.painting.BorderPainter;
import limelight.util.Box;
import limelight.util.Colors;

import java.awt.*;

public class TextPanelBorderPainter implements Painter
{
  public static Drawable normalBorder;
  public static Drawable focusedBorder;

  public static Painter instance = new TextPanelBorderPainter();

  static
  {
    normalBorder = NinePatch.load(Images.load("text_box.9.png"), true, true);
    focusedBorder = NinePatch.load(Images.load("text_box_focus.9.png"), true, true);
  }

  private TextPanelBorderPainter()
  {
  }

  public void paint(Graphics2D graphics, PaintablePanel panel)
  {
    if(shouldPaintSpecialBorder(panel))
    {
      try
      {
        Box bounds = panel.getMarginedBounds();
        normalBorder.draw(graphics, bounds.x, bounds.y, bounds.width, bounds.height);
        if(panel.hasFocus())
          focusedBorder.draw(graphics, bounds.x, bounds.y, bounds.width, bounds.height);
      }
      catch(Exception e)
      {
        System.err.println("TextPanel: NinePatch choked again");
      }
    }
    else
      BorderPainter.instance.paint(graphics, panel);
  }

  private boolean shouldPaintSpecialBorder(PaintablePanel panel)
  {
    return panel.getStyle().getCompiledTopBorderColor().getColor().equals(Colors.TRANSPARENT);
  }
}
