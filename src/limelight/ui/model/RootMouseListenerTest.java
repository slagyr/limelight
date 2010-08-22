//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.ui.api.MockProp;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;

public class RootMouseListenerTest
{
  private RootMouseListener listener;
  private PropPanel parent;
  private MockProp childProp;
  private PropPanel childPanel;
  private MockProp parentProp;
  private JPanel jpanel;
  private MockRootPanel root;

  @Before
  public void setUp() throws Exception
  {
    jpanel = new JPanel();

    root = new MockRootPanel();
    listener = new RootMouseListener(root);

    parentProp = new MockProp("rootProp");
    parent = new PropPanel(parentProp);

    childProp = new MockProp("childProp");
    childPanel = new PropPanel(childProp);

    root.add(parent);
    parent.add(childPanel);

    ScenePanel scenePanel = new ScenePanel(new MockProp());
    scenePanel.add(parent);
    scenePanel.setFrame(new MockPropFrame());

    parent.setSize(1000, 1000);
    childPanel.setLocation(250, 250);
    childPanel.setSize(500, 500);
  }

  @Test
  public void mousePressed() throws Exception
  {
    MouseEvent e1 = mouseEvent(0, 0);
    listener.mousePressed(e1);
    assertSame(parent, listener.pressedPanel);
    checkEvent(parentProp.pressedMouse, 0, 0);

    MouseEvent e2 = mouseEvent(500, 500);
    listener.mousePressed(e2);
    assertSame(childPanel, listener.pressedPanel);
    checkEvent(childProp.pressedMouse, 250, 250);
  }

  @Test
  public void mousePressedWhenChildDoesntAcceptIt() throws Exception
  {
    MouseEvent e2 = mouseEvent(500, 500);
    childProp.accept_mouse_pressed = false;
    listener.mousePressed(e2);
    assertSame(childPanel, listener.pressedPanel);
    assertEquals(null, childProp.pressedMouse);
    checkEvent(parentProp.pressedMouse, 500, 500);
  }

  private void checkEvent(Object actualEvent, int x, int y)
  {
    MouseEvent actual = (MouseEvent)actualEvent;
    assertEquals(x, actual.getX());
    assertEquals(y, actual.getX());
  }

  @Test
  public void mouseReleased() throws Exception
  {
    MouseEvent e1 = mouseEvent(0, 0);
    listener.mouseReleased(e1);
    checkEvent(parentProp.releasedMouse, 0, 0);

    MouseEvent e2 = mouseEvent(500, 500);
    listener.mouseReleased(e2);
    checkEvent(childProp.releasedMouse, 250, 250);
  }

  @Test
  public void mouseReleasedWhenChildDoesntAcceptIt() throws Exception
  {
    MouseEvent e2 = mouseEvent(500, 500);
    childProp.accept_mouse_released = false;
    listener.mouseReleased(e2);
    assertEquals(null, childProp.releasedMouse);
    checkEvent(parentProp.releasedMouse, 500, 500);
  }

  @Test
  public void mouseClick() throws Exception
  {
    MouseEvent e1 = mouseEvent(0, 0);
    listener.mousePressed(e1);
    listener.mouseReleased(e1);
    checkEvent(parentProp.clickedMouse, 0, 0);

    MouseEvent e2 = mouseEvent(500, 500);
    listener.mousePressed(e2);
    listener.mouseReleased(e2);
    checkEvent(childProp.clickedMouse, 250, 250);
  }

  @Test
  public void mouseClickWhenChildDoesntAcceptIt() throws Exception
  {
    MouseEvent e2 = mouseEvent(500, 500);
    childProp.accept_mouse_clicked = false;
    listener.mousePressed(e2);
    listener.mouseReleased(e2);
    assertEquals(null, childProp.clickedMouse);
    checkEvent(parentProp.clickedMouse, 500, 500);
  }

  @Test
  public void mouseClickButPanelChanged() throws Exception
  {
    MouseEvent e1 = mouseEvent(0, 0);
    MouseEvent e2 = mouseEvent(500, 500);

    listener.mousePressed(e1);
    listener.mouseReleased(e2);

    assertNull(parentProp.clickedMouse);
    assertNull(childProp.clickedMouse);
  }

  @Test
  public void mouseMoved() throws Exception
  {
    MouseEvent e1 = mouseEvent(0, 0);
    listener.mouseMoved(e1);
    checkEvent(parentProp.movedMouse, 0, 0);

    MouseEvent e2 = mouseEvent(500, 500);
    listener.mouseMoved(e2);
    checkEvent(childProp.movedMouse, 250, 250);
  }

  @Test
  public void mouseDraggedIsOnlyEffectiveAfterPress() throws Exception
  {
    MouseEvent e1 = mouseEvent(0, 0);
    listener.mouseDragged(e1);
    assertEquals(null, parentProp.draggedMouse);

    listener.mousePressed(e1);
    listener.mouseDragged(e1);
    checkEvent(parentProp.draggedMouse, 0, 0);
  }
  
  @Test
  public void mouseDraggedIsOnlyEffectiveOnPressedPanel() throws Exception
  {
    MouseEvent e1 = mouseEvent(0, 0);
    listener.mousePressed(e1);

    MouseEvent e2 = mouseEvent(500, 500);
    listener.mouseDragged(e2);
    assertEquals(null, childProp.draggedMouse);
    checkEvent(parentProp.draggedMouse, 500, 500);

    listener.mousePressed(e2);
    listener.mouseDragged(e2);
    checkEvent(childProp.draggedMouse, 250, 250);
  }

  @Test
  public void mouseEnteredAndExited() throws Exception
  {
    MockProp child2Block = new MockProp();
    PropPanel child2Panel = new PropPanel(child2Block);
    parent.add(child2Panel);
    child2Panel.setLocation(1, 1);
    child2Panel.setSize(100, 100);

    MouseEvent e1 = mouseEvent(0, 0);
    MouseEvent e2 = mouseEvent(50, 50);
    MouseEvent e3 = mouseEvent(500, 500);
    assertNull(listener.hooveredPanel);

    listener.mouseMoved(e1);
    assertSame(parent, listener.hooveredPanel);
    checkEvent(parentProp.enteredMouse, 0, 0);

    listener.mouseMoved(e2);
    assertSame(child2Panel, listener.hooveredPanel);
    assertNull(parentProp.exitedMouse);
    checkEvent(child2Block.enteredMouse, 49, 49);

    listener.mouseMoved(e3);
    assertSame(childPanel, listener.hooveredPanel);
    checkEvent(child2Block.exitedMouse, 499, 499);
    checkEvent(childProp.enteredMouse, 250, 250);
  }
  
  @Test
  public void draggingInvokesEnteredAndExited() throws Exception
  {
    MockProp child2 = new MockProp();
    PropPanel child2Panel = new PropPanel(child2);
    parent.add(child2Panel);
    child2Panel.setLocation(1, 1);
    child2Panel.setSize(100, 100);

    MouseEvent e1 = mouseEvent(0, 0);
    MouseEvent e2 = mouseEvent(50, 50);
    MouseEvent e3 = mouseEvent(500, 500);
    assertNull(listener.hooveredPanel);

    listener.mousePressed(e1);
    listener.mouseDragged(e1);
    assertSame(parent, listener.hooveredPanel);
    checkEvent(parentProp.enteredMouse, 0, 0);

    listener.mouseDragged(e2);
    assertSame(child2Panel, listener.hooveredPanel);
    assertNull(parentProp.exitedMouse);
    checkEvent(child2.enteredMouse, 49, 49);
    assertEquals(null, child2.draggedMouse);

    listener.mouseDragged(e3);
    assertSame(childPanel, listener.hooveredPanel);
    checkEvent(child2.exitedMouse, 499, 499);
    checkEvent(childProp.enteredMouse, 250, 250);
    assertEquals(null, childProp.draggedMouse);
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
