package limelight.ui.events.stage;

import limelight.events.Event;
import limelight.ui.model.Stage;

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
