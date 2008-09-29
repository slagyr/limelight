package limelight.styles.styling;

import limelight.styles.InvalidStyleAttributeError;
import junit.framework.TestCase;

public class OnOffAttributeCompilerTest extends TestCase
{
  private OnOffAttributeCompiler compiler;

  public void setUp() throws Exception
  {
    compiler = new OnOffAttributeCompiler();
    compiler.setName("on/off");
  }

  public void testValidValue() throws Exception
  {
    assertEquals(true, ((OnOffAttribute) compiler.compile("on")).isOn());
    assertEquals(true, ((OnOffAttribute) compiler.compile("ON")).isOn());
    assertEquals(true, ((OnOffAttribute) compiler.compile("oN")).isOn());
    assertEquals(true, ((OnOffAttribute) compiler.compile("On")).isOn());
    assertEquals(false, ((OnOffAttribute) compiler.compile("off")).isOn());
    assertEquals(false, ((OnOffAttribute) compiler.compile("OFF")).isOn());
    assertEquals(false, ((OnOffAttribute) compiler.compile("Off")).isOn());
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
      assertEquals("Invalid value 'blah' for on/off style attribute.", e.getMessage());
    }
  }
}
