package limelight.ui.model;

import limelight.styles.ScreenableStyle;
import limelight.util.Box;

import java.awt.*;

public class MockParentPanel extends ParentPanelBase
{
  public static int paintCount = 0;

  public ScreenableStyle style;
  public boolean canBeBuffered;
  public boolean wasPainted;
  public boolean floater;
  public int paintIndex;
  public boolean consumableAreaChangedCalled;
  public MockEventHandler mockEventHandler;

  public MockParentPanel()
  {
    style = new ScreenableStyle();
    eventHandler = mockEventHandler = new MockEventHandler(this);
  }

  @Override
  public Box getChildConsumableBounds()
  {
    return getBounds();
  }

  public void paintOn(Graphics2D graphics)
  {
    wasPainted = true;
    paintIndex = paintCount++;
  }

  public ScreenableStyle getStyle()
  {
    return style;
  }

  @Override
  public boolean canBeBuffered()
  {
    return canBeBuffered;
  }

  @Override
  public boolean isFloater()
  {
    return floater;
  }

  @Override
  public void consumableAreaChanged()
  {
    consumableAreaChangedCalled = true;
  }
}
