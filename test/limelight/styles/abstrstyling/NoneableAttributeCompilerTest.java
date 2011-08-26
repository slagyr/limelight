//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.styles.abstrstyling;

import limelight.styles.values.SimpleIntegerValue;
import limelight.styles.compiling.IntegerAttributeCompiler;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NoneableAttributeCompilerTest
{
  private NoneableAttributeCompiler<SimpleIntegerValue> compiler;

  @Before
  public void setUp() throws Exception
  {
    compiler = new NoneableAttributeCompiler<SimpleIntegerValue>(new IntegerAttributeCompiler());
    compiler.setName("noneable");
  }

  @Test
  public void validValue() throws Exception
  {
    assertEquals(true, ((NoneableValue) compiler.compile("none")).isNone());
    assertEquals(50, ((NoneableValue<SimpleIntegerValue>) compiler.compile("50")).getAttribute().getValue());
  }

  @Test
  public void validValuesWithColon() throws Exception
  {
    assertEquals(true, ((NoneableValue) compiler.compile(":none")).isNone());
    assertEquals(50, ((NoneableValue<SimpleIntegerValue>) compiler.compile(":50")).getAttribute().getValue());
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
      assertEquals("Invalid value 'blah' for noneable style attribute.", e.getMessage());
    }
  }
}
