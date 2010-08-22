//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui;

import limelight.styles.ScreenableStyle;
import limelight.ui.model.BasePanel;
import limelight.util.Box;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

public class MockPanel extends BasePanel
{
  public final ScreenableStyle style;
  public static int paintCount;
  public int paintIndex;
  public boolean wasPainted;
  public boolean canBeBuffered;
  public boolean changeMarkerWasReset;
  public boolean wasLaidOut;
  public boolean floater;
  public boolean consumableAreaChangedCalled;
  public boolean markedAsDirty;
  public boolean hasFocus;
  public KeyEvent typedKeyEvent;
  public KeyEvent pressedKeyEvent;
  public KeyEvent releasedKeyEvent;


  public MockPanel()
  {
    style = new ScreenableStyle();
    canBeBuffered = true;
  }

  public Box getChildConsumableArea()
  {
    return new Box(0, 0, getWidth(), getHeight());
  }

  public Box getBoxInsidePadding()
  {
    return getChildConsumableArea();
  }

  public ScreenableStyle getStyle()
  {
    return style;
  }

  public void paintOn(Graphics2D graphics)
  {
    wasPainted = true;
    paintIndex = paintCount++;
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

  @Override
  public void focusLost(FocusEvent e)
  {
    hasFocus = false;
  }

  @Override
  public void focusGained(FocusEvent e)
  {
    hasFocus = true;
  }

  @Override
  public void keyTyped(KeyEvent e)
  {
    typedKeyEvent = e;
  }

  @Override
  public void keyPressed(KeyEvent e)
  {
    pressedKeyEvent = e;
  }

  @Override
  public void keyReleased(KeyEvent e)
  {
    releasedKeyEvent = e;
  }
}
