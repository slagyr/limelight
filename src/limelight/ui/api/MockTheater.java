//- Copyright 2008 8th Light, Inc.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.api;

import limelight.ui.api.Theater;
import limelight.ui.api.Stage;

public class MockTheater implements Theater
{
  public Stage activatedStage;

  public void stage_activated(Stage stage)
  {
    activatedStage = stage;
  }
}
