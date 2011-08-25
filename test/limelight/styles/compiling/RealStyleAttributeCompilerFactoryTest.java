//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.compiling;

import limelight.styles.abstrstyling.StyleCompiler;
import limelight.styles.abstrstyling.NoneableAttributeCompiler;
import limelight.Context;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class RealStyleAttributeCompilerFactoryTest
{
  private RealStyleAttributeCompilerFactory factory;

  @Before
  public void setUp() throws Exception
  {
    factory = new RealStyleAttributeCompilerFactory();
  }

  @Test
  public void shouldInstall() throws Exception
  {
    RealStyleAttributeCompilerFactory.install();
    assertEquals(RealStyleAttributeCompilerFactory.class, Context.instance().styleAttributeCompilerFactory.getClass());
  }
  
  @Test
  public void shouldString() throws Exception
  {
    assertEquals(StringAttributeCompiler.class, factory.compiler("string", "blah").getClass());
  }

  @Test
  public void shouldInteger() throws Exception
  {
    assertEquals(IntegerAttributeCompiler.class, factory.compiler("integer", "blah").getClass());
  }

  @Test
  public void shouldPixels() throws Exception
  {
    assertEquals(PixelsAttributeCompiler.class, factory.compiler("pixels", "blah").getClass());
  }

  @Test
  public void shouldColor() throws Exception
  {
    assertEquals(ColorAttributeCompiler.class, factory.compiler("color", "blah").getClass());
  }

  @Test
  public void shouldOnOff() throws Exception
  {
    assertEquals(OnOffAttributeCompiler.class, factory.compiler("on/off", "blah").getClass());
  }

  @Test
  public void shouldPercentage() throws Exception
  {
    assertEquals(PercentageAttributeCompiler.class, factory.compiler("percentage", "blah").getClass());
  }

  @Test
  public void shouldDimension() throws Exception
  {
    assertEquals(DimensionAttributeCompiler.class, factory.compiler("dimension", "blah").getClass());
  }

  @Test
  public void shouldDegrees() throws Exception
  {
    assertEquals(DegreesAttributeCompiler.class, factory.compiler("degrees", "blah").getClass());
  }

  @Test
  public void shouldFillStrategy() throws Exception
  {
    assertEquals(FillStrategyAttributeCompiler.class, factory.compiler("fill strategy", "blah").getClass());
  }

  @Test
  public void shouldFontStyle() throws Exception
  {
    assertEquals(FontStyleAttributeCompiler.class, factory.compiler("font style", "blah").getClass());
  }

  @Test
  public void shouldHorizontalAlignment() throws Exception
  {
    assertEquals(HorizontalAlignmentAttributeCompiler.class, factory.compiler("horizontal alignment", "blah").getClass());
  }

  @Test
  public void shouldVerticalAlignment() throws Exception
  {
    assertEquals(VerticalAlignmentAttributeCompiler.class, factory.compiler("vertical alignment", "blah").getClass());
  }

  @Test
  public void shouldGetCursorCompiler() throws Exception
  {
    assertEquals(CursorAttributeCompiler.class, factory.compiler("cursor", "blah").getClass());
  }

  @Test
  public void shouldNoneableInteger() throws Exception
  {
    StyleCompiler compiler = factory.compiler("noneable integer", "blah");
    assertEquals(NoneableAttributeCompiler.class, compiler.getClass());
    assertEquals(IntegerAttributeCompiler.class, ((NoneableAttributeCompiler)compiler).getTarget().getClass());
  }
  
  @Test
  public void shouldNoneableString() throws Exception
  {
    StyleCompiler compiler = factory.compiler("noneable string", "blah");
    assertEquals(NoneableAttributeCompiler.class, compiler.getClass());
    assertEquals(StringAttributeCompiler.class, ((NoneableAttributeCompiler)compiler).getTarget().getClass());
  }

  @Test
  public void shouldXCoordinate() throws Exception
  {
    assertEquals(XCoordinateAttributeCompiler.class, factory.compiler("x-coordinate", "blah").getClass());
  }
  
  @Test
  public void shouldYCoordinate() throws Exception
  {
    assertEquals(YCoordinateAttributeCompiler.class, factory.compiler("y-coordinate", "blah").getClass());
  }
  
  @Test
  public void shouldSimpleDimensions() throws Exception
  {
    StyleCompiler compiler = factory.compiler("noneable simple dimension", "blah");
    assertEquals(NoneableAttributeCompiler.class, compiler.getClass());
    assertEquals(SimpleDimensionAttributeCompiler.class, ((NoneableAttributeCompiler)compiler).getTarget().getClass());
  }


}
