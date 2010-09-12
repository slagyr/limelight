//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.ui.MockPanel;
import limelight.ui.api.MockProp;
import limelight.ui.events.Event;
import limelight.ui.events.MousePressedEvent;
import org.junit.Before;
import org.junit.Test;

import limelight.ui.events.*;

import java.awt.*;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;

public class RootMouseListenerTest
{
  private RootMouseListener listener;
  private MockParentPanel parent;
  private MockPanel child;
  private Component component;
  private MockEventHandler parentEvents;
  private MockEventHandler childEvents;

  @Before
  public void setUp() throws Exception
  {
    component = new java.awt.Panel();

    MockRootPanel root = new MockRootPanel();
    listener = new RootMouseListener(root);

    parent = new MockParentPanel();
    child = new MockPanel();

    root.add(parent);
    parent.add(child);
    
    parentEvents = parent.mockEventHandler;
    childEvents = child.mockEventHandler;

    ScenePanel scenePanel = new ScenePanel(new MockProp());
    scenePanel.add(parent);
    scenePanel.setFrame(new MockPropFrame());

    parent.setSize(1000, 1000);
    child.setLocation(250, 250);
    child.setSize(500, 500);
  }

  @Test
  public void mousePressed() throws Exception
  {
    java.awt.event.MouseEvent e1 = event(0, 0);
    listener.mousePressed(e1);
    assertSame(parent, listener.pressedPanel);
    checkEvent(parentEvents.last(), MousePressedEvent.class, 0, 0);

    java.awt.event.MouseEvent e2 = event(500, 500);
    listener.mousePressed(e2);
    assertSame(child, listener.pressedPanel);
    checkEvent(childEvents.last(), MousePressedEvent.class, 500, 500);
  }

  @Test
  public void mouseReleased() throws Exception
  {
    java.awt.event.MouseEvent e1 = event(0, 0);
    listener.mouseReleased(e1);
    checkEvent(parentEvents.last(), MouseReleasedEvent.class, 0, 0);

    java.awt.event.MouseEvent e2 = event(500, 500);
    listener.mouseReleased(e2);
    checkEvent(childEvents.last(), MouseReleasedEvent.class, 500, 500);
  }

  @Test
  public void mouseClick() throws Exception
  {
    listener.mousePressed(event(0, 0));
    listener.mouseReleased(event(0, 0));
    checkEvent(parentEvents.last(), MouseClickedEvent.class, 0, 0);

    listener.mousePressed(event(500, 500));
    listener.mouseReleased(event(500, 500));
    checkEvent(childEvents.last(), MouseClickedEvent.class, 500, 500);
  }

  private void checkEvent(Event event, Class eventClass, int x, int y)
  {
    MouseEvent mouseEvent = (MouseEvent)event;
    assertEquals(eventClass, mouseEvent.getClass());
    assertEquals(new Point(x, y), mouseEvent.getAbsoluteLocation());
  }

  @Test
  public void mouseClickButPanelChanged() throws Exception
  {

    listener.mousePressed(event(0, 0));
    listener.mouseReleased(event(500, 500));

    checkEvent(parentEvents.last(), MousePressedEvent.class, 0, 0);
    checkEvent(childEvents.last(), MouseReleasedEvent.class, 500, 500);
  }

  @Test
  public void mouseMoved() throws Exception
  {
    listener.mouseMoved(event(0, 0));
    checkEvent(parentEvents.last(), MouseMovedEvent.class, 0, 0);

    listener.mouseMoved(event(500, 500));
    checkEvent(childEvents.last(), MouseMovedEvent.class, 500, 500);
  }

  @Test
  public void mouseDraggedIsOnlyEffectiveAfterPress() throws Exception
  {
    listener.mouseDragged(event(0, 0));
    checkEvent(parentEvents.last(), MouseEnteredEvent.class, 0, 0);

    listener.mousePressed(event(0, 0));
    listener.mouseDragged(event(0, 0));
    checkEvent(parentEvents.last(), MouseDraggedEvent.class, 0, 0);
  }
  
  @Test
  public void mouseDraggedIsOnlyEffectiveOnPressedPanel() throws Exception
  {
    listener.mousePressed(event(0, 0));

    listener.mouseDragged(event(500, 500));
    checkEvent(childEvents.last(), MouseEnteredEvent.class, 500, 500);
    checkEvent(parentEvents.last(), MouseDraggedEvent.class, 500, 500);

    listener.mousePressed(event(500, 500));
    listener.mouseDragged(event(500, 500));
    checkEvent(childEvents.last(), MouseDraggedEvent.class, 500, 500);
  }

  @Test
  public void mouseEnteredAndExited() throws Exception
  {
    MockPanel child2Panel = new MockPanel();
    parent.add(child2Panel);
    child2Panel.setLocation(1, 1);
    child2Panel.setSize(100, 100);

    assertNull(listener.hooveredPanel);

    listener.mouseMoved(event(0, 0));
    assertSame(parent, listener.hooveredPanel);
    checkEvent(parentEvents.nthLast(2), MouseEnteredEvent.class, 0, 0);
    checkEvent(parentEvents.last(), MouseMovedEvent.class, 0, 0);

    parentEvents.events.clear();
    listener.mouseMoved(event(50, 50));
    assertSame(child2Panel, listener.hooveredPanel);
    assertEquals(0, parentEvents.size());
    checkEvent(child2Panel.mockEventHandler.nthLast(2), MouseEnteredEvent.class, 50, 50);
    checkEvent(child2Panel.mockEventHandler.last(), MouseMovedEvent.class, 50, 50);

    listener.mouseMoved(event(500, 500));
    assertSame(child, listener.hooveredPanel);
    checkEvent(child2Panel.mockEventHandler.last(), MouseExitedEvent.class, 500, 500);
    checkEvent(childEvents.last(), MouseMovedEvent.class, 500, 500);
  }
  
  @Test
  public void draggingInvokesEnteredAndExited() throws Exception
  {
    MockPanel child2Panel = new MockPanel();
    parent.add(child2Panel);
    child2Panel.setLocation(1, 1);
    child2Panel.setSize(100, 100);

    assertNull(listener.hooveredPanel);

    listener.mousePressed(event(0, 0));
    listener.mouseDragged(event(0, 0));
    assertSame(parent, listener.hooveredPanel);
    checkEvent(parentEvents.last(), MouseDraggedEvent.class, 0, 0);

    listener.mouseDragged(event(50, 50));
    assertSame(child2Panel, listener.hooveredPanel);
    checkEvent(parentEvents.last(), MouseDraggedEvent.class, 50, 50);
    checkEvent(child2Panel.mockEventHandler.last(), MouseEnteredEvent.class, 50, 50);

    listener.mouseDragged(event(500, 500));
    assertSame(child, listener.hooveredPanel);
    checkEvent(child2Panel.mockEventHandler.last(), MouseExitedEvent.class, 500, 500);
    checkEvent(childEvents.last(), MouseEnteredEvent.class, 500, 500);
  }

  private java.awt.event.MouseEvent event(int x, int y)
  {
    return new java.awt.event.MouseEvent(component, 1, 2, 3, x, y, 1, false);
  }
}
