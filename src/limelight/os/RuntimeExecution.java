//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.os;

import java.io.IOException;

public class RuntimeExecution
{
  public Process exec(String... strings) throws IOException
  {
    Runtime rt = Runtime.getRuntime();
    return rt.exec(strings);
  }
}
