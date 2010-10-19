package limelight.model.api;

import java.util.Map;

public interface ProductionProxy
{
  Object callMethod(String name, Object... args);
  CastingDirector getCastingDirector();
  TheaterProxy getTheater();
  void illuminate();
  void loadLibraries();
  void loadStages();
  SceneProxy loadScene(String scenePath, Map<String, Object> options);
  void loadStyles(SceneProxy scene);
}
