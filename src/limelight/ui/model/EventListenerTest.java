//- Copyright � 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import junit.framework.TestCase;
import limelight.ui.api.MockProp;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class EventListenerTest extends TestCase
{
  private EventListener listener;
  private PropPanel root;
  private MockProp childProp;
  private PropPanel childPanel;
  private MockProp rootProp;
  private JPanel jpanel;

  public void setUp() throws Exception
  {
    jpanel = new JPanel();

    rootProp = new MockProp("rootProp");
    root = new PropPanel(rootProp);
    listener = new EventListener(root);

    childProp = new MockProp("childProp");
    childPanel = new PropPanel(childProp);

    root.add(childPanel);

    ScenePanel scenePanel = new ScenePanel(new MockProp());
    scenePanel.add(root);
    scenePanel.setFrame(new MockPropFrame());

    root.setSize(1000, 1000);
    childPanel.setLocation(250, 250);
    childPanel.setSize(500, 500);
  }

  public void testMousePressed() throws Exception
  {
    MouseEvent e1 = mouseEvent(0, 0);
    listener.mousePressed(e1);
    assertSame(root, listener.pressedPanel);
    checkEvent(e1, rootProp.pressedMouse, 0, 0);

    MouseEvent e2 = mouseEvent(500, 500);
    listener.mousePressed(e2);
    assertSame(childPanel, listener.pressedPanel);
    checkEvent(e2, childProp.pressedMouse, 250, 250);
  }

  public void testMousePressedWhenChildDoesntAcceptIt() throws Exception
  {
    MouseEvent e2 = mouseEvent(500, 500);
    childProp.accept_mouse_pressed = false;
    listener.mousePressed(e2);
    assertSame(childPanel, listener.pressedPanel);
    assertEquals(null, childProp.pressedMouse);
    checkEvent(e2, rootProp.pressedMouse, 500, 500);
  }

  private void checkEvent(MouseEvent expectedEvent, Object actualEvent, int x, int y)
  {
    MouseEvent actual = (MouseEvent)actualEvent;
//    assertEquals(expectedEvent, actualEvent);
    assertEquals(x, actual.getX());
    assertEquals(y, actual.getX());
  }

  public void testMouseReleased() throws Exception
  {
    MouseEvent e1 = mouseEvent(0, 0);
    listener.mouseReleased(e1);
    checkEvent(e1, rootProp.releasedMouse, 0, 0);

    MouseEvent e2 = mouseEvent(500, 500);
    listener.mouseReleased(e2);
    checkEvent(e2, childProp.releasedMouse, 250, 250);
  }

  public void testMouseReleasedWhenChildDoesntAcceptIt() throws Exception
  {
    MouseEvent e2 = mouseEvent(500, 500);
    childProp.accept_mouse_released = false;
    listener.mouseReleased(e2);
    assertEquals(null, childProp.releasedMouse);
    checkEvent(e2, rootProp.releasedMouse, 500, 500);
  }

  public void testMouseClick() throws Exception
  {
    MouseEvent e1 = mouseEvent(0, 0);
    listener.mousePressed(e1);
    listener.mouseReleased(e1);
    checkEvent(e1, rootProp.clickedMouse, 0, 0);

    MouseEvent e2 = mouseEvent(500, 500);
    listener.mousePressed(e2);
    listener.mouseReleased(e2);
    checkEvent(e2, childProp.clickedMouse, 250, 250);
  }

  public void testMouseClickWhenChildDoesntAcceptIt() throws Exception
  {
    MouseEvent e2 = mouseEvent(500, 500);
    childProp.accept_mouse_clicked = false;
    listener.mousePressed(e2);
    listener.mouseReleased(e2);
    assertEquals(null, childProp.clickedMouse);
    checkEvent(e2, rootProp.clickedMouse, 500, 500);
  }

  public void testMouseClickButPanelChanged() throws Exception
  {
    MouseEvent e1 = mouseEvent(0, 0);
    MouseEvent e2 = mouseEvent(500, 500);

    listener.mousePressed(e1);
    listener.mouseReleased(e2);

    assertNull(rootProp.clickedMouse);
    assertNull(childProp.clickedMouse);
  }

  public void testMouseMoved() throws Exception
  {
    MouseEvent e1 = mouseEvent(0, 0);
    listener.mouseMoved(e1);
    checkEvent(e1, rootProp.movedMouse, 0, 0);

    MouseEvent e2 = mouseEvent(500, 500);
    listener.mouseMoved(e2);
    checkEvent(e2, childProp.movedMouse, 250, 250);
  }

  public void testMouseDragged() throws Exception
  {
    MouseEvent e1 = mouseEvent(0, 0);
    listener.mouseDragged(e1);
    checkEvent(e1, rootProp.draggedMouse, 0, 0);

    MouseEvent e2 = mouseEvent(500, 500);
    listener.mouseDragged(e2);
    checkEvent(e2, childProp.draggedMouse, 250, 250);
  }

  public void testMouseEnteredAndExited() throws Exception
  {
    MockProp child2Block = new MockProp();
    PropPanel child2Panel = new PropPanel(child2Block);
    root.add(child2Panel);
    child2Panel.setLocation(1, 1);
    child2Panel.setSize(100, 100);

    MouseEvent e1 = mouseEvent(0, 0);
    MouseEvent e2 = mouseEvent(50, 50);
    MouseEvent e3 = mouseEvent(500, 500);
    assertNull(listener.hooveredPanel);

    listener.mouseMoved(e1);
    assertSame(root, listener.hooveredPanel);
    checkEvent(e1, rootProp.enteredMouse, 0, 0);

    listener.mouseMoved(e2);
    assertSame(child2Panel, listener.hooveredPanel);
    assertNull(rootProp.exitedMouse);
    checkEvent(e2, child2Block.enteredMouse, 49, 49);

    listener.mouseMoved(e3);
    assertSame(childPanel, listener.hooveredPanel);
    checkEvent(e3, child2Block.exitedMouse, 499, 499);
    checkEvent(e3, childProp.enteredMouse, 250, 250);
  }

  public void testMouseEnteredAndExitedWhileDragging() throws Exception
  {
    MockProp child2Block = new MockProp();
    PropPanel child2Panel = new PropPanel(child2Block);
    root.add(child2Panel);
    child2Panel.setLocation(1, 1);
    child2Panel.setSize(100, 100);

    MouseEvent e1 = mouseEvent(0, 0);
    MouseEvent e2 = mouseEvent(50, 50);
    MouseEvent e3 = mouseEvent(500, 500);
    assertNull(listener.hooveredPanel);

    listener.mouseDragged(e1);
    assertSame(root, listener.hooveredPanel);
    checkEvent(e1, rootProp.enteredMouse, 0, 0);

    listener.mouseDragged(e2);
    assertSame(child2Panel, listener.hooveredPanel);
    assertNull(rootProp.exitedMouse);
    checkEvent(e2, child2Block.enteredMouse, 49, 49);

    listener.mouseDragged(e3);
    assertSame(childPanel, listener.hooveredPanel);
    checkEvent(e3, child2Block.exitedMouse, 499, 499);
    checkEvent(e3, childProp.enteredMouse, 250, 250);
  }

  private MouseEvent mouseEvent(int x, int y)
  {
    return new MouseEvent(jpanel, 1, 2, 3, x, y, 1, false);
  }

  private MouseWheelEvent mouseWheelEvent(int x, int y)
  {
    return new MouseWheelEvent(jpanel, 1, 2, 3, x, y, 1, false, 1, 2, 3);
  }
}
