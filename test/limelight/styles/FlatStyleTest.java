//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles;

import limelight.styles.abstrstyling.PixelsValue;
import limelight.styles.values.SimpleCursorValue;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;

public class FlatStyleTest
{
  private FlatStyle style;
  private MockStyleObserver observer;

  @Before
  public void setUp() throws Exception
  {
    style = new FlatStyle();
    observer = new MockStyleObserver();
    style.addObserver(observer);
  }

  @Test
  public void shouldHandleObserving() throws Exception
  {
    MockStyleObserver observer = new MockStyleObserver();
    observer.changedStyle = null;
    style.addObserver(observer);

    style.setWidth("100");
    assertEquals(Style.WIDTH, observer.changedStyle);
    observer.changedStyle = null;

    style.setWidth("100");
    assertEquals(null, observer.changedStyle);

    style.setWidth("123");
    assertEquals(Style.WIDTH, observer.changedStyle);
  }

  @Test
  public void shouldHandleMultipleObservers() throws Exception
  {
    MockStyleObserver observer1 = new MockStyleObserver();
    style.addObserver(observer1);
    MockStyleObserver observer2 = new MockStyleObserver();
    style.addObserver(observer2);

    style.setHeight("123");

    assertEquals(Style.HEIGHT, observer1.changedStyle);
    assertEquals(Style.HEIGHT, observer2.changedStyle);
  }

  @Test
  public void shouldHandleRemoveObserver() throws Exception
  {
    MockStyleObserver observer1 = new MockStyleObserver();
    style.addObserver(observer1);
    MockStyleObserver observer2 = new MockStyleObserver();
    style.addObserver(observer2);

    style.removeObserver(observer1);
    style.setHeight("123");
    assertEquals(null, observer1.changedStyle);
    assertEquals(Style.HEIGHT, observer2.changedStyle);

    style.removeObserver(observer2);
    style.setWidth("123");
    assertEquals(null, observer1.changedStyle);
    assertEquals(Style.HEIGHT, observer2.changedStyle);
  }

  @Test
  public void shouldHandleChanges() throws Exception
  {
    assertEquals(false, observer.changed());

    style.setWidth("123");
    assertEquals(true, observer.changed());

    observer.flushChanges();
    assertEquals(false, observer.changed());

    style.setHeight("123");
    assertEquals(true, observer.changed());

    observer.flushChanges();
    assertEquals(false, observer.changed());
  }

  @Test
  public void shouldHandleSpecificChanges() throws Exception
  {
    assertEquals(false, observer.changed(Style.WIDTH));
    assertEquals(false, observer.changed(Style.HEIGHT));

    style.setWidth("123");
    assertEquals(true, observer.changed(Style.WIDTH));
    assertEquals(false, observer.changed(Style.HEIGHT));
    observer.flushChanges();
    assertEquals(false, observer.changed(Style.WIDTH));
    assertEquals(false, observer.changed(Style.HEIGHT));

    style.setHeight("321");
    assertEquals(false, observer.changed(Style.WIDTH));
    assertEquals(true, observer.changed(Style.HEIGHT));
    observer.flushChanges();
    assertEquals(false, observer.changed(Style.WIDTH));
    assertEquals(false, observer.changed(Style.HEIGHT));
  }

  @Test
  public void shouldHandleSetBorderWidthSetsWidthOnSidesAllCorners() throws Exception
  {
    style.setBorderWidth("5");

    assertEquals("5", style.getTopBorderWidth());
    assertEquals("5", style.getTopRightBorderWidth());
    assertEquals("5", style.getRightBorderWidth());
    assertEquals("5", style.getBottomRightBorderWidth());
    assertEquals("5", style.getBottomBorderWidth());
    assertEquals("5", style.getBottomLeftBorderWidth());
    assertEquals("5", style.getLeftBorderWidth());
    assertEquals("5", style.getTopLeftBorderWidth());
  }

  @Test
  public void shouldHandleSetBorderColorSetsColorOnSidesAllCorners() throws Exception
  {
    style.setBorderColor("blue");

    assertEquals("#0000ffff", style.getTopBorderColor());
    assertEquals("#0000ffff", style.getTopRightBorderColor());
    assertEquals("#0000ffff", style.getRightBorderColor());
    assertEquals("#0000ffff", style.getBottomRightBorderColor());
    assertEquals("#0000ffff", style.getBottomBorderColor());
    assertEquals("#0000ffff", style.getBottomLeftBorderColor());
    assertEquals("#0000ffff", style.getLeftBorderColor());
    assertEquals("#0000ffff", style.getTopLeftBorderColor());
  }

  @Test
  public void shouldHandleSettingRoundedCorderRadiusSetsRadiusOnAllCorders() throws Exception
  {
    style.setRoundedCornerRadius("5");

    assertEquals("5", style.getTopRightRoundedCornerRadius());
    assertEquals("5", style.getBottomRightRoundedCornerRadius());
    assertEquals("5", style.getBottomLeftRoundedCornerRadius());
    assertEquals("5", style.getTopLeftRoundedCornerRadius());
  }

  @Test
  public void shouldHandleSetTopRoundedCornerRadius() throws Exception
  {
    style.setTopRoundedCornerRadius("6");    

    assertEquals("6", style.getTopRightRoundedCornerRadius());
    assertEquals("6", style.getTopLeftRoundedCornerRadius());
  }

  @Test
  public void shouldHandleSetRightRoundedCornerRadius() throws Exception
  {
    style.setRightRoundedCornerRadius("6");

    assertEquals("6", style.getTopRightRoundedCornerRadius());
    assertEquals("6", style.getBottomRightRoundedCornerRadius());
  }

  @Test
  public void shouldHandleSetBottomRoundedCornerRadius() throws Exception
  {
    style.setBottomRoundedCornerRadius("6");

    assertEquals("6", style.getBottomRightRoundedCornerRadius());
    assertEquals("6", style.getBottomLeftRoundedCornerRadius());
  }

  @Test
  public void shouldHandleSetLeftRoundedCornerRadius() throws Exception
  {
    style.setLeftRoundedCornerRadius("6");

    assertEquals("6", style.getTopLeftRoundedCornerRadius());
    assertEquals("6", style.getBottomLeftRoundedCornerRadius());
  }

  @Test
  public void shouldHandleFloatingStyle() throws Exception
  {
    assertEquals("off", style.getFloat());

    style.setFloat("on");

    assertEquals("on", style.getFloat());
  }

  @Test
  public void shouldHandleXandY() throws Exception
  {
    assertEquals("0", style.getX());
    assertEquals("0", style.getY());

    style.setX("1");
    style.setY("2");

    assertEquals("1", style.getX());
    assertEquals("2", style.getY());
  }

  @Test
  public void shouldHandleScrollBars() throws Exception
  {
    style.setScrollbars("on");
    assertEquals("on", style.getVerticalScrollbar());
    assertEquals("on", style.getHorizontalScrollbar());

    style.setVerticalScrollbar("off");
    assertEquals("off", style.getVerticalScrollbar());

    style.setHorizontalScrollbar("off");
    assertEquals("off", style.getHorizontalScrollbar());
  }

  @Test
  public void shouldHandleSettingDefaultAffecsChanges() throws Exception
  {
    assertEquals(false, observer.changed(Style.WIDTH));

    style.setDefault(Style.WIDTH, style.getWidth());
    assertEquals(false, observer.changed(Style.WIDTH));

    style.setDefault(Style.WIDTH, "123");
    assertEquals(true, observer.changed(Style.WIDTH));
  }

  @Test
  public void shouldHandleMaxWidthAndHeight() throws Exception
  {
    assertEquals("none", style.getMaxWidth());
    assertEquals("none", style.getMaxHeight());

    style.setMaxWidth("100");
    style.setMaxHeight("200");

    assertEquals("100", style.getMaxWidth());
    assertEquals("200", style.getMaxHeight());
  }

  @Test
  public void shouldHandleMinWidthAndHeight() throws Exception
  {
    assertEquals("none", style.getMinWidth());
    assertEquals("none", style.getMinHeight());

    style.setMinWidth("100");
    style.setMinHeight("200");

    assertEquals("100", style.getMinWidth());
    assertEquals("200", style.getMinHeight());
  }
  
  @Test
  public void shouldHandleMarginsReturnPixlesAttribute() throws Exception
  {
    style.setMargin("10%");
    assertEquals(true, style.getCompiledTopMargin() instanceof PixelsValue);
    assertEquals(true, style.getCompiledRightMargin() instanceof PixelsValue);
    assertEquals(true, style.getCompiledBottomMargin() instanceof PixelsValue);
    assertEquals(true, style.getCompiledLeftMargin() instanceof PixelsValue);
  }
  
  @Test
  public void shouldHandlePaddingsReturnPixlesAttribute() throws Exception
  {
    style.setPadding("10%");
    assertEquals(true, style.getCompiledTopPadding() instanceof PixelsValue);
    assertEquals(true, style.getCompiledRightPadding() instanceof PixelsValue);
    assertEquals(true, style.getCompiledBottomPadding() instanceof PixelsValue);
    assertEquals(true, style.getCompiledLeftPadding() instanceof PixelsValue);
  }

  @Test
  public void shouldHandleRoundedCornerRadiusReturnPixlesAttribute() throws Exception
  {
    style.setRightRoundedCornerRadius("10%");
    assertEquals(true, style.getCompiledTopRightRoundedCornerRadius() instanceof PixelsValue);
    assertEquals(true, style.getCompiledBottomRightRoundedCornerRadius() instanceof PixelsValue);
    assertEquals(true, style.getCompiledBottomLeftRoundedCornerRadius() instanceof PixelsValue);
    assertEquals(true, style.getCompiledTopLeftRoundedCornerRadius() instanceof PixelsValue);
  }

  @Test
  public void shouldHandleBordersReturnPixlesAttribute() throws Exception
  {
    style.setBorderWidth("10%");
    assertEquals(true, style.getCompiledTopBorderWidth() instanceof PixelsValue);
    assertEquals(true, style.getCompiledTopRightBorderWidth() instanceof PixelsValue);
    assertEquals(true, style.getCompiledRightBorderWidth() instanceof PixelsValue);
    assertEquals(true, style.getCompiledBottomRightBorderWidth() instanceof PixelsValue);
    assertEquals(true, style.getCompiledBottomBorderWidth() instanceof PixelsValue);
    assertEquals(true, style.getCompiledBottomLeftBorderWidth() instanceof PixelsValue);
    assertEquals(true, style.getCompiledLeftBorderWidth() instanceof PixelsValue);
    assertEquals(true, style.getCompiledTopLeftBorderWidth() instanceof PixelsValue);
  }

  @Test
  public void shouldHandleAlignmentShortCuts() throws Exception
  {
    checkSetAlignment("center", "center", "center");
    checkSetAlignment("center center", "center", "center");
    checkSetAlignment("top right", "top", "right");
    checkSetAlignment("bottom left", "bottom", "left");
  }

  private void checkSetAlignment(String value, String expectedVerticalAlignment, String expectedHorizontalAlignment)
  {
    style.setAlignment(value);
    assertEquals(expectedVerticalAlignment, style.getVerticalAlignment());
    assertEquals(expectedHorizontalAlignment, style.getHorizontalAlignment());
  }
  
  @Test
  public void shouldHandleCursor() throws Exception
  {
    style.setCuror("hand");
    assertEquals("hand", style.getCursor());
    assertSame(SimpleCursorValue.HAND, style.getCompiledCursor());
  }



//  // String-Hash based : 3800000 sets and 3500000 gets took = 2611.0 milliseconds
//  // Array based: 3800000 sets and 3500000 gets took = 1863.0 milliseconds
//  @Test
//  public void shouldHandlePerformance() throws Exception
//  {
//    int iterations = 100000;
//    ArrayList<Method> setters = new ArrayList<Method>();
//    ArrayList<Method> getters = new ArrayList<Method>();
//    for (Method method : Style.class.getMethods())
//    {
//      if(method.getName().startsWith("set"))
//        setters.add(method);
//      else if(method.getName().startsWith("get") && method.getParameterTypes().length == 0)
//        getters.src/limelight/styles/FlatStyleTest.java(method);
//    }
//
//    Style style = new FlatStyle();
//    int sets = 0;
//    int gets = 0;
//
//    double startTime = System.currentTimeMillis();
//    for(int i = 0; i < iterations; i++)
//    {
//      for (Method method : setters)
//      {
//        method.invoke(style, method.getName());
//        sets++;
//      }
//      for (Method method : getters)
//      {
//        method.invoke(style);
//        gets++;
//      }
//    }
//    double duration = System.currentTimeMillis() - startTime;
//
//    System.out.println(sets + " sets and " + gets + " gets took = " + duration + " milliseconds");
//  }
}
