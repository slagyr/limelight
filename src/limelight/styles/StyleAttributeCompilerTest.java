package limelight.styles;

import junit.framework.TestCase;

public class StyleAttributeCompilerTest extends TestCase
{
  private TestableStyleAttributeCompilerTest compiler;

  private static class TestableStyleAttributeCompilerTest extends StyleAttributeCompiler
  {
    public StyleAttribute compile(String value)
    {
      return null;
    }
  }

  public void setUp() throws Exception
  {
    compiler = new TestableStyleAttributeCompilerTest();
  }
  
  public void testName() throws Exception
  {
    compiler.setName("My Attribute");
    assertEquals("My Attribute", compiler.getName());
  }

  public void testError() throws Exception
  {
    compiler.setName("My Attribute");
    InvalidStyleAttributeError error = compiler.makeError("blah");

    assertEquals("Invalid value 'blah' for My Attribute style attribute.", error.getMessage());
  }
}
