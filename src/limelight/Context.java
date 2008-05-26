//- Copyright 2008 8th Light, Inc.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight;

import limelight.io.TempDirectory;

public class Context
{
  private static Context instance;
  
  public TempDirectory tempDirectory;

  protected Context()
  {
  }

  public static Context instance()
  {
    if(instance == null)
      instance = new Context();
    return instance;
  }

  public static void removeInstance()
  {
    instance = null;
  }
}
