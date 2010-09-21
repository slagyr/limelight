//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.util;

import limelight.LimelightException;
import limelight.io.StreamReader;
import sun.management.FileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ResourceLoader
{
  private String root;

//    def load(path)
//      file_to_load = path_to(path)
//      raise LimelightException.new("File not found: #{file_to_load}") if not File.exists?(file_to_load)
//      return IO.read(file_to_load)
//    end
//
//    alias :read :load

  public static ResourceLoader forRoot(String rootPath)
  {
    ResourceLoader loader = new ResourceLoader();
    loader.resetOnRoot(rootPath);
    return loader;
  }

  protected ResourceLoader()
  {
  }

  public void resetOnRoot(String rootPath)
  {
    root = rootPath;
  }

  public String getRoot()
  {
    return root;
  }

  public String pathTo(String path)
  {
    return fileFor(path).getAbsolutePath();
  }

  private File fileFor(String path)
  {
    final File file = new File(path);
    if(file.isAbsolute())
      return file;
    else
      return new File(root, path);
  }

  public boolean exists(String path)
  {
    return fileFor(path).exists();
  }

  public String readText(String path)
  {
    File file = fileFor(path);
    StreamReader reader = null;
    try
    {
      reader = new StreamReader(new FileInputStream(file));
      return reader.readAll();
    }
    catch(FileNotFoundException e)
    {
      throw new LimelightException("Failed to read file", e);
    }
    finally
    {
      if(reader != null)
        reader.close();
    }
  }
}
