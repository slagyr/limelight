package limelight.ui.model;

import limelight.styles.ScreenableStyle;
import limelight.ui.EventAction;
import limelight.util.Box;

import java.awt.*;

public class TestableBasePanel extends BasePanel
{
  public final ScreenableStyle style = new ScreenableStyle();

  public Box getChildConsumableArea()
  {
    return null;
  }

  public Box getBoxInsidePadding()
  {
    return null;
  }

  public void paintOn(Graphics2D graphics)
  {
  }

  public ScreenableStyle getStyle()
  {
    return style;
  }
}
