//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.model;

import limelight.*;
import limelight.model.events.ProductionClosedEvent;
import limelight.ruby.MockRuntimeFactory;
import limelight.model.api.UtilitiesProduction;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;

public class StudioTest
{
  private Studio studio;
  private MockContext context;
  private MockRuntimeFactory mockRuntimeFactory;

  @Before
  public void setUp() throws Exception
  {
    Studio.uninstall();
    System.setProperty("limelight.home", "."); // For the RuntimeFactory to spawn productions properly
    context = MockContext.stub();
    studio = Studio.install();

    mockRuntimeFactory = new MockRuntimeFactory();
    context.runtimeFactory = mockRuntimeFactory;
  }

  @Test
  public void installing() throws Exception
  {
    assertSame(Studio.instance(), Context.instance().studio);
  }

  @Test
  public void indexingProductions() throws Exception
  {
    MockProduction production = new MockProduction("Max");
    add(production);

    assertSame(production, studio.get("Max"));
  }

  @Test
  public void getProductionWithNull() throws Exception
  {
    assertEquals(null, studio.get(null));
  }

  @Test
  public void itShouldNotAllowShutdownIfProuctionsDecline() throws Exception
  {
    MockProduction production = new MockProduction("Max");
    add(production);
    production.setAllowClose(false);

    assertEquals(false, studio.shouldAllowShutdown());
  }

  @Test
  public void itShouldAllowShutdownIfProuctionsAllow() throws Exception
  {
    MockProduction production = new MockProduction("Max");
    add(production);
    production.setAllowClose(true);

    assertEquals(true, studio.shouldAllowShutdown());
  }

  @Test
  public void removedClosedProductions() throws Exception
  {
    MockProduction production1 = new MockProduction("One");
    MockProduction production2 = new MockProduction("Two");
    add(production1);
    add(production2);

    new ProductionClosedEvent().dispatch(production1);

    assertEquals(null, studio.get("One"));
    assertEquals(1, studio.getProductions().size());
  }

  @Test
  public void shouldShutdownWhenLastProductionIsClosed() throws Exception
  {
    MockProduction production = new MockProduction("Max");
    add(production);

    new ProductionClosedEvent().dispatch(production);

    assertEquals(true, context.wasShutdown);    
  }

  @Test
  public void shouldGiveProductionsaNameIfItDoesntHaveOne() throws Exception
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

  @Test
  public void shouldGiveProductionsNewNamesWhenDuplicated() throws Exception
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

  @Test
  public void shouldShutdown() throws Exception
  {
    MockProduction production = new MockProduction("Max");
    add(production);
    production.setAllowClose(true);

    studio.shutdown();
    studio.shutdownThread.join();
    
    assertEquals(true, production.wasAskedIfAllowedToShutdown);
    assertEquals(true, production.closeFinalized);
    assertEquals(true, context.wasShutdown);
    assertEquals(true, studio.isShutdown());
  }

  private void add(MockProduction production)
  {
    studio.add(production);
  }

  @Test
  public void haveAUtilitiesProduction() throws Exception
  {
    MockProduction production = new MockProduction("utilities");
    studio.productionStub = production;

    UtilitiesProduction utilities = studio.utilitiesProduction();
    assertSame(production, utilities.getProduction());

    assertSame(utilities, studio.utilitiesProduction()); // no exception thrown
  }

// TODO MDM - This needs to be fixed
//  public void testShouldSendAlertWhenErrorOccursWhileOpeningProduction() throws Exception
//  {
//    mockRuntimeFactory.spawnException = new LimelightException("blah");
//    MockProduction production = new MockProduction("utilities");
//    studio.stubUtilitiesProduction(production);
//
//    studio.open("anything");
//
//    assertEquals("alert", production.lastMethodCalled);
//    assertEquals(true, production.lastMethodCallArgs[0].toString().contains("blah"));
//  }

//  public void testPublishProductionsOnDRb() throws Exception
//  {
//    MockProduction prod1 = new MockProduction("one");
//    MockProduction prod2 = new MockProduction("two");
//    MockProduction prod3 = new MockProduction("three");
//
//    studio.publishProductionsOnDRb(1234);
//    mockRuntimeFactory.spawnedProduction = prod1;
//    studio.open("one");
//    mockRuntimeFactory.spawnedProduction = prod2;
//    studio.open("two");
//    mockRuntimeFactory.spawnedProduction = prod3;
//    studio.open("three");
//
//    assertEquals(1234, prod1.drbPort);
//    assertEquals(1235, prod2.drbPort);
//    assertEquals(1236, prod3.drbPort);
//  }
//
//  public void testUtilitiedProductionDoesNotGetPublishedOnDRb() throws Exception
//  {
//    MockProduction production = new MockProduction("utilities");
//    mockRuntimeFactory.spawnedProduction = production;
//
//    studio.publishProductionsOnDRb(1234);
//    studio.utilitiesProduction();
//
//    assertEquals(0, production.drbPort);
//  }
}
