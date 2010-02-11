package limelight.ui.model.inputs.painters;

import limelight.ui.TypedLayout;
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
    if (boxInfo.getText() != null && boxInfo.getText().length() > 0)
    {
      textDimensions = boxInfo.calculateTextDimensions();
      Box box = new Box(TextModel.SIDE_TEXT_MARGIN,0,boxInfo.getPanelWidth() - TextModel.SIDE_TEXT_MARGIN,boxInfo.getPanelHeight());

      boxInfo.calculateTextXOffset(boxInfo.getPanelWidth(), textDimensions.width);
      boxInfo.getYPosFromIndex(boxInfo.getCursorIndex());

      int textX = boxInfo.getHorizontalAlignment().getX(textDimensions.width,box ) - boxInfo.getXOffset();

      float textY = boxInfo.getVerticalAlignment().getY(textDimensions.height, box) - boxInfo.yOffset;


      graphics.setColor(Color.black);
      for (TypedLayout layout : boxInfo.getTextLayouts()){
        textY += layout.getAscent();
        layout.draw(graphics, textX, textY + 1);
        textY += layout.getDescent() + layout.getLeading();
      }
    }
  }
}
