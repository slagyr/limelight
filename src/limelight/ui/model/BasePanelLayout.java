//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import limelight.ui.Panel;

import java.util.Map;

// TODO MDM - This class does nothing. Delete me!
public class BasePanelLayout extends SimpleLayout
{
  public static BasePanelLayout instance = new BasePanelLayout();
  public Panel lastPanelProcessed;

  public boolean overides(Layout other)
  {
    return false;
  }

}
