package limelight.ui;

import junit.framework.TestCase;

import java.awt.*;
import java.util.LinkedList;

public class ScrollViewPanelTest extends TestCase
{
  private MockBlockPanel parent;
  private ScrollPanel scrollPanel;
  private ScrollBarPanel verticalScrollBar;
  private ScrollBarPanel horizontalScrollBar;
  private ScrollViewPanel view;

  public void setUp() throws Exception
  {
    parent = new MockBlockPanel();
    parent.setParent(new MockRootBlockPanel());
    parent.childConsumableRectangle = new Rectangle(0, 0, 500, 500);
    scrollPanel = new ScrollPanel(new LinkedList<Panel>());
    horizontalScrollBar = scrollPanel.getHorizontalScrollBar();
    verticalScrollBar = scrollPanel.getVerticalScrollBar();
    view = scrollPanel.getView();
    parent.add(scrollPanel);
  }

  public void testConstruction() throws Exception
  {
    MockParentPanel parent = new MockParentPanel();
    MockPanel child1 = new MockPanel();
    MockPanel child2 = new MockPanel();
    parent.add(child1);
    parent.add(child2);

    view = new ScrollViewPanel(parent.getChildren());

    assertTrue(view.isChild(child1));
    assertTrue(view.isChild(child2));
  }

  public void testSnapToSize() throws Exception
  {
    scrollPanel.setSize(500, 500);

    view.snapToSize();

    assertEquals(485, view.getWidth());
    assertEquals(485, view.getHeight());
  }

  public void testUpdate() throws Exception
  {
    MockPanel child1 = new MockPanel();
    child1.setLocation(0, 0);
    MockPanel child2 = new MockPanel();
    child2.setLocation(50, 50);
    view.add(child1);
    view.add(child2);
    horizontalScrollBar.setValue(12);
    verticalScrollBar.setValue(34);

    view.update();

    assertEquals(new Point(-12, -34), child1.getLocation());
    assertEquals(new Point(38, 16), child2.getLocation());
  }

  public void testUpdateTwice() throws Exception
  {
      MockPanel child1 = new MockPanel();
    child1.setLocation(0, 0);
    MockPanel child2 = new MockPanel();
    child2.setLocation(50, 50);
    view.add(child1);
    view.add(child2);
    horizontalScrollBar.setValue(12);
    verticalScrollBar.setValue(34);

    view.update();
    horizontalScrollBar.setValue(21);
    verticalScrollBar.setValue(43);
    view.update();

    assertEquals(new Point(-21, -43), child1.getLocation());
    assertEquals(new Point(29, 7), child2.getLocation());
  }
}
