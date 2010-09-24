package limelight.model.api;

public interface ProductionProxy
{
  String getName();

  void setName(String name);

  boolean allowClose();

  void close();

  Object callMethod(String name, Object... args);

  void publish_on_drb(int port);

  void open();
}
