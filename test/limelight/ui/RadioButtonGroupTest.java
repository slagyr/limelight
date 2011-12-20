package limelight.ui;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RadioButtonGroupTest
{
  private RadioButtonGroup group;
  private FakeRadioButtonGroupMember member1;
  private FakeRadioButtonGroupMember member2;
  private FakeRadioButtonGroupMember member3;

  @Before
  public void setUp() throws Exception
  {
    group = new RadioButtonGroup();
    member1 = new FakeRadioButtonGroupMember();
    member2 = new FakeRadioButtonGroupMember();
    member3 = new FakeRadioButtonGroupMember();
  }

  @Test
  public void addingMember() throws Exception
  {
    group.add(member1);

    assertEquals(1, group.getButtons().size());
    assertEquals(member1, group.getButtons().get(0));
    assertEquals(group, member1.radioButtonGroup);
  }

  @Test
  public void singleSelection() throws Exception
  {
    group.add(member1);
    group.add(member2);
    group.add(member3);

    select(member1);
    checkSelection(member1, member2, member3);

    select(member2);
    checkSelection(member2, member1, member3);

    select(member3);
    checkSelection(member3, member2, member1);
  }

  private void checkSelection(FakeRadioButtonGroupMember expected, FakeRadioButtonGroupMember... others)
  {
    assertEquals(expected, group.getSelection());
    assertEquals(true, expected.selected);
    for(FakeRadioButtonGroupMember other : others)
      assertEquals(false, other.selected);
  }

  private void select(RadioButtonGroupMember member)
  {
    member.setSelected(true);
    group.buttonSelected(member);
  }

  @Test
  public void removingMember() throws Exception
  {
    group.add(member1);
    group.remove(member1);

    assertEquals(0, group.getButtons().size());
    assertEquals(null, member1.radioButtonGroup);
  }

  @Test
  public void addingSelectedMember() throws Exception
  {
    member1.selected = true;
    group.add(member1);
    assertEquals(member1, group.getSelection());

    member2.selected = true;
    group.add(member2);
    assertEquals(member2, group.getSelection());
    assertEquals(false, member1.selected);
  }

  @Test
  public void removingSelectedMember() throws Exception
  {
    member1.selected = true;
    group.add(member1);
    group.add(member2);

    group.remove(member1);

    assertEquals(null, group.getSelection());
  }
}
