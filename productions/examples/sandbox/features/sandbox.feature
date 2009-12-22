Feature: Sandbox
  In order to play with Limelight features
  As a user
  I want to see the features in action.

  Scenario: Sandbox
    Given I have the sanbox production opened
    When I do nothing
    Then The active stage title should be "Limelight Sandbox"

  Scenario: Click Me Scene
    Given I have the sanbox production opened
    And I click on "click_me_link"
    When I click on "chromaton"
    Then The text of "chromaton" should include "Clicks: 1"
    When I click on "chromaton"
    Then The text of "chromaton" should include "Clicks: 2"
    When I click on "chromaton"
    Then The text of "chromaton" should include "Clicks: 3"
    When I click on "chromaton"
    Then The text of "chromaton" should include "Clicks: 4"
    When I click on "chromaton"
    Then The text of "chromaton" should include "Clicks: 5"

  Scenario: Teaser Scene
    Given I have the sanbox production opened
    And I click on "teaser_link"
    When I hover over "fader1"
    And I hover over "fader2"
    And I hover over "fader3"
    And I hover over "fader4"

  Scenario: Homer Scene
    Given I have the sanbox production opened
    And I click on "homer_link"
    When I hover over "homer"
    And I hover over "nothing"

  Scenario: Gradients Scene
    Given I have the sanbox production opened
    And I click on "gradients_link"
    When I hover over "gradient1"
    And I hover over "gradient2"
    And I hover over "gradient3"
    And I hover over "gradient4"

  Scenario: Scrolling Scene
    Given I have the sanbox production opened
    And I click on "scrolling_link"
    When I scroll "right" in "one_table"
    When I scroll "down" in "one_table"
    When I scroll "right" in "two_table"
    When I scroll "down" in "three_table"

  Scenario: Inputs Scene
    Given I have the sanbox production opened
    And I click on "inputs_link"
    When I set value of "text_box_input" to "hello there"
    Then The text of "text_box_input" should include "hello there"
    And I set value of "text_area_input" to "I'm a little teapot"
    Then The text of "text_area_input" should include "teapot"
    And I set value of "check_box_input" to "on"
    Then The text of "check_box_input" should include "on"
    And I set value of "radio_1" to "on"
    Then The text of "radio_1" should include "on"
    And I set value of "radio_2" to "on"
    Then The text of "radio_2" should include "on"
    And I set value of "radio_3" to "on"
    Then The text of "radio_3" should include "on"
    And I set value of "combo_box_input" to "Indigo"
    Then The text of "combo_box_input" should include "Indigo"

  Scenario: Rounded Corner Scene
    Given I have the sanbox production opened
    And I click on "rounded_corners_link"
    When I click on all the rounded corners

  Scenario: Floaters Scene
    Given I have the sanbox production opened
    And I click on "floaters_link"
    When I move the mouse over "surface" at 99, 99
    And I move the mouse over "surface" at 651, 651

  Scenario: Sketching Scene
    Given I have the sanbox production opened
    And I click on "sketching_link"
    When I press "line"
    And I press the mouse on "sketchpad" at 50, 50
    And I drag the mouse on "sketchpad" to 550, 550
    And I press the mouse on "sketchpad" at 50, 550
    And I drag the mouse on "sketchpad" to 550, 50
    And I press "square"
    And I press the mouse on "sketchpad" at 300, 100
    And I press the mouse on "sketchpad" at 300, 500
    And I press "circle"
    And I press the mouse on "sketchpad" at 100, 300
    And I press the mouse on "sketchpad" at 500, 300

  Scenario: Images Scene
    Given I have the sanbox production opened
    And I click on "images_link"
    When I set value of "width_input" to "500"
    And I set value of "height_input" to "300"
    And I press "scaled_checkbox"
    And I press "scaled_checkbox"
    And I set value of "rotation_input" to "45"
    And I set value of "rotation_input" to "90"
    And I set value of "rotation_input" to "120"
    And I set value of "rotation_input" to "0"
    And I press "scaled_checkbox"
    And I set value of "horizontal_align_input" to "center"
    And I set value of "vertical_align_input" to "center"
    And I set value of "horizontal_align_input" to "right"
    And I set value of "vertical_align_input" to "bottom"

  Scenario: Framing Scene
    Given I have the sanbox production opened
    And I click on "frameing_link"
    When I set value of "margin_input" to "20%"
    When I set value of "border_input" to "20%"
    When I set value of "padding_input" to "20%"
    When I set value of "corner_input" to "20%"

  Scenario: Background Image Scene
    Given I have the sanbox production opened
    And I click on "background_image_link"
    When I set value of "fill_strategy_input" to "repeat_x"
    When I set value of "fill_strategy_input" to "repeat_y"
    When I set value of "fill_strategy_input" to "scaled"
    When I set value of "fill_strategy_input" to "scaled_x"
    When I set value of "fill_strategy_input" to "scaled_y"
    When I set value of "fill_strategy_input" to "static"
    When I set value of "x_input" to "center"
    When I set value of "y_input" to "center"
    When I set value of "x_input" to "right"
    When I set value of "y_input" to "bottom"

#  example_link :text => "Kiosk", :on_mouse_clicked => "scene.load('kiosk')", :id => "kiosk_link"
#  example_link :text => "Alerts", :on_mouse_clicked => "scene.load('alerts')", :id => "alerts_link"
#  example_link :text => "Stage Handles", :on_mouse_clicked => "scene.load('stage_handles')", :id => "stage_handles_link"


