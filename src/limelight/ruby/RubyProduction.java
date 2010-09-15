package limelight.ruby;

import limelight.Context;
import limelight.ui.api.Production;

public class RubyProduction implements Production
{
  private String path;
  private RuntimeFactory.BirthCertificate certificate;

  public RubyProduction(String path)
  {
    this.path = path;
  }

  public void open()
  {
    String src = RuntimeFactory.openProductionSrc(path);
    certificate = Context.instance().runtimeFactory.spawn(src);
  }

  public String getName()
  {
    return null;
  }

  public void setName(String name)
  {
  }

  public boolean allowClose()
  {
    return false;
  }

  public void close()
  {
    Context.instance().runtimeFactory.terminate(certificate);
  }

  public Object callMethod(String name, Object... args)
  {
    return null;
  }

  public void publish_on_drb(int port)
  {
  }
}
