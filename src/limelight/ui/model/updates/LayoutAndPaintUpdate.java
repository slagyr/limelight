package limelight.ui.model.updates;

import limelight.ui.Panel;

public class LayoutAndPaintUpdate extends PaintUpdate
{
  protected LayoutAndPaintUpdate(int severity)
  {
    super(severity);
  }

  public void performUpdate(Panel panel)
  {
    panel.doLayout();
    paintPanel(panel);
  }
}
