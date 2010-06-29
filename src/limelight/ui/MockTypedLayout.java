package limelight.ui;

import limelight.util.Box;

import java.awt.*;
import java.awt.font.TextHitInfo;

public class MockTypedLayout implements TypedLayout
{
  public static int ASCENT = 1;
  public static int DESCENT = 2;
  public static int LEADING = 3;
  public static int CHAR_WIDTH = 10;
  public static int CHAR_HEIGHT = 10;

  public String text;
  public boolean hasDrawn;
  public int x;
  public int y;

  public MockTypedLayout(String value)
  {
    text = value;
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

  public String getText()
  {
    return text;
  }

  public float getAscent()
  {
    return ASCENT;
  }

  public float getDescent()
  {
    return DESCENT;
  }

  public float getLeading()
  {
    return LEADING;
  }

  public TextHitInfo hitTestChar(float x, float y)
  {
    int index = (int) (x / 10);
    index = Math.min(index, text.length());
    return TextHitInfo.leading(index);
  }

  public int getIndexAt(int x)
  {
    int index = x / 10;
    return Math.min(index, text.length());
  }

  public Box getCaretShape(int caretIndex)
  {
    return new Box(0, 0, 1, 10);
  }

  public int getWidth()
  {
    return getWidthOf(text);
  }

  public int getHeight()
  {
    return 10;
  }

  public int getWidthOf(String text)
  {
    return CHAR_WIDTH * text.length();
  }
}
