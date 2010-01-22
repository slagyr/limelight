package limelight.ui.model.inputs.painters;

import limelight.ui.model.inputs.TextModel;
import limelight.ui.model.inputs.TextPanelPainter;
import limelight.util.Box;

import java.awt.*;

public class TextPanelTextPainter extends TextPanelPainter
{
  public TextPanelTextPainter(TextModel boxInfo)
  {
    super(boxInfo);
  }

  @Override
  public void paint(Graphics2D graphics)
  {
    Dimension textDimensions;
    if (boxInfo.text != null && boxInfo.text.length() > 0)
    {
      textDimensions = boxInfo.calculateTextDimensions();
      Box box = new Box(0,0,boxInfo.getPanelWidth(),boxInfo.getPanelHeight());

      boxInfo.calculateTextXOffset(boxInfo.getPanelWidth(), textDimensions.width);

      int textX = boxInfo.getHorizontalAlignment().getX(textDimensions.width,box ) + TextModel.LEFT_TEXT_MARGIN - boxInfo.getXOffset();

      float textY = boxInfo.getVerticalAlignment().getY(textDimensions.height, box) + boxInfo.getTextLayout().getAscent();


      graphics.setColor(Color.black);
      boxInfo.getTextLayout().draw(graphics, textX, textY + 1);
    }
  }
}
