package limelight.ui;

import junit.framework.TestCase;
import limelight.LimelightError;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class PanelTest extends TestCase
{
  private MockPanel panel;
  private MockRootBlockPanel parent;
  private MockRootBlockPanel child;
  private Panel sibling;
  private MockRootBlockPanel grandChild;
  private MockBlock block;
  private MouseEvent mouseEvent;
  private MouseWheelEvent mouseWheelEvent;

  public void setUp() throws Exception
  {
    panel = new MockPanel();
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

  public void testContainsRealativePoint() throws Exception
  {
    panel.setLocation(100, 200);
    panel.setSize(300, 400);

    assertFalse(panel.containsRelativePoint(new Point(0, 0)));
    assertFalse(panel.containsRelativePoint(new Point(1000, 1000)));
    assertFalse(panel.containsRelativePoint(new Point(99, 400)));
    assertFalse(panel.containsRelativePoint(new Point(400, 400)));
    assertFalse(panel.containsRelativePoint(new Point(200, 199)));
    assertFalse(panel.containsRelativePoint(new Point(200, 600)));

    assertTrue(panel.containsRelativePoint(new Point(200, 400)));
    assertTrue(panel.containsRelativePoint(new Point(100, 400)));
    assertTrue(panel.containsRelativePoint(new Point(399, 400)));
    assertTrue(panel.containsRelativePoint(new Point(200, 200)));
    assertTrue(panel.containsRelativePoint(new Point(200, 599)));
  }

  public void testIsAncestor() throws Exception
  {
    createFamilyTree();

    assertTrue(child.isAncestor(parent));
    assertTrue(sibling.isAncestor(parent));
    assertTrue(grandChild.isAncestor(parent));
    assertTrue(grandChild.isAncestor(child));

    assertFalse(child.isAncestor(sibling));
    assertFalse(child.isAncestor(grandChild));
  }

  private void createFamilyTree() throws SterilePanelException
  {
    parent = new MockRootBlockPanel();
    child = new MockRootBlockPanel();
    parent.add(child);
    grandChild = new MockRootBlockPanel();
    child.add(grandChild);
    sibling = new MockRootBlockPanel();
    parent.add(sibling);
  }

  public void testGetCommonAncestor() throws Exception
  {
    createFamilyTree();

    assertSame(parent, sibling.getClosestCommonAncestor(child));
    assertSame(parent, child.getClosestCommonAncestor(sibling));
    assertSame(parent, child.getClosestCommonAncestor(grandChild));
    assertSame(parent, grandChild.getClosestCommonAncestor(child));
    assertSame(parent, sibling.getClosestCommonAncestor(grandChild));
    assertSame(parent, grandChild.getClosestCommonAncestor(sibling));
    assertSame(child, grandChild.getClosestCommonAncestor(grandChild));
  }

  public void testGetClosestCommonAncestorExceptionCase() throws Exception
  {
    createFamilyTree();

    try
    {
      parent.getClosestCommonAncestor(child);
      fail("An exception is expected");
    }
    catch(LimelightError e)
    {  
    }
  }

  public void testGetAbsoluteLocation() throws Exception
  {
    createFamilyTree();

    parent.setLocation(1, 10);
    child.setLocation(2, 20);
    grandChild.setLocation(5, 50);

    assertEquals(new Point(1, 10), parent.getAbsoluteLocation());
    assertEquals(new Point(3, 30), child.getAbsoluteLocation());
    assertEquals(new Point(8, 80), grandChild.getAbsoluteLocation());
  }

  public void testContainsAbsolutePoint() throws Exception
  {
    createFamilyTree();

    parent.setLocation(1, 10);
    child.setLocation(2, 20);
    grandChild.setLocation(5, 50);
    grandChild.setSize(10, 10);

    assertFalse(grandChild.containsAbsolutePoint(new Point(0, 0)));
    assertFalse(grandChild.containsAbsolutePoint(new Point(100, 100)));
    assertFalse(grandChild.containsAbsolutePoint(new Point(7, 85)));
    assertFalse(grandChild.containsAbsolutePoint(new Point(18, 85)));
    assertFalse(grandChild.containsAbsolutePoint(new Point(15, 79)));
    assertFalse(grandChild.containsAbsolutePoint(new Point(15, 90)));

    assertTrue(grandChild.containsAbsolutePoint(new Point(8, 80)));
    assertTrue(grandChild.containsAbsolutePoint(new Point(17, 89)));
    assertTrue(grandChild.containsAbsolutePoint(new Point(15, 85)));
  }

  public void testMousePressed() throws Exception
  {
    setUpForEvent();
    panel.mousePressed(mouseEvent);
    assertSame(mouseEvent, block.pressedMouse);
  }

  private MouseEvent setUpForEvent() throws SterilePanelException
  {
    parent = new MockRootBlockPanel();
    parent.add(panel);
    block = (MockBlock)parent.getBlock();
    mouseEvent = new MouseEvent(new JPanel(), 1, 2, 3, 4, 5, 6, false);
    mouseWheelEvent = new MouseWheelEvent(new JPanel(), 1, 2, 3, 4, 5, 6, false, 7, 8, 9);
    return mouseEvent;
  }

  public void testMouseReleased() throws Exception
  {
    setUpForEvent();
    panel.mouseReleased(mouseEvent);
    assertSame(mouseEvent, block.releasedMouse);
  }

  public void testMouseClicked() throws Exception
  {
    setUpForEvent();
    panel.mouseClicked(mouseEvent);
    assertSame(mouseEvent, block.clickedMouse);
  }

  public void testMouseDragged() throws Exception
  {
    setUpForEvent();
    panel.mouseDragged(mouseEvent);
    assertSame(mouseEvent, block.draggedMouse);
  }

  public void testMouseEntered() throws Exception
  {
    setUpForEvent();
    panel.mouseEntered(mouseEvent);
    assertSame(mouseEvent, block.enteredMouse);
  }

  public void testMouseExited() throws Exception
  {
    setUpForEvent();
    panel.mouseExited(mouseEvent);
    assertSame(mouseEvent, block.exitedMouse);
  }

  public void testMouseMoved() throws Exception
  {
    setUpForEvent();
    panel.mouseMoved(mouseEvent);
    assertSame(mouseEvent, block.movedMouse);
  }
}

