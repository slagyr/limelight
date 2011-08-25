//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.compiling;

import limelight.styles.abstrstyling.StyleValue;
import limelight.styles.abstrstyling.InvalidStyleAttributeError;
import limelight.styles.values.SimpleColorValue;
import limelight.util.FakeKeyword;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class ColorAttributeCompilerTest
{
  private ColorAttributeCompiler compiler;

  @Before
  public void setUp() throws Exception
  {
    compiler = new ColorAttributeCompiler();
    compiler.setName("color");
  }

  @Test
  public void testValidValue() throws Exception
  {
    StyleValue attr = compiler.compile("red");

    assertEquals(Color.red, ((SimpleColorValue)attr).getColor());
  }
  
  @Test
  public void usingClojureStyleKeyword() throws Exception
  {
    StyleValue attr = compiler.compile(new FakeKeyword("blue"));

    assertEquals(Color.blue, ((SimpleColorValue)attr).getColor());
  }

  @Test
  public void testInvalidValue() throws Exception
  {
    try
    {
      compiler.compile("blah");
      fail("Should have thrown exception");
    }
    catch(InvalidStyleAttributeError e)
    {
      assertEquals("Invalid value 'blah' for color style attribute.", e.getMessage());
    }
  }
}
