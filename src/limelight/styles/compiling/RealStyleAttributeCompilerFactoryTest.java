//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.compiling;

import junit.framework.TestCase;
import limelight.styles.abstrstyling.StyleAttributeCompiler;
import limelight.styles.abstrstyling.NoneableAttributeCompiler;
import limelight.styles.compiling.StringAttributeCompiler;
import limelight.styles.compiling.VerticalAlignmentAttributeCompiler;
import limelight.styles.compiling.XCoordinateAttributeCompiler;
import limelight.styles.compiling.YCoordinateAttributeCompiler;
import limelight.Context;

public class RealStyleAttributeCompilerFactoryTest extends TestCase
{
  private RealStyleAttributeCompilerFactory factory;

  public void setUp() throws Exception
  {
    factory = new RealStyleAttributeCompilerFactory();
  }

  public void testInstall() throws Exception
  {
    RealStyleAttributeCompilerFactory.install();
    assertEquals(RealStyleAttributeCompilerFactory.class, Context.instance().styleAttributeCompilerFactory.getClass());
  }
  
  public void testString() throws Exception
  {
    assertEquals(StringAttributeCompiler.class, factory.compiler("string").getClass());
  }

  public void testInteger() throws Exception
  {
    assertEquals(IntegerAttributeCompiler.class, factory.compiler("integer").getClass());
  }

  public void testPixels() throws Exception
  {
    assertEquals(PixelsAttributeCompiler.class, factory.compiler("pixels").getClass());
  }

  public void testColor() throws Exception
  {
    assertEquals(ColorAttributeCompiler.class, factory.compiler("color").getClass());
  }

  public void testOnOff() throws Exception
  {
    assertEquals(OnOffAttributeCompiler.class, factory.compiler("on/off").getClass());  
  }

  public void testPercentage() throws Exception
  {
    assertEquals(PercentageAttributeCompiler.class, factory.compiler("percentage").getClass());  
  }

  public void testDimension() throws Exception
  {
    assertEquals(DimensionAttributeCompiler.class, factory.compiler("dimension").getClass());
  }

  public void testDegrees() throws Exception
  {
    assertEquals(DegreesAttributeCompiler.class, factory.compiler("degrees").getClass());
  }

  public void testFillStrategy() throws Exception
  {
    assertEquals(FillStrategyAttributeCompiler.class, factory.compiler("fill strategy").getClass());
  }

  public void testFontStyle() throws Exception
  {
    assertEquals(FontStyleAttributeCompiler.class, factory.compiler("font style").getClass());
  }

  public void testHorizontalAlignment() throws Exception
  {
    assertEquals(HorizontalAlignmentAttributeCompiler.class, factory.compiler("horizontal alignment").getClass());
  }

  public void testVerticalAlignment() throws Exception
  {
    assertEquals(VerticalAlignmentAttributeCompiler.class, factory.compiler("vertical alignment").getClass());
  }

  public void testNoneableInteger() throws Exception
  {
    StyleAttributeCompiler compiler = factory.compiler("noneable integer");
    assertEquals(NoneableAttributeCompiler.class, compiler.getClass());
    assertEquals(IntegerAttributeCompiler.class, ((NoneableAttributeCompiler)compiler).getTarget().getClass());
  }
  
  public void testNoneableString() throws Exception
  {
    StyleAttributeCompiler compiler = factory.compiler("noneable string");
    assertEquals(NoneableAttributeCompiler.class, compiler.getClass());
    assertEquals(StringAttributeCompiler.class, ((NoneableAttributeCompiler)compiler).getTarget().getClass());
  }

  public void testXCoordinate() throws Exception
  {
    assertEquals(XCoordinateAttributeCompiler.class, factory.compiler("x-coordinate").getClass());
  }
  
  public void testYCoordinate() throws Exception
  {
    assertEquals(YCoordinateAttributeCompiler.class, factory.compiler("y-coordinate").getClass());
  }
}
