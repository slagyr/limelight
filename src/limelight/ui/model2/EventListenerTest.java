package limelight.ui.model2;

import junit.framework.TestCase;
import limelight.ui.api.MockProp;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class EventListenerTest extends TestCase
{
  private EventListener listener;
  private PropPanel root;
  private MockProp childBlock;
  private PropPanel childPanel;
  private MockProp rootBlock;
  private JPanel jpanel;

  public void setUp() throws Exception
  {
    jpanel = new JPanel();

    rootBlock = new MockProp();
    root = new PropPanel(rootBlock);
    listener = new EventListener(root);

    childBlock = new MockProp();
    childPanel = new PropPanel(childBlock);

    root.add(childPanel);

    root.setSize(1000, 1000);
    childPanel.setLocation(250, 250);
    childPanel.setSize(500, 500);
  }

  public void testMousePressed() throws Exception
  {
    MouseEvent e1 = mouseEvent(0, 0);
    listener.mousePressed(e1);
    assertSame(e1, rootBlock.pressedMouse);
    assertSame(root, listener.pressedPanel);

    MouseEvent e2 = mouseEvent(500, 500);
    listener.mousePressed(e2);
    assertSame(e2, childBlock.pressedMouse);
    assertSame(childPanel, listener.pressedPanel);
  }

  public void testMouseReleased() throws Exception
  {
    MouseEvent e1 = mouseEvent(0, 0);
    listener.mouseReleased(e1);
    assertSame(e1, rootBlock.releasedMouse);

    MouseEvent e2 = mouseEvent(500, 500);
    listener.mouseReleased(e2);
    assertSame(e2, childBlock.releasedMouse);
  }

  public void testMouseClick() throws Exception
  {
    MouseEvent e1 = mouseEvent(0, 0);
    listener.mousePressed(e1);
    listener.mouseReleased(e1);
    assertSame(e1, rootBlock.clickedMouse);

    MouseEvent e2 = mouseEvent(500, 500);
    listener.mousePressed(e2);
    listener.mouseReleased(e2);
    assertSame(e2, childBlock.clickedMouse);
  }

  public void testMouseClickButPanelChanged() throws Exception
  {
    MouseEvent e1 = mouseEvent(0, 0);
    MouseEvent e2 = mouseEvent(500, 500);

    listener.mousePressed(e1);
    listener.mouseReleased(e2);

    assertNull(rootBlock.clickedMouse);
    assertNull(childBlock.clickedMouse);
  }

  public void testMouseMoved() throws Exception
  {
    MouseEvent e1 = mouseEvent(0, 0);
    listener.mouseMoved(e1);
    assertSame(e1, rootBlock.movedMouse);

    MouseEvent e2 = mouseEvent(500, 500);
    listener.mouseMoved(e2);
    assertSame(e2, childBlock.movedMouse);
  }

  public void testMouseDragged() throws Exception
  {
    MouseEvent e1 = mouseEvent(0, 0);
    listener.mouseDragged(e1);
    assertSame(e1, rootBlock.draggedMouse);

    MouseEvent e2 = mouseEvent(500, 500);
    listener.mouseDragged(e2);
    assertSame(e2, childBlock.draggedMouse);
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
    assertSame(e1, rootBlock.enteredMouse);

    listener.mouseMoved(e2);
    assertSame(child2Panel, listener.hooveredPanel);
    assertNull(rootBlock.exitedMouse);
    assertSame(e2, child2Block.enteredMouse);

    listener.mouseMoved(e3);
    assertSame(childPanel, listener.hooveredPanel);
    assertSame(e3, child2Block.exitedMouse);
    assertSame(e3, childBlock.enteredMouse);
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
    assertSame(e1, rootBlock.enteredMouse);

    listener.mouseDragged(e2);
    assertSame(child2Panel, listener.hooveredPanel);
    assertNull(rootBlock.exitedMouse);
    assertSame(e2, child2Block.enteredMouse);

    listener.mouseDragged(e3);
    assertSame(childPanel, listener.hooveredPanel);
    assertSame(e3, child2Block.exitedMouse);
    assertSame(e3, childBlock.enteredMouse);
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
