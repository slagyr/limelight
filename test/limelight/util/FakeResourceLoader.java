//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.util;

public class FakeResourceLoader extends ResourceLoader
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
