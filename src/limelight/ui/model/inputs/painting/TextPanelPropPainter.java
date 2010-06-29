package limelight.ui.model.inputs.painting;

import limelight.ui.PaintablePanel;
import limelight.ui.Painter;
import limelight.ui.painting.BackgroundPainter;

import java.awt.*;

public class TextPanelPropPainter implements Painter
{
  public static Painter instance = new TextPanelPropPainter();
  
  private TextPanelPropPainter()
  {
  }

  public void paint(Graphics2D graphics, PaintablePanel panel)
  {
    BackgroundPainter.instance.paint(graphics, panel);
    TextPanelBorderPainter.instance.paint(graphics, panel);
  }
}
