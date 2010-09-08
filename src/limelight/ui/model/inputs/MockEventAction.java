package limelight.ui.model.inputs;

import limelight.ui.EventAction;
import limelight.ui.Panel;
import limelight.ui.events.Event;

public class MockEventAction implements EventAction
{
  public boolean invoked;
  public Event event;
  public Panel recipient;

  public void invoke(Event event)
  {
    invoked = true;
    this.event = event;
    recipient = event.getRecipient();
  }
}
