package limelight.model.api;

import limelight.ui.model.PropPanel;

public interface Player
{
  Object cast(PropPanel prop);
  String getPath();
  String getName();
}
