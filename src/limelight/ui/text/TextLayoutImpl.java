//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.text;

import limelight.util.Box;
import sun.font.FontDesignMetrics;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextHitInfo;
import java.awt.font.TextLayout;

public class TextLayoutImpl implements TypedLayout
{
  TextLayout layout;
  String text;
  public boolean hasDrawn;
  private Font font;
  private FontRenderContext fontRenderContet;
  private FontMetrics metrics;

  public TextLayoutImpl(String string, Font font, FontRenderContext frc)
  {
    this.font = font;
    fontRenderContet = frc;
    text = string;
    hasDrawn = false;
  }

  public void draw(Graphics2D graphics, float x, float y)
  {
    hasDrawn = true;
    getLayout().draw(graphics, x, y);
  }

  public boolean hasDrawn()
  {
    return hasDrawn;
  }

  public float getAscent()
  {
    return getLayout().getAscent();
  }

  public float getDescent()
  {
    return getLayout().getDescent();
  }

  public float getLeading()
  {
    return getLayout().getLeading();
  }

  public TextHitInfo hitTestChar(float x, float y)
  {
    return getLayout().hitTestChar(x, y);
  }

  public int getIndexAt(int x)
  {
    int index = 0;
    int remainder = x;
    int charWidth = 0;
    while(remainder > 0 && index < text.length())
    {
      charWidth = getMetrics().charWidth(text.charAt(index));
      remainder -= charWidth;
      index++;
    }

    if(remainder < 0 && Math.abs(remainder) > (charWidth / 2))
      index --;
    
    return index;
  }

  public Box getCaretShape(int caretIndex)
  {
    String textLeftOfCaret = text.substring(0, caretIndex);
    int x = getMetrics().stringWidth(textLeftOfCaret);
    return new Box(x, 0, 1, getHeight() + 1);
  }

  public String toString()
  {
    return getLayout().toString();
  }

  public String getText()
  {
    if(text.length() == 1 && text.charAt(0) == KeyEvent.CHAR_UNDEFINED)
      return "";
    return text;
  }

  public int getWidth()
  {
    return getWidthOf(text);
  }

  public int getHeight()
  {
    return getMetrics().getAscent() + getMetrics().getDescent();
  }

  public int getWidthOf(String text)
  {
    return getMetrics().stringWidth(text);
  }

  private FontMetrics getMetrics()
  {
    if(metrics == null)
      metrics = FontDesignMetrics.getMetrics(font, fontRenderContet);
    return metrics;
  }

  public TextLayout getLayout()
  {
    if(layout == null)
    {
      if(text == null || text.length() == 0)
        layout = new TextLayout("" + KeyEvent.CHAR_UNDEFINED, font, fontRenderContet);
      else
        layout = new TextLayout(text, font, fontRenderContet);
    }
    return layout;
  }
}
