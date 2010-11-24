//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.io;

import limelight.LimelightException;

import java.io.*;

public class FileSystem
{
  public void createDirectory(String path)
  {
    File dir = new File(path);
    if(!dir.exists() && !dir.mkdirs())
      throw new LimelightException("Can't establish directory: " + path);
  }

  public boolean exists(String path)
  {
    return new File(path).exists();
  }

  public boolean isDirectory(String path)
  {
    return new File(path).isDirectory();
  }

  public void createTextFile(String path, String content)
  {
    createDirectory(FileUtil.parentPath(path));
    try
    {
      final OutputStream output = outputStream(path);
      output.write(content.getBytes());
      output.close();
    }
    catch(IOException e)
    {
      throw new LimelightException(e);
    }
  }

  public String readTextFile(String path)
  {
    StreamReader reader = new StreamReader(inputStream(path));
    String result = reader.readAll();
    reader.close();
    return result;
  }

  public String absolutePath(String path)
  {
    return new File(path).getAbsolutePath();
  }

  public String homeDir()
  {
    return System.getProperty("user.home");
  }

  public String workingDir()
  {
    return System.getProperty("user.dir");
  }

  public OutputStream outputStream(String path)
  {
    try
    {
      return new FileOutputStream(path);
    }
    catch(FileNotFoundException e)
    {
      throw new LimelightException(e);
    }
  }

  public InputStream inputStream(String path)
  {
    try
    {
      return new FileInputStream(path);
    }
    catch(FileNotFoundException e)
    {
      throw new LimelightException(e);
    }
  }

  public String[] fileListing(String directory)
  {
    return new File(directory).list();
  }

  public long modificationTime(String path)
  {
    return new File(path).lastModified();
  }

  public void deleteDirectory(String dirPath)
  {
    deleteDirectory(new File(dirPath));
  }

  public void deleteFile(String filename)
  {
    deleteFile(new File(filename));
  }

  private static void deleteDirectory(File current)
  {
    File[] files = current.listFiles();

    for(int i = 0; files != null && i < files.length; i++)
    {
      File file = files[i];
      if(file.isDirectory())
        deleteDirectory(file);
      else
        deleteFile(file);
    }
    deleteFile(current);
  }

  private static void deleteFile(File file)
  {
    if(!file.exists())
      return;
    if(!file.delete())
      throw new RuntimeException("Could not delete '" + file.getAbsoluteFile() + "'");
    waitUntilFileIsDeleted(file);
  }

  private static void waitUntilFileIsDeleted(File file)
  {
    int checks = 25;
    while(file.exists())
    {
      if(--checks <= 0)
      {
        System.out.println("Breaking out of delete wait");
        break;
      }
      try
      {
        Thread.sleep(200);
      }
      catch(InterruptedException e)
      {
        //okay
      }
    }
  }
}
