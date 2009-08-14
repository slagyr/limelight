package limelight.ui.model;

import limelight.ui.Panel;

public class TextPanelLayout implements Layout
{
  public static TextPanelLayout instance = new TextPanelLayout();

  public void doLayout(Panel thePanel)
  {
    TextPanel panel = (TextPanel) thePanel;
    panel.resetLayout();
    panel.compile();
  }

  public boolean overides(Layout other)
  {
    return true;
  }
}
