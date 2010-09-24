package limelight.ui.api;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MockCastingDirector implements CastingDirector
{
  public Map<PropProxy, List<String>> castings = new HashMap<PropProxy, List<String>>();

  public void castPlayer(PropProxy propProxy, String playerName)
  {
    if(!castings.containsKey(propProxy))
      castings.put(propProxy, new LinkedList<String>());
    
    castings.get(propProxy).add(playerName);
  }
}
