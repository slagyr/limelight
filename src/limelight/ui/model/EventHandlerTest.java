package limelight.ui.model;

import limelight.ui.EventAction;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.*;

import static junit.framework.Assert.assertEquals;

public class EventHandlerTest
{
  private MemoryAction action;
  private BasePanel panel;
  private BasePanel parent;
  private EventHandler handler;
  private Component component;

  @Before
  public void setUp() throws Exception
  {
    action = new MemoryAction();
    parent = new TestableBasePanel();
    panel = new TestableBasePanel();
    parent.add(panel);
    component = new java.awt.Panel();

    handler = panel.getEventHandler();
  }

  private static class MemoryAction implements EventAction
  {
    public boolean invoked;
    public void invoke(AWTEvent event)
    {
      invoked = true;
    }
  }

  private MouseEvent mouseEvent(int id)
  {
    return new MouseEvent(component, id, 2, 3, 4, 5, 6, false);
  }

  private FocusEvent focusEvent(int id)
  {
    return new FocusEvent(component, id);
  }

  private KeyEvent keyEvent(int id)
  {
    return new KeyEvent(component, id, 2, 3, KeyEvent.VK_UNDEFINED, 'a');
  }

  @Test
  public void unhandledInheritableEventsArePassedToParent() throws Exception
  {
    parent.getEventHandler().add(MouseEvent.MOUSE_PRESSED, action);

    panel.getEventHandler().dispatch(new MouseEvent(component, MouseEvent.MOUSE_PRESSED, 1, 2, 3, 4, 5, false));

    assertEquals(true, action.invoked);
  }

  @Test
  public void unhandledNoninheritableEventsArePassedToParent() throws Exception
  {
    parent.getEventHandler().add(KeyEvent.KEY_PRESSED, action);

    panel.getEventHandler().dispatch(new KeyEvent(component, KeyEvent.KEY_PRESSED, 1, 2, 3, 'a'));

    assertEquals(false, action.invoked);
  }

  @Test
  public void inhertiableEvents() throws Exception
  {
    assertEquals(true, EventHandler.isInheritable(MouseEvent.MOUSE_PRESSED));
    assertEquals(true, EventHandler.isInheritable(MouseEvent.MOUSE_RELEASED));
    assertEquals(true, EventHandler.isInheritable(MouseEvent.MOUSE_CLICKED));
    assertEquals(true, EventHandler.isInheritable(MouseEvent.MOUSE_DRAGGED));
    assertEquals(true, EventHandler.isInheritable(MouseEvent.MOUSE_MOVED));
    assertEquals(true, EventHandler.isInheritable(MouseEvent.MOUSE_WHEEL));

    assertEquals(false, EventHandler.isInheritable(MouseEvent.MOUSE_ENTERED));
    assertEquals(false, EventHandler.isInheritable(MouseEvent.MOUSE_EXITED));
    assertEquals(false, EventHandler.isInheritable(KeyEvent.KEY_PRESSED));
    assertEquals(false, EventHandler.isInheritable(KeyEvent.KEY_RELEASED));
    assertEquals(false, EventHandler.isInheritable(KeyEvent.KEY_TYPED));
    assertEquals(false, EventHandler.isInheritable(FocusEvent.FOCUS_GAINED));
    assertEquals(false, EventHandler.isInheritable(FocusEvent.FOCUS_LOST));
  }

  @Test
  public void addingMousePressAction() throws Exception
  {
    handler.onMousePress(action);
    handler.dispatch(mouseEvent(MouseEvent.MOUSE_PRESSED));
    assertEquals(true, action.invoked);
  }

  @Test
  public void addingMouseReleaseAction() throws Exception
  {
    handler.onMouseRelease(action);
    handler.dispatch(mouseEvent(MouseEvent.MOUSE_RELEASED));
    assertEquals(true, action.invoked);
  }

  @Test
  public void addingMouseClickAction() throws Exception
  {
    handler.onMouseClick(action);
    handler.dispatch(mouseEvent(MouseEvent.MOUSE_CLICKED));
    assertEquals(true, action.invoked);
  }

  @Test
  public void addingMouseMoveAction() throws Exception
  {
    handler.onMouseMove(action);
    handler.dispatch(mouseEvent(MouseEvent.MOUSE_MOVED));
    assertEquals(true, action.invoked);
  }

  @Test
  public void addingMouseDragAction() throws Exception
  {
    handler.onMouseDrag(action);
    handler.dispatch(mouseEvent(MouseEvent.MOUSE_DRAGGED));
    assertEquals(true, action.invoked);
  }

  @Test
  public void addingMouseEnterAction() throws Exception
  {
    handler.onMouseEnter(action);
    handler.dispatch(mouseEvent(MouseEvent.MOUSE_ENTERED));
    assertEquals(true, action.invoked);
  }

  @Test
  public void addingMouseExitAction() throws Exception
  {
    handler.onMouseExit(action);
    handler.dispatch(mouseEvent(MouseEvent.MOUSE_EXITED));
    assertEquals(true, action.invoked);
  }

  @Test
  public void addingMouseWheelMovedAction() throws Exception
  {
    handler.onMouseWheel(action);
    handler.dispatch(mouseEvent(MouseEvent.MOUSE_WHEEL));
    assertEquals(true, action.invoked);
  }

  @Test
  public void addingFocusLostAction() throws Exception
  {
    handler.onFocusLost(action);
    handler.dispatch(focusEvent(FocusEvent.FOCUS_LOST));
    assertEquals(true, action.invoked);
  }

  @Test
  public void addingFocusGainedAction() throws Exception
  {
    handler.onFocusGained(action);
    handler.dispatch(focusEvent(FocusEvent.FOCUS_GAINED));
    assertEquals(true, action.invoked);
  }
   
  @Test
  public void addingKeyTypedAction() throws Exception
  {
    handler.onKeyType(action);
    handler.dispatch(keyEvent(KeyEvent.KEY_TYPED));
    assertEquals(true, action.invoked);
  }

  @Test
  public void addingKeyPressAction() throws Exception
  {
    handler.onKeyPress(action);
    handler.dispatch(keyEvent(KeyEvent.KEY_PRESSED));
    assertEquals(true, action.invoked);
  }

  @Test
  public void addingKeyReleaseAction() throws Exception
  {
    handler.onKeyRelease(action);
    handler.dispatch(keyEvent(KeyEvent.KEY_RELEASED));
    assertEquals(true, action.invoked);
  }
//
//  @Test
//  public void addingButtonPressAction() throws Exception
//  {
//    handler.onButtonPress(action);
//    handler.buttonPressed(focusEvent());
//    assertEquals(true, action.invoked);
//  }
//
//  @Test
//  public void addingValueChangeAction() throws Exception
//  {
//    handler.onValueChange(action);
//    handler.valueChanged(focusEvent());
//    assertEquals(true, action.invoked);
//  }
}
