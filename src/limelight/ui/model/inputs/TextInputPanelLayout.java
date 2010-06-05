package limelight.ui.model.inputs;

import limelight.ui.Panel;

public class TextInputPanelLayout extends InputPanelLayout
{
  public static TextInputPanelLayout instance = new TextInputPanelLayout();

  public void doLayout(Panel thePanel)
  {
    ((TextInputPanel)thePanel).getModelInfo().clearLayouts();
    super.doLayout(thePanel);
  }
}