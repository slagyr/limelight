//- Copyright 2008 8th Light, Inc.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import junit.framework.TestCase;

import javax.swing.*;
import java.awt.*;

public class PropLayoutTest extends TestCase
{
  private MockPanel parent;
  private PropLayout layout;

  public void setUp() throws Exception
  {
    parent = new MockPanel();
    parent.prepForSnap(100, 100);
    layout = new PropLayout(parent);
  }

  public void testLayoutWithOneFullSizedChild() throws Exception
  {
    MockPanel child = createAndAddChildWithSize(100, 100);

    layout.layoutContainer(parent);

    assertTrue(child.wasLaidOut);
    assertEquals(new Point(0, 0), child.getLocation());
  }

  public void testLayoutWithTwoChildrenSameRow() throws Exception
  {
    MockPanel child1 = createAndAddChildWithSize(50, 50);
    MockPanel child2 = createAndAddChildWithSize(50, 50);

    layout.layoutContainer(parent);

    assertTrue(child1.wasLaidOut);
    assertTrue(child2.wasLaidOut);
    assertEquals(new Point(0, 0), child1.getLocation());
    assertEquals(new Point(50, 0), child2.getLocation());
  }

  public void testLayoutWrappingToNewRow() throws Exception
  {
    MockPanel child1 = createAndAddChildWithSize(50, 50);
    MockPanel child2 = createAndAddChildWithSize(50, 50);
    MockPanel child3 = createAndAddChildWithSize(50, 50);

    layout.layoutContainer(parent);

    assertEquals(new Point(0, 0), child1.getLocation());
    assertEquals(new Point(50, 0), child2.getLocation());
    assertEquals(new Point(0, 50), child3.getLocation());
  }

  public void testLayoutWithOneChildAlignedBottomRight() throws Exception
  {
    parent.getProp().getStyle().setVerticalAlignment("bottom");
    parent.getProp().getStyle().setHorizontalAlignment("right");
    MockPanel child = createAndAddChildWithSize(50, 50);

    layout.layoutContainer(parent);

    assertTrue(child.wasLaidOut);
    assertEquals(new Point(50, 50), child.getLocation());
  }

  public void testLayoutWithTwoChildrenAlignedCenterCenter() throws Exception
  {
    parent.getProp().getStyle().setVerticalAlignment("center");
    parent.getProp().getStyle().setHorizontalAlignment("center");
    MockPanel child1 = createAndAddChildWithSize(25, 50);
    MockPanel child2 = createAndAddChildWithSize(25, 50);

    layout.layoutContainer(parent);

    assertEquals(new Point(25, 25), child1.getLocation());
    assertEquals(new Point(50, 25), child2.getLocation());
  }

  public void testChildrenAreLaidout() throws Exception
  {
    MockPanel child = new MockPanel();
    child.prepForSnap(50, 50);
    child.getProp().getStyle().setHorizontalAlignment("center");
    child.getProp().getStyle().setVerticalAlignment("center");
    parent.add(child);

    MockPanel grandChild = new MockPanel();
    grandChild.prepForSnap(10, 10);
    child.add(grandChild);

    layout.layoutContainer(parent);

    assertEquals(new Point(0, 0), child.getLocation());
    assertEquals(new Point(20, 20), grandChild.getLocation());
  }

  private MockPanel createAndAddChildWithSize(int width, int height) throws limelight.ui.model.Panel.SterilePanelException
  {
    MockPanel child = new MockPanel();
    child.prepForSnap(width, height);
    parent.add(child);
    return child;
  }

  public void testSwitchingToScrollModeWhenChildrenAreTooBig() throws Exception
  {
    MockPanel child = new MockPanel();
    child.prepForSnap(500, 500);
    parent.add(child);

    layout.layoutContainer(parent);

    assertFalse(parent.hasChild(child));
    assertEquals(1, parent.getComponents().length);
    JScrollPane scrollPanel = (JScrollPane)parent.getComponents()[0];
    Component view = scrollPanel.getViewport().getComponents()[0];
    assertSame(child, ((Container)view).getComponents()[0]);
  }

  public void testSwitchingToScrollModeWhenChildrenAreTooWide() throws Exception
  {
    MockPanel child = new MockPanel();
    child.prepForSnap(500, 10);
    parent.add(child);

    layout.layoutContainer(parent);

    assertFalse(parent.hasChild(child));
    assertEquals(1, parent.getComponents().length);
    JScrollPane scrollPanel = (JScrollPane)parent.getComponents()[0];
    Component view = scrollPanel.getViewport().getComponents()[0];
    assertSame(child, ((Container)view).getComponents()[0]);
  }

  public void testSwitchingToScrollModeWhenChildrenAreTooTall() throws Exception
  {
    MockPanel child = new MockPanel();
    child.prepForSnap(10, 500);
    parent.add(child);

    layout.layoutContainer(parent);

    assertFalse(parent.hasChild(child));
    assertEquals(1, parent.getComponents().length);
    JScrollPane scrollPanel = (JScrollPane)parent.getComponents()[0];
    Component view = scrollPanel.getViewport().getComponents()[0];
    assertSame(child, ((Container)view).getComponents()[0]);
  }

  public void testNotSwitchingToScrollModeWhenChildrenAreTooWideButHorizontalScrollbarIsOff() throws Exception
  {
    MockPanel child = new MockPanel();
    child.prepForSnap(500, 10);
    parent.add(child);
    parent.getStyle().setHorizontalScrollbar("off");

    layout.layoutContainer(parent);

    assertEquals(true, parent.hasChild(child));
    assertSame(child, parent.getComponents()[0]);
  }

  public void testNotSwitchingToScrollModeWhenChildrenAreTooTallButVerticalScrollbarIsOff() throws Exception
  {
    MockPanel child = new MockPanel();
    child.prepForSnap(10, 500);
    parent.add(child);
    parent.getStyle().setVerticalScrollbar("off");

    layout.layoutContainer(parent);

    assertEquals(true, parent.hasChild(child));
    assertSame(child, parent.getComponents()[0]);
  }
  
  public void testAutoSize() throws Exception
  {
    MockPanel panel = new MockPanel();
    panel.getStyle().setWidth("auto");
    panel.getStyle().setHeight("auto");
    parent.add(panel);
    MockPanel child = new MockPanel();
    child.prepForSnap(50, 50);
    panel.add(child);

    layout.layoutContainer(parent);

    assertEquals(50, panel.getWidth());
    assertEquals(50, panel.getHeight());
  }

  public void testAutoWidthOnly() throws Exception
  {
    MockPanel panel = new MockPanel();
    panel.getStyle().setWidth("auto");
    panel.getStyle().setHeight("100");
    parent.add(panel);
    MockPanel child = new MockPanel();
    child.prepForSnap(50, 50);
    panel.add(child);

    layout.layoutContainer(parent);

    assertEquals(50, panel.getWidth());
    assertEquals(100, panel.getHeight());
  }

  public void testAutoHeightOnly() throws Exception
  {
    MockPanel panel = new MockPanel();
    panel.getStyle().setWidth("70%");
    panel.getStyle().setHeight("auto");
    parent.add(panel);
    MockPanel child = new MockPanel();
    child.prepForSnap(50, 50);
    panel.add(child);

    layout.layoutContainer(parent);

    assertEquals(70, panel.getWidth());
    assertEquals(50, panel.getHeight());
  }

  public void testAutoSizeIncludesMarginPaddingAndBorder() throws Exception
  {
    MockPanel panel = new MockPanel();
    panel.getStyle().setWidth("auto");
    panel.getStyle().setHeight("auto");
    panel.getStyle().setTopMargin("1");
    panel.getStyle().setTopBorderWidth("2");
    panel.getStyle().setTopPadding("3");
    panel.getStyle().setRightMargin("4");
    panel.getStyle().setRightBorderWidth("5");
    panel.getStyle().setRightPadding("6");
    panel.getStyle().setBottomMargin("7");
    panel.getStyle().setBottomBorderWidth("8");
    panel.getStyle().setBottomPadding("9");
    panel.getStyle().setLeftMargin("10");
    panel.getStyle().setLeftBorderWidth("11");
    panel.getStyle().setLeftPadding("12");
    parent.add(panel);
    MockPanel child = new MockPanel();
    child.prepForSnap(50, 50);
    panel.add(child);

    layout.layoutContainer(parent);

    assertEquals(98, panel.getWidth());
    assertEquals(80, panel.getHeight());
  }

  public void testAutoSizeWithChildrenCentered() throws Exception
  {
    MockPanel panel = new MockPanel();
    panel.getStyle().setWidth("auto");
    panel.getStyle().setHeight("auto");
    panel.getStyle().setHorizontalAlignment("center");
    panel.getStyle().setVerticalAlignment("center");
    panel.getStyle().setMargin("10");
    parent.add(panel);
    MockPanel child = new MockPanel();
    child.prepForSnap(50, 50);
    panel.add(child);

    layout.layoutContainer(parent);

    assertEquals(10, child.getX());
    assertEquals(10, child.getY());
    assertEquals(70, panel.getWidth());
    assertEquals(70, panel.getHeight());
  }
  
  public void testAutoSizingWithNoChildren() throws Exception
  {
    MockPanel panel = new MockPanel();
    panel.getStyle().setWidth("auto");
    panel.getStyle().setHeight("auto");
    parent.add(panel);

    layout.layoutContainer(parent);

    assertEquals(0, panel.getWidth());
    assertEquals(0, panel.getHeight());
  }

  public void testFloatersAreNoConsideredInDeterminingScrollMode() throws Exception
  {
    MockPanel child = new MockPanel();
    child.prepForSnap(50, 50);
    MockPanel floater = new MockPanel();
    floater.getStyle().setFloat("on");
    floater.prepForSnap(500, 500);
    parent.add(child);
    parent.add(floater);

    layout.layoutContainer(parent);

    assertEquals(2, parent.getComponents().length);
    assertSame(child, parent.getComponents()[0]);
    assertSame(floater, parent.getComponents()[1]);
  }
  
  public void testFloatersDoNotInfluenceAutoSize() throws Exception
  {
    MockPanel panel = new MockPanel();
    panel.getStyle().setWidth("auto");
    panel.getStyle().setHeight("auto");
    parent.add(panel);
    MockPanel child = new MockPanel();
    child.prepForSnap(50, 50);
    panel.add(child);
    MockPanel floater = new MockPanel();
    floater.getStyle().setFloat("on");
    floater.prepForSnap(500, 500);
    panel.add(floater);

    layout.layoutContainer(parent);

    assertEquals(50, panel.getWidth());
    assertEquals(50, panel.getHeight());
  }
  
  public void testFloatersAreLaidOut() throws Exception
  {
    MockPanel floater = createAndAddChildWithSize(50, 50);
    floater.getStyle().setFloat("on");
    floater.getStyle().setX("2");
    floater.getStyle().setY("3");
    MockPanel child = createAndAddChildWithSize(50, 50);

    layout.layoutContainer(parent);

    assertTrue(floater.wasLaidOut);
    assertTrue(child.wasLaidOut);
    assertEquals(new Point(2, 3), floater.getLocation());
    assertEquals(new Point(0, 0), child.getLocation());
  }
}

