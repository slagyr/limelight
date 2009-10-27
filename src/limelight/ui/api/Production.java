package limelight.ui.api;

public interface Production
{
  String getName();
  void setName(String name);
  boolean allowClose();
  void close();
  Object callMethod(String name, Object... args);
}
