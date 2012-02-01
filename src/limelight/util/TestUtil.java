//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.util;

import limelight.io.FileSystem;

import java.awt.*;

public class TestUtil
{
  public static final FileSystem fs = new FileSystem();
  public static final String DATA_DIR = fs.absolutePath(fs.join("etc", "test_data"));
  public static Boolean headless;

  public static String dataDirPath(String... args)
  {
    return fs.join(DATA_DIR, args);
  }

  public static boolean notHeadless()
  {
    if(headless == null)
      headless = GraphicsEnvironment.isHeadless();
    return !headless;
  }
}
