//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

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