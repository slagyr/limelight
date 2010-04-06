//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.LimelightError;

public class SterilePanelException extends LimelightError
{
  SterilePanelException(String name)
  {
    super("The panel for prop named '" + name + "' has been sterilized. Child components may not be added.");
  }
}
