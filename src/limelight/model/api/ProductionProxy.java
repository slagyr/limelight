//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.model.api;

import java.util.Map;

public interface ProductionProxy
{
  //TODO MDM get rid of me
  Object callMethod(String name, Object... args);

  TheaterProxy getTheater();
  void illuminate();
  void loadLibraries();
  void loadStages();
  SceneProxy loadScene(String scenePath, Map<String, Object> options);
  void loadStyles(SceneProxy scene);
}
