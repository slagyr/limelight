package limelight.ui.model.inputs.painters;

import limelight.ui.model.inputs.TextModel;
import limelight.ui.model.inputs.TextPanelPainter;
import limelight.util.Box;

import java.awt.*;

public class TextPanelSelectionPainter extends TextPanelPainter
{
  public TextPanelSelectionPainter(TextModel boxInfo)
  {
    super(boxInfo);
  }

  @Override
  public void paint(Graphics2D graphics)
  {
    if (boxInfo.text != null && boxInfo.text.length() > 0)
    {
      graphics.setColor(Color.cyan);
      Rectangle rect = boxInfo.getSelectedRegion();
      if(rect != null)
        graphics.fillRect(rect.x,rect.y,rect.width,rect.height);
    }
  }
}
