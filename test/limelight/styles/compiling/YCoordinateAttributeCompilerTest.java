//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.styles.compiling;

import limelight.styles.abstrstyling.InvalidStyleAttributeError;
import limelight.styles.VerticalAlignment;
import limelight.styles.values.StaticYCoordinateValue;
import limelight.styles.values.PercentageYCoordinateValue;
import limelight.styles.values.AlignedYCoordinateValue;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class YCoordinateAttributeCompilerTest
{
  private YCoordinateAttributeCompiler compiler;

  @Before
  public void setUp() throws Exception
  {
    compiler = new YCoordinateAttributeCompiler();
    compiler.setName("y-coordinate");
  }

  @Test
  public void validValue() throws Exception
  {
    assertEquals(StaticYCoordinateValue.class, compiler.compile("123").getClass());
    assertEquals(StaticYCoordinateValue.class, compiler.compile("-123").getClass());
    assertEquals(StaticYCoordinateValue.class, compiler.compile("123.567").getClass());
    assertEquals(PercentageYCoordinateValue.class, compiler.compile("50%").getClass());
    assertEquals(PercentageYCoordinateValue.class, compiler.compile("3.14%").getClass());
    assertEquals(AlignedYCoordinateValue.class, compiler.compile("top").getClass());
    assertEquals(AlignedYCoordinateValue.class, compiler.compile("center").getClass());
    assertEquals(AlignedYCoordinateValue.class, compiler.compile("bottom").getClass());

    assertEquals(123, ((StaticYCoordinateValue) compiler.compile("123")).getValue());
    assertEquals(-123, ((StaticYCoordinateValue) compiler.compile("-123")).getValue());
    assertEquals(0, ((StaticYCoordinateValue) compiler.compile("0")).getValue());
    assertEquals(50.0, ((PercentageYCoordinateValue) compiler.compile("50%")).getPercentage(), 0.01);
    assertEquals(3.14, ((PercentageYCoordinateValue) compiler.compile("3.14%")).getPercentage(), 0.01);
    assertEquals(VerticalAlignment.TOP, ((AlignedYCoordinateValue)compiler.compile("top")).getAlignment());
    assertEquals(VerticalAlignment.CENTER, ((AlignedYCoordinateValue)compiler.compile("center")).getAlignment());
    assertEquals(VerticalAlignment.BOTTOM, ((AlignedYCoordinateValue)compiler.compile("bottom")).getAlignment());
  }

  @Test
  public void validValueWithColons() throws Exception
  {
    assertEquals(123, ((StaticYCoordinateValue) compiler.compile(":123")).getValue());
    assertEquals(-123, ((StaticYCoordinateValue) compiler.compile(":-123")).getValue());
    assertEquals(0, ((StaticYCoordinateValue) compiler.compile(":0")).getValue());
    assertEquals(50.0, ((PercentageYCoordinateValue) compiler.compile(":50%")).getPercentage(), 0.01);
    assertEquals(3.14, ((PercentageYCoordinateValue) compiler.compile(":3.14%")).getPercentage(), 0.01);
    assertEquals(VerticalAlignment.TOP, ((AlignedYCoordinateValue)compiler.compile(":top")).getAlignment());
    assertEquals(VerticalAlignment.CENTER, ((AlignedYCoordinateValue)compiler.compile(":center")).getAlignment());
    assertEquals(VerticalAlignment.BOTTOM, ((AlignedYCoordinateValue)compiler.compile(":bottom")).getAlignment());
  }

  @Test
  public void invalidValues() throws Exception
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
