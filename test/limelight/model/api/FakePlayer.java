package limelight.model.api;

import limelight.ui.model.PropPanel;

public class FakePlayer implements Player
{
  public String name;
  public PropPanel castedProp;

  public FakePlayer(String name)
  {
    this.name = name;
  }

  public void cast(PropPanel prop)
  {
    castedProp = prop;
  }
}
