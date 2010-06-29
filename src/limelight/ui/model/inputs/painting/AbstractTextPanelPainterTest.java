//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs.painting;

import limelight.ui.MockGraphics;
import limelight.ui.MockPanel;
import limelight.ui.model.inputs.TextBox2Panel;
import limelight.ui.model.inputs.TextInputPanel;
import limelight.ui.model.inputs.TextModel;

import java.awt.*;

public class AbstractTextPanelPainterTest
{
  public TextPanelPainter painter;
  public TextModel boxInfo;
  public TextInputPanel panel;
  public MockGraphics graphics;
  public Rectangle testBox;
  public MockPanel parent;

  protected void testClassInit()
  {
    panel = new TextBox2Panel();
    parent = new MockPanel();
    parent.add(panel);
    parent.setSize(150, 28);
    panel.doLayout();
    boxInfo = panel.getModel();
    boxInfo.setText("Some Text");
    boxInfo.setCaretIndex(4);
    graphics = new MockGraphics();
    panel.setCursorOn(true);
  }
}
