package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.styles.InvalidStyleAttributeError;

public class HorizontalAlignmentAttributeCompilerTest extends TestCase
{
    private HorizontalAlignmentAttributeCompiler compiler;

  public void setUp() throws Exception
  {
    compiler = new HorizontalAlignmentAttributeCompiler();
    compiler.setName("horizontal alignment");
  }

  public void testValidValue() throws Exception
  {
    assertEquals("left", ((HorizontalAlignmentAttribute) compiler.compile("left")).getAlignment());
  }

  public void testInvalidValue() throws Exception
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
      assertEquals("Invalid value '" + value + "' for horizontal alignment style attribute.", e.getMessage());
    }
  }
}
