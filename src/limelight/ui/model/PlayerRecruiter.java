//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import limelight.builtin.BuiltinBeacon;
import limelight.java.JavaCastingDirector;
import limelight.model.api.CastingDirector;

public class PlayerRecruiter
{
  private CastingDirector builtinCastingDirector;

  public PlayerRecruiter()
  {
    builtinCastingDirector = new JavaCastingDirector(ClassLoader.getSystemClassLoader());
  }

  public void recruit(PropPanel panel, String playerName, CastingDirector castingDirector)
  {
    final Scene scene = panel.getRoot();
    final String scenePlayersPath = scene.getResourceLoader().pathTo("players");

    if(recruitFrom(panel, playerName, castingDirector, scenePlayersPath))
      return;

    if(scene.getProduction() != null)
    {
      final String productionPlayersPath = scene.getProduction().getResourceLoader().pathTo("players");
      if(recruitFrom(panel, playerName, castingDirector, productionPlayersPath))
        return;
    }

    recruitFrom(panel, playerName, builtinCastingDirector, BuiltinBeacon.getBuiltinPlayersPath());
  }

  private boolean recruitFrom(PropPanel panel, String playerName, CastingDirector castingDirector, String playersPath)
  {
    if(castingDirector.hasPlayer(playerName, playersPath))
    {
      castingDirector.castPlayer(panel.getProxy(), playerName, playersPath);
      return true;
    }
    return false;
  }

  public void setBuiltinCastingDirector(CastingDirector director)
  {
    this.builtinCastingDirector = director;
  }

  public CastingDirector getBuiltinCastingDirector()
  {
    return builtinCastingDirector;
  }
}
