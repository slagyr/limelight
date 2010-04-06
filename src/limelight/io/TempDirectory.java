//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.io;

import java.io.File;

public class TempDirectory
{
  private final File root;

  public TempDirectory()
  {
    String path = FileUtil.pathTo(System.getProperty("java.io.tmpdir"), "limelight");
    root = new File(path);
    if(!root.exists())
      root.mkdirs();
  }

  public File getRoot()
  {
    return root;
  }

  public File createNewDirectory()
  {
    File directory = new File(root, uniqueFilename());  
    directory.mkdir();
    return directory;
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
    return new File(root, name).exists();
  }

  public void cleanup()
  {
    FileUtil.deleteFileSystemDirectory(root);
  }

  public File getDownloadsDirectory()
  {
    File downloadsDirectory = new File(root, "downloads");
    if(!downloadsDirectory.exists())
      downloadsDirectory.mkdir();
    return downloadsDirectory;
  }
}
