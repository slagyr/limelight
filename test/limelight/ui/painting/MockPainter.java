//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.painting;

import limelight.ui.PaintablePanel;
import limelight.ui.Painter;

import java.awt.*;

public class MockPainter implements Painter
{
  public boolean painted;

  public void paint(Graphics2D graphics, PaintablePanel panel)
  {
    painted = true;
  }
}
