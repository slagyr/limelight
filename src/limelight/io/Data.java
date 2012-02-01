//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.io;

import limelight.Context;
import limelight.LimelightException;

public class Data
{
  private static String root;
  private static String downloadsDir;
  private static String productionsDir;
  private static String logFile;

  public static void reset()
  {
    root = null;
    downloadsDir = null;
    productionsDir = null;
    logFile = null;
  }

  public static String getRoot()
  {
    if(root == null)
    {
      root = Context.instance().os.dataRoot();
      if(!Context.fs().exists(root))
        Context.fs().createDirectory(root);
    }
    return root;
  }

  public static String downloadsDir()
  {
    if(downloadsDir == null)
    {
      downloadsDir = Context.fs().join(getRoot(), "Downloads");
      if(!Context.fs().exists(downloadsDir))
        Context.fs().createDirectory(downloadsDir);
    }
    return downloadsDir;
  }

  public static String productionsDir()
  {
    if(productionsDir == null)
    {
      productionsDir = Context.fs().join(getRoot(), "Productions");
      if(!Context.fs().exists(productionsDir))
        Context.fs().createDirectory(productionsDir);
    }
    return productionsDir;
  }

  public static String logFile()
  {
    if(logFile == null)
      logFile = Context.fs().join(getRoot(), "log.txt");
    return logFile;
  }
}
