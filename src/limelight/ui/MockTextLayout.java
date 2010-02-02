package limelight.ui;

import limelight.util.Box;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextHitInfo;
import java.awt.geom.Rectangle2D;

public class MockTextLayout implements TypedLayout
{
  String text;
  public boolean hasDrawn;
  public int x, y;

  public MockTextLayout(String string, Font font, FontRenderContext frc)
  {
    text = string;
    hasDrawn = false;
  }

  public void draw(Graphics2D graphics, float x, float y)
  {
    hasDrawn = true;
    this.x = (int)x;
    this.y = (int)y;
  }

  public boolean hasDrawn()
  {
    return hasDrawn;
  }

  public float getAscent()
  {
    return 0;
  }

  public float getDescent()
  {
    return 0;
  }

  public float getLeading()
  {
    return 0;
  }

  public Rectangle2D getBounds()
  {
    return new Box(x,y,x,y);
  }

  public TextHitInfo hitTestChar(float x, float y)
  {
    return null;
  }

  public String toString()
  {
    return text;
  }

  public String getText()
  {
    return text;
  }
}
