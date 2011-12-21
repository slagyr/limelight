package limelight.model.api;

import limelight.Context;
import limelight.ui.model.PropPanel;
import limelight.util.Opts;

import java.util.Map;

public class FakePlayer implements Player
{
  public String path;
  public PropPanel castedProp;
  public Map<String, Object> appliedOptions;

  public FakePlayer(String path)
  {
    this.path = path;
  }

  public Object cast(PropPanel prop)
  {
    castedProp = prop;
    return "Fake Casted Player Object";
  }

  public String getPath()
  {
    return path;
  }

  public String getName()
  {
    return Context.fs().filename(path);
  }

  public Map<String, Object> applyOptions(PropPanel prop, Map<String, Object> options)
  {
    appliedOptions = new Opts(options);
    return options;
  }
}
