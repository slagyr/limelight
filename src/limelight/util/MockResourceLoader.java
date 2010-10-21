//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.util;

public class MockResourceLoader extends ResourceLoader
{
  public String readTextResult;
  public String pathToReadText;

  public String pathTo(String path)
  {
    return path;
  }

  @Override
  public String readText(String path)
  {
    pathToReadText = path;
    return readTextResult;
  }
}
