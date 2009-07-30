package limelight.ui.model;

import limelight.ui.Panel;

public class TextPanelLayout implements Layout
{
  public static TextPanelLayout instance = new TextPanelLayout();

  public void doLayout(Panel thePanel)
  {
    TextPanel panel = (TextPanel)thePanel;
    panel.resetLayout();
    try
    {
      panel.compile();
    }
    catch(Exception e)
    {
      //okay
    }
  }

  public boolean overides(Layout other)
  {
    return true;
  }
}
