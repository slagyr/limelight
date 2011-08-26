//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.events.stage;

import limelight.events.Event;
import limelight.model.Stage;

public abstract class StageEvent extends Event
{
  private Stage stage;

  public Stage getStage()
  {
    return stage;
  }

  public void setStage(Stage stage)
  {
    subject = stage;
    this.stage = stage;
  }

  public void dispatch(Stage stage)
  {
    setStage(stage);
    stage.getEventHandler().dispatch(this);
  }
}
