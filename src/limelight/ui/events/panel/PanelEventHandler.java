package limelight.ui.events.panel;

import limelight.events.Event;
import limelight.events.EventHandler;
import limelight.ui.Panel;

public class PanelEventHandler extends EventHandler
{
  private Panel panel;

  public PanelEventHandler(Panel panel)
  {
    this.panel = panel;
  }

  @Override
  protected void handleUndispatchedEvent(Event e)
  {
    PanelEvent event = (PanelEvent)e;
    if(event.isInheritable())
    {
      Panel parent = panel.getParent();
      if(parent != null)
        event.dispatch(parent);
    }
  }
}
