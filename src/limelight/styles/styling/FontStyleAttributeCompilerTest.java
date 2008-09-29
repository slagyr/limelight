package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.styles.InvalidStyleAttributeError;

public class FontStyleAttributeCompilerTest extends TestCase
{
  private FontStyleAttributeCompiler compiler;

  public void setUp() throws Exception
  {
    compiler = new FontStyleAttributeCompiler();
    compiler.setName("font style");
  }

  public void testValidValue() throws Exception
  {
    assertEquals(true, ((FontStyleAttribute) compiler.compile("bold")).isBold());
  }

  public void testInvalidValue() throws Exception
  {
    checkForError("blah");
    checkForError("bold and italic");
    checkForError("bold or bold");
    checkForError("underlined");
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
      assertEquals("Invalid value '" + value + "' for font style style attribute.", e.getMessage());
    }
  }
}
