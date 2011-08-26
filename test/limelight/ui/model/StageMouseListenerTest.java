//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import limelight.model.api.FakePropProxy;
import limelight.ui.MockPanel;
import limelight.ui.events.panel.MousePressedEvent;
import limelight.ui.events.panel.*;
import limelight.ui.model.inputs.MockEventAction;
import org.junit.Before;
import org.junit.Test;


import java.awt.*;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;

public class StageMouseListenerTest
{
  private StageMouseListener listener;
  private MockParentPanel parent;
  private MockPanel child;
  private Component component;
  private MockEventAction parentAction;
  private MockEventAction childAction;

  @Before
  public void setUp() throws Exception
  {
    component = new java.awt.Panel();

    FakeScene root = new FakeScene();
    listener = new StageMouseListener(root);

    parent = new MockParentPanel();
    child = new MockPanel();

    root.add(parent);
    parent.add(child);

    ScenePanel scenePanel = new ScenePanel(new FakePropProxy());
    scenePanel.add(parent);
    scenePanel.setStage(new MockStage());

    parent.setSize(1000, 1000);
    child.setLocation(250, 250);
    child.setSize(500, 500);
    
    parentAction = new MockEventAction();
    childAction = new MockEventAction();
  }

  @Test
  public void mousePressed() throws Exception
  {
    parent.getEventHandler().add(MousePressedEvent.class, parentAction);
    child.getEventHandler().add(MousePressedEvent.class, childAction);
    
    java.awt.event.MouseEvent e1 = event(0, 0);
    listener.mousePressed(e1);
    assertSame(parent, listener.pressedPanel);
    checkEvent(parentAction, MousePressedEvent.class, 0, 0);

    java.awt.event.MouseEvent e2 = event(500, 500);
    listener.mousePressed(e2);
    assertSame(child, listener.pressedPanel);
    checkEvent(childAction, MousePressedEvent.class, 500, 500);
  }

  @Test
  public void mouseReleased() throws Exception
  {                           
    parent.getEventHandler().add(MouseReleasedEvent.class, parentAction);
    child.getEventHandler().add(MouseReleasedEvent.class, childAction);
    
    java.awt.event.MouseEvent e1 = event(0, 0);
    listener.mouseReleased(e1);
    checkEvent(parentAction, MouseReleasedEvent.class, 0, 0);

    java.awt.event.MouseEvent e2 = event(500, 500);
    listener.mouseReleased(e2);
    checkEvent(childAction, MouseReleasedEvent.class, 500, 500);
  }

  @Test
  public void mouseClick() throws Exception
  {
    parent.getEventHandler().add(MouseClickedEvent.class, parentAction);
    child.getEventHandler().add(MouseClickedEvent.class, childAction);

    listener.mousePressed(event(0, 0));
    listener.mouseReleased(event(0, 0));
    checkEvent(parentAction, MouseClickedEvent.class, 0, 0);

    listener.mousePressed(event(500, 500));
    listener.mouseReleased(event(500, 500));
    checkEvent(childAction, MouseClickedEvent.class, 500, 500);
  }

  private void checkEvent(MockEventAction action, Class eventClass, int x, int y)
  {
    MouseEvent mouseEvent = (MouseEvent)action.event;
    assertEquals(eventClass, mouseEvent.getClass());
    assertEquals(new Point(x, y), mouseEvent.getAbsoluteLocation());
  }

  @Test
  public void mouseClickButPanelChanged() throws Exception
  {
    parent.getEventHandler().add(MousePressedEvent.class, parentAction);
    child.getEventHandler().add(MouseReleasedEvent.class, childAction);

    listener.mousePressed(event(0, 0));
    listener.mouseReleased(event(500, 500));

    checkEvent(parentAction, MousePressedEvent.class, 0, 0);
    checkEvent(childAction, MouseReleasedEvent.class, 500, 500);
  }

  @Test
  public void mouseMoved() throws Exception
  {
    parent.getEventHandler().add(MouseMovedEvent.class, parentAction);
    child.getEventHandler().add(MouseMovedEvent.class, childAction);

    listener.mouseMoved(event(0, 0));
    checkEvent(parentAction, MouseMovedEvent.class, 0, 0);

    listener.mouseMoved(event(500, 500));
    checkEvent(childAction, MouseMovedEvent.class, 500, 500);
  }

  @Test
  public void mouseDraggedIsOnlyEffectiveAfterPress() throws Exception
  {
    parent.getEventHandler().add(MouseEnteredEvent.class, parentAction);
    parent.getEventHandler().add(MouseDraggedEvent.class, parentAction);

    listener.mouseDragged(event(0, 0));
    checkEvent(parentAction, MouseEnteredEvent.class, 0, 0);

    listener.mousePressed(event(0, 0));
    listener.mouseDragged(event(0, 0));
    checkEvent(parentAction, MouseDraggedEvent.class, 0, 0);
  }
  
  @Test
  public void mouseDraggedIsOnlyEffectiveOnPressedPanel() throws Exception
  {
    parent.getEventHandler().add(MouseDraggedEvent.class, parentAction);
    child.getEventHandler().add(MouseEnteredEvent.class, childAction);
    child.getEventHandler().add(MouseDraggedEvent.class, childAction);

    listener.mousePressed(event(0, 0));

    listener.mouseDragged(event(500, 500));
    checkEvent(childAction, MouseEnteredEvent.class, 500, 500);
    checkEvent(parentAction, MouseDraggedEvent.class, 500, 500);

    listener.mousePressed(event(500, 500));
    listener.mouseDragged(event(500, 500));
    checkEvent(childAction, MouseDraggedEvent.class, 500, 500);
  }

  @Test
  public void mouseEnteredAndExited() throws Exception
  {
    MockPanel child2Panel = new MockPanel();
    parent.add(child2Panel);
    child2Panel.setLocation(1, 1);
    child2Panel.setSize(100, 100);

    assertNull(listener.hooveredPanel);

    MockEventAction moveAction = new MockEventAction();
    parent.getEventHandler().add(MouseEnteredEvent.class, parentAction);
    parent.getEventHandler().add(MouseMovedEvent.class, moveAction);
    listener.mouseMoved(event(0, 0));
    assertSame(parent, listener.hooveredPanel);
    checkEvent(parentAction, MouseEnteredEvent.class, 0, 0);
    checkEvent(moveAction, MouseMovedEvent.class, 0, 0);

    moveAction.reset();
    parentAction.reset();
    MockEventAction child2MoveAction = new MockEventAction();
    child2Panel.getEventHandler().add(MouseEnteredEvent.class, childAction);
    child2Panel.getEventHandler().add(MouseMovedEvent.class, child2MoveAction);
    listener.mouseMoved(event(50, 50));
    assertSame(child2Panel, listener.hooveredPanel);
    assertEquals(false, moveAction.invoked || parentAction.invoked);
    checkEvent(childAction, MouseEnteredEvent.class, 50, 50);
    checkEvent(child2MoveAction, MouseMovedEvent.class, 50, 50);

    child2MoveAction.reset();
    MockEventAction childExitAction = new MockEventAction();
    MockEventAction childMoveAction = new MockEventAction();
    child2Panel.getEventHandler().add(MouseExitedEvent.class, childExitAction);
    child.getEventHandler().add(MouseMovedEvent.class, childMoveAction);
    listener.mouseMoved(event(500, 500));
    assertSame(child, listener.hooveredPanel);
    checkEvent(childExitAction, MouseExitedEvent.class, 500, 500);
    checkEvent(childMoveAction, MouseMovedEvent.class, 500, 500);
  }

  @Test
  public void draggingInvokesEnteredAndExited() throws Exception
  {
    MockPanel child2Panel = new MockPanel();
    parent.add(child2Panel);
    child2Panel.setLocation(1, 1);
    child2Panel.setSize(100, 100);

    assertNull(listener.hooveredPanel);

    MockEventAction parentDragAction = new MockEventAction();
    parent.getEventHandler().add(MouseDraggedEvent.class, parentDragAction);
    listener.mousePressed(event(0, 0));
    listener.mouseDragged(event(0, 0));
    assertSame(parent, listener.hooveredPanel);
    checkEvent(parentDragAction, MouseDraggedEvent.class, 0, 0);

    parentDragAction.reset();
    MockEventAction child2EnteredAction = new MockEventAction();
    child2Panel.getEventHandler().add(MouseEnteredEvent.class, child2EnteredAction);
    listener.mouseDragged(event(50, 50));
    assertSame(child2Panel, listener.hooveredPanel);
    checkEvent(parentDragAction, MouseDraggedEvent.class, 50, 50);
    checkEvent(child2EnteredAction, MouseEnteredEvent.class, 50, 50);
                                        
    MockEventAction child2ExitedAction = new MockEventAction();
    child2Panel.getEventHandler().add(MouseExitedEvent.class, child2ExitedAction);
    MockEventAction childEnteredAction = new MockEventAction();
    child.getEventHandler().add(MouseEnteredEvent.class, childEnteredAction);
    listener.mouseDragged(event(500, 500));
    assertSame(child, listener.hooveredPanel);
    checkEvent(child2ExitedAction, MouseExitedEvent.class, 500, 500);
    checkEvent(childEnteredAction, MouseEnteredEvent.class, 500, 500);
  }

  private java.awt.event.MouseEvent event(int x, int y)
  {
    return new java.awt.event.MouseEvent(component, 1, 2, 3, x, y, 1, false);
  }
}
