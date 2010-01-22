package limelight.ui.model.inputs.painters;

import limelight.ui.model.inputs.TextModel;
import limelight.ui.model.inputs.TextPanelPainter;

import java.awt.*;

public class TextPanelBoxPainter extends TextPanelPainter
{
  public TextPanelBoxPainter(TextModel boxInfo)
  {
    super(boxInfo);
  }

  @Override
  public void paint(Graphics2D graphics)
  {
    graphics.setColor(Color.lightGray);
    graphics.fillRect(0, 0, boxInfo.getPanelWidth(), boxInfo.getPanelHeight());

    if(boxInfo.isFocused())
      graphics.setColor(Color.green);
    else
      graphics.setColor(Color.gray);
    graphics.drawRect(0, 0,  boxInfo.getPanelWidth() - 1, boxInfo.getPanelHeight() - 1);
  }
}
