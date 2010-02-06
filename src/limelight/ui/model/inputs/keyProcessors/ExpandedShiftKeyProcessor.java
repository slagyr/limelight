package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

import java.awt.event.KeyEvent;

public class ExpandedShiftKeyProcessor extends KeyProcessor
{
  public ExpandedShiftKeyProcessor(TextModel modelInfo)
  {
    super(modelInfo);
  }

  @Override
  public void processKey(KeyEvent event)
  {
    KeyProcessor basicShiftProcessor = new ShiftKeyProcessor(modelInfo);
    int keyCode = event.getKeyCode();

    if(modelInfo.isMoveUpEvent(keyCode))
    {
      modelInfo.initSelection();
      modelInfo.moveCursorUpALine();
    }
    else if(modelInfo.isMoveDownEvent(keyCode))
    {
      modelInfo.initSelection();
      modelInfo.moveCursorDownALine();

    }
    else
      basicShiftProcessor.processKey(event);
  }
}
