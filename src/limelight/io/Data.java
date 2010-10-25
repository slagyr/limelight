package limelight.io;

import limelight.Context;
import limelight.LimelightException;

public class Data
{
  private static String root;
  private static String downloadsDir;
  private static String productionsDir;

  public static void reset()
  {
    root = null;
    downloadsDir = null;
    productionsDir = null;
  }

  public static String getRoot()
  {
    if(root == null)
      root = Context.instance().os.dataRoot();
    return root;
  }

  public static String downloadsDir()
  {
    if(downloadsDir == null)
      downloadsDir = FileUtil.join(getRoot(), "Downloads");
    return downloadsDir;
  }

  public static String productionsDir()
  {
    if(productionsDir == null)
      productionsDir = FileUtil.join(getRoot(), "Productions");
    return productionsDir;
  }

  public static void establishDirs()
  {
    final FileSystem fs = Context.fs();
    if(!fs.exists(downloadsDir()))
      fs.createDirectory(downloadsDir());
    if(!fs.exists(productionsDir()))
      fs.createDirectory(productionsDir());
  }
}
