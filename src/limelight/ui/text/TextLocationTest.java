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

  @Test
  public void shouldFindFirstLocation() throws Exception
  {
    addLine("blah");

    TextLocation location = TextLocation.fromIndex(lines, 0);

    assertEquals(0, location.line);
    assertEquals(0, location.index);
  }

  @Test
  public void shouldFindEndOfSingleLine() throws Exception
  {
    addLine("blah");

    TextLocation location = TextLocation.fromIndex(lines, 4);

    assertEquals(0, location.line);
    assertEquals(4, location.index);
  }

  @Test
  public void shouldFindEndOfMultipleLines() throws Exception
  {
    addLine("blah\n");
    addLine("foo\n");
    addLine("bar");

    TextLocation location = TextLocation.fromIndex(lines, 12);

    assertEquals(2, location.line);
    assertEquals(3, location.index);
  }

  @Test
  public void shouldPutLocationOnNewLineIfFollowingNewline() throws Exception
  {
    addLine("blah\n");
    addLine("");

    TextLocation location = TextLocation.fromIndex(lines, 5);

    assertEquals(1, location.line);
    assertEquals(0, location.index);
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
