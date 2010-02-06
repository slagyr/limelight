package limelight.ui.model.inputs.painters;

import limelight.ui.model.inputs.TextModel;
import limelight.ui.model.inputs.TextPanelPainter;

import java.awt.*;
import java.util.ArrayList;

public class TextPanelSelectionPainter extends TextPanelPainter
{
  public TextPanelSelectionPainter(TextModel boxInfo)
  {
    super(boxInfo);
  }

  public void paint(Graphics2D graphics)
  {
    if (!boxInfo.selectionOn)
      return;
    if (boxInfo.getText() != null && boxInfo.getText().length() > 0)
    {
      graphics.setColor(Color.cyan);
      ArrayList<Rectangle> regions = boxInfo.getSelectionRegions();
      if (regions != null)
      {
        for (Rectangle rect : regions)
          graphics.fillRect(rect.x, rect.y, rect.width, rect.height);
      }
    }
  }
}
