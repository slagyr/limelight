package limelight.builtin;

import limelight.io.FileUtil;

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
      builtinPath = FileUtil.parentPath(resource.toString());
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
