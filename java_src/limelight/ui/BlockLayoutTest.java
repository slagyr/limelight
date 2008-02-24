package limelight.ui;

import junit.framework.TestCase;

import java.awt.*;

public class BlockLayoutTest extends TestCase
{
  private MockPanel parent;
  private BlockLayout layout;

  public void setUp() throws Exception
  {
    parent = new MockPanel();
    parent.getStyle().setWidth("100");
    parent.getStyle().setHeight("100");
    layout = new BlockLayout(parent);
  }

//  public void testLayoutWithOneFullSizedChild() throws Exception
//  {
//    MockPanel child = createAndAddChildWithSize(100, 100);
//
//    layout.doLayout();
//
//    assertTrue(child.wasLaidOut);
//    assertEquals(new Point(0, 0), child.getLocation());
//  }
//
//  public void testLayoutWithTwoChildrenSameRow() throws Exception
//  {
//    MockPanel child1 = createAndAddChildWithSize(50, 50);
//    MockPanel child2 = createAndAddChildWithSize(50, 50);
//
//    layout.doLayout();
//
//    assertTrue(child1.wasLaidOut);
//    assertTrue(child2.wasLaidOut);
//    assertEquals(new Point(0, 0), child1.getLocation());
//    assertEquals(new Point(50, 0), child2.getLocation());
//  }
//
//  public void testLayoutWrappingToNewRow() throws Exception
//  {
//    MockPanel child1 = createAndAddChildWithSize(50, 50);
//    MockPanel child2 = createAndAddChildWithSize(50, 50);
//    MockPanel child3 = createAndAddChildWithSize(50, 50);
//
//    layout.doLayout();
//
//    assertEquals(new Point(0, 0), child1.getLocation());
//    assertEquals(new Point(50, 0), child2.getLocation());
//    assertEquals(new Point(0, 50), child3.getLocation());
//  }
//
//  public void testLayoutWithOneChildAlignedBottomRight() throws Exception
//  {
//    parent.getBlock().getStyle().setVerticalAlignment("bottom");
//    parent.getBlock().getStyle().setHorizontalAlignment("right");
//    MockPanel child = createAndAddChildWithSize(50, 50);
//
//    layout.doLayout();
//
//    assertTrue(child.wasLaidOut);
//    assertEquals(new Point(50, 50), child.getLocation());
//  }
//
//  public void testLayoutWithTwoChildrenAlignedCenterCenter() throws Exception
//  {
//    parent.getBlock().getStyle().setVerticalAlignment("center");
//    parent.getBlock().getStyle().setHorizontalAlignment("center");
//    MockPanel child1 = createAndAddChildWithSize(25, 50);
//    MockPanel child2 = createAndAddChildWithSize(25, 50);
//
//    layout.doLayout();
//
//    assertEquals(new Point(25, 25), child1.getLocation());
//    assertEquals(new Point(50, 25), child2.getLocation());
//  }
//
//  public void testChildrenAreLaidout() throws Exception
//  {
//    MockBlockPanel child = new MockBlockPanel();
//    child.getBlock().getStyle().setWidth("50");
//    child.getBlock().getStyle().setHeight("50");
//    child.getBlock().getStyle().setHorizontalAlignment("center");
//    child.getBlock().getStyle().setVerticalAlignment("center");
//    parent.add(child);
//
//    MockPanel grandChild = new MockPanel();
//    grandChild.prepForSnap(10, 10);
//    child.add(grandChild);
//
//    layout.doLayout();
//
//    assertEquals(new Point(0, 0), child.getLocation());
//    assertEquals(new Point(20, 20), grandChild.getLocation());
//  }
//
//  private MockPanel createAndAddChildWithSize(int width, int height) throws SterilePanelException
//  {
//    MockPanel child = new MockPanel();
//    child.prepForSnap(width, height);
//    parent.add(child);
//    return child;
//  }
//
////  public void setUp() throws Exception
////  {
////    MockRootBlockPanel root = new MockRootBlockPanel();
////    panel = new MockBlockPanel();
////    root.add(panel);
////    block = (MockBlock)panel.getBlock();
////    style = block.getStyle();
////    layout = panel.getLayout();
////
////    style.setHeight("100");
////    style.setWidth("100");
////
////    child = new MockPanel();
////    panel.add(child);
////  }
////
////  public void testSwitchingToScrollModeWhenChildrenAreTooBig() throws Exception
////  {
////    child.prepForSnap(500, 500);
////
////    layout.doLayout();
////
////    assertFalse(panel.isChild(child));
////    assertEquals(1, panel.getChildren().size());
////    ScrollPanel scrollPanel = (ScrollPanel)panel.getChildren().get(0);
////    assertSame(child, scrollPanel.getView().getChildren().get(0));
////  }
////
////  public void testSwitchingToScrollModeWhenChildrenAreTooWide() throws Exception
////  {
////    child.prepForSnap(500, 10);
////
////    layout.doLayout();
////
////    assertFalse(panel.isChild(child));
////    assertEquals(1, panel.getChildren().size());
////    ScrollPanel scrollPanel = (ScrollPanel)panel.getChildren().get(0);
////    assertSame(child, scrollPanel.getView().getChildren().get(0));
////  }
////
////  public void testSwitchingToScrollModeWhenChildrenAreTooTall() throws Exception
////  {
////    child.prepForSnap(10, 500);
////
////    layout.doLayout();
////
////    assertFalse(panel.isChild(child));
////    assertEquals(1, panel.getChildren().size());
////    ScrollPanel scrollPanel = (ScrollPanel)panel.getChildren().get(0);
////    assertSame(child, scrollPanel.getView().getChildren().get(0));
////  }
}

