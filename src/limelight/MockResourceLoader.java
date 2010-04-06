//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight;

public class MockResourceLoader implements ResourceLoader
{
  public String pathTo(String relativePath)
  {
    return relativePath;
  }
}
