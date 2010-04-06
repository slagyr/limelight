//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.util;

import limelight.io.FileUtil;

public class TestUtil
{
  public static final String TMP_DIR = FileUtil.buildPath("etc", "tmp");
  public static final String DATA_DIR = FileUtil.buildPath("etc", "test_data");

  public static String dataDirPath(String... args)
  {
    return FileUtil.buildOnPath(DATA_DIR, args);
  }

  public static String tmpDirPath(String... args)
  {
    return FileUtil.buildOnPath(TMP_DIR, args);
  }
}
