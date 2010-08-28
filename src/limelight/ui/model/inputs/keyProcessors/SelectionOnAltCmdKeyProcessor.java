//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.events.KeyEvent;
import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

// TODO MDM Get rid of all the SelectionOn processors.  They are more weight than their worth.
// The Non-SelectionOn processors will have to check if selection is on.
// Maybe.  At least look into it.
public class SelectionOnAltCmdKeyProcessor extends KeyProcessor
{
  public static KeyProcessor instance = new SelectionOnAltCmdKeyProcessor();

  public void processKey(KeyEvent event, TextModel model)
  {
  }
}
