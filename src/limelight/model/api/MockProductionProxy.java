package limelight.model.api;

public class MockProductionProxy implements ProductionProxy
{
  public CastingDirector castingDirector;
  public TheaterProxy theater;

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
  }

  public Object callMethod(String name, Object... args)
  {
    return null;
  }

  public void publish_on_drb(int port)
  {
  }

  public void open()
  {
  }

  public CastingDirector getCastingDirector()
  {
    return castingDirector;
  }

  public TheaterProxy getTheater()
  {
    return theater;
  }
}
