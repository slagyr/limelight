//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs.painting;

import limelight.ui.MockGraphics;
import limelight.ui.MockPanel;
import limelight.ui.MockTypedLayoutFactory;
import limelight.ui.model.inputs.*;
import limelight.util.Box;

import java.awt.*;

public class AbstractTextPanelPainterTest
{
  public TextPanelPainter painter;
  public TextModel model;
  public MockTextContainer container;
  public MockGraphics graphics;

  protected void testClassInit()
  {
    container = new MockTextContainer();
    container.bounds = new Box(0, 0, 150, 28);

    model = new MultiLineTextModel(container);
    model.setTypedLayoutFactory(MockTypedLayoutFactory.instance);
    model.setText("Some Text");
    model.setCaretIndex(4);

    graphics = new MockGraphics();
    container.cursorOn = true;
  }
}
