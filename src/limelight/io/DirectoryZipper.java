//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.io;

import limelight.Context;
import limelight.LimelightException;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class DirectoryZipper
{
  private String directoryPath;
  private ZipInputStream zipInput;
  private ZipOutputStream zipOutput;
  private String locationPath;
  private boolean isRootDirectoryUnzipped;
  private FileSystem fs = Context.fs();

  public static DirectoryZipper fromDir(String rootDir)
  {
    DirectoryZipper zipper = new DirectoryZipper();
    zipper.setDirectoryPath(rootDir);
    return zipper;
  }

  public static DirectoryZipper fromZip(InputStream input)
  {
    DirectoryZipper zipper = new DirectoryZipper();
    zipper.setInput(input);
    return zipper;
  }

  public String getDirectoryPath()
  {
    return directoryPath;
  }

  public void zipTo(OutputStream output)
  {
    try
    {
      if(!fs.exists(directoryPath) && !fs.isDirectory(directoryPath))
        throw new LimelightException(directoryPath + " is not a valid directory");

      zipOutput = new ZipOutputStream(output);

      zipDirectory(directoryPath);

      zipOutput.finish();
      zipOutput.close();
    }
    catch(LimelightException e)
    {
      throw e;
    }
    catch(Exception e)
    {
      throw new LimelightException(e);
    }
  }

  public void unzip(String location) throws Exception
  {
    for(ZipEntry entry = zipInput.getNextEntry(); entry != null; entry = zipInput.getNextEntry())
    {
      String absolutePath = fs.absolutePath(fs.join(location, entry.getName()));
      if(entry.isDirectory())
        unzipDirectory(absolutePath);
      else
        unzipFile(absolutePath);

    }
  }

  private void setInput(InputStream input)
  {
    zipInput = new ZipInputStream(input);
  }

  private void setDirectoryPath(String path)
  {
    directoryPath = fs.absolutePath(path);
    locationPath = fs.parentPath(directoryPath);
  }

  private void zipDirectory(String directory) throws Exception
  {
    zipOutput.putNextEntry(makeEntryFrom(directory));
    zipOutput.closeEntry();
    String[] children = fs.fileListing(directory);
    for(String child : children)
      zipFile(fs.join(directory, child));
  }

  public String getProductionName()
  {
    return fs.filename(directoryPath);
  }

  private void zipFile(String file) throws Exception
  {
    if(fs.isDirectory(file))
      zipDirectory(file);
    else
    {
      zipOutput.putNextEntry(makeEntryFrom(file));
      copyFileToZip(file);
      zipOutput.closeEntry();
    }
  }

  private ZipEntry makeEntryFrom(String file)
  {
    ZipEntry entry = new ZipEntry(entryName(file));
    entry.setTime(fs.modificationTime(file));
    if(fs.isDirectory(file))
      entry.setSize(0);
    return entry;
  }

  private void copyFileToZip(String file) throws Exception
  {
    StreamReader reader = new StreamReader(fs.inputStream(file));
    while(!reader.isEof())
      zipOutput.write(reader.readBytes(1000));
  }

  private String entryName(String file)
  {
    String name = fs.absolutePath(file).substring(locationPath.length() + 1);
    if(fs.isDirectory(file))
      name = name + "/";
    return name;
  }

  private void unzipFile(String absolutePath) throws Exception
  {
    OutputStream fileOutput = fs.outputStream(absolutePath);
    IoUtil.copyBytes(zipInput, fileOutput);
    fileOutput.close();
  }

  private void unzipDirectory(String absolutePath)
  {
    fs.createDirectory(absolutePath);
    if(!isRootDirectoryUnzipped)
    {
      directoryPath = absolutePath;
      isRootDirectoryUnzipped = true;
    }
  }
}
