//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles;

import junit.framework.TestCase;
import limelight.styles.values.SimpleColorValue;
import limelight.styles.compiling.PixelsAttributeCompiler;
import limelight.styles.compiling.XCoordinateAttributeCompiler;
import limelight.styles.compiling.YCoordinateAttributeCompiler;
import limelight.styles.compiling.SimpleDimensionAttributeCompiler;
import limelight.styles.abstrstyling.NoneableAttributeCompiler;
import limelight.util.Colors;

import java.awt.*;

public class StyleTest extends TestCase
{
  public void testSizeDefaults() throws Exception
  {
    assertEquals("auto", Style.WIDTH.defaultValue.toString());
    assertEquals("auto", Style.HEIGHT.defaultValue.toString());
  }

  public void testFontDefaults() throws Exception
  {
    assertEquals("Arial", Style.FONT_FACE.defaultValue.toString());
    assertEquals("12", Style.FONT_SIZE.defaultValue.toString());
    assertEquals(Color.black, ((SimpleColorValue)Style.TEXT_COLOR.defaultValue).getColor());
    assertEquals("plain", Style.FONT_STYLE.defaultValue.toString());
  }

  public void testScrolling() throws Exception
  {
    assertEquals("off", Style.VERTICAL_SCROLLBAR.defaultValue.toString());
    assertEquals("off", Style.HORIZONTAL_SCROLLBAR.defaultValue.toString());
  }

  public void testBackgroundDefaults() throws Exception
  {
    assertEquals(Colors.resolve("transparent"), ((SimpleColorValue)Style.BACKGROUND_COLOR.defaultValue).getColor());
    assertEquals("none", Style.BACKGROUND_IMAGE.defaultValue.toString());
    assertEquals("repeat", Style.BACKGROUND_IMAGE_FILL_STRATEGY.defaultValue.toString());
  }

  public void testGradientDefaults() throws Exception
  {
    assertEquals("off", Style.GRADIENT.defaultValue.toString());
    assertEquals(Colors.resolve("transparent"), ((SimpleColorValue)Style.SECONDARY_BACKGROUND_COLOR.defaultValue).getColor());
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

  public void testMarginsUsePixleCompilers() throws Exception
  {
    assertEquals(PixelsAttributeCompiler.class, Style.TOP_MARGIN.compiler.getClass());
    assertEquals(PixelsAttributeCompiler.class, Style.RIGHT_MARGIN.compiler.getClass());
    assertEquals(PixelsAttributeCompiler.class, Style.BOTTOM_MARGIN.compiler.getClass());
    assertEquals(PixelsAttributeCompiler.class, Style.LEFT_MARGIN.compiler.getClass());
  }

  public void testPaddingsUsesPixleCompilers() throws Exception
  {
    assertEquals(PixelsAttributeCompiler.class, Style.TOP_PADDING.compiler.getClass());
    assertEquals(PixelsAttributeCompiler.class, Style.RIGHT_PADDING.compiler.getClass());
    assertEquals(PixelsAttributeCompiler.class, Style.BOTTOM_PADDING.compiler.getClass());
    assertEquals(PixelsAttributeCompiler.class, Style.LEFT_PADDING.compiler.getClass());
  }

  public void testRoundedCornerRadiusUsesPixleCompilers() throws Exception
  {
    assertEquals(PixelsAttributeCompiler.class, Style.TOP_RIGHT_ROUNDED_CORNER_RADIUS.compiler.getClass());
    assertEquals(PixelsAttributeCompiler.class, Style.BOTTOM_RIGHT_ROUNDED_CORNER_RADIUS.compiler.getClass());
    assertEquals(PixelsAttributeCompiler.class, Style.BOTTOM_LEFT_ROUNDED_CORNER_RADIUS.compiler.getClass());
    assertEquals(PixelsAttributeCompiler.class, Style.TOP_LEFT_ROUNDED_CORNER_RADIUS.compiler.getClass());
  }

  public void testBordersUsesPixleCompilers() throws Exception
  {
    assertEquals(PixelsAttributeCompiler.class, Style.TOP_BORDER_WIDTH.compiler.getClass());
    assertEquals(PixelsAttributeCompiler.class, Style.TOP_RIGHT_BORDER_WIDTH.compiler.getClass());
    assertEquals(PixelsAttributeCompiler.class, Style.RIGHT_BORDER_WIDTH.compiler.getClass());
    assertEquals(PixelsAttributeCompiler.class, Style.BOTTOM_RIGHT_BORDER_WIDTH.compiler.getClass());
    assertEquals(PixelsAttributeCompiler.class, Style.BOTTOM_BORDER_WIDTH.compiler.getClass());
    assertEquals(PixelsAttributeCompiler.class, Style.BOTTOM_LEFT_BORDER_WIDTH.compiler.getClass());
    assertEquals(PixelsAttributeCompiler.class, Style.LEFT_BORDER_WIDTH.compiler.getClass());
    assertEquals(PixelsAttributeCompiler.class, Style.TOP_LEFT_BORDER_WIDTH.compiler.getClass());
  }
  
  public void testXandY() throws Exception
  {
    assertEquals(XCoordinateAttributeCompiler.class, Style.X.compiler.getClass());
    assertEquals(YCoordinateAttributeCompiler.class, Style.Y.compiler.getClass());
  }

  public void testBackgroundImageXandY() throws Exception
  {
    assertEquals(XCoordinateAttributeCompiler.class, Style.BACKGROUND_IMAGE_X.compiler.getClass());
    assertEquals(YCoordinateAttributeCompiler.class, Style.BACKGROUND_IMAGE_Y.compiler.getClass());
  }
  
  public void testMinMaxWidthAndMinHeightCompilers() throws Exception
  {
    assertEquals(NoneableAttributeCompiler.class, Style.MIN_WIDTH.compiler.getClass());
    assertEquals(SimpleDimensionAttributeCompiler.class, ((NoneableAttributeCompiler)Style.MIN_WIDTH.compiler).getTarget().getClass());
    assertEquals(NoneableAttributeCompiler.class, Style.MIN_HEIGHT.compiler.getClass());
    assertEquals(SimpleDimensionAttributeCompiler.class, ((NoneableAttributeCompiler)Style.MIN_HEIGHT.compiler).getTarget().getClass());
    assertEquals(NoneableAttributeCompiler.class, Style.MAX_WIDTH.compiler.getClass());
    assertEquals(SimpleDimensionAttributeCompiler.class, ((NoneableAttributeCompiler)Style.MIN_WIDTH.compiler).getTarget().getClass());
    assertEquals(NoneableAttributeCompiler.class, Style.MAX_HEIGHT.compiler.getClass());
    assertEquals(SimpleDimensionAttributeCompiler.class, ((NoneableAttributeCompiler)Style.MIN_HEIGHT.compiler).getTarget().getClass());
  }
}
