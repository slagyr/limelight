package limelight.io;

import limelight.LimelightException;
import limelight.util.StringUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Templater
{
  private static final Pattern TOKEN_PATTERN = Pattern.compile("!-(\\w+)-!");

  private TemplaterLogger logger;
  private FileSystem fileSystem;
  private String destinationRoot;
  private String sourceRoot;
  private Map<String, String> tokens = new HashMap<String, String>();
  private boolean destinationRootVerified;

  public Templater(String destination, String source)
  {
    logger = new TemplaterLogger();
    fileSystem = new FileSystem();
    destinationRoot = destination;
    sourceRoot = source;
  }

  public void setLogger(TemplaterLogger logger)
  {
    this.logger = logger;
  }

  public String getDestinationRoot()
  {
    return destinationRoot;
  }

  public void setFileSystem(FileSystem fileSystem)
  {
    this.fileSystem = fileSystem;
  }

  public void directory(String dir)
  {
    verifyDestinationRoot();
    final String fullPath = FileUtil.join(destinationRoot, dir);
    if(fileSystem.exists(fullPath))
      return;

    directory(FileUtil.parentPath(dir));
    creatingDirectory(dir);
    fileSystem.createDirectory(fullPath);
  }

  private void verifyDestinationRoot()
  {
    if(!destinationRootVerified)
    {
      if(!fileSystem.exists(destinationRoot))
        throw new LimelightException("Templater destination root doesn't exist: " + destinationRoot);
      destinationRootVerified = true;
    }
  }

  public String getSourceRoot()
  {
    return sourceRoot;
  }

  public void file(String filePath, String template)
  {
    directory(FileUtil.parentPath(filePath));
    final String templateContent = fileSystem.readTextFile(FileUtil.join(sourceRoot, template));

    final String destination = FileUtil.join(destinationRoot, filePath);
    if(fileSystem.exists(destination))
      fileExists(filePath);
    else
    {
      creatingFile(filePath);
      fileSystem.createFile(destination, replaceTokens(templateContent));
    }
  }

  public void addToken(String token, String value)
  {
    tokens.put(token, value);
  }

  private String replaceTokens(String content)
  {
    return StringUtil.gsub(content, TOKEN_PATTERN, new StringUtil.Gsuber()
    {
      public String replacementFor(Matcher matcher)
      {
        final String token = matcher.group(1);
        String tokenValue = tokens.get(token);
        if(tokenValue == null)
          tokenValue = "UNKNOWN TOKEN";
        return tokenValue;
      }
    });
  }

  private void creatingFile(String filePath)
  {
    logger.say("\tcreating file:       " + filePath);
  }

  private void fileExists(String filePath)
  {
    logger.say("\tfile already exists: " + filePath);
  }

  private void creatingDirectory(String dir)
  {
    logger.say("\tcreating directory:  " + dir);
  }

  public static class TemplaterLogger
  {
    public void say(String message)
    {
      System.out.println(message);
    }
  }
}
