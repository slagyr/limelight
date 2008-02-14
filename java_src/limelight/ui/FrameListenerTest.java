package limelight.ui;

import junit.framework.TestCase;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class FrameListenerTest extends TestCase
{
  private FrameListener listener;
  private Frame frame;
  private MockBlock childBlock;
  private ParentPanel childPanel;
  private MockBlock rootBlock;

  public void setUp() throws Exception
  {
    rootBlock = new MockBlock();
    frame = new Frame(rootBlock);
    listener = new FrameListener(frame);

    childBlock = new MockBlock();
    childPanel = new BlockPanel(childBlock);

    frame.getPanel().add(childPanel);

    frame.getPanel().setSize(1000, 1000);
    childPanel.setLocation(250, 250);
    childPanel.setSize(500, 500);

  }

  public void testMousePressed() throws Exception
  {
    MouseEvent e1 = mouseEvent(0, 0);
    listener.mousePressed(e1);
    assertSame(e1, rootBlock.pressedMouse);
    assertSame(frame.getPanel(), listener.pressedPanel);

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
    MockBlock child2Block = new MockBlock();
    ParentPanel child2Panel = new BlockPanel(child2Block);
    frame.getPanel().add(child2Panel);
    child2Panel.setLocation(1, 1);
    child2Panel.setSize(100, 100);

    MouseEvent e1 = mouseEvent(0, 0);
    MouseEvent e2 = mouseEvent(50, 50);
    MouseEvent e3 = mouseEvent(500, 500);
    assertNull(listener.hooveredPanel);

    listener.mouseMoved(e1);
    assertSame(frame.getPanel(), listener.hooveredPanel);
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
    MockBlock child2Block = new MockBlock();
    ParentPanel child2Panel = new BlockPanel(child2Block);
    frame.getPanel().add(child2Panel);
    child2Panel.setLocation(1, 1);
    child2Panel.setSize(100, 100);

    MouseEvent e1 = mouseEvent(0, 0);
    MouseEvent e2 = mouseEvent(50, 50);
    MouseEvent e3 = mouseEvent(500, 500);
    assertNull(listener.hooveredPanel);

    listener.mouseDragged(e1);
    assertSame(frame.getPanel(), listener.hooveredPanel);
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
    return new MouseEvent(frame, 1, 2, 3, x, y, 1, false);
  }

  private MouseWheelEvent mouseWheelEvent(int x, int y)
  {
    return new MouseWheelEvent(frame, 1, 2, 3, x, y, 1, false, 1, 2, 3);
  }
}
