//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.model;

import limelight.Context;
import limelight.builtin.BuiltinBeacon;
import limelight.java.JavaCastingDirector;
import limelight.model.api.CastingDirector;
import limelight.ui.model.PropPanel;
import limelight.ui.model.Scene;

public class PlayerRecruiter
{
  private CastingDirector builtinCastingDirector;

  public static PlayerRecruiter installed()
  {
    PlayerRecruiter playerRecruiter = new PlayerRecruiter();
    Context.instance().playerRecruiter = playerRecruiter;
    return playerRecruiter;
  }

  public PlayerRecruiter()
  {
    builtinCastingDirector = new JavaCastingDirector(ClassLoader.getSystemClassLoader());
  }

  public String recruit(PropPanel panel, String playerName, CastingDirector castingDirector)
  {
    final Scene scene = panel.getRoot();
    final String scenePlayersPath = Context.fs().pathTo(scene.getPath(), "players");

    if(recruitFrom(panel, playerName, castingDirector, scenePlayersPath))
      return scene.getAbsoluteName() + "/" + playerName;

    if(scene.getProduction() != null)
    {
      final String productionPlayersPath = Context.fs().pathTo(scene.getProduction().getPath(), "players");
      if(recruitFrom(panel, playerName, castingDirector, productionPlayersPath))
        return productionPlayersPath;
    }

    if(recruitFrom(panel, playerName, builtinCastingDirector, BuiltinBeacon.getBuiltinPlayersPath()))
      return BuiltinBeacon.getBuiltinPlayersPath();

    return null;
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
