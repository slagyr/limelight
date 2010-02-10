package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

import java.awt.event.KeyEvent;

public class SelectionOnCmdKeyProcessor extends KeyProcessor
{
  public SelectionOnCmdKeyProcessor(TextModel boxInfo)
  {
    super(boxInfo);
  }

  public void processKey(KeyEvent event)
  {
    switch (event.getKeyCode())
    {
      case KeyEvent.VK_A:
        selectAll();
        break;
      case KeyEvent.VK_V:
        modelInfo.deleteSelection();
        modelInfo.pasteClipboard();
        modelInfo.selectionOn = false;
        break;
      case KeyEvent.VK_C:
        modelInfo.copySelection();
        break;
      case KeyEvent.VK_X:
        modelInfo.copySelection();
        modelInfo.deleteSelection();
        modelInfo.selectionOn = false;
        break;
      case KeyEvent.VK_RIGHT:
        sendCursorToEndOfLine();
        modelInfo.selectionOn = false;
        break;
      case KeyEvent.VK_LEFT:
        sendCursorToStartOfLine();
        modelInfo.selectionOn = false;
        break;
    }
  }

}
