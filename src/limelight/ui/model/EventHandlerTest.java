package limelight.ui.model;

import limelight.ui.EventAction;
import limelight.ui.events.*;
import limelight.ui.events.Event;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static junit.framework.Assert.assertEquals;

public class EventHandlerTest
{
  private MemoryAction action;
  private BasePanel panel;
  private BasePanel parent;
  private EventHandler handler;

  @Before
  public void setUp() throws Exception
  {
    action = new MemoryAction();
    parent = new TestableBasePanel();
    panel = new TestableBasePanel();
    parent.add(panel);

    handler = panel.getEventHandler();
  }

  private static class MemoryAction implements EventAction
  {
    public boolean invoked;
    public void invoke(Event event)
    {
      invoked = true;
    }
  }
  
  @Test
  public void unhandledInheritableEventsArePassedToParent() throws Exception
  {
    parent.getEventHandler().add(MousePressedEvent.class, action);

    panel.getEventHandler().dispatch(new MousePressedEvent(panel, 0, new Point(0, 0), 0));

    assertEquals(true, action.invoked);
  }

  @Test
  public void unhandledNoninheritableEventsArePassedToParent() throws Exception
  {
    parent.getEventHandler().add(KeyPressedEvent.class, action);

    panel.getEventHandler().dispatch(new KeyPressedEvent(panel, 0, KeyEvent.KEY_ENTER, KeyEvent.LOCATION_LEFT));

    assertEquals(false, action.invoked);
  }

  @Test
  public void addingMousePressAction() throws Exception
  {
    handler.onMousePress(action);
    handler.dispatch(new MousePressedEvent(panel, 0, new Point(0, 0), 0));
    assertEquals(true, action.invoked);
  }

  @Test
  public void addingMouseReleaseAction() throws Exception
  {
    handler.onMouseRelease(action);
    handler.dispatch(new MouseReleasedEvent(panel, 0, new Point(0, 0), 0));
    assertEquals(true, action.invoked);
  }

  @Test
  public void addingMouseClickAction() throws Exception
  {
    handler.onMouseClick(action);
    handler.dispatch(new MouseClickedEvent(panel, 0, new Point(0, 0), 0));
    assertEquals(true, action.invoked);
  }

  @Test
  public void addingMouseMoveAction() throws Exception
  {
    handler.onMouseMove(action);
    handler.dispatch(new MouseMovedEvent(panel, 0, new Point(0, 0), 0));
    assertEquals(true, action.invoked);
  }

  @Test
  public void addingMouseDragAction() throws Exception
  {
    handler.onMouseDrag(action);
    handler.dispatch(new MouseDraggedEvent(panel, 0, new Point(0, 0), 0));
    assertEquals(true, action.invoked);
  }

  @Test
  public void addingMouseEnterAction() throws Exception
  {
    handler.onMouseEnter(action);
    handler.dispatch(new MouseEnteredEvent(panel, 0, new Point(0, 0), 0));
    assertEquals(true, action.invoked);
  }

  @Test
  public void addingMouseExitAction() throws Exception
  {
    handler.onMouseExit(action);
    handler.dispatch(new MouseExitedEvent(panel, 0, new Point(0, 0), 0));
    assertEquals(true, action.invoked);
  }

  @Test
  public void addingMouseWheelMovedAction() throws Exception
  {
    handler.onMouseWheel(action);
    handler.dispatch(new MouseWheelEvent(panel, 0, new Point(0, 0), 0, 1, 2, 3));
    assertEquals(true, action.invoked);
  }

  @Test
  public void addingFocusLostAction() throws Exception
  {
    handler.onFocusLost(action);
    handler.dispatch(new FocusLostEvent(panel));
    assertEquals(true, action.invoked);
  }

  @Test
  public void addingFocusGainedAction() throws Exception
  {
    handler.onFocusGained(action);
    handler.dispatch(new FocusGainedEvent(panel));
    assertEquals(true, action.invoked);
  }
   
  @Test
  public void addingCharTypedAction() throws Exception
  {
    handler.onCharTyped(action);
    handler.dispatch(new CharTypedEvent(panel, 0, 'a'));
    assertEquals(true, action.invoked);
  }

  @Test
  public void addingKeyPressAction() throws Exception
  {
    handler.onKeyPress(action);
    handler.dispatch(new KeyPressedEvent(panel, 0, KeyEvent.KEY_ENTER, KeyEvent.LOCATION_LEFT));
    assertEquals(true, action.invoked);
  }

  @Test
  public void addingKeyReleaseAction() throws Exception
  {
    handler.onKeyRelease(action);
    handler.dispatch(new KeyReleasedEvent(panel, 0, KeyEvent.KEY_ENTER, KeyEvent.LOCATION_LEFT));
    assertEquals(true, action.invoked);
  }
}
