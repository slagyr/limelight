package limelight.ui;

import junit.framework.TestCase;

import java.awt.event.MouseWheelEvent;
import java.util.LinkedList;

public class ScrollPanelTest extends TestCase
{
  private MockBlockPanel parent;
  private ScrollPanel panel;

  public void setUp() throws Exception
  {
    parent = new MockBlockPanel();
    parent.childConsumableRectangle = new Rectangle(0, 0, 500, 500);
    panel = new ScrollPanel(new LinkedList<Panel>());
    parent.add(panel);
  }

  public void testSnapToSize() throws Exception
  {
    panel.snapToSize();
    assertEquals(500, panel.getWidth());
    assertEquals(500, panel.getHeight());
  }

  public void testComponents() throws Exception
  {
    assertEquals(3, panel.getChildren().size());
    assertTrue(panel.isChild(panel.getView()));
    assertTrue(panel.isChild(panel.getVerticalScrollBar()));
    assertTrue(panel.isChild(panel.getHorizontalScrollBar()));
  }

  public void testDoLayout() throws Exception
  {
    parent.setParent(new MockRootBlockPanel());
    MockPanel child = new MockPanel();
    child.prepForSnap(1000, 1000);
    panel.getView().add(child);

    panel.doLayout();

    assertEquals(new Rectangle(0, 0, 485, 485), panel.getView().getBounds());
    assertEquals(new Rectangle(485, 0, 15, 485), panel.getVerticalScrollBar().getBounds());
    assertEquals(new Rectangle(0, 485, 485, 15), panel.getHorizontalScrollBar().getBounds());
  }

  public void testVerticalMouseWheelMovement() throws Exception
  {
    MockRootBlockPanel root = new MockRootBlockPanel();
    parent.setParent(root);
    int modifer = 0;
    int scrollAmount = 8;
    int wheelRotation = 2;
    MouseWheelEvent e = new MouseWheelEvent(root.getFrame(), 1, 2, modifer, 4, 5, 6, false, 7, scrollAmount, wheelRotation);

    panel.mouseWheelMoved(e);

    assertEquals(16, panel.getVerticalScrollBar().getScrollBar().getValue());        
    assertEquals(0, panel.getHorizontalScrollBar().getScrollBar().getValue());
  }

  public void testHorizontalMouseWheelMovement() throws Exception
  {
    MockRootBlockPanel root = new MockRootBlockPanel();
    parent.setParent(root);
    int modifer = 1;
    int scrollAmount = 8;
    int wheelRotation = 2;
    MouseWheelEvent e = new MouseWheelEvent(root.getFrame(), 1, 2, modifer, 4, 5, 6, false, 7, scrollAmount, wheelRotation);

    panel.mouseWheelMoved(e);

    assertEquals(0, panel.getVerticalScrollBar().getScrollBar().getValue());
    assertEquals(16, panel.getHorizontalScrollBar().getScrollBar().getValue());
  }

  public void testScrollBarsOn() throws Exception
  {
    assertTrue(panel.isVerticalScrollBarOn());
    assertTrue(panel.isHorizontalScrollBarOn());

    panel.disableVerticalScrollBar();
    assertFalse(panel.isVerticalScrollBarOn());
    assertFalse(panel.isChild(panel.getVerticalScrollBar()));
    assertTrue(panel.isHorizontalScrollBarOn());

    panel.enableVerticalScrollBar();
    assertTrue(panel.isVerticalScrollBarOn());
    assertTrue(panel.isChild(panel.getVerticalScrollBar()));
    assertTrue(panel.isHorizontalScrollBarOn());

    panel.disableHorizontalScrollBar();
    assertTrue(panel.isVerticalScrollBarOn());
    assertFalse(panel.isHorizontalScrollBarOn());
    assertFalse(panel.isChild(panel.getHorizontalScrollBar()));

    panel.enableHorizontalScrollBar();
    assertTrue(panel.isVerticalScrollBarOn());
    assertTrue(panel.isHorizontalScrollBarOn());
    assertTrue(panel.isChild(panel.getHorizontalScrollBar()));
  }
}
