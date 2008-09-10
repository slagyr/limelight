//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.io;

import limelight.LimelightException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Downloader
{
  private TempDirectory tempDirectory;

  public Downloader(TempDirectory tempDirectory)
  {
    this.tempDirectory = tempDirectory;
  }

  public TempDirectory getTempDirectory()
  {
    return tempDirectory;
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

      URLConnection connection = url.openConnection();
      String filename = calculateFilename(url, connection);
      File file = new File(tempDirectory.getDownloadsDirectory(), filename);

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

  private String calculateFilename(URL url, URLConnection connection)
  {
    String filename = null;
    String contentDisposition = connection.getHeaderField("Content-Disposition");
    if(contentDisposition != null)
    {
      Pattern regex = Pattern.compile("filename=\"(.*)\"");
      Matcher matcher = regex.matcher(contentDisposition);
      if(matcher.find())
        filename = matcher.group(1);
    }
    if(filename == null)
      filename = new File(url.getFile()).getName();
    return filename;
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
