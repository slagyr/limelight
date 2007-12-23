package limelight;

import java.awt.*;

public class MockPainter extends Painter
{
  public boolean painted;

  public void paint(Graphics2D graphics)
  {
    painted = true;
  }
}
