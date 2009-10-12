//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.compiling;

import junit.framework.TestCase;
import limelight.styles.abstrstyling.InvalidStyleAttributeError;
import limelight.styles.VerticalAlignment;
import limelight.styles.styling.StaticYCoordinateAttribute;
import limelight.styles.styling.PercentageYCoordinateAttribute;
import limelight.styles.styling.AlignedYCoordinateAttribute;

public class YCoordinateAttributeCompilerTest extends TestCase
{
  private YCoordinateAttributeCompiler compiler;

  public void setUp() throws Exception
  {
    compiler = new YCoordinateAttributeCompiler();
    compiler.setName("y-coordinate");
  }

  public void testValidValue() throws Exception
  {
    assertEquals(StaticYCoordinateAttribute.class, compiler.compile("123").getClass());
    assertEquals(StaticYCoordinateAttribute.class, compiler.compile("-123").getClass());
    assertEquals(StaticYCoordinateAttribute.class, compiler.compile("123.567").getClass());
    assertEquals(PercentageYCoordinateAttribute.class, compiler.compile("50%").getClass());
    assertEquals(PercentageYCoordinateAttribute.class, compiler.compile("3.14%").getClass());
    assertEquals(AlignedYCoordinateAttribute.class, compiler.compile("top").getClass());
    assertEquals(AlignedYCoordinateAttribute.class, compiler.compile("center").getClass());
    assertEquals(AlignedYCoordinateAttribute.class, compiler.compile("bottom").getClass());

    assertEquals(123, ((StaticYCoordinateAttribute) compiler.compile("123")).getValue());
    assertEquals(-123, ((StaticYCoordinateAttribute) compiler.compile("-123")).getValue());
    assertEquals(0, ((StaticYCoordinateAttribute) compiler.compile("0")).getValue());
    assertEquals(50.0, ((PercentageYCoordinateAttribute) compiler.compile("50%")).getPercentage(), 0.01);
    assertEquals(3.14, ((PercentageYCoordinateAttribute) compiler.compile("3.14%")).getPercentage(), 0.01);
    assertEquals(VerticalAlignment.TOP, ((AlignedYCoordinateAttribute)compiler.compile("top")).getAlignment());
    assertEquals(VerticalAlignment.CENTER, ((AlignedYCoordinateAttribute)compiler.compile("center")).getAlignment());
    assertEquals(VerticalAlignment.BOTTOM, ((AlignedYCoordinateAttribute)compiler.compile("bottom")).getAlignment());
  }

  public void testInvalidValues() throws Exception
  {
    checkForError("200%");
    checkForError("blah");
    checkForError("left");
  }

  private void checkForError(String value)
  {
    try
    {
      compiler.compile(value);
      fail("should have throw error");
    }
    catch(InvalidStyleAttributeError e)
    {
      assertEquals("Invalid value '" + value + "' for y-coordinate style attribute.", e.getMessage());
    }
  }
}
