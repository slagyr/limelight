//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

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
  private FileSystem fs;
  private String destinationRoot;
  private String sourceRoot;
  private Map<String, String> tokens = new HashMap<String, String>();
  private boolean destinationRootVerified;

  public Templater(String destination, String source)
  {
    logger = new TemplaterLogger();
    fs = new FileSystem();
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

  public void setFs(FileSystem fs)
  {
    this.fs = fs;
  }

  public void directory(String dir)
  {
    verifyDestinationRoot();
    final String fullPath = fs.join(destinationRoot, dir);
    if(fs.exists(fullPath))
      return;

    directory(fs.parentPath(dir));
    creatingDirectory(dir);
    fs.createDirectory(fullPath);
  }

  private void verifyDestinationRoot()
  {
    if(!destinationRootVerified)
    {
      if(!fs.exists(destinationRoot))
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
    directory(fs.parentPath(filePath));
    final String templateContent = fs.readTextFile(fs.join(sourceRoot, template));

    final String destination = fs.join(destinationRoot, filePath);
    if(fs.exists(destination))
      fileExists(filePath);
    else
    {
      creatingFile(filePath);
      fs.createTextFile(destination, replaceTokens(templateContent));
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
