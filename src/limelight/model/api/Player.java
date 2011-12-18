package limelight.model.api;

import limelight.ui.model.PropPanel;

public interface Player
{
  void cast(PropPanel prop);
  String getPath();
  String getName();
}
