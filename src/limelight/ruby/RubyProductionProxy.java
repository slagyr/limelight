package limelight.ruby;

import limelight.model.api.ProductionProxy;
import limelight.model.api.SceneProxy;

import java.util.Map;

public interface RubyProductionProxy extends ProductionProxy
{
  void illuminate();
  void loadLibraries();
  void loadStages();
  SceneProxy loadScene(String scenePath, Map<String, Object> options);
  void loadStyles(SceneProxy scene);
}
