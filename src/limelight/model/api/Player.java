package limelight.model.api;

import limelight.ui.model.PropPanel;
import java.util.Map;

public interface Player
{
  Object cast(PropPanel prop);
  String getPath();
  String getName();
  Map<String, Object> applyOptions(PropPanel prop, Map<String, Object> options);
}
