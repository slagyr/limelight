//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.compiling;

import junit.framework.TestCase;
import limelight.styles.abstrstyling.StyleCompiler;
import limelight.styles.abstrstyling.NoneableAttributeCompiler;
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
    assertEquals(StringAttributeCompiler.class, factory.compiler("string", "blah").getClass());
  }

  public void testInteger() throws Exception
  {
    assertEquals(IntegerAttributeCompiler.class, factory.compiler("integer", "blah").getClass());
  }

  public void testPixels() throws Exception
  {
    assertEquals(PixelsAttributeCompiler.class, factory.compiler("pixels", "blah").getClass());
  }

  public void testColor() throws Exception
  {
    assertEquals(ColorAttributeCompiler.class, factory.compiler("color", "blah").getClass());
  }

  public void testOnOff() throws Exception
  {
    assertEquals(OnOffAttributeCompiler.class, factory.compiler("on/off", "blah").getClass());
  }

  public void testPercentage() throws Exception
  {
    assertEquals(PercentageAttributeCompiler.class, factory.compiler("percentage", "blah").getClass());
  }

  public void testDimension() throws Exception
  {
    assertEquals(DimensionAttributeCompiler.class, factory.compiler("dimension", "blah").getClass());
  }

  public void testDegrees() throws Exception
  {
    assertEquals(DegreesAttributeCompiler.class, factory.compiler("degrees", "blah").getClass());
  }

  public void testFillStrategy() throws Exception
  {
    assertEquals(FillStrategyAttributeCompiler.class, factory.compiler("fill strategy", "blah").getClass());
  }

  public void testFontStyle() throws Exception
  {
    assertEquals(FontStyleAttributeCompiler.class, factory.compiler("font style", "blah").getClass());
  }

  public void testHorizontalAlignment() throws Exception
  {
    assertEquals(HorizontalAlignmentAttributeCompiler.class, factory.compiler("horizontal alignment", "blah").getClass());
  }

  public void testVerticalAlignment() throws Exception
  {
    assertEquals(VerticalAlignmentAttributeCompiler.class, factory.compiler("vertical alignment", "blah").getClass());
  }

  public void testNoneableInteger() throws Exception
  {
    StyleCompiler compiler = factory.compiler("noneable integer", "blah");
    assertEquals(NoneableAttributeCompiler.class, compiler.getClass());
    assertEquals(IntegerAttributeCompiler.class, ((NoneableAttributeCompiler)compiler).getTarget().getClass());
  }
  
  public void testNoneableString() throws Exception
  {
    StyleCompiler compiler = factory.compiler("noneable string", "blah");
    assertEquals(NoneableAttributeCompiler.class, compiler.getClass());
    assertEquals(StringAttributeCompiler.class, ((NoneableAttributeCompiler)compiler).getTarget().getClass());
  }

  public void testXCoordinate() throws Exception
  {
    assertEquals(XCoordinateAttributeCompiler.class, factory.compiler("x-coordinate", "blah").getClass());
  }
  
  public void testYCoordinate() throws Exception
  {
    assertEquals(YCoordinateAttributeCompiler.class, factory.compiler("y-coordinate", "blah").getClass());
  }
  
  public void testSimpleDimensions() throws Exception
  {
    StyleCompiler compiler = factory.compiler("noneable simple dimension", "blah");
    assertEquals(NoneableAttributeCompiler.class, compiler.getClass());
    assertEquals(SimpleDimensionAttributeCompiler.class, ((NoneableAttributeCompiler)compiler).getTarget().getClass());
  }
}
