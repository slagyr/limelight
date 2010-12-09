package limelight.java;

import limelight.model.Stage;
import limelight.model.api.StageProxy;
import limelight.ui.model.FramedStage;
import limelight.util.OptionsMap;

import java.util.Map;

public class JavaStage implements StageProxy
{
  private FramedStage peer;

  JavaStage(String name, OptionsMap options)
  {
    peer = new FramedStage(name, this);
    peer.applyOptions(options);
  }

  public Stage getPeer()
  {
    return peer;
  }

  public void applyOptions(Map<String, Object> options)
  {
  }

}
