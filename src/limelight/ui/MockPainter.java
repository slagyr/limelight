package limelight.ui;

import limelight.ui.Painter;

import java.awt.*;

public class MockPainter extends Painter
{
  public boolean painted;

  public void paint(Graphics2D graphics)
  {
    painted = true;
  }
}
