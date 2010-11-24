//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

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
