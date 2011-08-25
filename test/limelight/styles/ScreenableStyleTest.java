//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles;

import junit.framework.TestCase;

public class ScreenableStyleTest extends TestCase
{
  private ScreenableStyle style;
  private RichStyle style2;
  private RichStyle style3;
  private MockStyleObserver observer;

  public void setUp() throws Exception
  {
    style = new ScreenableStyle();
    style2 = new RichStyle();
    style3 = new RichStyle();

    observer = new MockStyleObserver();
    style.addObserver(observer);
  }

  public void testNoScreenAtFirst() throws Exception
  {
    assertEquals(null, style.getScreen());
  }

  public void testAddingEmptyScreenDoesntRegisterChanges() throws Exception
  {
    style.setWidth("100");
    observer.flushChanges();
    style.applyScreen(style2);

    assertSame(style2, style.getScreen());
    assertEquals("100", style.getWidth());
    assertEquals(false, observer.changed());
    assertEquals(0, observer.getChangedCount());
  }

  public void testAddingFullScreenAppliesChanges() throws Exception
  {
    style.setWidth("100");
    observer.flushChanges();
    style2.setWidth("123");

    style.applyScreen(style2);

    assertEquals("123", style.getWidth());
    assertEquals(1, observer.getChangedCount());
    assertEquals(true, observer.changed());
    assertEquals(true, observer.changed(Style.WIDTH));
  }

  public void testRemovingEmptyScreenDoesntAffectChanges() throws Exception
  {
    style.setWidth("100");
    observer.flushChanges();
    style.applyScreen(style2);
    observer.flushChanges();

    style.removeScreen();

    assertSame(null, style.getScreen());
    assertEquals("100", style.getWidth());
    assertEquals(false, observer.changed());
    assertEquals(0, observer.getChangedCount());
  }

  public void testRemovingFullScreenAppliesChanges() throws Exception
  {
    style.setWidth("100");
    observer.flushChanges();
    style2.setWidth("123");
    style.applyScreen(style2);
    observer.flushChanges();

    style.removeScreen();

    assertEquals("100", style.getWidth());
    assertEquals(1, observer.getChangedCount());
    assertEquals(true, observer.changed());
    assertEquals(true, observer.changed(Style.WIDTH));
  }

  public void testCantApplyMultipleScreens() throws Exception
  {
    style.applyScreen(style2);

    try
    {
      style.applyScreen(style3);
      fail("Should have throws an exception");
    }
    catch(Exception e)
    {
      assertEquals("Screen already applied", e.getMessage());  
    }
  }

  public void testNotifyObserversWhenAddingOrRemovingScreen() throws Exception
  {
    MockStyleObserver observer = new MockStyleObserver();
    style.addObserver(observer);

    style2.setWidth("123");
    style2.setHeight("321");
    style.applyScreen(style2);

    assertEquals(2, observer.attributeChanges.size());
    assertEquals(2, observer.valueChanges.size());
    assertEquals(Style.WIDTH, observer.attributeChanges.get(0));
    assertEquals(Style.HEIGHT, observer.attributeChanges.get(1));
    assertEquals("123", observer.valueChanges.get(0).toString());
    assertEquals("321", observer.valueChanges.get(1).toString());

    style.removeScreen();

    assertEquals(4, observer.attributeChanges.size());
    assertEquals(4, observer.valueChanges.size());
    assertEquals(Style.WIDTH, observer.attributeChanges.get(2));
    assertEquals(Style.HEIGHT, observer.attributeChanges.get(3));
    assertEquals("auto", observer.valueChanges.get(2).toString());
    assertEquals("auto", observer.valueChanges.get(3).toString());
  }
}
