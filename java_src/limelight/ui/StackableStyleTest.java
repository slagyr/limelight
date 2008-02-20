package limelight.ui;

import junit.framework.TestCase;

public class StackableStyleTest extends TestCase
{
  private StackableStyle style;
  private FlatStyle style2;
  private FlatStyle style3;

  public void setUp() throws Exception
  {
    style = new StackableStyle();
    style2 = new FlatStyle();
    style3 = new FlatStyle();

    style.push(style2);
    style.push(style3);
  }

  public void testWithNothingStacked() throws Exception
  {
    style = new StackableStyle();

    style.setWidth("100");
    assertEquals("100", style.getWidth());
    assertTrue(style.changed());
    assertTrue(style.changed(Style.WIDTH));
  }

  public void testChanges() throws Exception
  {
    assertFalse(style.changed());

    style.setWidth("123");
    assertTrue(style.changed());

    style.flushChanges();
    assertFalse(style.changed());

    style2.setWidth("123");
    assertTrue(style.changed());

    style.flushChanges();
    assertFalse(style.changed());

    style3.setWidth("123");
    assertFalse(style.changed());

    style3.setWidth("321");
    assertTrue(style.changed());

    style.flushChanges();
    assertFalse(style.changed());
  }

  public void testSpecificChanges() throws Exception
  {
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

  public void testAddingToTopAffectsChanges() throws Exception
  {
    FlatStyle newStyle = new FlatStyle();
    newStyle.setWidth("100");

    style.push(newStyle);
    assertTrue(style.changed());
    assertTrue(style.changed(Style.WIDTH));

    style.flushChanges();

    FlatStyle anotherNewStyle = new FlatStyle();
    anotherNewStyle.setWidth("100");
    style.push(anotherNewStyle);
    assertFalse(style.changed());
  }

  public void testAddingToBottomAffectsChanges() throws Exception
  {
    style.setWidth("100");
    style.flushChanges();

    FlatStyle newStyle = new FlatStyle();
    newStyle.setWidth("200");

    style.addToBottom(newStyle);
    assertFalse(style.changed());

    FlatStyle anotherNewStyle = new FlatStyle();
    anotherNewStyle.setHeight("100");
    style.addToBottom(anotherNewStyle);
    assertTrue(style.changed());
    assertTrue(style.changed(Style.HEIGHT));
  }

  public void testRemoveFromTopAffectsChanges() throws Exception
  {
    FlatStyle newStyle = new FlatStyle();
    newStyle.setWidth("100");
    FlatStyle anotherNewStyle = new FlatStyle();
    anotherNewStyle.setWidth("100");

    style.push(newStyle);
    style.push(anotherNewStyle);
    style.flushChanges();

    style.pop();
    assertFalse(style.changed());

    style.pop();
    assertTrue(style.changed());
    assertTrue(style.changed(Style.WIDTH));
  }

  public void testRemoveFromBottomAffectsChanges() throws Exception
  {
    FlatStyle newStyle = new FlatStyle();
    newStyle.setWidth("100");
    FlatStyle anotherNewStyle = new FlatStyle();
    anotherNewStyle.setWidth("200");

    style.addToBottom(newStyle);
    style.addToBottom(anotherNewStyle);
    style.flushChanges();

    style.removeFromBottom();
    assertFalse(style.changed());

    style.removeFromBottom();
    assertTrue(style.changed());
    assertTrue(style.changed(Style.WIDTH));
  }
}
