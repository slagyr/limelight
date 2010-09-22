package limelight.ui.api;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MockCastingDirector implements CastingDirector
{
  public Map<Prop, List<String>> castings = new HashMap<Prop, List<String>>();

  public void castPlayer(Prop prop, String playerName)
  {
    if(!castings.containsKey(prop))
      castings.put(prop, new LinkedList<String>());
    
    castings.get(prop).add(playerName);
  }
}
