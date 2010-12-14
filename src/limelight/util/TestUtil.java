//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

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
