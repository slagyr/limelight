package limelight.ui;

import junit.framework.TestCase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.util.LinkedList;

public class ScrollBarPanelTest extends TestCase
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
  
  public void testOrientation() throws Exception
  {
    assertEquals(JScrollBar.HORIZONTAL, horizontalScrollBar.getScrollBar().getOrientation());
    assertEquals(JScrollBar.VERTICAL, verticalScrollBar.getScrollBar().getOrientation());
  }

  public void testSetSize() throws Exception
  {
    horizontalScrollBar.setSize(100, 200);
    assertEquals(100, horizontalScrollBar.getScrollBar().getWidth());
    assertEquals(200, horizontalScrollBar.getScrollBar().getHeight());
  }

  public void testReset() throws Exception
  {
    horizontalScrollBar.reset(500, 100);

    assertEquals(500, horizontalScrollBar.getScrollBar().getMaximum());
    assertEquals(100, horizontalScrollBar.getScrollBar().getVisibleAmount());
    assertEquals(90, horizontalScrollBar.getScrollBar().getBlockIncrement());
  }

  public void testHorizontalScrollBarMovement() throws Exception
  {
    assertEquals(1, horizontalScrollBar.getScrollBar().getAdjustmentListeners().length);

    AdjustmentEvent e = new AdjustmentEvent(horizontalScrollBar.getScrollBar(), 1, 2, 123);
    horizontalScrollBar.getScrollBar().getAdjustmentListeners()[0].adjustmentValueChanged(e);

    assertEquals(new Point(123, 0), view.getStartingPoint());
  }

  public void testVerticalScrollBarMovement() throws Exception
  {
    assertEquals(1, verticalScrollBar.getScrollBar().getAdjustmentListeners().length);

    AdjustmentEvent e = new AdjustmentEvent(verticalScrollBar.getScrollBar(), 1, 2, 123);
    verticalScrollBar.getScrollBar().getAdjustmentListeners()[0].adjustmentValueChanged(e);

    assertEquals(new Point(0, 123), view.getStartingPoint());
  }

  public void testSnapToSizeWhenBothScrollBarsAreEnabled() throws Exception
  {
    scrollPanel.setSize(500, 500);

    horizontalScrollBar.snapToSize();
    assertEquals(485, horizontalScrollBar.getWidth());
    assertEquals(15, horizontalScrollBar.getHeight());

    verticalScrollBar.snapToSize();
    assertEquals(15, verticalScrollBar.getWidth());
    assertEquals(485, verticalScrollBar.getHeight());
  }

  public void testSnapToSizeWhenHorizontalScrollBarIsDisabled() throws Exception
  {
    scrollPanel.setSize(500, 500);
    scrollPanel.disableHorizontalScrollBar();

    horizontalScrollBar.snapToSize();
    assertEquals(0, horizontalScrollBar.getWidth());
    assertEquals(0, horizontalScrollBar.getHeight());

    verticalScrollBar.snapToSize();
    assertEquals(15, verticalScrollBar.getWidth());
    assertEquals(500, verticalScrollBar.getHeight());  
  }
  
  public void testSnapToSizeWhenVerticalScrollBarIsDisabled() throws Exception
  {
    scrollPanel.setSize(500, 500);
    scrollPanel.disableVerticalScrollBar();

    horizontalScrollBar.snapToSize();
    assertEquals(500, horizontalScrollBar.getWidth());
    assertEquals(15, horizontalScrollBar.getHeight());

    verticalScrollBar.snapToSize();
    assertEquals(0, verticalScrollBar.getWidth());
    assertEquals(0, verticalScrollBar.getHeight());
  }

}
