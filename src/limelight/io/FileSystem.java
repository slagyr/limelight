//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.io;

import limelight.LimelightException;
import limelight.util.StringUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileSystem
{
  protected String separator;

  public FileSystem()
  {
    separator = System.getProperty("file.separator");
  }

  public void createDirectory(String path)
  {
    resolve(path).mkdirs();
  }

  public boolean exists(String path)
  {
    return resolve(path).exists();
  }

  public boolean isDirectory(String path)
  {
    return resolve(path).isDirectory();
  }

  public String absolutePath(String path)
  {
    return resolve(path).getAbsolutePath();
  }

  public OutputStream outputStream(String path)
  {
    return resolve(path).outputStream();
  }

  public InputStream inputStream(String path)
  {
    return resolve(path).inputStream();
  }

  public String[] fileListing(String path)
  {
    return resolve(path).listing();
  }

  public long modificationTime(String path)
  {
    return resolve(path).lastModified();
  }

  public void delete(String path)
  {
    resolve(path).delete();
  }

  public void createTextFile(String path, String content)
  {
    createDirectory(parentPath(path));
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

  // UTILITY  METHODS --------------------------------------------------------------------------------------------------

  public String separator()
  {
    return separator;
  }

  public String homeDir()
  {
    return System.getProperty("user.home");
  }

  public String workingDir()
  {
    return System.getProperty("user.dir");
  }

  public String join(String... parts)
  {
    return removeDuplicateSeprators(StringUtil.join(separator, (Object[]) parts));
  }

  public String baseName(String path)
  {
    final File file = new File(path);
    String name = file.getName();
    final int extensionIndex = name.lastIndexOf(".");
    if(extensionIndex == -1)
      return name;
    else
      return name.substring(0, extensionIndex);
  }

  public String fileExtension(String path)
  {
    final File file = new File(path);
    String name = file.getName();
    final int extensionIndex = name.lastIndexOf(".");
    if(extensionIndex == -1)
      return "";
    else
      return name.substring(extensionIndex);
  }

  public String parentPath(String path)
  {
    final int lastSeparator = path.lastIndexOf(separator);
    if(lastSeparator == -1)
      return ".";
    return path.substring(0, lastSeparator);
  }

  public String filename(String path)
  {
    if("/".equals(path))
      return path;
    if(path.endsWith(separator))
      path = path.substring(0, path.length() - separator.length());
    final int lastSeparator = path.lastIndexOf(separator);
    if(lastSeparator == -1)
      return path;
    return path.substring(lastSeparator + 1);
  }

  // HELPER METHODS ----------------------------------------------------------------------------------------------------

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
    int checks = 10;
    while(file.exists())
    {
      if(--checks <= 0)
      {
        System.out.println("Breaking out of delete wait");
        break;
      }
      try
      {
        Thread.sleep(100);
      }
      catch(InterruptedException e)
      {
        //okay
      }
    }
  }

  private String removeDuplicateSeprators(String path)
  {
    return path.replace(separator + separator, separator);
  }

  // WHERE THE MAGIC HAPPENS -------------------------------------------------------------------------------------------

  protected Path resolve(String path)
  {
    if(path.startsWith("file:"))
      return new FilePath(path);
    else if(path.startsWith("jar:"))
      return new ZipPath(path, this);
    else
      return new FilePath(path);
  }

  protected static interface Path
  {
    boolean exists();

    void mkdirs();

    boolean isDirectory();

    OutputStream outputStream();

    InputStream inputStream();

    String getAbsolutePath();

    void delete();

    String[] listing();

    long lastModified();

    File file();
  }

  private static class FilePath implements Path
  {
    private String path;
    private File file;

    public FilePath(String path)
    {
      this.path = path;
      if(path.startsWith("file:"))
        this.path = path.substring(5);
    }

    public File file()
    {
      if(file == null)
        file = new File(path);
      return file;
    }

    public boolean exists()
    {
      return file().exists();
    }

    public void mkdirs()
    {
      if(!file().exists() && !file().mkdirs())
        throw new LimelightException("Can't establish directory: " + path);
    }

    public boolean isDirectory()
    {
      return file().isDirectory();
    }

    public OutputStream outputStream()
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

    public InputStream inputStream()
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

    public String getAbsolutePath()
    {
      try
      {
        return "file:" + file().getCanonicalPath();
      }
      catch(IOException e)
      {
        throw new LimelightException(e);
      }
    }

    public void delete()
    {
      if(file().isDirectory())
        deleteDirectory(file());
      else
        deleteFile(file());
    }

    public String[] listing()
    {
      return file().list();
    }

    public long lastModified()
    {
      return file().lastModified();
    }
  }

  private static class ZipPath implements Path
  {
    private Path pathToZip;
    private String pathToFile;
    private ZipFile zip;
    private FileSystem fs;

    private ZipPath(String path, FileSystem fs)
    {
      this.fs = fs;
      final int bangIndex = path.indexOf("!");
      if(bangIndex == -1)
        throw new LimelightException("Invalid Jar file path: " + path);

      pathToZip = fs.resolve(path.substring(4, bangIndex));
      pathToFile = path.substring(bangIndex + 2);
    }

    private ZipFile zip()
    {
      if(zip == null)
      {
        try
        {
          zip = new ZipFile(pathToZip.file());
        }
        catch(IOException e)
        {
          throw new LimelightException(e);
        }
      }
      return zip;
    }

    private ZipEntry zipEntry()
    {
      return zip().getEntry(pathToFile);
    }

    public boolean exists()
    {
      return pathToZip.exists() && (zipEntry() != null);
    }

    public void mkdirs()
    {
      throw new LimelightException("JarPath.mkdirs() is not supported");
    }

    public boolean isDirectory()
    {
      if(pathToFile.endsWith("/"))
        return zipEntry().isDirectory();
      else
      {
        final ZipEntry entry = zip().getEntry(pathToFile + "/");
        return entry != null && entry.isDirectory();
      }
    }

    public OutputStream outputStream()
    {
      throw new LimelightException("JarPath.outputStream() is not supported");
    }

    public InputStream inputStream()
    {
      try
      {
        return zip().getInputStream(zipEntry());
      }
      catch(IOException e)
      {
        throw new LimelightException(e);
      }
    }

    public String getAbsolutePath()
    {
      return "jar:" + pathToZip.getAbsolutePath() + "!/" + pathToFile;
    }

    public void delete()
    {
      throw new LimelightException("JarPath.delete() is not supported");
    }

    public String[] listing()
    {
      if(!isDirectory())
        return new String[0];
      else
      {
        pathToFile = pathToFile.endsWith("/") ? pathToFile : pathToFile + "/";
        ArrayList<String> list = new ArrayList<String>();
        final Enumeration<? extends ZipEntry> entries = zip().entries();
        while(entries.hasMoreElements())
        {
          ZipEntry entry = entries.nextElement();
          final String entryName = entry.getName();
          if(entryName.startsWith(pathToFile))
          {
            String name = entryName.substring(pathToFile.length());
            if(name.endsWith("/"))
              name = name.substring(0, name.length() - 2);
            if(name.length() > 0 && name.indexOf("/") == -1)
              list.add(fs.filename(entryName));
          }
        }
        return list.toArray(new String[list.size()]);
      }
    }

    public long lastModified()
    {
      return zipEntry().getTime();
    }

    public File file()
    {
      throw new LimelightException("JarPath.file() is not supported");
    }
  }
}
