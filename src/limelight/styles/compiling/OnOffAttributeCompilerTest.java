//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.compiling;

import limelight.styles.abstrstyling.InvalidStyleAttributeError;
import limelight.styles.values.SimpleOnOffValue;
import junit.framework.TestCase;
import junit.framework.Assert;

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
    Assert.assertEquals(true, ((SimpleOnOffValue) compiler.compile("on")).isOn());
    assertEquals(true, ((SimpleOnOffValue) compiler.compile("ON")).isOn());
    assertEquals(true, ((SimpleOnOffValue) compiler.compile("oN")).isOn());
    assertEquals(true, ((SimpleOnOffValue) compiler.compile("On")).isOn());
    assertEquals(false, ((SimpleOnOffValue) compiler.compile("off")).isOn());
    assertEquals(false, ((SimpleOnOffValue) compiler.compile("OFF")).isOn());
    assertEquals(false, ((SimpleOnOffValue) compiler.compile("Off")).isOn());
  }

  public void testBooleanValue() throws Exception
  {
    assertEquals(true, ((SimpleOnOffValue) compiler.compile(true)).isOn());
    assertEquals(true, ((SimpleOnOffValue) compiler.compile("true")).isOn());
    assertEquals(true, ((SimpleOnOffValue) compiler.compile("TRUE")).isOn());
    assertEquals(false, ((SimpleOnOffValue) compiler.compile(false)).isOn());
    assertEquals(false, ((SimpleOnOffValue) compiler.compile("false")).isOn());
    assertEquals(false, ((SimpleOnOffValue) compiler.compile("FALSE")).isOn());
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
