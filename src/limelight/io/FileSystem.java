//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.io;

import limelight.Context;
import limelight.LimelightException;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileSystem
{
  public static final Pattern WinDrive = Pattern.compile("[A-Z]\\:(\\\\|/)");

  protected String separator = System.getProperty("file.separator");
  protected boolean windows = System.getProperty("os.name").toLowerCase().contains("windows");

  public static FileSystem installed()
  {
    FileSystem fs = new FileSystem();
    Context.instance().fs = fs;
    return fs;
  }

  public void createDirectory(String path)
  {
    resolve(path).mkdirs();
  }

  public boolean exists(String path)
  {
    return resolve(path).exists();
  }

  public boolean isRoot(String path)
  {
    return resolve(path).isRoot();
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

  public String parentPath(String path)
  {
    return resolve(path).parentPath();
  }

  public boolean isAbsolute(String path)
  {
    return resolve(path).isAbsolute();
  }

  public String relativePathBetween(String origin, String target)
  {
    if(origin.equals(target))
      return ".";

    final String absoluteOrigin = absolutePath(origin);
    final String absoluteTarget = absolutePath(target);
    if(absoluteOrigin.equals(absoluteTarget))
      return ".";

    String path = "";
    String commonParent = absoluteOrigin;
    while(!absoluteTarget.startsWith(commonParent))
    {
      path += ".." + "/";
      commonParent = parentPath(commonParent);
      if(isRoot(commonParent))
        break;
    }
    String result = path + absoluteTarget.substring(commonParent.length());
    result = result.startsWith("/") ? result.substring(1) : result;
    return removeDuplicateSeparators(result.replace("/", separator()));
  }

  // UTILITY  METHODS --------------------------------------------------------------------------------------------------

  public String separator()
  {
    return separator;
  }

  public String homeDir()
  {
    return absolutePath(System.getProperty("user.home"));
  }

  public String workingDir()
  {
    return absolutePath(System.getProperty("user.dir"));
  }

  public String join(String root, String... parts)
  {
    if(parts.length == 0)
      return root;
    Path path = resolve(root);
    for(String part : parts)
      path = path.append(part);
    return path.toPath();
  }

  public String baseName(String path)
  {
    String name = filename(path);
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

  public String filename(String path)
  {
    if((windows && WinDrive.matcher(path).matches()) || "/".equals(path))
      return path;
    path = path.replace(separator, "/");
    if(path.endsWith("/"))
      path = path.substring(0, path.length() - 1);
    final int lastSeparator = path.lastIndexOf("/");
    if(lastSeparator == -1)
      return path;
    return path.substring(lastSeparator + 1);
  }

  public String pathTo(String parent, String target)
  {
    if(target == null)
      return parent;
    else if(parent == null || isAbsolute(target))
      return target;
    else
      return join(parent, target);
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

  protected String removeDuplicateSeparators(String path)
  {
    return removeDuplicateSeparators(path, separator());
  }

  protected String removeDuplicateSeparators(String path, String separator)
  {
    final String duplicate = separator + separator;
    if(path.contains(duplicate))
      return removeDuplicateSeparators(path.replace(duplicate, separator));
    else
      return path;
  }

  // WHERE THE MAGIC HAPPENS -------------------------------------------------------------------------------------------

  protected Path resolve(String path)
  {
    if(path.startsWith("file:"))
      return new UriPath(this, path);
    else if(path.startsWith("jar:"))
      return new ZipPath(this, path);
    else
      return new RelativePath(this, path);
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

    boolean isRoot();

    String parentPath();

    boolean isAbsolute();

    Path append(String part);

    String toPath();
  }

  private static abstract class FileBasedPath implements Path
  {
    protected abstract File file();

    public boolean exists()
    {
      return file().exists();
    }

    public void mkdirs()
    {
      if(!file().exists() && !file().mkdirs())
        throw new LimelightException("Can't establish directory: " + file().getPath());
    }

    public boolean isDirectory()
    {
      return file().isDirectory();
    }

    public OutputStream outputStream()
    {
      try
      {
        return new FileOutputStream(file());
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
        return new FileInputStream(file());
      }
      catch(FileNotFoundException e)
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

    public boolean isRoot()
    {
      try
      {
        return file().getCanonicalFile().getParent() == null;
      }
      catch(IOException e)
      {
        return false;
      }
    }
  }

  private static class UriPath extends FileBasedPath
  {
    private URI uri;
    private File file;
    private FileSystem fs;

    private UriPath(FileSystem fs, String path)
    {
      this.fs = fs;
      try
      {
        if(path.charAt(5) != '/')
          uri = new URI("file:/" + path.substring(5));
        else
          uri = new URI(path);
      }
      catch(URISyntaxException e)
      {
        throw new LimelightException("Failed to create URIPath from: " + path, e);
      }
    }

    protected File file()
    {
      if(file == null)
        file = new File(uri.getRawPath());
      return file;
    }

    public String getAbsolutePath()
    {
      return uri.toString();
    }

    public String parentPath()
    {
      String path = uri.toString();
      if("file:/".equals(path) || WinDrive.matcher(path.substring(6)).matches())
        return path;
      final int lastSlashIndex = path.lastIndexOf("/");
      if(lastSlashIndex == 8 && WinDrive.matcher(path.substring(6, 9)).matches())
        return path.substring(0, lastSlashIndex + 1);
      else if(lastSlashIndex == 5)
        return "file:/";
      else
        return path.substring(0, lastSlashIndex);
    }

    public boolean isAbsolute()
    {
      return uri.isAbsolute();
    }

    public Path append(String part)
    {
      String path = uri.toString();
      return new UriPath(fs, fs.removeDuplicateSeparators(path + "/" + part, "/"));
    }

    public String toPath()
    {
      return getAbsolutePath();
    }
  }

  private static class RelativePath extends FileBasedPath
  {
    private String path;
    private File file;
    private FileSystem fs;

    public RelativePath(FileSystem fs, String path)
    {
      this.fs = fs;
      this.path = path;
    }

    public File file()
    {
      if(file == null)
        file = new File(path);
      return file;
    }

    public String parentPath()
    {
      final File parent = file().getAbsoluteFile().getParentFile();
      if(parent != null)
        return parent.toURI().toString();
      else
        return getAbsolutePath();
    }

    public boolean isAbsolute()
    {
      return fs.windows ? startsWithWinDive() : path.startsWith(fs.separator);
    }

    public Path append(String part)
    {
      return new RelativePath(fs, fs.removeDuplicateSeparators(path + fs.separator() + part));
    }

    public String toPath()
    {
      return path;
    }

    private boolean startsWithWinDive()
    {
      final Matcher matcher = WinDrive.matcher(path);
      return matcher.find() && matcher.start() == 0;
    }

    public String getAbsolutePath()
    {
      return file().toURI().toString();
    }
  }

  private static class ZipPath implements Path
  {
    private FileBasedPath zipPath;
    private String filePath;
    private ZipFile zip;
    private FileSystem fs;

    private ZipPath(FileSystem fs, String path)
    {
      this.fs = fs;
      final int bangIndex = path.indexOf("!");
      if(bangIndex == -1)
        throw new LimelightException("Invalid Jar file path: " + path);

      zipPath = (FileBasedPath) fs.resolve(path.substring(4, bangIndex));
      filePath = path.substring(bangIndex + 2);
    }

    private ZipPath(FileSystem fs, FileBasedPath zipPath, ZipFile zip, String filePath)
    {
      this.fs = fs;
      this.zipPath = zipPath;
      this.zip = zip;
      this.filePath = filePath;
    }

    private ZipFile zip()
    {
      if(zip == null)
      {
        try
        {
          zip = new ZipFile(zipPath.file());
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
      return zip().getEntry(filePath);
    }

    public boolean exists()
    {
      return zipPath.exists() && (zipEntry() != null);
    }

    public void mkdirs()
    {
      throw new LimelightException("JarPath.mkdirs() is not supported");
    }

    public boolean isDirectory()
    {
      if(filePath.endsWith("/"))
        return zipEntry().isDirectory();
      else
      {
        final ZipEntry entry = zip().getEntry(filePath + "/");
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
      return "jar:" + zipPath.getAbsolutePath() + "!/" + filePath;
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
        filePath = filePath.endsWith("/") ? filePath : filePath + "/";
        ArrayList<String> list = new ArrayList<String>();
        final Enumeration<? extends ZipEntry> entries = zip().entries();
        while(entries.hasMoreElements())
        {
          ZipEntry entry = entries.nextElement();
          final String entryName = entry.getName();
          if(entryName.startsWith(filePath))
          {
            String name = entryName.substring(filePath.length());
            if(name.endsWith("/"))
              name = name.substring(0, name.length() - 2);
            if(name.length() > 0 && !name.contains("/"))
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

    public boolean isRoot()
    {
      return filePath.isEmpty();
    }

    public String parentPath()
    {
      final String base = "jar:" + zipPath.getAbsolutePath() + "!/";
      final int lastSlashIndex = filePath.lastIndexOf("/");
      if(lastSlashIndex > 0)
        return base + filePath.substring(0, lastSlashIndex);
      else
        return base;
    }

    public boolean isAbsolute()
    {
      return zipPath.isAbsolute();
    }

    public Path append(String part)
    {
      final String newFilePath = fs.removeDuplicateSeparators(filePath + "/" + part, "/");
      return new ZipPath(fs, zipPath, zip, newFilePath);
    }

    public String toPath()
    {
      return getAbsolutePath();
    }
  }
}
