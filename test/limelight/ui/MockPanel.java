//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui;

import limelight.styles.ScreenableStyle;
import limelight.ui.model.PanelBase;

import java.awt.*;

public class MockPanel extends PanelBase
{
  public final ScreenableStyle style;
  public int paintIndex;
  public boolean wasPainted;
  public boolean canBeBuffered;
  public boolean changeMarkerWasReset;
//  public boolean wasLaidOut;
  public boolean floater;
  public boolean consumableAreaChangedCalled;
  public boolean markedAsDirty;

  public MockPanel()
  {
    style = new ScreenableStyle();
    canBeBuffered = true;
  }

  public ScreenableStyle getStyle()
  {
    return style;
  }

  public void paintOn(Graphics2D graphics)
  {
    wasPainted = true;
  }

  public boolean canBeBuffered()
  {
    return canBeBuffered;
  }

  public void stubAbsoluteLocation(Point location)
  {
    absoluteLocation = location;
  }

  public boolean isFloater()
  {
    return floater;
  }

  @Override
  public void consumableAreaChanged()
  {
    consumableAreaChangedCalled = true;
  }

  @Override
  public void markAsDirty()
  {
    markedAsDirty = true;
  }
}
