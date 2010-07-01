//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

import java.awt.event.KeyEvent;

public class SelectionOnCmdKeyProcessor extends KeyProcessor
{
  public static KeyProcessor instance = new SelectionOnCmdKeyProcessor();

  public void processKey(KeyEvent event, TextModel boxInfo)
  {
    switch (event.getKeyCode())
    {
      case KeyEvent.VK_A:
        boxInfo.selectAll();
        break;
      case KeyEvent.VK_V:
        boxInfo.deleteSelection();
        boxInfo.pasteClipboard();
        boxInfo.setSelectionOn(false);
        break;
      case KeyEvent.VK_C:
        boxInfo.copySelection();
        break;
      case KeyEvent.VK_X:
        boxInfo.copySelection();
        boxInfo.deleteSelection();
        boxInfo.setSelectionOn(false);
        break;
      case KeyEvent.VK_RIGHT:
        boxInfo.sendCaretToEndOfLine();
        boxInfo.setSelectionOn(false);
        break;
      case KeyEvent.VK_LEFT:
        boxInfo.sendCursorToStartOfLine();
        boxInfo.setSelectionOn(false);
        break;
      case KeyEvent.VK_UP:
        boxInfo.setCaretIndex(0);
        boxInfo.setSelectionOn(false);
        break;
      case KeyEvent.VK_DOWN:
        boxInfo.setCaretIndex(boxInfo.getText().length());
        boxInfo.setSelectionOn(false);
        break;
    }
  }

}
