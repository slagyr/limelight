package limelight.ui;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;

public class TextLayoutImpl implements TypedLayout
{
  TextLayout layout;
  public int x;
  public int y;
  public String text;
  public boolean hasDrawn;

  public TextLayoutImpl(String string, Font font, FontRenderContext frc)
  {
    layout = new TextLayout(string, font, frc);
    text = string;
    hasDrawn = false;
  }

  public void draw(Graphics2D graphics, int x, int y)
  {
    this.x = x;
    this.y = y;
    hasDrawn = true;
  }

  public boolean hasDrawn()
  {
    return hasDrawn;
  }

  public String getText()
  {
    return text;
  }

  public int getX()
  {
    return x;
  }

  public int getY()
  {
    return y;
  }
}
