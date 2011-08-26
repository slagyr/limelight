//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.util;

import limelight.Context;
import limelight.io.FileSystem;

public class ResourceLoader
{
  private String root;
  private FileSystem fs;

  public static ResourceLoader forRoot(String rootPath)
  {
    ResourceLoader loader = new ResourceLoader();
    loader.resetOnRoot(rootPath);
    return loader;
  }

  protected ResourceLoader()
  {
    fs = Context.fs();
  }

  public void resetOnRoot(String rootPath)
  {
    root = rootPath;
  }

  public String getRoot()
  {
    return root;
  }

  public String pathTo(String path)
  {
    return fs.absolutePath(pathFor(path));
  }

  private String pathFor(String path)
  {
    if(path.equals(fs.absolutePath(path)))
      return path;
    else
      return fs.join(root, path);
  }

  public boolean exists(String path)
  {
    return fs.exists(pathFor(path));
  }

  public String readText(String path)
  {
    return fs.readTextFile(pathFor(path));
  }
}
