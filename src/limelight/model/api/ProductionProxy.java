package limelight.model.api;

public interface ProductionProxy
{
  Object callMethod(String name, Object... args);

  void publish_on_drb(int port);

  void open();

  CastingDirector getCastingDirector();

  TheaterProxy getTheater();
}
