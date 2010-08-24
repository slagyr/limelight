package limelight.ui.model;

import limelight.ui.events.Event;

import java.util.LinkedList;

public class MockEventHandler extends EventHandler
{
  public LinkedList<Event> events = new LinkedList<Event>();

  public MockEventHandler(limelight.ui.Panel panel)
  {
    super(panel);
  }

  @Override
  public void dispatch(Event event)
  {
    events.add(event);
  }

  public Event last()
  {
    return nthLast(1);
  }

  public Event nthLast(int n)
  {
    return events.get(events.size() - n);
  }

  public int size()
  {
    return events.size();
  }
}
