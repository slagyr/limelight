//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.io;

import java.io.*;
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

  private void setInput(InputStream input)
  {
    zipInput = new ZipInputStream(input);
  }

  private void setDirectoryPath(String path)
  {
    directoryPath = new File(path).getAbsolutePath();
    locationPath = new File(directoryPath).getParentFile().getAbsolutePath();
  }

  public String getDirectoryPath()
  {
    return directoryPath;
  }

  public void zipTo(OutputStream output) throws Exception
  {
    File directory = new File(directoryPath);
    if(!directory.exists() || !directory.isDirectory())
      throw new IOException(directoryPath + " is not a valid directory");

    zipOutput = new ZipOutputStream(output);

    zipDirectory(directory);

    zipOutput.finish();
    zipOutput.close();
  }

  private void zipDirectory(File directory) throws Exception
  {
    zipOutput.putNextEntry(makeEntryFrom(directory));
    zipOutput.closeEntry();
    File[] children = directory.listFiles();
    for(File child : children)
      zipFile(child);
  }

  private void zipFile(File file) throws Exception
  {
    if(file.isDirectory())
      zipDirectory(file);
    else
    {
      zipOutput.putNextEntry(makeEntryFrom(file));
      copyFileToZip(file);
      zipOutput.closeEntry();
    }
  }

  private ZipEntry makeEntryFrom(File file)
  {
    ZipEntry entry = new ZipEntry(entryName(file));
    entry.setTime(file.lastModified());
    if(file.isDirectory())
      entry.setSize(0);
    return entry;
  }

  private void copyFileToZip(File file) throws Exception
  {
    StreamReader reader = new StreamReader(new FileInputStream(file));
    while(!reader.isEof())
        zipOutput.write(reader.readBytes(1000));
  }

  private String entryName(File file)
  {
    String name = file.getAbsolutePath().substring(locationPath.length() + 1);
    if(file.isDirectory())
      name = name + FileUtil.seperator();
    return name;
  }

  public void unzip(String location) throws Exception
  {
    String absoluteLocation = new File(location).getAbsolutePath();
    for(ZipEntry entry = zipInput.getNextEntry(); entry != null; entry = zipInput.getNextEntry())
    {
      String absolutePath = FileUtil.pathTo(absoluteLocation, entry.getName());
      if(entry.isDirectory())
        unzipDirectory(absolutePath);
      else
        unzipFile(absolutePath);

    }
  }

  private void unzipFile(String absolutePath) throws Exception
  {
    FileOutputStream fileOutput = new FileOutputStream(absolutePath);
    FileUtil.copyBytes(zipInput, fileOutput);
    fileOutput.close();
  }

  private void unzipDirectory(String absolutePath)
  {
    FileUtil.makeDir(absolutePath);
    if(!isRootDirectoryUnzipped)
    {
      directoryPath = absolutePath;
      isRootDirectoryUnzipped = true;
    }
  }

  public String getProductionName()
  {
    return new File(directoryPath).getName();
  }
}
