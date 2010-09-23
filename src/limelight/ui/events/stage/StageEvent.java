package limelight.ui.events.stage;

import limelight.events.Event;
import limelight.ui.model.PropFrame;

public abstract class StageEvent extends Event
{
  private PropFrame stage;

  public PropFrame getStage()
  {
    return stage;
  }

  public void setStage(PropFrame stage)
  {
    subject = stage;
    this.stage = stage;
  }

  public void dispatch(PropFrame stage)
  {
    setStage(stage);
    stage.getEventHandler().dispatch(this);
  }
}
