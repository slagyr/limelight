//- Copyright 2008 8th Light, Inc.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.io;

import limelight.LimelightException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Downloader
{
  private TempDirectory tempDirectory;

  public Downloader(TempDirectory tempDirectory)
  {
    this.tempDirectory = tempDirectory;
  }

  public File download(String location) throws LimelightException
  {
    try
    {
      URL url = new URL(location);      
      if("file".equals(url.getProtocol()))
        return loadFile(url.getPath());
      else
        return download(url);
    }
    catch(MalformedURLException e)
    {
      return loadFile(location);
    }
  }

  private File download(URL url) throws LimelightException
  {
    try
    {
      String filename = new File(url.getFile()).getName();
      File file = new File(tempDirectory.getDownloadsDirectory(), filename);

      URLConnection connection = url.openConnection();
      connection.connect();
      OutputStream output = new FileOutputStream(file);
      FileUtil.copyBytes(connection.getInputStream(), output);
      output.close();

      return file;
    }
    catch(FileNotFoundException fnfe)
    {
      return notFound(url.toString());
    }
    catch(Exception e)
    {
      throw new LimelightException("Download failed: " + url.toString() + "\n" + e.toString());
    }
  }

  private File loadFile(String location) throws LimelightException
  {
    File file = new File(location);
    if(file.exists())
      return file;
    else
      return notFound(location);
  }

  private File notFound(String location) throws LimelightException
  {
    throw new LimelightException("Not found: " + location);
  }
}
