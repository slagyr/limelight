package limelight.ui.model.inputs;

import limelight.ui.EventAction;
import limelight.ui.Panel;
import limelight.ui.events.Event;

public class PropogateToParentAction implements EventAction
{
  public static PropogateToParentAction instance = new PropogateToParentAction();

  public void invoke(Event event)
  {
    final Panel parent = event.getRecipient().getParent();
    if(parent != null)
      event.dispatch(parent);
  }
}
