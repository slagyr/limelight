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
