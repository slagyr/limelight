//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model.inputs;

import limelight.ui.Panel;
import limelight.ui.model.Layout;
import limelight.ui.model.text.offsetting.XOffsetStrategy;
import limelight.ui.model.text.offsetting.YOffsetStrategy;

import java.util.Map;

public class TextInputPanelLayout extends InputPanelLayout
{
  public static TextInputPanelLayout instance = new TextInputPanelLayout();

  @Override
  public void doExpansion(Panel thePanel)
  {
    TextInputPanel textInputPanel = (TextInputPanel) thePanel;
    textInputPanel.getModel().clearCache();
    super.doExpansion(thePanel);
    textInputPanel.getModel().recalculateOffset(XOffsetStrategy.CENTERED, YOffsetStrategy.FITTING);
  }
}