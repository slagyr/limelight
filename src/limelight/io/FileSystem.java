package limelight.io;

import limelight.LimelightException;

import java.io.File;

public class FileSystem
{
  public void createDirectory(String path)
  {
    FileUtil.establishDirectory(path);
  }

  public boolean exists(String path)
  {
    return new File(path).exists();
  }

  public boolean isDirectory(String path)
  {
    return new File(path).isDirectory();
  }

  public void createFile(String path, String content)
  {
    FileUtil.establishFile(path, content);
  }

  public String readTextFile(String path)
  {
    return FileUtil.getFileContent(path);
  }
}
