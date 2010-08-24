package limelight.ui;

import limelight.ui.events.Event;

import java.util.ArrayList;
import java.util.List;

public class EventActionMulticaster implements EventAction
{
  private EventAction first;
  private EventAction second;

  public static EventAction add(EventAction first, EventAction second)
  {
    if(first == null)
      return second;
    if(second == null)
      return first;

    return new EventActionMulticaster(first, second);
  }

  public static EventAction remove(EventAction action, EventAction removed)
  {
    if(action == removed)
      return null;
    else if(action instanceof EventActionMulticaster)
      return ((EventActionMulticaster)action).remove(removed);
    return action;
  }

  public static List<EventAction> collect(EventAction action)
  {
    List<EventAction> actions = new ArrayList<EventAction>();
    collect(action, actions);
    return actions;
  }

  private static void collect(EventAction action, List<EventAction> actions)
  {
    if(action == null)
      return;

    if(action instanceof EventActionMulticaster)
    {
      final EventActionMulticaster multicaster = (EventActionMulticaster) action;
      collect(multicaster.getFirst(), actions);
      collect(multicaster.getSecond(), actions);
    }
    else
      actions.add(action);
  }

  public EventActionMulticaster(EventAction first, EventAction second)
  {
    this.first = first;
    this.second = second;
  }

  private EventAction remove(EventAction removed)
  {
    if(first == removed)
      return second;
    if(second == removed)
      return first;

    EventAction firstRemoval = EventActionMulticaster.remove(first, removed);
    EventAction secondRemoval = EventActionMulticaster.remove(second, removed);

    if(first == firstRemoval && second == secondRemoval)
      return this;

    return EventActionMulticaster.add(firstRemoval, secondRemoval);
  }

  public EventAction getFirst()
  {
    return first;
  }

  public EventAction getSecond()
  {
    return second;
  }

  public void invoke(Event event)
  {
    first.invoke(event);
    second.invoke(event);
  }
}
