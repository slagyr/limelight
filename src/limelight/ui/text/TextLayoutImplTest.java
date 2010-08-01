package limelight.ui.text;

import limelight.ui.model.TextPanel;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.*;
import java.awt.font.FontRenderContext;

import static org.junit.Assert.assertEquals;

public class TextLayoutImplTest
{
  private static Font courier;
  private static FontRenderContext context;

  @BeforeClass
  public static void suiteSetUp()
  {
    courier = new Font("courier", Font.PLAIN, 12);
    context = TextPanel.getRenderContext();
  }

  @Test
  public void shouldGetText() throws Exception
  {
    assertEquals("blah", new TextLayoutImpl("blah", courier, context).getText());
  }

  @Test
  public void shouldGetIndexAtXIsAlwaysGreaterThenZero() throws Exception
  {
    TextLayoutImpl line = new TextLayoutImpl("blah", courier, context);
    assertEquals(0, line.getIndexAt(-1));
    assertEquals(0, line.getIndexAt(-10));
  }
  
  @Test
  public void shouldKnowWhenTextDoesntEndInNewline() throws Exception
  {
    TextLayoutImpl line = new TextLayoutImpl("blah", courier, context);

    assertEquals(false, line.endsWithNewline());
    assertEquals("blah", line.getText());
    assertEquals(4, line.length());
    assertEquals("blah", line.getVisibleText());
    assertEquals(4, line.visibleLength());
  }

  @Test
  public void shouldRemoveNewlinesFromText() throws Exception
  {
    TextLayoutImpl line = new TextLayoutImpl("blah\n", courier, context);

    assertEquals(true, line.endsWithNewline());
    assertEquals("blah\n", line.getText());
    assertEquals(5, line.length());
    assertEquals("blah", line.getVisibleText());
    assertEquals(4, line.visibleLength());
  }

  @Test
  public void shouldRemoveNewlinesFromTextWithCarriageReturn() throws Exception
  {
    TextLayoutImpl line = new TextLayoutImpl("blah\r\n", courier, context);

    assertEquals(true, line.endsWithNewline());
    assertEquals("blah\r\n", line.getText());
    assertEquals(6, line.length());
    assertEquals("blah", line.getVisibleText());
    assertEquals(4, line.visibleLength());
  }

  @Test
  public void shouldHandleLineWithOnlyNewline() throws Exception
  {
    TextLayoutImpl line = new TextLayoutImpl("\n", courier, context);

    assertEquals(true, line.endsWithNewline());
    assertEquals("\n", line.getText());
    assertEquals(1, line.length());
    assertEquals("", line.getVisibleText());
    assertEquals(0, line.visibleLength());
  }

// MDM - The actual measurements of text can vary at runtime causing these tests to occasionally fail.
  
//  @Test
//  public void shouldMeasureText() throws Exception
//  {
//    assertEquals(21, new TextLayoutImpl("abc", courier, context).getWidth());
//    assertEquals(42, new TextLayoutImpl("abc123", courier, context).getWidth());
//    assertEquals(84, new TextLayoutImpl("abc123xyz789", courier, context).getWidth());
//  }
//
//  @Test
//  public void shouldMeasureTrailingWhiteSpace() throws Exception
//  {
//    assertEquals(28, new TextLayoutImpl("abc ", courier, context).getWidth());
//    assertEquals(28, new TextLayoutImpl("abc\t", courier, context).getWidth());
//  }
//
//  @Test
//  public void shouldMeasurePreceedingWhiteSpace() throws Exception
//  {
//    assertEquals(28, new TextLayoutImpl(" abc", courier, context).getWidth());
//    assertEquals(28, new TextLayoutImpl("\tabc", courier, context).getWidth());
//  }
//
//  @Test
//  public void shouldMeasureEmptyString() throws Exception
//  {
//    assertEquals(0, new TextLayoutImpl("", courier, context).getWidth());
//  }
//
//  @Test
//  public void shouldGetIndexAt() throws Exception
//  {
//    TextLayoutImpl layout = new TextLayoutImpl("abc 123 xyz", courier, context);
//    assertEquals(0, layout.getIndexAt(0));
//    assertEquals(3, layout.getIndexAt(21));
//    assertEquals(6, layout.getIndexAt(42));
//    assertEquals(11, layout.getIndexAt(1000));
//  }
//
//  @Test
//  public void shouldGetIndexAtInbetweenSpots() throws Exception
//  {
//    TextLayoutImpl layout = new TextLayoutImpl("abc 123 xyz", courier, context);
//    assertEquals(0, layout.getIndexAt(2));
//    assertEquals(3, layout.getIndexAt(19));
//    assertEquals(3, layout.getIndexAt(23));
//    assertEquals(6, layout.getIndexAt(40));
//    assertEquals(6, layout.getIndexAt(44));
//  }
//
//  @Test
//  public void shouldHaveSameWidthWithOrWithoutNewline() throws Exception
//  {
//    assertEquals(28, new TextLayoutImpl("blah", courier, context).getWidth());
//    assertEquals(28, new TextLayoutImpl("blah\n", courier, context).getWidth());
//  }
}
