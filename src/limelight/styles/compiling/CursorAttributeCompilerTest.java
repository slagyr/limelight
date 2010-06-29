package limelight.styles.compiling;

import junit.framework.Assert;
import limelight.styles.abstrstyling.InvalidStyleAttributeError;
import limelight.styles.values.SimpleCursorValue;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

public class CursorAttributeCompilerTest
{
  private CursorAttributeCompiler compiler;

  @Before
  public void setUp() throws Exception
  {
    compiler = new CursorAttributeCompiler();
    compiler.setName("Cursor");
  }

  @Test
  public void testValidValue() throws Exception
  {
    assertEquals(SimpleCursorValue.DEFAULT, compiler.compile("default"));
    assertEquals(SimpleCursorValue.TEXT, compiler.compile("text"));
    assertEquals(SimpleCursorValue.HAND, compiler.compile("hand"));
    assertEquals(SimpleCursorValue.CROSSHAIR, compiler.compile("crosshair"));
  }

  @Test
  public void shouldHaveInvalidValue() throws Exception
  {
    checkForError("blah");
  }

  private void checkForError(String value)
  {
    try
    {
      compiler.compile(value);
      fail("should have throw error");
    }
    catch(InvalidStyleAttributeError e)
    {
      assertEquals("Invalid value '" + value + "' for Cursor style attribute.", e.getMessage());
    }
  }
}
