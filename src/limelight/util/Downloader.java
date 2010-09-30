package limelight.util;

import limelight.Context;
import limelight.LimelightException;
import limelight.io.FileUtil;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Downloader
{
  public static Pattern filenameRegex = Pattern.compile("filename=\"?(.*)\"?");

  private File root;
  private File downloadDir;

  public Downloader(String root)
  {
    this.root = new File(root);
  }

  public Downloader()
  {
    root = new File(Context.instance().os.dataRoot());
  }

  public File getRoot()
  {
    return root;
  }

  public File downloadDir()
  {
    if(downloadDir == null)
    {
      downloadDir = new File(FileUtil.buildPath(root.getPath(), "Downloads"));
      if(!downloadDir.exists() && !downloadDir.mkdirs())
        throw new LimelightException("The Downloads directory (" + downloadDir.getAbsolutePath() + ") does not exist and cannot be created.");
    }
    return downloadDir;
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
    String filename = urlFile.getName();
    return filename;
  }

  private File findUniqueDownloadDestination(String filename)
  {
    File attempt = new File(FileUtil.buildPath(downloadDir().getAbsolutePath(), filename));
    String baseName = FileUtil.baseName(attempt.getName());
    String extension = FileUtil.fileExtension(attempt.getName());
    int suffix = 2;
    while(attempt.exists())
      attempt = new File(FileUtil.buildPath(downloadDir().getAbsolutePath(), baseName + "_" + suffix++ + extension));
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
