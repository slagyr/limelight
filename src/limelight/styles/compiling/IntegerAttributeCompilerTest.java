//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.compiling;

import junit.framework.TestCase;
import junit.framework.Assert;
import limelight.styles.abstrstyling.StyleValue;
import limelight.styles.abstrstyling.InvalidStyleAttributeError;
import limelight.styles.values.SimpleIntegerValue;

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
    StyleValue attr = compiler.compile("123");

    Assert.assertEquals(123, ((SimpleIntegerValue)attr).getValue());
  }

  public void testFloatValues() throws Exception
  {
    assertEquals(123, ((SimpleIntegerValue) compiler.compile("123")).getValue());
    assertEquals(123, ((SimpleIntegerValue) compiler.compile("123.0")).getValue());
    assertEquals(123, ((SimpleIntegerValue) compiler.compile("123.44123423456367")).getValue());
    assertEquals(124, ((SimpleIntegerValue) compiler.compile("123.5")).getValue());
    assertEquals(0, ((SimpleIntegerValue) compiler.compile("0.0")).getValue());
    assertEquals(1, ((SimpleIntegerValue) compiler.compile("0.5")).getValue());
  }
  
  public void testObjectValues() throws Exception
  {
    assertEquals(123, ((SimpleIntegerValue) compiler.compile(123)).getValue());
    assertEquals(123, ((SimpleIntegerValue) compiler.compile(new Integer(123))).getValue());
    assertEquals(123, ((SimpleIntegerValue) compiler.compile(new Float(123.0))).getValue());
    assertEquals(123, ((SimpleIntegerValue) compiler.compile(new Double(123.0))).getValue());
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
