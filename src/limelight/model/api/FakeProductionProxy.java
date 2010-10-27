package limelight.model.api;

import java.util.Map;

public class FakeProductionProxy implements ProductionProxy
{
  public CastingDirector castingDirector;
  public TheaterProxy theater;

  public Object callMethod(String name, Object... args)
  {
    return null;
  }

  public CastingDirector getCastingDirector()
  {
    return castingDirector;
  }

  public TheaterProxy getTheater()
  {
    return theater;
  }

  public void illuminate()
  {
  }

  public void loadLibraries()
  {
  }

  public void loadStages()
  {
  }

  public SceneProxy loadScene(String scenePath, Map<String, Object> options)
  {
    return null;
  }

  public void loadStyles(SceneProxy scene)
  {
  }
}
