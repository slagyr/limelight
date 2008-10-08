package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.styles.abstrstyling.StyleAttribute;
import limelight.styles.abstrstyling.InvalidStyleAttributeError;

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

    assertEquals(123, ((SimpleIntegerAttribute)attr).getValue());
  }

  public void testFloatValues() throws Exception
  {
    assertEquals(123, ((SimpleIntegerAttribute) compiler.compile("123")).getValue());
    assertEquals(123, ((SimpleIntegerAttribute) compiler.compile("123.0")).getValue());
    assertEquals(123, ((SimpleIntegerAttribute) compiler.compile("123.44123423456367")).getValue());
    assertEquals(124, ((SimpleIntegerAttribute) compiler.compile("123.5")).getValue());
    assertEquals(0, ((SimpleIntegerAttribute) compiler.compile("0.0")).getValue());
    assertEquals(1, ((SimpleIntegerAttribute) compiler.compile("0.5")).getValue());
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
