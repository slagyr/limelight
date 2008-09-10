//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui;

import javax.swing.*;
import java.util.Hashtable;

public class ButtonGroupCache
{
  private Hashtable<String, ButtonGroup> cache;

  public ButtonGroupCache()
  {
    cache = new Hashtable<String, ButtonGroup>();
  }

  public ButtonGroup get(String groupName)
  {
    if(!cache.containsKey(groupName))
      cache.put(groupName, new ButtonGroup());
     return cache.get(groupName);
  }

  public Hashtable<String, ButtonGroup> getCache()
  {
    return cache;
  }
}
