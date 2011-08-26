//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.io;

import limelight.Context;

public class TempDirectory
{
  private final String root;

  public TempDirectory()
  {
    root = fs().join(System.getProperty("java.io.tmpdir"), "limelight");
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
    String path = fs().join(root, uniqueFilename());
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
    return fs().exists(fs().join(root, name));
  }

  public void cleanup()
  {
    fs().delete(root);
  }

  public String getDownloadsDirectory()
  {
    String downloadsDirectory = fs().join(root, "downloads");
    if(!fs().exists(downloadsDirectory))
      fs().createDirectory(downloadsDirectory);
    return downloadsDirectory;
  }
}
