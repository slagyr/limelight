package limelight.io;

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
  private File destinationRoot;
  public static File stubbedGetResult;

  public static File get(String resource)
  {
    if(stubbedGetResult != null)
      return stubbedGetResult;
    return new Downloader().download(resource);    
  }

  public Downloader()
  {
    Data.establishDirs();
    destinationRoot = Data.downloadsDir();
  }

  public Downloader(String root)
  {
    destinationRoot = new File(root);
  }

  public File getDestinationRoot()
  {
    return destinationRoot;
  }

  public File download(String resource)
  {
    try
    {
      URL url = parseURL(resource);
      URLConnection urlConnection = url.openConnection();
      File destination = calculateDesintationFile(url, urlConnection);

      downloadData(urlConnection, destination);

      return destination;
    }
    catch(Exception e)
    {
      throw new LimelightException("Failed to download resource: " + e.toString(), e);
    }
  }

  private void downloadData(URLConnection urlConnection, File destination) throws Exception
  {
    InputStream input = urlConnection.getInputStream();
    FileOutputStream output = new FileOutputStream(destination);

    FileUtil.copyBytes(input, output);

    input.close();
    output.close();
  }

  private File calculateDesintationFile(URL url, URLConnection urlConnection)
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

  private File findUniqueDownloadDestination(String filename)
  {
    File attempt = new File(destinationRoot, filename);
    String baseName = FileUtil.baseName(attempt.getName());
    String extension = FileUtil.fileExtension(attempt.getName());
    int suffix = 2;
    while(attempt.exists())
      attempt = new File(destinationRoot, baseName + "_" + suffix++ + extension);
    return attempt;
  }

  private URL parseURL(String resource) throws MalformedURLException
  {
    final File file = new File(resource);
    if(file.exists())
      resource = "file://" + file.getAbsolutePath();
    return new URL(resource);
  }

}
