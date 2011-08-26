//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.styles.compiling;

import limelight.styles.abstrstyling.InvalidStyleAttributeError;
import limelight.styles.values.SimpleOnOffValue;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OnOffAttributeCompilerTest
{
  private OnOffAttributeCompiler compiler;

  @Before
  public void setUp() throws Exception
  {
    compiler = new OnOffAttributeCompiler();
    compiler.setName("on/off");
  }

  @Test
  public void validValue() throws Exception
  {
    assertEquals(true, ((SimpleOnOffValue) compiler.compile("on")).isOn());
    assertEquals(true, ((SimpleOnOffValue) compiler.compile("ON")).isOn());
    assertEquals(true, ((SimpleOnOffValue) compiler.compile("oN")).isOn());
    assertEquals(true, ((SimpleOnOffValue) compiler.compile("On")).isOn());
    assertEquals(false, ((SimpleOnOffValue) compiler.compile("off")).isOn());
    assertEquals(false, ((SimpleOnOffValue) compiler.compile("OFF")).isOn());
    assertEquals(false, ((SimpleOnOffValue) compiler.compile("Off")).isOn());
  }

  @Test
  public void booleanValue() throws Exception
  {
    assertEquals(true, ((SimpleOnOffValue) compiler.compile(true)).isOn());
    assertEquals(true, ((SimpleOnOffValue) compiler.compile("true")).isOn());
    assertEquals(true, ((SimpleOnOffValue) compiler.compile("TRUE")).isOn());
    assertEquals(false, ((SimpleOnOffValue) compiler.compile(false)).isOn());
    assertEquals(false, ((SimpleOnOffValue) compiler.compile("false")).isOn());
    assertEquals(false, ((SimpleOnOffValue) compiler.compile("FALSE")).isOn());
  }
  
  @Test
  public void validValuesWithColons() throws Exception
  {
    assertEquals(true, ((SimpleOnOffValue) compiler.compile(":on")).isOn());
    assertEquals(true, ((SimpleOnOffValue) compiler.compile(":true")).isOn());
    assertEquals(false, ((SimpleOnOffValue) compiler.compile(":off")).isOn());
    assertEquals(false, ((SimpleOnOffValue) compiler.compile(":false")).isOn());
  }

  @Test
  public void invalidValue() throws Exception
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
