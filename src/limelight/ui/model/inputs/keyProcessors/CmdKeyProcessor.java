//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.events.KeyEvent;
import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

public class CmdKeyProcessor extends KeyProcessor
{
  public static KeyProcessor instance = new CmdKeyProcessor();

  public void processKey(KeyEvent event, TextModel model)
  {
    switch (event.getKeyCode())
    {
      case KeyEvent.KEY_A:
        model.selectAll();
        break;
      case KeyEvent.KEY_V:
        model.pasteClipboard();
        break;
      case KeyEvent.KEY_RIGHT:
        model.sendCaretToEndOfLine();
        break;
      case KeyEvent.KEY_LEFT:
        model.sendCursorToStartOfLine();
        break;
      case KeyEvent.KEY_UP:
        model.setCaretIndex(0);
        break;
      case KeyEvent.KEY_DOWN:
        model.setCaretIndex(model.getText().length());
        break;


    }
  }

}
