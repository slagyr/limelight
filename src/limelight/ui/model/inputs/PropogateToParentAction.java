package limelight.ui.model.inputs;

import limelight.events.Event;
import limelight.events.EventAction;
import limelight.ui.Panel;
import limelight.ui.events.panel.PanelEvent;

public class PropogateToParentAction implements EventAction
{
  public static PropogateToParentAction instance = new PropogateToParentAction();

  public void invoke(Event event)
  {
    PanelEvent panelEvent = (PanelEvent)event;
    final Panel parent = panelEvent.getRecipient().getParent();
    if(parent != null)
      panelEvent.dispatch(parent);
  }
}
