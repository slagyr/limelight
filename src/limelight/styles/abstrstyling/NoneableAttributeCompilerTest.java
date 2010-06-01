//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.abstrstyling;

import junit.framework.TestCase;
import limelight.styles.abstrstyling.InvalidStyleAttributeError;
import limelight.styles.values.SimpleIntegerValue;
import limelight.styles.compiling.IntegerAttributeCompiler;

public class NoneableAttributeCompilerTest extends TestCase
{
  private NoneableAttributeCompiler<SimpleIntegerValue> compiler;

  public void setUp() throws Exception
  {
    compiler = new NoneableAttributeCompiler<SimpleIntegerValue>(new IntegerAttributeCompiler());
    compiler.setName("noneable");
  }

  public void testValidValue() throws Exception
  {
    assertEquals(true, ((NoneableValue) compiler.compile("none")).isNone());
    assertEquals(50, ((NoneableValue<SimpleIntegerValue>) compiler.compile("50")).getAttribute().getValue());
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
