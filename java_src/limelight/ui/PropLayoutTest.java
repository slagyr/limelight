package limelight.ui;

import junit.framework.TestCase;

public class PropLayoutTest extends TestCase
{
  private MockPanel parent;
  private PropLayout layout;

  public void setUp() throws Exception
  {
    parent = new MockPanel();
    parent.getStyle().setWidth("100");
    parent.getStyle().setHeight("100");
    layout = new PropLayout(parent);
  }

  public void testToKeepAntHappy() throws Exception
  {
    
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
//    parent.getProp().getStyle().setVerticalAlignment("bottom");
//    parent.getProp().getStyle().setHorizontalAlignment("right");
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
//    parent.getProp().getStyle().setVerticalAlignment("center");
//    parent.getProp().getStyle().setHorizontalAlignment("center");
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
//    MockPropPanel child = new MockPropPanel();
//    child.getProp().getStyle().setWidth("50");
//    child.getProp().getStyle().setHeight("50");
//    child.getProp().getStyle().setHorizontalAlignment("center");
//    child.getProp().getStyle().setVerticalAlignment("center");
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
////    MockRootPropPanel root = new MockRootPropPanel();
////    panel = new MockPropPanel();
////    root.add(panel);
////    prop = (MockProp)panel.getProp();
////    style = prop.getStyle();
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

