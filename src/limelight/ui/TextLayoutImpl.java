package limelight.ui;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextHitInfo;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

public class TextLayoutImpl implements TypedLayout
{
  TextLayout layout;
  public boolean hasDrawn;

  public TextLayoutImpl(String string, Font font, FontRenderContext frc)
  {
    layout = new TextLayout(string, font, frc);
    hasDrawn = false;
  }

  public void draw(Graphics2D graphics, float x, float y)
  {
    hasDrawn = true;
    layout.draw(graphics, x, y);
  }

  public boolean hasDrawn()
  {
    return hasDrawn;
  }

  public float getAscent()
  {
    return layout.getAscent();
  }

  public float getDescent()
  {
    return layout.getDescent();
  }

  public float getLeading()
  {
    return layout.getLeading();
  }

  public Rectangle2D getBounds()
  {
    return layout.getBounds();
  }

  public TextHitInfo hitTestChar(float x, float y)
  {
    return layout.hitTestChar(x,y);
  }

  public String toString()
  {
    return layout.toString();
  }
}
