package limelight.ui;

import junit.framework.TestCase;

public class FlatStyleTest extends TestCase
{
  private FlatStyle style;

  public void setUp() throws Exception
  {
    style = new FlatStyle();
  }

  public void testSettingEmptyStringIsSameAsNull() throws Exception
  {
    style.setWidth("");
    assertEquals(null, style.getWidth());
    style.setWidth("      ");
    assertEquals(null, style.getWidth());
    style.setWidth(null);
    assertEquals(null, style.getWidth());
  }

  private class MockStyleObserver implements StyleObserver
  {
    public int changedStyle;

    public void styleChanged(int key)
    {
      changedStyle = key;
    }
  }

  public void testObserving() throws Exception
  {
    MockStyleObserver observer = new MockStyleObserver();
    observer.changedStyle = -1;
    style.addObserver(observer);

    style.setWidth("100");
    assertEquals(Style.WIDTH, observer.changedStyle);
    observer.changedStyle = -1;

    style.setWidth("100");
    assertEquals(-1, observer.changedStyle);

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
    assertEquals(0, observer1.changedStyle);
    assertEquals(Style.HEIGHT, observer2.changedStyle);

    style.removeObserver(observer2);
    style.setWidth("123");
    assertEquals(0, observer1.changedStyle);
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

    assertEquals("blue", style.getTopBorderColor());
    assertEquals("blue", style.getTopRightBorderColor());
    assertEquals("blue", style.getRightBorderColor());
    assertEquals("blue", style.getBottomRightBorderColor());
    assertEquals("blue", style.getBottomBorderColor());
    assertEquals("blue", style.getBottomLeftBorderColor());
    assertEquals("blue", style.getLeftBorderColor());
    assertEquals("blue", style.getTopLeftBorderColor());
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
//        getters.add(method);
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
