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

    final boolean couldRecruitFromPScene = recruitFrom(panel, playerName, castingDirector, scenePlayersPath);
    if(!couldRecruitFromPScene)
    {
      final String productionPlayersPath = scene.getProduction().getResourceLoader().pathTo("players");
      final boolean couldRecruitFromProduction = recruitFrom(panel, playerName, castingDirector, productionPlayersPath);
      if(!couldRecruitFromProduction)
      {
        boolean couldRecruitDefaultPlayer = recruitFrom(panel, playerName, builtinCastingDirector, BuiltinBeacon.getBuiltinPlayersPath());
      }
    }
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
