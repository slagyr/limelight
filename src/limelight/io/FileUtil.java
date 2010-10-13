//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.io;

import limelight.LimelightException;
import limelight.util.StringUtil;

import java.io.*;

public class FileUtil
{
  public static String separator()
  {
    return System.getProperty("file.separator");
  }

  public static String join(String... parts)
  {
    return removeDuplicateSeprators(StringUtil.join(separator(), parts));
  }

  private static String removeDuplicateSeprators(String path)
  {
    return path.replace(separator() + separator(), separator());
  }

  public static String buildOnPath(String base, String... parts)
  {
    return removeDuplicateSeprators(base + separator() + join(parts));
  }

  public static String pathTo(String... parts)
  {
    return join(parts);
  }

  public static String absolutePath(String path)
  {
    return new File(path).getAbsolutePath();
  }

  public static String currentPath()
  {
    return absolutePath("");
  }

  public static File establishDirectory(String path)
  {
    return establishDirectory(new File(path));
  }

  public static File establishDirectory(File dir)
  {
    dir.mkdirs();
    return dir;
  }

  public static File establishFile(String path, String content)
  {
    return establishFile(new File(path), content);
  }

  private static File establishFile(File file, String content)
  {
    final File parentDir = file.getParentFile();
    if(!parentDir.exists())
      establishDirectory(parentDir);
    return createFile(file, content);
  }

  public static File createFile(String path, String content)
  {
    return createFile(new File(path), content);
  }

  public static File createFile(File file, String content)
  {
    try
    {
      FileOutputStream fileOutput = new FileOutputStream(file);
      fileOutput.write(content.getBytes());
      fileOutput.close();
    }
    catch(IOException e)
    {
      e.printStackTrace();
    }
    return file;
  }

  public static void appendToFile(String filename, String content)
  {
    try
    {
      FileOutputStream fileOutput = new FileOutputStream(filename, true);
      fileOutput.write(content.getBytes());
      fileOutput.close();
    }
    catch(IOException e)
    {
      e.printStackTrace();
    }
  }

  public static File makeDir(String path)
  {
    File dir = new File(path);
    dir.mkdir();
    return dir;
  }

  public static void deleteFileSystemDirectory(String dirPath)
  {
    deleteFileSystemDirectory(new File(dirPath));
  }

  public static void deleteFileSystemDirectory(File current)
  {
    File[] files = current.listFiles();

    for(int i = 0; files != null && i < files.length; i++)
    {
      File file = files[i];
      if(file.isDirectory())
        deleteFileSystemDirectory(file);
      else
        deleteFile(file);
    }
    deleteFile(current);
  }

  public static void deleteFile(String filename)
  {
    deleteFile(new File(filename));
  }

  public static void deleteFile(File file)
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

  public static String getFileContent(String path)
  {
    File input = new File(path);
    return getFileContent(input);
  }

  public static String getFileContent(File input)
  {
    return new String(getFileBytes(input));
  }

  public static byte[] getFileBytes(File input)
  {
    try
    {
      long size = input.length();
      FileInputStream stream = new FileInputStream(input);
      byte[] bytes = new StreamReader(stream).readBytes((int) size);
      stream.close();
      return bytes;
    }
    catch(IOException e)
    {
      throw new LimelightException(e);
    }
  }

  public static void copyBytes(InputStream input, OutputStream output)
  {
    try
    {
      BufferedInputStream bufferedInput = new BufferedInputStream(input);
      StreamReader reader = new StreamReader(bufferedInput);
      BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);
      while(!reader.isEof())
        bufferedOutput.write(reader.readBytes(1000));
      bufferedOutput.flush();
    }
    catch(IOException e)
    {
      throw new LimelightException(e);
    }
  }

  public static String baseName(String path)
  {
    final File file = new File(path);
    String name = file.getName();
    final int extensionIndex = name.lastIndexOf(".");
    if(extensionIndex == -1)
      return name;
    else
      return name.substring(0, extensionIndex);
  }

  public static String fileExtension(String path)
  {
    final File file = new File(path);
    String name = file.getName();
    final int extensionIndex = name.lastIndexOf(".");
    if(extensionIndex == -1)
      return "";
    else
      return name.substring(extensionIndex);
  }

  public static String parentPath(String path)
  {
    final int lastSeparator = path.lastIndexOf(separator());
    if(lastSeparator == -1)
      return "";
    return path.substring(0, lastSeparator);
  }

  public static String filename(String path)
  {
    final int lastSeparator = path.lastIndexOf(separator());
    if(lastSeparator == -1)
      return path;
    return path.substring(lastSeparator + 1);
  }
}
