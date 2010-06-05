//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs.painters;

import com.android.ninepatch.NinePatch;
import limelight.ui.model.Drawable;
import limelight.ui.model.inputs.TextModel;
import limelight.ui.model.inputs.TextPanelPainter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class TextPanelBackgroundPainter extends TextPanelPainter
{
  public static Drawable normalBorder;
  public static Drawable focusedBorder;

  public static TextPanelPainter instance = new TextPanelBackgroundPainter();

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

  private TextPanelBackgroundPainter()
  {
  }

  public void paint(Graphics2D graphics, TextModel boxInfo)
  {
    Dimension dimension = new Dimension(boxInfo.getPanelWidth(), boxInfo.getPanelHeight());
    normalBorder.draw(graphics, 0, 0, dimension.width, dimension.height);
    if(boxInfo.isFocused())
      focusedBorder.draw(graphics, 0, 0, dimension.width, dimension.height);
  }
}
