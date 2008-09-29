//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles;

import junit.framework.TestCase;
import limelight.styles.styling.RealStyleAttributeCompilerFactory;
import limelight.styles.styling.ColorAttribute;
import limelight.util.Colors;

import java.awt.*;

public class StyleTest extends TestCase
{
  static
  {
    RealStyleAttributeCompilerFactory.install();
  }

  public void testSizeDefaults() throws Exception
  {
    assertEquals("auto", Style.WIDTH.defaultValue.toString());
    assertEquals("auto", Style.HEIGHT.defaultValue.toString());
  }

  public void testFontDefaults() throws Exception
  {
    assertEquals("Arial", Style.FONT_FACE.defaultValue.toString());
    assertEquals("12", Style.FONT_SIZE.defaultValue.toString());
    assertEquals(Color.black, ((ColorAttribute)Style.TEXT_COLOR.defaultValue).getColor());
    assertEquals("plain", Style.FONT_STYLE.defaultValue.toString());
  }

  public void testScrolling() throws Exception
  {
    assertEquals("off", Style.VERTICAL_SCROLLBAR.defaultValue.toString());
    assertEquals("off", Style.HORIZONTAL_SCROLLBAR.defaultValue.toString());
  }

  public void testBackgroundDefaults() throws Exception
  {
    assertEquals(Colors.resolve("transparent"), ((ColorAttribute)Style.BACKGROUND_COLOR.defaultValue).getColor());
    assertEquals("none", Style.BACKGROUND_IMAGE.defaultValue.toString());
    assertEquals("repeat", Style.BACKGROUND_IMAGE_FILL_STRATEGY.defaultValue.toString());
  }

  public void testGradientDefaults() throws Exception
  {
    assertEquals("off", Style.GRADIENT.defaultValue.toString());
    assertEquals(Colors.resolve("transparent"), ((ColorAttribute)Style.SECONDARY_BACKGROUND_COLOR.defaultValue).getColor());
    assertEquals("90", Style.GRADIENT_ANGLE.defaultValue.toString());
    assertEquals("100%", Style.GRADIENT_PENETRATION.defaultValue.toString());
    assertEquals("off", Style.CYCLIC_GRADIENT.defaultValue.toString());
  }

  public void testTransparencyDefaults() throws Exception
  {
    assertEquals("0%", Style.TRANSPARENCY.defaultValue.toString());
  }

  public void testMarginDefaults() throws Exception
  {
    assertEquals("0", Style.TOP_MARGIN.defaultValue.toString());
    assertEquals("0", Style.RIGHT_MARGIN.defaultValue.toString());
    assertEquals("0", Style.BOTTOM_MARGIN.defaultValue.toString());
    assertEquals("0", Style.LEFT_MARGIN.defaultValue.toString());
  }
  
  public void testPaddingDefaults() throws Exception
  {
    assertEquals("0", Style.TOP_PADDING.defaultValue.toString());
    assertEquals("0", Style.RIGHT_PADDING.defaultValue.toString());
    assertEquals("0", Style.BOTTOM_PADDING.defaultValue.toString());
    assertEquals("0", Style.LEFT_PADDING.defaultValue.toString());
  }

  public void testBorderColorDefaults() throws Exception
  {
    assertEquals("#000000ff", Style.TOP_BORDER_COLOR.defaultValue.toString());
    assertEquals("#000000ff", Style.RIGHT_BORDER_COLOR.defaultValue.toString());
    assertEquals("#000000ff", Style.BOTTOM_BORDER_COLOR.defaultValue.toString());
    assertEquals("#000000ff", Style.LEFT_BORDER_COLOR.defaultValue.toString());
  }

  public void testBorderWidthDefaults() throws Exception
  {
    assertEquals("0", Style.TOP_BORDER_WIDTH.defaultValue.toString());
    assertEquals("0", Style.RIGHT_BORDER_WIDTH.defaultValue.toString());
    assertEquals("0", Style.BOTTOM_BORDER_WIDTH.defaultValue.toString());
    assertEquals("0", Style.LEFT_BORDER_WIDTH.defaultValue.toString());
  }

  public void testRoundedCorderRadiusDefaults() throws Exception
  {
    assertEquals("0", Style.TOP_RIGHT_ROUNDED_CORNER_RADIUS.defaultValue.toString());
    assertEquals("0", Style.BOTTOM_RIGHT_ROUNDED_CORNER_RADIUS.defaultValue.toString());
    assertEquals("0", Style.BOTTOM_LEFT_ROUNDED_CORNER_RADIUS.defaultValue.toString());
    assertEquals("0", Style.TOP_LEFT_ROUNDED_CORNER_RADIUS.defaultValue.toString());
  }

  public void testRoundedCorderWidthDefaults() throws Exception
  {
    assertEquals("0", Style.TOP_RIGHT_BORDER_WIDTH.defaultValue.toString());
    assertEquals("0", Style.BOTTOM_RIGHT_BORDER_WIDTH.defaultValue.toString());
    assertEquals("0", Style.BOTTOM_LEFT_BORDER_WIDTH.defaultValue.toString());
    assertEquals("0", Style.TOP_LEFT_BORDER_WIDTH.defaultValue.toString());
  }

  public void testRoundedCorderColorDefaults() throws Exception
  {
    assertEquals("#000000ff", Style.TOP_RIGHT_BORDER_COLOR.defaultValue.toString());
    assertEquals("#000000ff", Style.BOTTOM_RIGHT_BORDER_COLOR.defaultValue.toString());
    assertEquals("#000000ff", Style.BOTTOM_LEFT_BORDER_COLOR.defaultValue.toString());
    assertEquals("#000000ff", Style.TOP_LEFT_BORDER_COLOR.defaultValue.toString());
  }

  public void testMinAndMaxWidthAndHeight() throws Exception
  {
    assertEquals("none", Style.MIN_WIDTH.defaultValue.toString());
    assertEquals("none", Style.MIN_HEIGHT.defaultValue.toString());
    assertEquals("none", Style.MAX_WIDTH.defaultValue.toString());
    assertEquals("none", Style.MAX_HEIGHT.defaultValue.toString());
  }
}
