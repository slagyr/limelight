//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

import java.awt.event.KeyEvent;

public class SelectionOnAltCmdKeyProcessor extends KeyProcessor
{
  public static KeyProcessor instance = new SelectionOnAltCmdKeyProcessor();

  public void processKey(KeyEvent event, TextModel boxInfo)
  {
  }
}
