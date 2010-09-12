//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui;

import limelight.styles.ScreenableStyle;
import limelight.ui.model.PanelBase;
import limelight.ui.model.MockEventHandler;

import java.awt.*;

public class MockPanel extends PanelBase
{
  public final ScreenableStyle style;
  public int paintIndex;
  public boolean wasPainted;
  public boolean canBeBuffered;
  public boolean changeMarkerWasReset;
  public boolean wasLaidOut;
  public boolean floater;
  public boolean consumableAreaChangedCalled;
  public boolean markedAsDirty;
  public MockEventHandler mockEventHandler;

  public MockPanel()
  {
    style = new ScreenableStyle();
    canBeBuffered = true;
    eventHandler = mockEventHandler = new MockEventHandler(this);
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

  public void doLayout()
  {
    super.doLayout();
    wasLaidOut = true;
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
