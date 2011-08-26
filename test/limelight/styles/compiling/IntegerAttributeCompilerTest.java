//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.styles.compiling;

import limelight.styles.abstrstyling.StyleValue;
import limelight.styles.abstrstyling.InvalidStyleAttributeError;
import limelight.styles.values.SimpleIntegerValue;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class IntegerAttributeCompilerTest
{
  private IntegerAttributeCompiler compiler;

  @Before
  public void setUp() throws Exception
  {
    compiler = new IntegerAttributeCompiler();
    compiler.setName("width");
  }

  @Test
  public void validValue() throws Exception
  {
    StyleValue attr = compiler.compile("123");

    assertEquals(123, ((SimpleIntegerValue)attr).getValue());
  }

  @Test
  public void floatValues() throws Exception
  {
    assertEquals(123, ((SimpleIntegerValue) compiler.compile("123")).getValue());
    assertEquals(123, ((SimpleIntegerValue) compiler.compile("123.0")).getValue());
    assertEquals(123, ((SimpleIntegerValue) compiler.compile("123.44123423456367")).getValue());
    assertEquals(124, ((SimpleIntegerValue) compiler.compile("123.5")).getValue());
    assertEquals(0, ((SimpleIntegerValue) compiler.compile("0.0")).getValue());
    assertEquals(1, ((SimpleIntegerValue) compiler.compile("0.5")).getValue());
  }

  @Test
  public void objectValues() throws Exception
  {
    assertEquals(123, ((SimpleIntegerValue) compiler.compile(123)).getValue());
    assertEquals(123, ((SimpleIntegerValue) compiler.compile(new Integer(123))).getValue());
    assertEquals(123, ((SimpleIntegerValue) compiler.compile(new Float(123.0))).getValue());
    assertEquals(123, ((SimpleIntegerValue) compiler.compile(new Double(123.0))).getValue());
  }
  
  @Test
  public void withColons() throws Exception
  {
    assertEquals(123, ((SimpleIntegerValue) compiler.compile(":123")).getValue());
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
      assertEquals("Invalid value 'blah' for width style attribute.", e.getMessage());
    }
  }
}
