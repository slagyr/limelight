//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import limelight.styles.abstrstyling.InvalidStyleAttributeError;
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
    assertEquals(true, ((SimpleOnOffAttribute) compiler.compile("on")).isOn());
    assertEquals(true, ((SimpleOnOffAttribute) compiler.compile("ON")).isOn());
    assertEquals(true, ((SimpleOnOffAttribute) compiler.compile("oN")).isOn());
    assertEquals(true, ((SimpleOnOffAttribute) compiler.compile("On")).isOn());
    assertEquals(false, ((SimpleOnOffAttribute) compiler.compile("off")).isOn());
    assertEquals(false, ((SimpleOnOffAttribute) compiler.compile("OFF")).isOn());
    assertEquals(false, ((SimpleOnOffAttribute) compiler.compile("Off")).isOn());
  }

  public void testBooleanValue() throws Exception
  {
    assertEquals(true, ((SimpleOnOffAttribute) compiler.compile(true)).isOn());
    assertEquals(true, ((SimpleOnOffAttribute) compiler.compile("true")).isOn());
    assertEquals(true, ((SimpleOnOffAttribute) compiler.compile("TRUE")).isOn());
    assertEquals(false, ((SimpleOnOffAttribute) compiler.compile(false)).isOn());
    assertEquals(false, ((SimpleOnOffAttribute) compiler.compile("false")).isOn());
    assertEquals(false, ((SimpleOnOffAttribute) compiler.compile("FALSE")).isOn());
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
