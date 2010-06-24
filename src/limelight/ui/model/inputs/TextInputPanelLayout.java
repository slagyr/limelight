package limelight.ui.model.inputs;

import limelight.ui.Panel;

public class TextInputPanelLayout extends InputPanelLayout
{
  public static TextInputPanelLayout instance = new TextInputPanelLayout();

  public void doLayout(Panel thePanel)
  {
    TextInputPanel textInputPanel = (TextInputPanel) thePanel;
    textInputPanel.getModel().clearLayouts();
    super.doLayout(thePanel);
    textInputPanel.getModel().recalculateOffset();
  }
}