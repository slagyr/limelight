//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.styles.ScreenableStyle;
import limelight.util.Box;

import java.awt.*;

public class TestableParentPanel extends ParentPanelBase
{
  @Override
  public Box getChildConsumableBounds()
  {
    return null;
  }

  public void paintOn(Graphics2D graphics)
  {
  }

  public ScreenableStyle getStyle()
  {
    return null;
  }
}
