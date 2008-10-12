package limelight;

public class MockResourceLoader implements ResourceLoader
{
  public String pathTo(String relativePath)
  {
    return relativePath;
  }
}
