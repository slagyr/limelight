package limelight.ui.model.inputs;

import limelight.ui.EventAction;
import limelight.ui.events.Event;

public class MockEventAction implements EventAction
{
  public boolean invoked;
  public Event event;

  public void invoke(Event event)
  {
    invoked = true;
    this.event = event;
  }
}
