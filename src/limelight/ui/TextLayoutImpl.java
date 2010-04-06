//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextHitInfo;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

public class TextLayoutImpl implements TypedLayout
{
  TextLayout layout;
  String text;
  public boolean hasDrawn;

  public TextLayoutImpl(String string, Font font, FontRenderContext frc)
  {
    if(string.length() == 0)
    {
      Character nonChar = KeyEvent.CHAR_UNDEFINED;
      string = nonChar.toString();
    }
    layout = new TextLayout(string, font, frc);
    text = string;
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

  public String getText()
  {
    if (text.length() == 1 && text.charAt(0) == KeyEvent.CHAR_UNDEFINED)
      return "";
    return text;
  }
}
