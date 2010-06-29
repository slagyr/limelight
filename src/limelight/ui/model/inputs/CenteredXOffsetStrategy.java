package limelight.ui.model.inputs;

import limelight.util.Box;

import java.awt.*;

public class CenteredXOffsetStrategy implements XOffsetStrategy
{
  public int calculateXOffset(TextModel model)
  {
    int xOffset = model.getXOffset();
    Box boundingBox = model.getPanel().getBoundingBox();
    Dimension textDimensions = model.getTextDimensions();
    int absoluteCaretX = model.getCaretX();
    int relativeCaretX = absoluteCaretX + xOffset;

    if(relativeCaretX >= boundingBox.width || relativeCaretX < 0)
    {
      xOffset = (absoluteCaretX - boundingBox.width / 2) * -1;
      int maxOffset = boundingBox.width - textDimensions.width - model.getCaretWidth();
      if(xOffset < maxOffset)
        xOffset = maxOffset;
      else if(xOffset > 0)
        xOffset = 0;

      relativeCaretX = absoluteCaretX + xOffset;
      if(relativeCaretX == boundingBox.width)
        xOffset -= model.getCaretWidth();
    }
    return xOffset;
  }
}
