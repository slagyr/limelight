//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.painting;

import limelight.ui.*;
import limelight.ui.model.RootPanel;
import limelight.ui.model.ImageCache;
import limelight.ui.model.PaintJob;
import limelight.ui.api.PropablePanel;
import limelight.styles.Style;
import limelight.styles.abstrstyling.StringAttribute;
import limelight.styles.abstrstyling.NoneableAttribute;
import limelight.util.Colors;
import limelight.util.Box;
import limelight.util.NanoTimer;
import limelight.util.Debug;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BackgroundPainter extends Painter
{
  public BackgroundPainter(PaintablePanel panel)
  {
    super(panel);
  }

  public void paint(Graphics2D graphics)
  {
    Style style = getStyle();
    Border border = panel.getBorderShaper();
    Shape insideBorder = border.getShapeInsideBorder();

    Color backgroundColor = style.getCompiledBackgroundColor().getColor();

    if(style.getCompiledGradient().isOn())
      new GradientPainter(panel).paint(graphics);
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

    NoneableAttribute<StringAttribute> backgroundImageAttribute = style.getCompiledBackgroundImage();
    if (!backgroundImageAttribute.isNone())
    {
      try
      {
        Box borderFrame = panel.getBoxInsideBorders();
        RootPanel rootPanel = ((PropablePanel) panel).getRoot();
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
