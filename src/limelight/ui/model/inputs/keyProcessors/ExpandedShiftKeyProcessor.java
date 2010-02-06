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

    if(isMoveUpEvent(keyCode))
    {
      initSelection();
      moveCursorUpALine();
    }
    else if(isMoveDownEvent(keyCode))
    {
      initSelection();
      moveCursorDownALine();
    }
    else
      basicShiftProcessor.processKey(event);
  }
}
