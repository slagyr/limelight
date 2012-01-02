;- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
;- Limelight and all included source files are distributed under terms of the MIT License.

(ns limelight.clojure.prop-spec
  (:use
    [speclj.core]
    [limelight.clojure.spec-helper]
    [limelight.clojure.prop])
  (:import
    [limelight.clojure.prop Prop]))

(defn actions-for [prop event]
  (-> prop
    (._peer)
    (deref)
    (.getEventHandler)
    (.getActions event)))

(describe "Prop"

  (it "has limelight lineage"
    (should (isa? Prop limelight.model.api.PropProxy)))

  (it "can be created"
    (let [prop (new-prop (hash-map :name "Bill"))]
      (should-not= nil @(._peer prop))
      (should= limelight.ui.model.PropPanel (type @(._peer prop)))))

  (context "event options"

    (with prop (new-prop {:name "Bill"}))

    (for [[event name] {(limelight.ui.events.panel.MouseClickedEvent. 0 nil 0) "mouse-clicked"
                        (limelight.ui.events.panel.MousePressedEvent. 0 nil 0) "mouse-pressed"
                        (limelight.ui.events.panel.MouseReleasedEvent. 0 nil 0) "mouse-released"
                        (limelight.ui.events.panel.MouseMovedEvent. 0 nil 0) "mouse-moved"
                        (limelight.ui.events.panel.MouseDraggedEvent. 0 nil 0) "mouse-dragged"
                        (limelight.ui.events.panel.MouseEnteredEvent. 0 nil 0) "mouse-entered"
                        (limelight.ui.events.panel.MouseExitedEvent. 0 nil 0) "mouse-exited"
                        (limelight.ui.events.panel.MouseWheelEvent. 0, nil, 1, 0, 0, 0) "mouse-wheel"
                        (limelight.ui.events.panel.KeyPressedEvent. 0 0 0) "key-pressed"
                        (limelight.ui.events.panel.KeyReleasedEvent. 0 0 0) "key-released"
                        (limelight.ui.events.panel.CharTypedEvent. 0 \a) "char-typed"
                        (limelight.ui.events.panel.FocusGainedEvent.) "focus-gained"
                        (limelight.ui.events.panel.FocusLostEvent.) "focus-lost"
                        (limelight.ui.events.panel.ButtonPushedEvent.) "button-pushed"
                        (limelight.ui.events.panel.ValueChangedEvent.) "value-changed"
                        (limelight.ui.events.panel.IlluminatedEvent. nil) "illuminated"
                        (limelight.ui.events.panel.SceneOpenedEvent.) "scene-opened"
                        }]
      (for [[action-type action] {"fn" (fn [_] (System/setProperty "test.event" name))
                                  "data" `(System/setProperty "test.event" ~name)
                                  "string" (str "(System/setProperty \"test.event\" \"" name "\")")}]
        (it (str "handles " name " options with " action-type)
          (let [actions-before (actions-for @prop (class event))
                options (limelight.util.Opts. {(keyword (str "on-" name)) action})]
            (.applyOptions @prop options)
            (let [actions-after (actions-for @prop (class event))]
              (should= 1 (- (count actions-after) (count actions-before)))
              (should-not-throw (.dispatch event @(._peer @prop)))
              (should= name (System/getProperty "test.event"))
              (should= 0 (count options)))))
        )
      )
    )
  )




