//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model.inputs;

import limelight.events.Event;
import limelight.events.EventAction;
import limelight.ui.Panel;
import limelight.ui.events.panel.PanelEvent;

public class MockEventAction implements EventAction
{
  public boolean invoked;
  public Event event;
  public Panel recipient;

  public void invoke(Event event)
  {
    invoked = true;
    this.event = event;
    if(event instanceof PanelEvent)
      recipient = ((PanelEvent)event).getRecipient();
  }

  public void reset()
  {
    invoked = false;
    event = null;
    recipient = null;
  }
}
