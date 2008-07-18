package limelight.ui.model2.updates;

import limelight.ui.Panel;
import limelight.ui.model2.PropPanel;
import limelight.ui.model2.PropPanelLayout;

public class ScrollChangedUpdate extends PaintUpdate
{
  protected ScrollChangedUpdate(int severity)
  {
    super(severity);
  }

  public void performUpdate(Panel panel)
  {
    PropPanel propPanel = (PropPanel)panel;
    PropPanelLayout layout = propPanel.getLayout();
    layout.layoutRows();
    paintPanel(panel);
  }
}
