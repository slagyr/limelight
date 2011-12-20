//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.java;

import limelight.model.CastingDirector;
import limelight.io.FakeFileSystem;
import limelight.model.api.Player;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class JavaPlayerRecruiterTest
{
  private FakeFileSystem fs;
  private JavaPlayerRecruiter recruiter;

  @Before
  public void setUp() throws Exception
  {
    CastingDirector.installed();
    fs = FakeFileSystem.installed();
    fs.createTextFile("/testProduction/production.xml", "<production/>");
    recruiter = new JavaPlayerRecruiter(new PlayerClassLoader("/testProduction/classes"));
  }

  @Test
  public void recruitsOnePlayer() throws Exception
  {
    JavaProductionTest.writeSamplePlayerTo(fs.outputStream("/testProduction/classes/SamplePlayer.class"));
    fs.createTextFile("/testProduction/aScene/players/foo.xml", "<player class='SamplePlayer'/>");

    final JavaPlayer player = (JavaPlayer)recruiter.recruitPlayer("foo", "/testProduction/aScene/players");

    assertNotNull(player);
    assertEquals("SamplePlayer", player.getPlayerClass().getName());
  }

  @Test
  public void cachesKnownPlayers() throws Exception
  {
    JavaProductionTest.writeSamplePlayerTo(fs.outputStream("/testProduction/classes/SamplePlayer.class"));
    fs.createTextFile("/testProduction/aScene/players/foo.xml", "<player class='SamplePlayer'/>");

    final Player player1 = recruiter.recruitPlayer("foo", "/testProduction/aScene/players");
    final Player player2 = recruiter.recruitPlayer("foo", "/testProduction/aScene/players");

    assertSame(player1, player2);
  }
}
