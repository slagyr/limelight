package limelight.ui.painting;

import limelight.ui.PaintablePanel;
import limelight.ui.Painter;

import java.awt.*;

public class DefaultPainter implements Painter
{
  public static Painter instance = new DefaultPainter();

  private DefaultPainter()
  {
  }

  public void paint(Graphics2D graphics, PaintablePanel panel)
  {
    BackgroundPainter.instance.paint(graphics, panel);
    BorderPainter.instance.paint(graphics, panel);
  }
}
