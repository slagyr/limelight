//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs.painters;

import com.android.ninepatch.NinePatch;
import limelight.Context;
import limelight.ui.PaintablePanel;
import limelight.ui.Painter;
import limelight.ui.model.Drawable;
import limelight.ui.model.PropPanel;
import limelight.ui.painting.BorderPainter;
import limelight.util.Box;
import limelight.util.Colors;
import limelight.util.Debug;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class TextPanelBorderPainter implements Painter
{
  public static Drawable normalBorder;
  public static Drawable focusedBorder;

  public static Painter instance = new TextPanelBorderPainter();

  static
  {
    try
    {
      normalBorder = NinePatch.load(ImageIO.read(ClassLoader.getSystemClassLoader().getResource("limelight/ui/images/text_box.9.png")), true, true);
      focusedBorder = NinePatch.load(ImageIO.read(ClassLoader.getSystemClassLoader().getResource("limelight/ui/images/text_box_focus.9.png")), true, true);
    }
    catch(IOException e)
    {
      throw new RuntimeException("Could not load TextPanel border images", e);
    }
  }

  private TextPanelBorderPainter()
  {
  }

  public void paint(Graphics2D graphics, PaintablePanel panel)
  {
    if(shouldPaintSpecialBorder(panel))
    {
      Box bounds = panel.getBoxInsideMargins();
      normalBorder.draw(graphics, 0, 0, bounds.width, bounds.height);
      if(panel.hasFocus())
        focusedBorder.draw(graphics, 0, 0, bounds.width, bounds.height);
    }
    else
      BorderPainter.instance.paint(graphics, panel);
  }

  private boolean shouldPaintSpecialBorder(PaintablePanel panel)
  {
    return panel.getStyle().getCompiledTopBorderColor().getColor().equals(Colors.TRANSPARENT);
  }
}
