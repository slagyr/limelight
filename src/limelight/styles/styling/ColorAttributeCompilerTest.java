//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.styles.abstrstyling.StyleAttribute;
import limelight.styles.abstrstyling.InvalidStyleAttributeError;

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
    StyleAttribute attr = compiler.compile("red");

    assertEquals(Color.red, ((SimpleColorAttribute)attr).getColor());
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
