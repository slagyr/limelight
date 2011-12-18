//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.model;

import limelight.builtin.BuiltinBeacon;
import limelight.io.FakeFileSystem;
import limelight.java.JavaPlayerRecruiter;
import limelight.model.api.*;
import limelight.ui.model.PropPanel;
import limelight.ui.model.ScenePanel;
import limelight.util.Util;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.*;

public class CastingDirectorTest
{
  private PropPanel panel;
  private CastingDirector castingDirector;
  private FakePlayerRecruiter playerRecruiter;
  private FakePlayerRecruiter builtinPlayerRecruiter;

  @Before
  public void setUp() throws Exception
  {
    FakeProduction production = new FakeProduction("/path/to/testProduction");
    ScenePanel scene = new ScenePanel(new FakePropProxy(), Util.toMap("name", "theScene", "path", "theScene"));
    scene.setProduction(production);
    panel = new PropPanel(new FakePropProxy());
    scene.add(panel);
    playerRecruiter = new FakePlayerRecruiter();
    castingDirector = new CastingDirector();
    builtinPlayerRecruiter = new FakePlayerRecruiter();
    castingDirector.setBuiltinPlayerRecruiter(builtinPlayerRecruiter);
    FakeFileSystem.installed();
  }

  @Test
  public void recruitingFromTheScene() throws Exception
  {
    playerRecruiter.recruitablePath = "/path/to/testProduction/theScene/players";

    Player player = castingDirector.castRole(panel, "blah", playerRecruiter);

    assertEquals("blah", player.getName());
    assertEquals("/path/to/testProduction/theScene/players/blah", player.getPath());
    assertSame(player, panel.getPlayers().get(0));
    assertSame(panel, ((FakePlayer)player).castedProp);
  }

  @Test
  public void recruitingFromTheProduction() throws Exception
  {
    playerRecruiter.recruitablePath = "/path/to/testProduction/players";

    Player player = castingDirector.castRole(panel, "blah", playerRecruiter);

    assertEquals("blah", player.getName());
    assertEquals("/path/to/testProduction/players/blah", player.getPath());
    assertSame(player, panel.getPlayers().get(0));
    assertSame(panel, ((FakePlayer)player).castedProp);
  }

  @Test
  public void builtinCastingDirector() throws Exception
  {
    castingDirector = new CastingDirector();
    final PlayerRecruiter director = castingDirector.getBuiltinPlayerRecruiter();
    assertNotNull(director);
    assertEquals(JavaPlayerRecruiter.class, director.getClass());
    assertEquals(ClassLoader.getSystemClassLoader(), ((JavaPlayerRecruiter)director).getClassLoader());
  }

  @Test
  public void recruitBuiltinPlayers() throws Exception
  {
    playerRecruiter.recruitablePath = "none";
    builtinPlayerRecruiter.recruitablePath = BuiltinBeacon.getBuiltinPlayersPath();

    Player player = castingDirector.castRole(panel, "blah", playerRecruiter);

    assertEquals("blah", player.getName());
    assertEquals(BuiltinBeacon.getBuiltinPlayersPath() + "/blah", player.getPath());
    assertSame(player, panel.getPlayers().get(0));
    assertSame(panel, ((FakePlayer)player).castedProp);
  }
}
