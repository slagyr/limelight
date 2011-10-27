//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.model.api;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FakeCastingDirector implements CastingDirector
{
  public Map<PropProxy, List<String>> castings = new HashMap<PropProxy, List<String>>();
  public String recruitablePath;

  public boolean hasPlayer(String playerName, String playersPath)
  {
    return recruitablePath == null || playersPath.equals(recruitablePath);
  }

  public void castPlayer(PropProxy propProxy, String playerName, String playersPath)
  {
    if(!castings.containsKey(propProxy))
      castings.put(propProxy, new LinkedList<String>());

    castings.get(propProxy).add(playersPath + "/" + playerName);
  }
}
