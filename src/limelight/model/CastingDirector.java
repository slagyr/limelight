//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.model;

import limelight.Context;
import limelight.builtin.BuiltinBeacon;
import limelight.java.JavaPlayerRecruiter;
import limelight.model.api.Player;
import limelight.model.api.PlayerRecruiter;
import limelight.ui.model.PropPanel;
import limelight.ui.model.Scene;
import limelight.util.StringUtil;
import sun.swing.StringUIClientPropertyKey;

public class CastingDirector
{
  private PlayerRecruiter builtinPlayerRecruiter;

  public static CastingDirector installed()
  {
    CastingDirector playerRecruiter = new CastingDirector();
    Context.instance().castingDirector = playerRecruiter;
    return playerRecruiter;
  }

  public CastingDirector()
  {
    builtinPlayerRecruiter = new JavaPlayerRecruiter(ClassLoader.getSystemClassLoader());
  }

  public Player castRole(PropPanel panel, String playerName, PlayerRecruiter playerRecruiter)
  {
    Player result;

    final Scene scene = panel.getRoot();
    final String scenePlayersPath = Context.fs().pathTo(scene.getPath(), "players");
    result = castFrom(panel, playerName, playerRecruiter, scenePlayersPath);

    if(result == null && scene.getProduction() != null)
    {
      final String productionPlayersPath = Context.fs().pathTo(scene.getProduction().getPath(), "players");
      result = castFrom(panel, playerName, playerRecruiter, productionPlayersPath);
    }

    if(result == null)
      result = castFrom(panel, playerName, builtinPlayerRecruiter, BuiltinBeacon.getBuiltinPlayersPath());

    return result;
  }

  private Player castFrom(PropPanel panel, String playerName, PlayerRecruiter playerRecruiter, String playersPath)
  {
    String normalizedName = StringUtil.spearCase(playerName);
    if(playerRecruiter.canRecruit(normalizedName, playersPath))
    {
      final Player player = playerRecruiter.recruitPlayer(normalizedName, playersPath);
      player.cast(panel);
      panel.addPlayer(player);
      return player;
    }
    return null;
  }

  public void setBuiltinPlayerRecruiter(limelight.model.api.PlayerRecruiter director)
  {
    this.builtinPlayerRecruiter = director;
  }

  public limelight.model.api.PlayerRecruiter getBuiltinPlayerRecruiter()
  {
    return builtinPlayerRecruiter;
  }
}
