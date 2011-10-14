//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.io;

import limelight.Context;
import limelight.LimelightException;
import limelight.util.Util;
import org.jruby.util.io.SelectorFactory;

import java.io.*;
import java.util.*;

public class FakeFileSystem extends FileSystem
{
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
    separator = "/";
    root = FakeFile.directory("");
    workingDirectory = root;
  }

  @Override
  public String workingDir()
  {
    return pathOf(workingDirectory);
  }

  public void setWorkingDirectory(String path)
  {
    final FakeFilePath filePath = (FakeFilePath) resolve(path);
    filePath.mkdirs();
    workingDirectory = filePath.fake();
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

  private String pathOf(FakeFile file)
  {
    String path = "";
    for(FakeFile f = file; f != null; f = f.parent)
      path = f.name + (path.isEmpty() ? "" : separator + path);
    return path;
  }

  @Override
  protected Path resolve(String path)
  {
    return new FakeFilePath(this, path);
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
      else if("..".equals(name))
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

  private static class FakeFileOutputStream extends ByteArrayOutputStream
  {
    private FakeFile file;

    public FakeFileOutputStream(FakeFile file)
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

  private static class FakeFilePath implements Path
  {
    private FakeFileSystem fs;
    private String path;
    private FakeFile file;

    public FakeFilePath(FakeFileSystem fs, String path)
    {
      this.fs = fs;
      this.path = path;
    }

    private FakeFile resolvePath(String path)
    {
      if(".".equals(path) || path == null)
        return fs.workingDirectory;
      else if(isRoot(path))
        return fs.root;

      String parentPath = fs.parentPath(path);
      FakeFile parent = resolveParent(parentPath);
      if(parent != null)
        return parent.get(fs.filename(path));
      return null;
    }

    private boolean isRoot(String path)
    {
      return "".equals(path) || "/".equals(path);
    }

    private FakeFile resolveParent(String parentPath)
    {
      return parentPath == null ? fs.workingDirectory : resolvePath(parentPath);
    }

    private FakeFile fake()
    {
      if(file == null)
        file = resolvePath(path);
      return file;
    }

    private void ensureExistence()
    {
      if(!exists())
        throw new LimelightException("[FakeFileSystem] File not found: " + path);
    }

    public boolean exists()
    {
      return fake() != null;
    }

    public void mkdirs()
    {
      if(exists())
        return;

      final FakeFilePath parentPath = new FakeFilePath(fs, fs.parentPath(path));
      parentPath.mkdirs();

      final FakeFile newDir = FakeFile.directory(fs.filename(path));
      parentPath.fake().add(newDir);
    }

    public boolean isDirectory()
    {
      return exists() && fake().isDirectory;
    }

    public OutputStream outputStream()
    {
      final FakeFilePath parentPath = new FakeFilePath(fs, fs.parentPath(path));
      parentPath.mkdirs();
      final FakeFile file = FakeFile.file(fs.filename(path));
      parentPath.fake().add(file);

      return new FakeFileOutputStream(fake());
    }

    public InputStream inputStream()
    {
      ensureExistence();
      return new ByteArrayInputStream(fake().content);
    }

    public String getAbsolutePath()
    {
      if(path == null || ".".equals(path))
        return fs.pathOf(fs.workingDirectory);
      else if(path.startsWith(fs.separator()))
        return path;
      else
        return fs.pathOf(fs.workingDirectory) + fs.separator() + path;
    }

    public void delete()
    {
      if(!exists())
        return;
      FakeFile parent = new FakeFilePath(fs, fs.parentPath(path)).fake();
      parent.children.remove(fake().name);
    }

    public String[] listing()
    {
      ensureExistence();
      if(!fake().isDirectory)
        throw new LimelightException("Not a directory: " + path);
      final Set<String> childNames = fake().children.keySet();
      String[] files = new String[childNames.size()];
      int i = 0;
      for(String childName : childNames)
        files[i++] = childName;
      return files;
    }

    public long lastModified()
    {
      ensureExistence();
      return fake().modificationTime;
    }

    public File file()
    {
      throw new LimelightException("FakeFilePath.file() not supported");
    }

    public boolean isRoot()
    {
       return fake() == fs.root;
    }

    public String parentPath()
    {
      if(path == null)
        return null;
      int lastSeparatorIndex = path.lastIndexOf(fs.separator);
      if(lastSeparatorIndex == -1)
        return null;
      else
        return path.substring(0, lastSeparatorIndex);
    }
  }
}
