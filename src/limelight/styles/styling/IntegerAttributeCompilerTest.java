package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.styles.StyleAttribute;
import limelight.styles.InvalidStyleAttributeError;

public class IntegerAttributeCompilerTest extends TestCase
{
  private IntegerAttributeCompiler compiler;

  public void setUp() throws Exception
  {
    compiler = new IntegerAttributeCompiler();
    compiler.setName("width");
  }

  public void testValidValue() throws Exception
  {
    StyleAttribute attr = compiler.compile("123");

    assertEquals(123, ((IntegerAttribute)attr).getValue());
  }

  public void testInvalidValue() throws Exception
  {
    try
    {
      compiler.compile("blah");
      fail("Should have thrown exception");
    }
    catch(InvalidStyleAttributeError e)
    {
      assertEquals("Invalid value 'blah' for width style attribute.", e.getMessage());
    }
  }
}
