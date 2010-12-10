//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.io;

import limelight.Context;

public class TempDirectory
{
  private final String root;

  public TempDirectory()
  {
    root = FileUtil.pathTo(System.getProperty("java.io.tmpdir"), "limelight");
    if(!fs().exists(root))
      fs().createDirectory(root);
  }

  private FileSystem fs()
  {
    return Context.fs();
  }

  public String getRoot()
  {
    return root;
  }

  public String createNewDirectory()
  {
    String path = FileUtil.join(root, uniqueFilename());
    fs().createDirectory(path);
    return path;
  }

  private String uniqueFilename()
  {
    String timestamp = System.currentTimeMillis() + "";
    String name = timestamp;
    for(int i = 2; nameTaken(name); i++)
      name = timestamp + "_" + i;
    return name;
  }

  private boolean nameTaken(String name)
  {
    return fs().exists(FileUtil.join(root, name));
  }

  public void cleanup()
  {
    fs().delete(root);
  }

  public String getDownloadsDirectory()
  {
    String downloadsDirectory = FileUtil.join(root, "downloads");
    if(!fs().exists(downloadsDirectory))
      fs().createDirectory(downloadsDirectory);
    return downloadsDirectory;
  }
}
