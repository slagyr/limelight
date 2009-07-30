package limelight.ui.model.inputs;

import limelight.ui.Panel;
import limelight.ui.model.inputs.InputPanel;
import limelight.ui.model.Layout;
import limelight.util.Box;

public class InputPanelLayout implements Layout
{
  public static InputPanelLayout instance = new InputPanelLayout();

  public void doLayout(Panel thePanel)
  {
    InputPanel panel = (InputPanel)thePanel;
    panel.resetLayout();
    Box bounds = panel.getParent().getBoxInsidePadding();
    panel.setLocation(bounds.x, bounds.y);
    panel.setSize(bounds.width, bounds.height);
  }

  public boolean overides(Layout other)
  {
    return true;
  }
}
