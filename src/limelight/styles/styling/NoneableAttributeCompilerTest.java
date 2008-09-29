package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.styles.InvalidStyleAttributeError;

public class NoneableAttributeCompilerTest extends TestCase
{
  private NoneableAttributeCompiler<IntegerAttribute> compiler;

  public void setUp() throws Exception
  {
    compiler = new NoneableAttributeCompiler<IntegerAttribute>(new IntegerAttributeCompiler());
    compiler.setName("noneable");
  }

  public void testValidValue() throws Exception
  {
    assertEquals(true, ((NoneableAttribute) compiler.compile("none")).isNone());
    assertEquals(50, ((NoneableAttribute<IntegerAttribute>) compiler.compile("50")).getAttribute().getValue());
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
      assertEquals("Invalid value 'blah' for noneable style attribute.", e.getMessage());
    }
  }
}
