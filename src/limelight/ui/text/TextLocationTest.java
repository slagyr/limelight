package limelight.ui.text;

import limelight.LimelightException;
import limelight.ui.MockTypedLayout;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

public class TextLocationTest
{
  private ArrayList<TypedLayout> lines;

  @Before
  public void setUp() throws Exception
  {
    lines = new ArrayList<TypedLayout>();
  }

  private void addLine(String text)
  {
    lines.add(new MockTypedLayout(text));
  }

  private TextLocation location(int line, int index)
  {
    return TextLocation.at(line, index);
  }

  @Test
  public void shouldFindFirstLocation() throws Exception
  {
    addLine("blah");

    assertEquals(location(0, 0), TextLocation.fromIndex(lines, 0));
  }

  @Test
  public void shouldFindEndOfSingleLine() throws Exception
  {
    addLine("blah");

    assertEquals(location(0, 4), TextLocation.fromIndex(lines, 4));
  }

  @Test
  public void shouldFindEndOfMultipleLines() throws Exception
  {
    addLine("blah\n");
    addLine("foo\n");
    addLine("bar");

    assertEquals(location(2, 3), TextLocation.fromIndex(lines, 12));
  }

  @Test
  public void shouldHandleNewlinesDivisions() throws Exception
  {
    addLine("a\n");
    addLine("b\n");
    addLine("c");

    assertEquals(location(0, 0), TextLocation.fromIndex(lines, 0));
    assertEquals(location(0, 1), TextLocation.fromIndex(lines, 1));
    assertEquals(location(1, 0), TextLocation.fromIndex(lines, 2));
    assertEquals(location(1, 1), TextLocation.fromIndex(lines, 3));
    assertEquals(location(2, 0), TextLocation.fromIndex(lines, 4));
    assertEquals(location(2, 1), TextLocation.fromIndex(lines, 5));
  }

  @Test
  public void shouldHandleCarriageReturnChars() throws Exception
  {
    addLine("a\r\n");
    addLine("b\r\n");
    addLine("c");

    assertEquals(location(0, 0), TextLocation.fromIndex(lines, 0));
    assertEquals(location(0, 1), TextLocation.fromIndex(lines, 1));
    assertEquals(location(0, 2), TextLocation.fromIndex(lines, 2));
    assertEquals(location(1, 0), TextLocation.fromIndex(lines, 3));
    assertEquals(location(1, 1), TextLocation.fromIndex(lines, 4));
    assertEquals(location(1, 2), TextLocation.fromIndex(lines, 5));
    assertEquals(location(2, 0), TextLocation.fromIndex(lines, 6));
    assertEquals(location(2, 1), TextLocation.fromIndex(lines, 7));
  }

  @Test
  public void shouldPutLocationOnNewLineIfFollowingNewline() throws Exception
  {
    addLine("blah\n");
    addLine("");

    assertEquals(location(1, 0), TextLocation.fromIndex(lines, 5));
  }
  
  @Test
  public void shouldRaiseWhenLocationNotFound() throws Exception
  {
    addLine("blah");

    try
    {
      TextLocation.fromIndex(lines, 10);
      fail("Should have thrown an error");
    }
    catch(LimelightException e)
    {
      assertEquals("Can't find text location for index", e.getMessage());
    }
  }
}
