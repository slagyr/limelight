package limelight.ui.model.inputs;

import limelight.ui.Panel;
import limelight.ui.model.inputs.offsetting.XOffsetStrategy;
import limelight.ui.model.inputs.offsetting.YOffsetStrategy;

public class TextInputPanelLayout extends InputPanelLayout
{
  public static TextInputPanelLayout instance = new TextInputPanelLayout();

  public void doLayout(Panel thePanel)
  {
    TextInputPanel textInputPanel = (TextInputPanel) thePanel;
    textInputPanel.getModel().clearCache();
    super.doLayout(thePanel);
    textInputPanel.getModel().recalculateOffset(XOffsetStrategy.CENTERED, YOffsetStrategy.FITTING);
  }
}