package limelight.ui;

import junit.framework.TestCase;

import java.util.LinkedList;

public class ScrollViewLayoutTest extends TestCase
{
  private MockBlockPanel parent;
  private ScrollPanel scrollPanel;
  private ScrollBarPanel verticalScrollBar;
  private ScrollBarPanel horizontalScrollBar;
  private ScrollViewPanel view;
  private MockPanel child;

  public void setUp() throws Exception
  {
    parent = new MockBlockPanel();
    parent.setParent(new MockRootBlockPanel());
    parent.childConsumableRectangle = new Rectangle(0, 0, 500, 500);
    scrollPanel = new ScrollPanel(new LinkedList<Panel>());
    horizontalScrollBar = scrollPanel.getHorizontalScrollBar();
    verticalScrollBar = scrollPanel.getVerticalScrollBar();
    view = scrollPanel.getView();
    child = new MockPanel();
    view.add(child);
    parent.add(scrollPanel);
  }

  public void testNormalLayout() throws Exception
  {
    scrollPanel.setSize(500, 500);
    child.prepForSnap(1000, 1000);

    view.doLayout();

    assertEquals(485, view.getWidth());
    assertEquals(485, view.getHeight());
    assertTrue(scrollPanel.isHorizontalScrollBarOn());
    assertTrue(scrollPanel.isVerticalScrollBarOn());
  }

  public void testVerticalScrollBarNotNeeded() throws Exception
  {
    scrollPanel.setSize(500, 500);
    child.prepForSnap(1000, 500);

    view.doLayout();

    assertEquals(500, view.getWidth());
    assertEquals(485, view.getHeight());
    assertTrue(scrollPanel.isHorizontalScrollBarOn());
    assertFalse(scrollPanel.isVerticalScrollBarOn());
  }

  public void testHorizontalScrollBarNotNeeded() throws Exception
  {
    scrollPanel.setSize(500, 500);
    child.prepForSnap(485, 1000);

    view.doLayout();

    assertEquals(485, view.getWidth());
    assertEquals(500, view.getHeight());
    assertFalse(scrollPanel.isHorizontalScrollBarOn());
    assertTrue(scrollPanel.isVerticalScrollBarOn());
  }

  public void testNoScrollingIsRequired() throws Exception
  {
    scrollPanel.setSize(500, 500);
    child.prepForSnap(500, 500);

    view.doLayout();

    assertEquals(1, parent.getChildren().size());
    assertFalse(parent.isChild(scrollPanel));
    assertSame(child, parent.getChildren().get(0));
    assertSame(parent, child.getParent());
  }
}
