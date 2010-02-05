package limelight.ui.model.inputs.painters;

import limelight.ui.model.inputs.TextModel;
import limelight.ui.model.inputs.TextPanelPainter;

import java.awt.*;

public class TextPanelBackgroundPainter extends TextPanelPainter
{
  public TextPanelBackgroundPainter(TextModel boxInfo)
  {
    super(boxInfo);
  }

  @Override
  public void paint(Graphics2D graphics)
  {
    graphics.setColor(Color.white);
    Rectangle box = boxInfo.getPaintableRegion().getBounds();
    graphics.fillRect(box.x, box.y,box.width , box.height);

    if(boxInfo.isFocused())
      graphics.setColor(Color.green);
    else
      graphics.setColor(Color.gray);
    graphics.drawRect(0, 0,  boxInfo.getPanelWidth() - 1, boxInfo.getPanelHeight() - 1);
  }
}
