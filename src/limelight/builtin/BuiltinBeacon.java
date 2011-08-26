//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.builtin;

import limelight.Context;

import java.net.URL;

public class BuiltinBeacon
{
  private static String builtinPath;

  public static String getBuiltinPath()
  {
    if(builtinPath == null)
    {
      final Class<BuiltinBeacon> klass = BuiltinBeacon.class;
      final String resourcePath = klass.getName().replace(".", "/") + ".class";
      final URL resource = klass.getClassLoader().getResource(resourcePath);
      builtinPath = Context.fs().parentPath(resource.toString());
    }
    return builtinPath;
  }

  public static String getBuiltinPlayersPath()
  {
    return getBuiltinPath() + "/players";
  }

  public static String getBuiltinProductionsPath()
  {
    return getBuiltinPath() + "/productions";
  }
}
