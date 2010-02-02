package limelight.ui.model.inputs.painters;

import limelight.ui.TypedLayout;
import limelight.ui.model.inputs.TextModel;
import limelight.ui.model.inputs.TextPanelPainter;
import limelight.util.Box;

import java.awt.*;
import java.util.ArrayList;

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
      ArrayList<TypedLayout> layouts = boxInfo.getTextLayouts();
      Box box = new Box(TextModel.LEFT_TEXT_MARGIN,0,boxInfo.getPanelWidth() - TextModel.LEFT_TEXT_MARGIN,boxInfo.getPanelHeight());

      boxInfo.calculateTextXOffset(boxInfo.getPanelWidth(), textDimensions.width);

      int textX = boxInfo.getHorizontalAlignment().getX(textDimensions.width,box ) - boxInfo.getXOffset();

      float textY = boxInfo.getVerticalAlignment().getY(textDimensions.height, box) + layouts.get(0).getAscent(); //Todo JMM get max ascent


      graphics.setColor(Color.black);
      for (int i = 0; i < layouts.size(); i++){
        layouts.get(i).draw(graphics, textX, textY + 1);
      }
    }
  }
}
