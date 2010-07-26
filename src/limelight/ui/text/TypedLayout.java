//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.text;

import limelight.util.Box;
import limelight.util.StringUtil;

import java.awt.*;
import java.awt.font.TextHitInfo;

public abstract class TypedLayout
{
  private int trailingNewlinesChars;
  protected String text;

  public TypedLayout(String string)
  {
    trailingNewlinesChars = countTrailingNewlines(string);
    text = string;
  }

  private int countTrailingNewlines(String string)
  {
    int newLineChars = 0;
    for(int i = string.length(); i > 0; i--)
    {
      if(StringUtil.isNewlineChar(string.charAt(i - 1)))
        newLineChars++;
      else
        break;
    }
    return newLineChars;
  }

  public abstract void draw(Graphics2D graphics, float x, float y);

  public String getText()
  {
    return text;
  }

  public int length()
  {
    return text.length();
  }

  public String getVisibleText()
  {
    return text.substring(0, text.length() - trailingNewlinesChars);
  }

  public int visibleLength()
  {
    return length() - trailingNewlinesChars;
  }

  public boolean endsWithNewline()
  {
    return trailingNewlinesChars > 0;
  }

  public abstract float getAscent();
  public abstract float getDescent();
  public abstract float getLeading();
  public abstract int getWidthOf(String text);
  public abstract int getWidth();
  public abstract int getVisibleWidth();
  public abstract int getHeight();
  public abstract int getHeightWithLeading();
  public abstract int getX(int index);
  public abstract int getIndexAt(int x);
  public abstract Box getCaretShape(int caretIndex);
  public abstract TextHitInfo hitTestChar(float x, float y); // TODO MDM Can I be deleted?
}
