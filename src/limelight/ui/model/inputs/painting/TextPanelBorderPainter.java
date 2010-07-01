//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs.painting;

import com.android.ninepatch.NinePatch;
import limelight.ui.PaintablePanel;
import limelight.ui.Painter;
import limelight.ui.model.Drawable;
import limelight.ui.painting.BorderPainter;
import limelight.util.Box;
import limelight.util.Colors;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class TextPanelBorderPainter implements Painter
{
  public static Drawable normalBorder;
  public static Drawable focusedBorder;

  public static Painter instance = new TextPanelBorderPainter();

  static
  {
    try
    {
      ClassLoader classLoader = TextPanelBorderPainter.class.getClassLoader();
      normalBorder = NinePatch.load(ImageIO.read(classLoader.getResource("limelight/ui/images/text_box.9.png")), true, true);
      focusedBorder = NinePatch.load(ImageIO.read(classLoader.getResource("limelight/ui/images/text_box_focus.9.png")), true, true);
    }
    catch(IOException e)
    {
      throw new RuntimeException("Could not load TextPanel border images", e);
    }
    catch(Exception e)
    {
      System.err.println("e = " + e);
      e.printStackTrace();
      throw new RuntimeException(e);
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
