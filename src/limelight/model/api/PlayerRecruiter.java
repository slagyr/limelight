//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.model.api;

public interface PlayerRecruiter
{
  boolean canRecruit(String playerName, String playersPath);
  Player recruitPlayer(PropProxy propProxy, String playerName, String playersPath);
}
