//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.events;

import limelight.ui.EventActionMulticaster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

// TODO MDM - Add a way to remove actions
public class EventHandler
{
  private static final List<EventAction> NO_ACTIONS = Collections.unmodifiableList(new ArrayList<EventAction>());

  private static class EventDispatcher
  {
    private Class<? extends Event> eventClass;
    private EventAction action;

    private EventDispatcher(Class<? extends Event> eventClass, EventAction action)
    {
      this.eventClass = eventClass;
      this.action = action;
    }
  }

  private LinkedList<EventDispatcher> dispatchers;

  public synchronized void add(Class<? extends Event> eventClass, EventAction action)
  {
    if(action == null)
      return;

    if(dispatchers == null)
      dispatchers = new LinkedList<EventDispatcher>();

    EventDispatcher dispatcher = get(eventClass);
    if(dispatcher == null)
      dispatchers.add(new EventDispatcher(eventClass, action));
    else
      dispatcher.action = EventActionMulticaster.add(dispatcher.action, action);
  }

  public synchronized void remove(Class<? extends Event> eventClass, EventAction action)
  {
    final EventDispatcher dispatcher = get(eventClass);
    if(dispatcher != null)
    {
      dispatcher.action = EventActionMulticaster.remove(dispatcher.action, action);
      if(dispatcher.action == null)
        dispatchers.remove(dispatcher);
    }
  }

  private EventDispatcher get(Class<? extends Event> eventClass)
  {
    if(dispatchers == null)
      return null;

    for(EventDispatcher dispatcher : dispatchers)
    {
      if(dispatcher.eventClass == eventClass)
        return dispatcher;
    }
    return null;
  }

  public List<EventAction> getActions(Class<? extends Event> eventClass)
  {
    final EventDispatcher dispatcher = get(eventClass);
    if(dispatcher != null)
      return EventActionMulticaster.collect(dispatcher.action);
    else
      return NO_ACTIONS;
  }

  public synchronized void dispatch(Event event)
  {
    EventDispatcher dispatcher = get(event.getClass());
    if(dispatcher != null)
      dispatcher.action.invoke(event);
    else
      handleUndispatchedEvent(event);
  }

  protected void handleUndispatchedEvent(Event event)
  {
    
  }
}
