//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs.painters;

import limelight.ui.MockGraphics;
import limelight.ui.MockPanel;
import limelight.ui.model.inputs.TextBox2Panel;
import limelight.ui.model.inputs.TextInputPanel;
import limelight.ui.model.inputs.TextModel;
import limelight.ui.model.inputs.TextPanelPainter;

import java.awt.*;

public class AbstractTextPanelPainterTest
{
  static TextPanelPainter painter;
  static TextModel boxInfo;
  static TextInputPanel panel;
  static MockGraphics graphics;
  static Rectangle testBox;

  protected void testClassInit()
  {
    panel = new TextBox2Panel();
    MockPanel parent = new MockPanel();
    parent.add(panel);
    parent.setSize(150, 28);
    panel.doLayout();
    boxInfo = panel.getModelInfo();
    boxInfo.setText("Some Text");
    boxInfo.setCursorIndex(4);
    graphics = new MockGraphics();
    panel.setCursorOn(true);
  }
}
