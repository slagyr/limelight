package limelight.styles.values;

import limelight.styles.abstrstyling.CursorValue;

import java.awt.*;

public class SimpleCursorValue implements CursorValue
{
  public static final CursorValue DEFAULT = new SimpleCursorValue(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
  public static final CursorValue HAND = new SimpleCursorValue(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
  public static final CursorValue TEXT = new SimpleCursorValue(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
  public static final CursorValue CROSSHAIR = new SimpleCursorValue(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

  private Cursor cursor;

  public SimpleCursorValue(Cursor cursor)
  {
    this.cursor = cursor;
  }

  public Cursor getCursor()
  {
    return cursor;
  }

  @Override
  public String toString()
  {
    if(this == DEFAULT)
      return "default";
    else if(this == HAND)
      return "hand";
    else if(this == TEXT)
      return "text";
    else if(this == CROSSHAIR)
      return "crosshair";
    else
      return super.toString();
  }
}
