package limelight.ui.events.panel;

import limelight.ui.Panel;

public class CastEvent extends PanelEvent
{
  public CastEvent(Panel panel)
  {
    setSource(panel);
  }
}
