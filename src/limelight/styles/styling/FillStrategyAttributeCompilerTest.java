package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.styles.InvalidStyleAttributeError;

public class FillStrategyAttributeCompilerTest extends TestCase
{
  private FillStrategyAttributeCompiler compiler;

  public void setUp() throws Exception
  {
    compiler = new FillStrategyAttributeCompiler();
    compiler.setName("fill strategy");
  }

  public void testValidValue() throws Exception
  {
    assertEquals("static", ((FillStrategyAttribute) compiler.compile("static")).getName());
    assertEquals("repeat", ((FillStrategyAttribute) compiler.compile("repeat")).getName());
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
      assertEquals("Invalid value '" + value + "' for fill strategy style attribute.", e.getMessage());
    }
  }
}
