package limelight.ui.model;

import limelight.ui.EventAction;
import limelight.ui.EventActionMulticaster;
import limelight.ui.Panel;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

public class EventHandler
{
  private Panel panel;

  private static class EventDispatcher
  {
    private int id;
    private EventAction action;

    private EventDispatcher(int id, EventAction action)
    {
      this.id = id;
      this.action = action;
    }
  }

  public static boolean isInheritable(int id)
  {
    switch(id)
    {
      case MouseEvent.MOUSE_PRESSED:
      case MouseEvent.MOUSE_RELEASED:
      case MouseEvent.MOUSE_CLICKED:
      case MouseEvent.MOUSE_DRAGGED:
      case MouseEvent.MOUSE_MOVED:
      case MouseEvent.MOUSE_WHEEL:
        return true;
    }
    return false;
  }

  private LinkedList<EventDispatcher> dispatchers;

  public EventHandler(Panel panel)
  {
    this.panel = panel;
  }

  public void add(int id, EventAction action)
  {
    if(action == null)
      return;

    if(dispatchers == null)
      dispatchers = new LinkedList<EventDispatcher>();

    EventDispatcher dispatcher = get(id);
    if(dispatcher == null)
      dispatchers.add(new EventDispatcher(id, action));
    else
      dispatcher.action = EventActionMulticaster.add(dispatcher.action, action);
  }

  private EventDispatcher get(int id)
  {
    if(dispatchers == null)
      return null;

    for(EventDispatcher dispatcher : dispatchers)
    {
      if(dispatcher.id == id)
        return dispatcher;
    }
    return null;
  }

  public void dispatch(AWTEvent event)
  {
    EventDispatcher dispatcher = get(event.getID());
    if(dispatcher != null)
      dispatcher.action.invoke(event);
    else if(isInheritable(event.getID()))
    {
      Panel parent = panel.getParent();
      if(parent != null)
        parent.getEventHandler().dispatch(event);
    }
  }

  public void onMousePress(EventAction action)
  {
    add(MouseEvent.MOUSE_PRESSED, action);
  }

  public void onMouseRelease(EventAction action)
  {
    add(MouseEvent.MOUSE_RELEASED, action);
  }

  public void onMouseClick(EventAction action)
  {
    add(MouseEvent.MOUSE_CLICKED, action);
  }

  public void onMouseMove(EventAction action)
  {
    add(MouseEvent.MOUSE_MOVED, action);
  }

  public void onMouseDrag(EventAction action)
  {
    add(MouseEvent.MOUSE_DRAGGED, action);
  }

  public void onMouseEnter(EventAction action)
  {
    add(MouseEvent.MOUSE_ENTERED, action);
  }

  public void onMouseExit(EventAction action)
  {
    add(MouseEvent.MOUSE_EXITED, action);
  }

  public void onMouseWheel(EventAction action)
  {
    add(MouseEvent.MOUSE_WHEEL, action);
  }

  public void onFocusGained(EventAction action)
  {
    add(FocusEvent.FOCUS_GAINED, action);
  }

  public void onFocusLost(EventAction action)
  {
    add(FocusEvent.FOCUS_LOST, action);
  }

  public void onKeyType(EventAction action)
  {
    add(KeyEvent.KEY_TYPED, action);
  }

  public void onKeyRelease(EventAction action)
  {
    add(KeyEvent.KEY_RELEASED, action);
  }

  public void onKeyPress(EventAction action)
  {
    add(KeyEvent.KEY_PRESSED, action);
  }
}
