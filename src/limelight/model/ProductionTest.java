package limelight.model;

import limelight.ui.api.CastingDirector;
import limelight.ui.api.MockCastingDirector;
import limelight.ui.api.MockProductionProxy;
import limelight.ui.api.ProductionProxy;
import limelight.util.ResourceLoader;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ProductionTest
{
  private Production production;

  private static class TestableProduction extends Production
  {
    public TestableProduction(String path)
    {
      super(path);
    }

    @Override
    public void close()
    {
    }
  }

  @Before
  public void setUp() throws Exception
  {
    production = new TestableProduction("/foo/bar");
  }

  @Test
  public void nameIsSetBasedOnPath() throws Exception
  {
    assertEquals("bar", production.getName());
  }

  @Test
  public void settingTheName() throws Exception
  {
    production.setName("foo");

    assertEquals("foo", production.getName());
  }

  @Test
  public void allowClose() throws Exception
  {
    assertEquals(true, production.allowClose());

    production.setAllowClose(false);

    assertEquals(false, production.allowClose());
  }

  @Test
  public void hasLoaderWithRoot() throws Exception
  {
    ResourceLoader loader = production.getResourceLoader();

    assertEquals("/foo/bar", loader.getRoot());
  }

  @Test
  public void accessingTheProxy() throws Exception
  {
    ProductionProxy proxy = new MockProductionProxy();
    assertEquals(null, production.getProxy());
    production.setProxy(proxy);

    assertEquals(proxy, production.getProxy());
  }

  @Test
  public void accessingTheCastingDirector() throws Exception
  {
    CastingDirector director = new MockCastingDirector();
    assertEquals(null, production.getCastingDirector());

    production.setCastingDirector(director);

    assertEquals(director, production.getCastingDirector());
  }
}
