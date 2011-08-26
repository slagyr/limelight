//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.model.api;

public interface CastingDirector
{
  boolean hasPlayer(String playerName, String playersPath);
  void castPlayer(PropProxy propProxy, String playerName, String playersPath);
}
