package limelight.styles.values;

import org.junit.Test;

import java.awt.*;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;

public class SimpleCursorValueTest
{
  @Test
  public void shouldCursors() throws Exception
  {
    assertSame(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR), SimpleCursorValue.DEFAULT.getCursor());
    assertSame(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR), SimpleCursorValue.HAND.getCursor());
    assertSame(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR), SimpleCursorValue.TEXT.getCursor());
    assertSame(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR), SimpleCursorValue.CROSSHAIR.getCursor());
  }
  
  @Test
  public void shouldHaveCorrectStringValues() throws Exception
  {
    assertEquals("default", SimpleCursorValue.DEFAULT.toString());
    assertEquals("hand", SimpleCursorValue.HAND.toString());
    assertEquals("text", SimpleCursorValue.TEXT.toString());
    assertEquals("crosshair", SimpleCursorValue.CROSSHAIR.toString());
  }
}
