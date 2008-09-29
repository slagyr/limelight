//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles;

import junit.framework.TestCase;
import limelight.styles.styling.RealStyleAttributeCompilerFactory;

public class FlatStyleTest extends TestCase
{
  static
  {
    RealStyleAttributeCompilerFactory.install();
  }
  
  private FlatStyle style;

  public void setUp() throws Exception
  {
    style = new FlatStyle();
  }

  public void testObserving() throws Exception
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

  public void testMultipleObservers() throws Exception
  {
    MockStyleObserver observer1 = new MockStyleObserver();
    style.addObserver(observer1);
    MockStyleObserver observer2 = new MockStyleObserver();
    style.addObserver(observer2);

    style.setHeight("123");

    assertEquals(Style.HEIGHT, observer1.changedStyle);
    assertEquals(Style.HEIGHT, observer2.changedStyle);
  }

  public void testRemoveObserver() throws Exception
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

  public void testChanges() throws Exception
  {
    assertFalse(style.changed());

    style.setWidth("123");
    assertTrue(style.changed());

    style.flushChanges();
    assertFalse(style.changed());

    style.setHeight("123");
    assertTrue(style.changed());

    style.flushChanges();
    assertFalse(style.changed());
  }

  public void testSpecificChanges() throws Exception
  {
    assertFalse(style.changed(Style.WIDTH));
    assertFalse(style.changed(Style.HEIGHT));

    style.setWidth("123");
    assertTrue(style.changed(Style.WIDTH));
    assertFalse(style.changed(Style.HEIGHT));
    style.flushChanges();
    assertFalse(style.changed(Style.WIDTH));
    assertFalse(style.changed(Style.HEIGHT));

    style.setHeight("321");
    assertFalse(style.changed(Style.WIDTH));
    assertTrue(style.changed(Style.HEIGHT));
    style.flushChanges();
    assertFalse(style.changed(Style.WIDTH));
    assertFalse(style.changed(Style.HEIGHT));
  }

  public void testChangeCount() throws Exception
  {
    assertEquals(0, style.getChangedCount());

    style.setWidth("123");
    assertEquals(1, style.getChangedCount());

    style.setHeight("123");
    assertEquals(2, style.getChangedCount());

    style.flushChanges();
    assertEquals(0, style.getChangedCount());
  }

  public void testSetBorderWidthSetsWidthOnSidesAllCorners() throws Exception
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

  public void testSetBorderColorSetsColorOnSidesAllCorners() throws Exception
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

  public void testSettingRoundedCorderRadiusSetsRadiusOnAllCorders() throws Exception
  {
    style.setRoundedCornerRadius("5");

    assertEquals("5", style.getTopRightRoundedCornerRadius());
    assertEquals("5", style.getBottomRightRoundedCornerRadius());
    assertEquals("5", style.getBottomLeftRoundedCornerRadius());
    assertEquals("5", style.getTopLeftRoundedCornerRadius());
  }

  public void testSetTopRoundedCornerRadius() throws Exception
  {
    style.setTopRoundedCornerRadius("6");    

    assertEquals("6", style.getTopRightRoundedCornerRadius());
    assertEquals("6", style.getTopLeftRoundedCornerRadius());
  }

  public void testSetRightRoundedCornerRadius() throws Exception
  {
    style.setRightRoundedCornerRadius("6");

    assertEquals("6", style.getTopRightRoundedCornerRadius());
    assertEquals("6", style.getBottomRightRoundedCornerRadius());
  }

  public void testSetBottomRoundedCornerRadius() throws Exception
  {
    style.setBottomRoundedCornerRadius("6");

    assertEquals("6", style.getBottomRightRoundedCornerRadius());
    assertEquals("6", style.getBottomLeftRoundedCornerRadius());
  }

  public void testSetLeftRoundedCornerRadius() throws Exception
  {
    style.setLeftRoundedCornerRadius("6");

    assertEquals("6", style.getTopLeftRoundedCornerRadius());
    assertEquals("6", style.getBottomLeftRoundedCornerRadius());
  }

  public void testFloatingStyle() throws Exception
  {
    assertEquals("off", style.getFloat());

    style.setFloat("on");

    assertEquals("on", style.getFloat());
  }

  public void testXandY() throws Exception
  {
    assertEquals("0", style.getX());
    assertEquals("0", style.getY());

    style.setX("1");
    style.setY("2");

    assertEquals("1", style.getX());
    assertEquals("2", style.getY());
  }

  public void testScrollBars() throws Exception
  {
    style.setScrollbars("on");
    assertEquals("on", style.getVerticalScrollbar());
    assertEquals("on", style.getHorizontalScrollbar());

    style.setVerticalScrollbar("off");
    assertEquals("off", style.getVerticalScrollbar());

    style.setHorizontalScrollbar("off");
    assertEquals("off", style.getHorizontalScrollbar());
  }

  public void testSettingDefaultAffecsChanges() throws Exception
  {
    assertEquals(false, style.changed(Style.WIDTH));

    style.setDefault(Style.WIDTH, style.getWidth());
    assertEquals(false, style.changed(Style.WIDTH));

    style.setDefault(Style.WIDTH, "123");
    assertEquals(true, style.changed(Style.WIDTH));
  }

  public void testMaxWidthAndHeight() throws Exception
  {
    assertEquals("none", style.getMaxWidth());
    assertEquals("none", style.getMaxHeight());

    style.setMaxWidth("100");
    style.setMaxHeight("200");

    assertEquals("100", style.getMaxWidth());
    assertEquals("200", style.getMaxHeight());
  }

  public void testMinWidthAndHeight() throws Exception
  {
    assertEquals("none", style.getMinWidth());
    assertEquals("none", style.getMinHeight());

    style.setMinWidth("100");
    style.setMinHeight("200");

    assertEquals("100", style.getMinWidth());
    assertEquals("200", style.getMinHeight());
  }

//  // String-Hash based : 3800000 sets and 3500000 gets took = 2611.0 milliseconds
//  // Array based: 3800000 sets and 3500000 gets took = 1863.0 milliseconds
//  public void testPerformance() throws Exception
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
