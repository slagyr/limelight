package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

import java.awt.event.KeyEvent;

public class ExpandedSelectionOnShiftKeyProcessor extends KeyProcessor
{
  public ExpandedSelectionOnShiftKeyProcessor(TextModel modelInfo)
  {
    super(modelInfo);
  }

  @Override
  public void processKey(KeyEvent event)
  {
    KeyProcessor basicSelectionShiftProcessor = new SelectionOnShiftKeyProcessor(modelInfo);
    int keyCode = event.getKeyCode();

    if(isMoveUpEvent(keyCode))
      moveCursorUpALine();
    else if(isMoveDownEvent(keyCode))
      moveCursorDownALine();
    else
      basicSelectionShiftProcessor.processKey(event);
  }
}
