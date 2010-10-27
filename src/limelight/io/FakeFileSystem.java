package limelight.io;

import limelight.Context;
import limelight.LimelightException;
import limelight.util.Util;

import java.io.*;
import java.util.*;

public class FakeFileSystem extends FileSystem
{
  private static String separator = System.getProperty("file.separator");
  private FakeFile root;
  private FakeFile workingDirectory;

  public static FakeFileSystem installed()
  {
    FakeFileSystem fs = new FakeFileSystem();
    Context.instance().fs = fs;
    return fs;
  }

  public FakeFileSystem()
  {
    root = FakeFile.directory("");
    workingDirectory = root;
  }

  @Override
  public void createDirectory(String path)
  {
    establishDirectory(path);
  }

  @Override
  public boolean exists(String path)
  {
    return resolve(path) != null;
  }

  @Override
  public boolean isDirectory(String path)
  {
    final FakeFile file = resolve(path);
    return file != null && file.isDirectory;
  }

  @Override
  public void createTextFile(String path, String content)
  {
    FakeFile file = establishFile(path);
    file.content = content.getBytes();
  }

  @Override
  public String readTextFile(String path)
  {
    FakeFile file = checkedResolve(path);
    return new String(file.content);
  }

  @Override
  public String absolutePath(String path)
  {
    if(path.startsWith(separator))
      return path;
    return pathOf(workingDirectory) + "/" + path;
  }

  @Override
  public OutputStream outputStream(String path)
  {
    if(!exists(parentPath(path)))
      throw fileNotFound(path);
    FakeFile file = establishFile(path);
    return new FakeFileOutputSteam(file);
  }

  @Override
  public InputStream inputStream(String path)
  {
    FakeFile file = checkedResolve(path);
    return new ByteArrayInputStream(file.content);
  }

  @Override
  public String[] fileListing(String path)
  {
    FakeFile dir = checkedResolve(path);
    if(!dir.isDirectory)
      throw new LimelightException("Not a directory: " + path);
    final Set<String> childNames = dir.children.keySet();
    String[] files = new String[childNames.size()];
    int i = 0;
    for(String childName : childNames)
      files[i++] = childName;  
    return files;
  }

  @Override
  public long modificationTime(String path)
  {
    return checkedResolve(path).modificationTime;
  }

  @Override
  public void deleteDirectory(String dirPath)
  {
    deleteFile(dirPath);      
  }

  @Override
  public void deleteFile(String path)
  {
    FakeFile parent = checkedResolve(parentPath(path));
    parent.children.remove(FileUtil.filename(path));
  }

  @Override
  public String workingDir()
  {
    return pathOf(workingDirectory);
  }

  public void setWorkingDirectory(String path)
  {
    workingDirectory = resolve(path);
  }

  public String inspect()
  {
    StringBuffer buffer = new StringBuffer();
    inspect(root, buffer);
    return buffer.toString();
  }

  private void inspect(FakeFile file, StringBuffer buffer)
  {
    for(int i = 0; i < file.depth(); i++)
      buffer.append("| ");

    if(file.isDirectory)
      buffer.append("+ ");
    else
      buffer.append("- ");

    buffer.append(file.name);

    if(!file.isDirectory)
      buffer.append(" : ").append(file.content.length).append(" bytes");

    buffer.append(Util.ENDL);

    if(file.isDirectory)
    {
      List<String> childNames = new ArrayList<String>(file.children.keySet());
      Collections.sort(childNames);
      for(String childName : childNames)
        inspect(file.children.get(childName), buffer);
    }
  }

  private FakeFile checkedResolve(String path)
  {
    FakeFile file = resolve(path);
    if(file == null)
      throw fileNotFound(path);
    return file;
  }

  private LimelightException fileNotFound(String path)
  {
    return new LimelightException("[FakeFileSystem] File not found: " + path);
  }

  private FakeFile establishFile(String path)
  {
    FakeFile parent = establishDirectory(parentPath(path));
    FakeFile file = FakeFile.file(filenameOf(path));
    parent.add(file);
    return file;
  }

  private FakeFile establishDirectory(String path)
  {
    if(exists(path))
      return resolve(path);

    String parentPath = parentPath(path);
    FakeFile parent = resolveParent(parentPath);
    if(parent == null)
      parent = establishDirectory(parentPath);

    final FakeFile newDir = FakeFile.directory(filenameOf(path));
    parent.add(newDir);
    return newDir;
  }

  private FakeFile resolve(String path)
  {
    if(isRoot(path))
      return root;

    String parentPath = parentPath(path);
    FakeFile parent = resolveParent(parentPath);
    if(parent != null)
      return parent.get(filenameOf(path));
    return null;
  }

  private FakeFile resolveParent(String parentPath)
  {
    return parentPath == null ? workingDirectory : resolve(parentPath);
  }

  private boolean isRoot(String path)
  {
    return "".equals(path) || "/".equals(path);
  }

  private String filenameOf(String path)
  {
    final int lastSeparator = path.lastIndexOf(separator);
    if(lastSeparator == -1)
      return path;
    return path.substring(lastSeparator + 1);
  }

  private String parentPath(String path)
  {
    final int lastSeparator = path.lastIndexOf(separator);
    if(lastSeparator == -1)
      return null;
    return path.substring(0, lastSeparator);
  }

  private String pathOf(FakeFile file)
  {
    String path = "";
    for(FakeFile f = file; f != null; f = f.parent)
      path = f.name + (path.isEmpty() ? "" : separator + path);
    return path;
  }

  private static class FakeFile
  {
    private FakeFile parent;
    private String name;
    private byte[] content;
    private boolean isDirectory;
    private Map<String, FakeFile> children;
    private long modificationTime;

    public FakeFile(String name)
    {
      this.name = name;
      modified();
    }

    public static FakeFile directory(String name)
    {
      FakeFile dir = new FakeFile(name);
      dir.isDirectory = true;
      dir.children = new HashMap<String, FakeFile>();
      return dir;
    }

    public static FakeFile file(String name)
    {
      FakeFile file = new FakeFile(name);
      file.isDirectory = false;
      return file;
    }

    public void add(FakeFile file)
    {
      children.put(file.name, file);
      file.parent = this;
    }

    public FakeFile get(String name)
    {
      if(".".equals(name))
        return this;
      else
        if("..".equals(name))
          return parent == null ? this : parent;
        else
          return children.get(name);
    }

    public int depth()
    {
      return parent == null ? 0 : parent.depth() + 1;
    }

    public void modified()
    {
      modificationTime = System.currentTimeMillis();
    }
  }

  private class FakeFileOutputSteam extends ByteArrayOutputStream
  {
    private FakeFile file;

    public FakeFileOutputSteam(FakeFile file)
    {
      super();
      this.file = file;
    }

    @Override
    public void close() throws IOException
    {
      super.close();
      file.content = this.toByteArray();
      file.modified();
    }
  }
}
