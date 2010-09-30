package limelight.io;

import limelight.Context;
import limelight.LimelightException;

import java.io.File;

public class Data
{
  private static File root;
  private static File downloadsDir;
  private static File productionsDir;

  public static void reset()
  {
    root = null;
    downloadsDir = null;
    productionsDir = null;
  }

  public static File getRoot()
  {
    if(root == null)
      root = new File(Context.instance().os.dataRoot());
    return root;
  }

  public static File downloadsDir()
  {
    if(downloadsDir == null)
      downloadsDir = new File(getRoot(), "Downloads");
    return downloadsDir;
  }

  public static File productionsDir()
  {
    if(productionsDir == null)
      productionsDir = new File(getRoot(), "Productions");
    return productionsDir;
  }

  public static void establishDirs()
  {
    boolean okay = downloadsDir().exists() || downloadsDir().mkdirs();
    okay &= productionsDir().exists() || productionsDir().mkdirs();
    if(!okay)
      throw new LimelightException("Data dirs do not exist and cannot be created.");
  }
}
