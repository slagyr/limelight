package limelight.ui.model.inputs;

import limelight.util.Box;

public class FittingYOffsetStrategy implements YOffsetStrategy
{
  public int calculateYOffset(TextModel model)
  {
    int yOffset = model.getYOffset();
    Box boundingBox = model.getPanel().getBoundingBox();
    int absoluteCaretY = model.getCaretY();
    int relativeCaretY = absoluteCaretY + yOffset;

    if(relativeCaretY >= boundingBox.height)
      yOffset = (absoluteCaretY - boundingBox.height - model.getActiveLayout().getHeight()) * -1;
    else if(relativeCaretY < 0)
      yOffset = absoluteCaretY * -1;
    
    return yOffset;
  }
}
