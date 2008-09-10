//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles;

import junit.framework.TestCase;

public class StyleTest extends TestCase
{
  public void testSizeDefaults() throws Exception
  {
    assertEquals("auto", Style.WIDTH.defaultValue);
    assertEquals("auto", Style.HEIGHT.defaultValue);
  }

  public void testFontDefaults() throws Exception
  {
    assertEquals("Arial", Style.FONT_FACE.defaultValue);
    assertEquals("12", Style.FONT_SIZE.defaultValue);
    assertEquals("black", Style.TEXT_COLOR.defaultValue);
    assertEquals("plain", Style.FONT_STYLE.defaultValue);
  }

  public void testScrolling() throws Exception
  {
    assertEquals("auto", Style.VERTICAL_SCROLLBAR.defaultValue);
    assertEquals("auto", Style.HORIZONTAL_SCROLLBAR.defaultValue);
  }

  public void testBackgroundDefaults() throws Exception
  {
    assertEquals("transparent", Style.BACKGROUND_COLOR.defaultValue);
    assertEquals("none", Style.BACKGROUND_IMAGE.defaultValue);
    assertEquals("repeat", Style.BACKGROUND_IMAGE_FILL_STRATEGY.defaultValue);
  }

  public void testGradientDefaults() throws Exception
  {
    assertEquals("off", Style.GRADIENT.defaultValue);
    assertEquals("transparent", Style.SECONDARY_BACKGROUND_COLOR.defaultValue);
    assertEquals("90", Style.GRADIENT_ANGLE.defaultValue);
    assertEquals("100", Style.GRADIENT_PENETRATION.defaultValue);
    assertEquals("off", Style.CYCLIC_GRADIENT.defaultValue);
  }

  public void testTransparencyDefaults() throws Exception
  {
    assertEquals("0", Style.TRANSPARENCY.defaultValue);  
  }

  public void testMarginDefaults() throws Exception
  {
    assertEquals("0", Style.TOP_MARGIN.defaultValue);
    assertEquals("0", Style.RIGHT_MARGIN.defaultValue);
    assertEquals("0", Style.BOTTOM_MARGIN.defaultValue);
    assertEquals("0", Style.LEFT_MARGIN.defaultValue);
  }
  
  public void testPaddingDefaults() throws Exception
  {
    assertEquals("0", Style.TOP_PADDING.defaultValue);
    assertEquals("0", Style.RIGHT_PADDING.defaultValue);
    assertEquals("0", Style.BOTTOM_PADDING.defaultValue);
    assertEquals("0", Style.LEFT_PADDING.defaultValue);
  }

  public void testBorderColorDefaults() throws Exception
  {
    assertEquals("black", Style.TOP_BORDER_COLOR.defaultValue);
    assertEquals("black", Style.RIGHT_BORDER_COLOR.defaultValue);
    assertEquals("black", Style.BOTTOM_BORDER_COLOR.defaultValue);
    assertEquals("black", Style.LEFT_BORDER_COLOR.defaultValue);
  }

  public void testBorderWidthDefaults() throws Exception
  {
    assertEquals("0", Style.TOP_BORDER_WIDTH.defaultValue);
    assertEquals("0", Style.RIGHT_BORDER_WIDTH.defaultValue);
    assertEquals("0", Style.BOTTOM_BORDER_WIDTH.defaultValue);
    assertEquals("0", Style.LEFT_BORDER_WIDTH.defaultValue);
  }

  public void testRoundedCorderRadiusDefaults() throws Exception
  {
    assertEquals("0", Style.TOP_RIGHT_ROUNDED_CORNER_RADIUS.defaultValue);
    assertEquals("0", Style.BOTTOM_RIGHT_ROUNDED_CORNER_RADIUS.defaultValue);
    assertEquals("0", Style.BOTTOM_LEFT_ROUNDED_CORNER_RADIUS.defaultValue);
    assertEquals("0", Style.TOP_LEFT_ROUNDED_CORNER_RADIUS.defaultValue);
  }

  public void testRoundedCorderWidthDefaults() throws Exception
  {
    assertEquals("0", Style.TOP_RIGHT_BORDER_WIDTH.defaultValue);
    assertEquals("0", Style.BOTTOM_RIGHT_BORDER_WIDTH.defaultValue);
    assertEquals("0", Style.BOTTOM_LEFT_BORDER_WIDTH.defaultValue);
    assertEquals("0", Style.TOP_LEFT_BORDER_WIDTH.defaultValue);
  }

  public void testRoundedCorderColorDefaults() throws Exception
  {
    assertEquals("black", Style.TOP_RIGHT_BORDER_COLOR.defaultValue);
    assertEquals("black", Style.BOTTOM_RIGHT_BORDER_COLOR.defaultValue);
    assertEquals("black", Style.BOTTOM_LEFT_BORDER_COLOR.defaultValue);
    assertEquals("black", Style.TOP_LEFT_BORDER_COLOR.defaultValue);
  }

  public void testMinAndMaxWidthAndHeight() throws Exception
  {
    assertEquals("none", Style.MIN_WIDTH.defaultValue);
    assertEquals("none", Style.MIN_HEIGHT.defaultValue);
    assertEquals("none", Style.MAX_WIDTH.defaultValue);
    assertEquals("none", Style.MAX_HEIGHT.defaultValue);
  }
}
