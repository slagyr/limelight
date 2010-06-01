//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.compiling;

import junit.framework.TestCase;
import junit.framework.Assert;
import limelight.styles.abstrstyling.StyleValue;
import limelight.styles.abstrstyling.InvalidStyleAttributeError;
import limelight.styles.values.SimpleColorValue;

import java.awt.*;

public class ColorAttributeCompilerTest extends TestCase
{
  private ColorAttributeCompiler compiler;

  public void setUp() throws Exception
  {
    compiler = new ColorAttributeCompiler();
    compiler.setName("color");
  }

  public void testValidValue() throws Exception
  {
    StyleValue attr = compiler.compile("red");

    Assert.assertEquals(Color.red, ((SimpleColorValue)attr).getColor());
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
      assertEquals("Invalid value 'blah' for color style attribute.", e.getMessage());
    }
  }
}
