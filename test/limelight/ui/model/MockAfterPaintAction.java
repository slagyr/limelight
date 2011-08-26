//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import limelight.ui.painting.PaintAction;

import java.awt.*;

public class MockAfterPaintAction implements PaintAction
{
  public boolean invoked;

  public void invoke(Graphics2D graphics)
  {
    invoked = true;
  }
}
