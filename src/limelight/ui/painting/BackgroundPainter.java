//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.painting;

import limelight.styles.abstrstyling.NoneableValue;
import limelight.ui.*;
import limelight.ui.model.RootPanel;
import limelight.ui.model.ImageCache;
import limelight.styles.Style;
import limelight.styles.abstrstyling.StringValue;
import limelight.util.Box;

import java.awt.*;
import java.io.IOException;

public class BackgroundPainter implements Painter
{
  public static Painter instance = new BackgroundPainter();

  private BackgroundPainter()
  {
  }

  public void paint(Graphics2D graphics, PaintablePanel panel)
  {
    Style style = panel.getStyle();
    Border border = panel.getBorderShaper();
    Shape insideBorder = border.getShapeInsideBorder();

    Color backgroundColor = style.getCompiledBackgroundColor().getColor();

    if(style.getCompiledGradient().isOn())
      GradientPainter.instance.paint(graphics, panel);
    else
    {
      if(backgroundColor.getAlpha() > 0)
      {
        graphics.setColor(backgroundColor);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.fill(insideBorder);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
      }
    }

    NoneableValue<StringValue> backgroundImageAttribute = style.getCompiledBackgroundImage();
    if (!backgroundImageAttribute.isNone())
    {
      try
      {
        Box borderFrame = panel.getBorderedBounds();
        RootPanel rootPanel = panel.getRoot();

        // TODO MDM - getting a NullPointer here. The Panel was removed from the scene in between the start of the paint cycle and here.
        if(rootPanel == null)
          return;

        ImageCache cache = rootPanel.getImageCache();
        Image image = cache.getImage(backgroundImageAttribute.getAttribute().getValue());
        Graphics2D borderedGraphics = (Graphics2D) graphics.create(borderFrame.x, borderFrame.y, borderFrame.width, borderFrame.height);
        style.getCompiledBackgroundImageFillStrategy().fill(borderedGraphics, image, style.getCompiledBackgroundImageX(), style.getCompiledBackgroundImageY());
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
  }
}
