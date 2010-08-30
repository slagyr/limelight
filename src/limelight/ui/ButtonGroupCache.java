//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui;

import java.util.Hashtable;

public class ButtonGroupCache
{
  private final Hashtable<String, RadioButtonGroup> cache;

  public ButtonGroupCache()
  {
    cache = new Hashtable<String, RadioButtonGroup>();
  }

  public RadioButtonGroup get(String groupName)
  {
    if(!cache.containsKey(groupName))
      cache.put(groupName, new RadioButtonGroup());
    return cache.get(groupName);
  }

  public Hashtable<String, RadioButtonGroup> getCache()
  {
    return cache;
  }
}
