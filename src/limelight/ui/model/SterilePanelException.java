//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

public class SterilePanelException extends Error
{
  SterilePanelException(String name)
  {
    super("The panel for prop '" + name + "' has been sterilized. Child components may not be added.");
  }
}
