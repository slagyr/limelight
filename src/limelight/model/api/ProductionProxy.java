//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.model.api;

import limelight.styles.RichStyle;

import java.util.Map;

public interface ProductionProxy
{
  //TODO MDM get rid of me
  Object send(String name, Object... args);

  TheaterProxy getTheater();
  void illuminate();
  void loadLibraries();
  void loadStages();
  SceneProxy loadScene(String scenePath, Map<String, Object> options);
  Map<String, RichStyle> loadStyles(String scene, Map<String, RichStyle> extendableStyles);
}
