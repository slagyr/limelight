//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.java;

import limelight.model.CastingDirector;
import limelight.io.FakeFileSystem;
import limelight.model.api.FakePlayerRecruiter;
import limelight.model.api.Player;
import limelight.ui.events.panel.CastEvent;
import limelight.ui.events.panel.MouseClickedEvent;
import limelight.ui.model.ScenePanel;
import limelight.util.Opts;
import limelight.util.Util;
import org.junit.Before;
import org.junit.Test;
import sun.tools.javap.oldjavap.JavaP;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class JavaPlayerRecruiterTest
{
  private FakeFileSystem fs;
  private JavaPlayerRecruiter recruiter;
  private JavaScene scene;

  @Before
  public void setUp() throws Exception
  {
    CastingDirector.installed();
    fs = FakeFileSystem.installed();
    fs.createTextFile("/testProduction/production.xml", "<production/>");
    JavaProduction production = new JavaProduction("/testProduction");
    production.illuminate();
    ScenePanel scenePeer = (ScenePanel) production.loadScene("aScene", new Opts());
    scenePeer.setPlayerRecruiter(new FakePlayerRecruiter());
    scenePeer.illuminate();
    scene = (JavaScene) scenePeer.getProxy();
    recruiter = new JavaPlayerRecruiter(new PlayerClassLoader("/testProduction/classes"));
  }

  @Test
  public void recruitsOnePlayer() throws Exception
  {
    JavaProductionTest.writeSamplePlayerTo(fs.outputStream("/testProduction/classes/SamplePlayer.class"));
    fs.createTextFile("/testProduction/aScene/players/foo.xml", "<player class='SamplePlayer'/>");

    final JavaProp prop = new JavaProp(Util.toMap("name", "foo"));
    scene.add(prop);
    final JavaPlayer player = (JavaPlayer)recruiter.recruitPlayer(prop, "foo", "/testProduction/aScene/players");

    assertNotNull(player);
    assertEquals("SamplePlayer", player.getPlayer().getClass().getName());
  }
}
