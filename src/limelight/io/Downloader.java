//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.io;

import limelight.Context;
import limelight.LimelightException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Downloader
{
  public static Pattern filenameRegex = Pattern.compile("filename=\"?(.*)\"?");
  public static String stubbedGetResult;

  private String destinationRoot;
  private FileSystem fs = Context.fs();

  public static String get(String resource)
  {
    if(stubbedGetResult != null)
      return stubbedGetResult;
    return new Downloader().download(resource);
  }

  public Downloader()
  {
    destinationRoot = Data.downloadsDir();
  }

  public Downloader(String root)
  {
    destinationRoot = root;
  }

  public String getDestinationRoot()
  {
    return destinationRoot;
  }

  public String download(String resource)
  {
    try
    {
      URL url = parseURL(resource);
      URLConnection urlConnection = url.openConnection();
      String destination = calculateDesintationFile(url, urlConnection);

      downloadData(urlConnection, destination);

      return destination;
    }
    catch(Exception e)
    {
      throw new LimelightException("Failed to download resource: " + e.toString(), e);
    }
  }

  private void downloadData(URLConnection urlConnection, String destination) throws Exception
  {
    InputStream input;
    if("file".equals(urlConnection.getURL().getProtocol()))
      input = fs.inputStream(urlConnection.getURL().getFile());
    else
      input = urlConnection.getInputStream();

    OutputStream output = new BufferedOutputStream(fs.outputStream(destination));

    IoUtil.copyBytes(input, output);

    input.close();
    output.close();
  }

  private String calculateDesintationFile(URL url, URLConnection urlConnection)
  {
    final String contentDisposition = urlConnection.getHeaderField("Content-Disposition");
    String filename = contentDisposition == null ? getFilenameFromUrl(url) : getFilenameFromContentDisposition(contentDisposition);
    return findUniqueDownloadDestination(filename);
  }

  private String getFilenameFromContentDisposition(String contentDisposition)
  {
    Matcher matcher = filenameRegex.matcher(contentDisposition);
    if(matcher.find())
      return matcher.group(1);
    else
      throw new LimelightException("Can't find filename in Content-Disposition: " + contentDisposition);
  }

  private String getFilenameFromUrl(URL url)
  {
    String urlFilename = url.getFile();
    File urlFile = new File(urlFilename);
    return urlFile.getName();
  }

  private String findUniqueDownloadDestination(String filename)
  {
    String attempt = fs.join(destinationRoot, filename);
    String baseName = fs.baseName(attempt);
    String extension = fs.fileExtension(attempt);
    int suffix = 2;
    while(fs.exists(attempt))
      attempt = fs.join(destinationRoot, baseName + "_" + suffix++ + extension);
    return attempt;
  }

  private URL parseURL(String resource) throws MalformedURLException
  {
    if(fs.exists(resource))
      resource = "file://" + resource;
    return new URL(resource);
  }

}
