//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.model.api;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FakeCastingDirector implements CastingDirector
{
  public Map<PropProxy, List<String>> castings = new HashMap<PropProxy, List<String>>();

  public void castPlayer(PropProxy propProxy, String playerName)
  {
    if(!castings.containsKey(propProxy))
      castings.put(propProxy, new LinkedList<String>());
    
    castings.get(propProxy).add(playerName);
  }
}
