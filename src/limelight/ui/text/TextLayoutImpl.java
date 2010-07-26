//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.text;

import limelight.util.Box;
import limelight.util.StringUtil;
import sun.font.FontDesignMetrics;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextHitInfo;
import java.awt.font.TextLayout;

public class TextLayoutImpl extends TypedLayout
{
  TextLayout layout;
  private Font font;
  private FontRenderContext fontRenderContet;
  private FontMetrics metrics;

  public TextLayoutImpl(String text, Font font, FontRenderContext frc)
  {
    super(text);
    this.font = font;
    fontRenderContet = frc;
  }

  public void draw(Graphics2D graphics, float x, float y)
  {
    getLayout().draw(graphics, x, y);
  }

  public float getAscent()
  {
    return getMetrics().getAscent();
  }

  public float getDescent()
  {
    return getMetrics().getDescent();
  }

  public float getLeading()
  {
    return getMetrics().getLeading();
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

    if(index > 0 && remainder < 0 && Math.abs(remainder) > (charWidth / 2))
      index--;

    while(index > 0 && StringUtil.isNewlineChar(text.charAt(index - 1)))
      index--;

    return index;
  }

  public Box getCaretShape(int caretIndex)
  {
    String textLeftOfCaret = text.substring(0, caretIndex);
    int x = getMetrics().stringWidth(textLeftOfCaret);
    return new Box(x, 0, 1, getHeight() + 1);
  }

  public int getWidth()
  {
    return getWidthOf(text);
  }

  @Override
  public int getVisibleWidth()
  {
    return getWidthOf(getVisibleText());
  }

  public int getHeight()
  {
    return getMetrics().getAscent() + getMetrics().getDescent();
  }

  public int getHeightWithLeading()
  {
    return getHeight() + getMetrics().getLeading();
  }

  public int getX(int index)
  {
    String textBeforeIndex = getText().substring(0, index);
    return getWidthOf(textBeforeIndex);
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
      if(getText() == null || getText().length() == 0)
        layout = new TextLayout("" + KeyEvent.CHAR_UNDEFINED, font, fontRenderContet);
      else
        layout = new TextLayout(getText(), font, fontRenderContet);
    }
    return layout;
  }

}
