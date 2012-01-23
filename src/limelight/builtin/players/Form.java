package limelight.builtin.players;

import limelight.ui.events.panel.CastEvent;
import limelight.ui.model.PropPanel;

import java.util.HashMap;
import java.util.Map;

public class Form
{
  private PropPanel propPanel;

  public void install(CastEvent event)
  {
    propPanel = (PropPanel)event.getRecipient();
    propPanel.getBackstage().put("form", this);
  }

  public Map<String, Object> getData()
  {

    return new HashMap<String, Object>();
  }
}
