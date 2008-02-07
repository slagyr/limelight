package limelight.ui;

import java.awt.*;

public class MockParentPanel extends ParentPanel
{
  public Rectangle getChildConsumableArea()
  {
    return new Rectangle(0, 0, getWidth(), getHeight());
  }

  public void snapToSize()
  {
  }

  public void paintOn(Graphics2D graphics)
  {
  }
}
