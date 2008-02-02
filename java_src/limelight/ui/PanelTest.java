package limelight.ui;

import junit.framework.TestCase;

public class PanelTest extends TestCase
{
  private TestablePanel panel;
  private MockBlock block;

  public void setUp() throws Exception
  {
    block = new MockBlock();
    panel = new TestablePanel(block);
  }

  public void testPanelHasDefaultSize() throws Exception
  {
    assertEquals(50, panel.getHeight());
    assertEquals(50, panel.getWidth());
  }

  public void testLocationDefaults() throws Exception
  {
    assertEquals(0, panel.getX());
    assertEquals(0, panel.getY());
  }

  public void testCanSetSize() throws Exception
  {
    panel.setSize(100, 200);
    assertEquals(100, panel.getWidth());
    assertEquals(200, panel.getHeight());

    panel.setWidth(300);
    assertEquals(300, panel.getWidth());

    panel.setHeight(400);
    assertEquals(400, panel.getHeight());
  }

  public void testCanSetLocation() throws Exception
  {
    panel.setLocation(123, 456);
    assertEquals(123, panel.getX());
    assertEquals(456, panel.getY());

    panel.setX(234);
    assertEquals(234, panel.getX());

    panel.setY(567);
    assertEquals(567, panel.getY());
  }
}

