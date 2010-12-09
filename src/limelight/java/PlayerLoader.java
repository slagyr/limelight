package limelight.java;

import limelight.Context;
import limelight.LimelightException;
import limelight.io.StreamReader;

import java.lang.reflect.Constructor;

public class PlayerLoader extends ClassLoader
{
  private String classpath;

  public PlayerLoader()
  {
    super();
  }

  public void setClasspath(String classpath)
  {
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

  public Object loadPlayer(String name)
  {
    try
    {
      Class playerClass = loadClass(name);
      final Constructor constructor = playerClass.getConstructor();
      return constructor.newInstance();
    }
    catch(Exception e)
    {
      throw new LimelightException(e);
    }
  }
}
