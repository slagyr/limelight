//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.model.api;

//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;

public class FakePlayerRecruiter implements PlayerRecruiter
{
//  public Map<PropProxy, List<String>> recruits = new HashMap<PropProxy, List<String>>();
  public String recruitablePath;

  public boolean canRecruit(String playerName, String playersPath)
  {
    return recruitablePath == null || playersPath.equals(recruitablePath);
  }

  public Player recruitPlayer(String playerName, String playersPath)
  {
//    if(!recruits.containsKey(propProxy))
//      recruits.put(propProxy, new LinkedList<String>());
//
//    recruits.get(propProxy).add(playersPath + "/" + playerName);

    return new FakePlayer(playersPath + "/" + playerName);
  }
}
