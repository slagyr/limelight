//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.api;

import junit.framework.TestCase;
import limelight.*;

public class StudioTest extends TestCase
{
  private Studio studio;
  private MockContext context;
  private MockRuntimeFactory mockRuntimeFactory;

  public void setUp() throws Exception
  {
    Studio.uninstall();
    System.setProperty("limelight.home", "."); // For the RuntimeFactory to spawn productions properly
    context = MockContext.stub();
    studio = Studio.install();

    mockRuntimeFactory = new MockRuntimeFactory();
    context.runtimeFactory = mockRuntimeFactory;
  }

  public void testInstalling() throws Exception
  {
    assertSame(Studio.instance(), Context.instance().studio);
  }

  public void testIndexingProductions() throws Exception
  {
    MockProduction production = new MockProduction("Max");
    add(production);

    assertSame(production, studio.get("Max"));
  }
  
  public void testGetProductionWithNull() throws Exception
  {
    assertEquals(null, studio.get(null));
  }

  public void testItShouldNotAllowShutdownIfProuctionsDecline() throws Exception
  {
    MockProduction production = new MockProduction("Max");
    add(production);
    production.allowShutdown = false;

    assertEquals(false, studio.shouldAllowShutdown());
  }

  public void testItShouldAllowShutdownIfProuctionsAllow() throws Exception
  {
    MockProduction production = new MockProduction("Max");
    add(production);
    production.allowShutdown = true;

    assertEquals(true, studio.shouldAllowShutdown());
  }

  public void testRemovedClosedProductions() throws Exception
  {
    MockProduction production1 = new MockProduction("One");
    MockProduction production2 = new MockProduction("Two");
    add(production1);
    add(production2);

    studio.productionClosed(production1);

    assertEquals(null, studio.get("One"));
    assertEquals(1, studio.getProductions().size());
  }

  public void testShouldShutdownWhenLastProductionIsClosed() throws Exception
  {
    MockProduction production = new MockProduction("Max");
    add(production);

    studio.productionClosed(production);

    assertEquals(true, context.wasShutdown);    
  }

  public void testShouldGiveProductionsaNameIfItDoesntHaveOne() throws Exception
  {
    MockProduction production1 = new MockProduction(null);
    MockProduction production2 = new MockProduction("");
    MockProduction production3 = new MockProduction(" \t\n");
    add(production1);
    add(production2);
    add(production3);

    assertEquals("anonymous", production1.getName());
    assertEquals("anonymous_2", production2.getName());
    assertEquals("anonymous_3", production3.getName());
  }

  public void testShouldGiveProductionsNewNamesWhenDuplicated() throws Exception
  {
    MockProduction production1 = new MockProduction("Fido");
    MockProduction production2 = new MockProduction("Fido");
    MockProduction production3 = new MockProduction("Fido");
    add(production1);
    add(production2);
    add(production3);

    assertEquals("Fido", production1.getName());
    assertEquals("Fido_2", production2.getName());
    assertEquals("Fido_3", production3.getName());
  }

  public void testShouldShutdown() throws Exception
  {
    MockProduction production = new MockProduction("Max");
    add(production);
    production.allowShutdown = true;

    studio.shutdown();
    studio.shutdownThread.join();
    
    assertEquals(true, production.wasAskedIfAllowedToShutdown);
    assertEquals(true, production.wasClosed);
    assertEquals(true, context.wasShutdown);
    assertEquals(true, studio.isShutdown());
  }

  private void add(MockProduction production)
  {
    studio.add(new RuntimeFactory.BirthCertificate(production));
  }

  public void testHaveAUtilitiesProduction() throws Exception
  {
    MockProduction production = new MockProduction("utilities");
    mockRuntimeFactory.spawnedProduction = production;

    UtilitiesProduction utilities = studio.utilitiesProduction();
    assertSame(production, utilities.getProduction());

    mockRuntimeFactory.spawnException = new Exception("blah");
    assertSame(utilities, studio.utilitiesProduction()); // no exception thrown
  }

  public void testShouldSendAlertWhenErrorOccursWhileOpeningProduction() throws Exception
  {
    mockRuntimeFactory.spawnException = new Exception("blah");
    MockProduction production = new MockProduction("utilities");
    studio.stubUtilitiesProduction(production);

    studio.open("anything");

    assertEquals("alert", production.lastMethodCalled);
    assertEquals(true, production.lastMethodCallArgs[0].toString().contains("blah"));
  }
}
