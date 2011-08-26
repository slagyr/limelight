//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.util;

import limelight.Context;

public class TestUtil
{
  public static final String DATA_DIR = Context.fs().join("etc", "test_data");

  public static String dataDirPath(String... args)
  {
    return Context.fs().join(DATA_DIR, Context.fs().join(args));
  }
}
