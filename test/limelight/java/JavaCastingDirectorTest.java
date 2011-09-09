//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.java;

import limelight.model.PlayerRecruiter;
import limelight.model.api.FakeCastingDirector;
import limelight.io.FakeFileSystem;
import limelight.ui.events.panel.CastEvent;
import limelight.ui.events.panel.MouseClickedEvent;
import limelight.ui.model.ScenePanel;
import limelight.util.OptionsMap;
import limelight.util.Util;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class JavaCastingDirectorTest
{
  private FakeFileSystem fs;
  private JavaProduction production;
  private JavaCastingDirector director;
  private ScenePanel scenePeer;
  private JavaScene scene;

  @Before
  public void setUp() throws Exception
  {
    PlayerRecruiter.installed();
    fs = FakeFileSystem.installed();
    fs.createTextFile("/testProduction/production.xml", "<production/>");
    production = new JavaProduction("/testProduction");
    production.illuminate();
    scenePeer = (ScenePanel)production.loadScene("aScene", new OptionsMap());
    scenePeer.setCastingDirector(new FakeCastingDirector());
    scenePeer.illuminate();
    scene = (JavaScene)scenePeer.getProxy();
    director = new JavaCastingDirector(new PlayerClassLoader("/testProduction/classes"));
  }

  @Test
  public void castsOnePlayer() throws Exception
  {
    JavaProductionTest.writeSamplePlayerTo(fs.outputStream("/testProduction/classes/SamplePlayer.class"));
    fs.createTextFile("/testProduction/aScene/players/foo.xml", "<player class='SamplePlayer'/>");

    final JavaProp prop = new JavaProp(Util.toMap("name", "foo"));
    scene.add(prop);
    director.castPlayer(prop, "foo", "/testProduction/aScene/players");

    assertEquals("foo", prop.getName());
    assertEquals(1, prop.getPlayers().size());
    Object player = prop.getPlayers().get(0);
    assertNotNull(player);
    assertEquals("SamplePlayer", player.getClass().getName());
  }
//
//  @Test
//  public void castedPlayerCanAddEvents() throws Exception
//  {
//    JavaProductionTest.writeSamplePlayerTo(fs.outputStream("/testProduction/classes/SamplePlayer.class"));
//    fs.createTextFile("/testProduction/aScene/players/foo.xml", "<player class='SamplePlayer'><onMouseClicked>sampleAction</onMouseClicked></player>");
//
//    final JavaProp prop = new JavaProp(Util.toMap("name", "foo"));
//    scene.add(prop);
//    director.castPlayer(prop, "foo", scenePlayersPath);
//
//    assertEquals(1, prop.getPeer().getEventHandler().getActions(MouseClickedEvent.class).size());
//    new MouseClickedEvent(0, null, 1).dispatch(prop.getPeer());
//    Object player = prop.getPlayers().get(0);
//    assertEquals(1, player.getClass().getField("invocations").get(player));
//  }
//
//  @Test
//  public void onCastEvent() throws Exception
//  {
//    JavaProductionTest.writeSamplePlayerTo(fs.outputStream("/testProduction/classes/SamplePlayer.class"));
//    fs.createTextFile("/testProduction/aScene/players/foo.xml", "<player class='SamplePlayer'><onCast>sampleActionWithEvent</onCast></player>");
//
//    final JavaProp prop = new JavaProp(Util.toMap("name", "foo"));
//    scene.add(prop);
//    director.castPlayer(prop, "foo", scenePlayersPath);
//
//    assertEquals(0, prop.getPeer().getEventHandler().getActions(CastEvent.class).size());
//    Object player = prop.getPlayers().get(0);
//    assertEquals(1, player.getClass().getField("invocations").get(player));
//    assertEquals(CastEvent.class, player.getClass().getField("event").get(player).getClass());
//  }

}
