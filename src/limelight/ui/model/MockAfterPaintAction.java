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
