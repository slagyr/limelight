//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.java;

import limelight.Context;
import limelight.io.StreamReader;

public class PlayerClassLoader extends ClassLoader
{
  private String classpath;

  public PlayerClassLoader(String classpath)
  {
    super();
    this.classpath = classpath;
  }

  @Override
  protected Class<?> findClass(String name) throws ClassNotFoundException
  {
    String path = classpath + "/" + name.replace('.', '/') + ".class";
    if(Context.fs().exists(path))
    {
      StreamReader reader = new StreamReader(Context.fs().inputStream(path));
      byte[] classBytes = reader.readAllBytes();
      reader.close();
      return defineClass(name, classBytes, 0, classBytes.length);
    }
    else
      throw new ClassNotFoundException(name);
  }

  public String getClasspath()
  {
    return classpath;
  }

  public void setClasspath(String classpath)
  {
    this.classpath = classpath;
  }
}
