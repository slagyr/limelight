package limelight.model.api;

import limelight.Context;
import limelight.ui.model.PropPanel;

public class FakePlayer implements Player
{
  public String path;
  public PropPanel castedProp;

  public FakePlayer(String path)
  {
    this.path = path;
  }

  public void cast(PropPanel prop)
  {
    castedProp = prop;
  }

  public String getPath()
  {
    return path;
  }

  public String getName()
  {
    return Context.fs().filename(path);
  }
}
