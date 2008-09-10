//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles;

import junit.framework.TestCase;

public class RichStyleTest extends TestCase
{
  private RichStyle style;
  private RichStyle style2;
  private RichStyle style3;
  private RichStyle style4;
  private RichStyle newStyle;
  private RichStyle anotherNewStyle;

  public void setUp() throws Exception
  {
    style = new RichStyle();
    style2 = new RichStyle();
    style3 = new RichStyle();
    style4 = new RichStyle();
    newStyle = new RichStyle();
    anotherNewStyle = new RichStyle();
  }

  public void testHasDefaultsFirst() throws Exception
  {
    for(StyleDescriptor descriptor : Style.STYLE_LIST)
      assertEquals(descriptor.defaultValue, style.get(descriptor));
  }
  
  public void testCanSetNewValues() throws Exception
  {
    style.setBackgroundColor("blue");
    assertEquals("blue", style.getBackgroundColor());
    assertEquals("blue", style.get(Style.BACKGROUND_COLOR));
  }

  public void testInsetNext() throws Exception
  {
    style.addExtension(style2);
    assertEquals(true, style.hasExtension(style2));
    assertEquals(true, style2.hasObserver(style));

    style.addExtension(style3);
    assertEquals(true, style.hasExtension(style3));
    assertEquals(true, style3.hasObserver(style));
  }

  public void testRemoveExtension() throws Exception
  {
    style.addExtension(style2);
    style2.addExtension(style3);

    style.removeExtension(style2);

    assertEquals(true, style2.hasExtension(style3));
    assertEquals(false, style.hasExtension(style2));
  }

  public void testWithNothingStacked() throws Exception
  {
    style.setWidth("100");
    assertEquals("100", style.getWidth());
    assertEquals(true, style.changed());
    assertEquals(true, style.changed(Style.WIDTH));
  }

  public void testChangesFromBelow() throws Exception
  {
    style.addExtension(style2);
    style.addExtension(style3);

    assertFalse(style.changed());

    style.setWidth("123");
    assertTrue(style.changed());

    style.flushChanges();
    assertFalse(style.changed());

    style2.setWidth("123");
    assertFalse(style.changed());

    style.flushChanges();
    assertFalse(style.changed());

    style3.setWidth("123");
    assertFalse(style.changed());

    style3.setHeight("321");
    assertTrue(style.changed());

    style.flushChanges();
    assertFalse(style.changed());
  }

  public void testSpecificChanges() throws Exception
  {
    style.addExtension(style2);
    style.addExtension(style3);
    
    assertFalse(style.changed(Style.WIDTH));
    assertFalse(style.changed(Style.HEIGHT));

    style3.setWidth("123");
    assertTrue(style.changed(Style.WIDTH));
    assertFalse(style.changed(Style.HEIGHT));
    style.flushChanges();
    assertFalse(style.changed(Style.WIDTH));
    assertFalse(style.changed(Style.HEIGHT));

    style2.setWidth("123");
    assertTrue(style.changed(Style.WIDTH));
    assertFalse(style.changed(Style.HEIGHT));
    style.flushChanges();
    assertFalse(style.changed(Style.WIDTH));
    assertFalse(style.changed(Style.HEIGHT));
  }

  public void testAddingToBottomAffectsChanges() throws Exception
  {
    style.setWidth("100");
    style.flushChanges();

    newStyle.setWidth("200");

    style.addExtension(newStyle);
    assertFalse(style.changed());

    anotherNewStyle.setHeight("100");
    style.addExtension(anotherNewStyle);
    assertTrue(style.changed());
    assertTrue(style.changed(Style.HEIGHT));
  }

  public void testRemoveFromBottomAffectsChanges() throws Exception
  {
    newStyle.setWidth("100");
    anotherNewStyle.setWidth("200");

    style.addExtension(newStyle);
    style.addExtension(anotherNewStyle);
    style.flushChanges();

    style.removeExtension(anotherNewStyle);
    assertFalse(style.changed());

    style.removeExtension(newStyle);
    assertTrue(style.changed());
    assertTrue(style.changed(Style.WIDTH));
  }

  public void testCanHaveLotsOfExtenders() throws Exception
  {
    style.setWidth("100");

    style2.addExtension(style);
    style3.addExtension(style);
    style4.addExtension(style);

    assertEquals(true, style2.hasExtension(style));
    assertEquals(true, style3.hasExtension(style));
    assertEquals(true, style4.hasExtension(style));
    assertEquals(true, style.hasObserver(style2));
    assertEquals(true, style.hasObserver(style3));
    assertEquals(true, style.hasObserver(style4));
    assertEquals("100", style2.getWidth());
    assertEquals("100", style3.getWidth());
    assertEquals("100", style4.getWidth());
  }

}
