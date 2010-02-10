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
        modelInfo.selectAll();
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
        modelInfo.sendCursorToEndOfLine();
        modelInfo.selectionOn = false;
        break;
      case KeyEvent.VK_LEFT:
        modelInfo.sendCursorToStartOfLine();
        modelInfo.selectionOn = false;
        break;
      case KeyEvent.VK_UP:
        modelInfo.setCursorIndex(0);
        modelInfo.selectionOn = false;
        break;
      case KeyEvent.VK_DOWN:
        modelInfo.setCursorIndex(modelInfo.getText().length());
        modelInfo.selectionOn = false;
        break;
    }
  }

}
