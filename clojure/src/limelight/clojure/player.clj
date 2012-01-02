;- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
;- Limelight and all included source files are distributed under terms of the MIT License.

(ns limelight.clojure.player)

(declare *action-cache*)

(defn add-action-with-bindings [event-type action]
  (let [event-action (reify limelight.events.EventAction (invoke [this e] (action e)))
        actions (or (@*action-cache* event-type) [])]
    (swap! *action-cache* assoc event-type (conj actions event-action))))

(defmacro add-action [event-type & forms]
  (if (vector? (first forms))
    `(add-action-with-bindings ~event-type (fn ~(first forms) ~@(rest forms)))
    `(add-action-with-bindings ~event-type (fn [~'%] ~@forms))))

(defmacro on-mouse-clicked [& forms] `(add-action ~limelight.ui.events.panel.MouseClickedEvent ~@forms))
(defmacro on-mouse-pressed [& forms] `(add-action ~limelight.ui.events.panel.MousePressedEvent ~@forms))
(defmacro on-mouse-released [& forms] `(add-action ~limelight.ui.events.panel.MouseReleasedEvent ~@forms))
(defmacro on-mouse-entered [& forms] `(add-action ~limelight.ui.events.panel.MouseEnteredEvent ~@forms))
(defmacro on-mouse-exited [& forms] `(add-action ~limelight.ui.events.panel.MouseExitedEvent ~@forms))
(defmacro on-mouse-moved [& forms] `(add-action ~limelight.ui.events.panel.MouseMovedEvent ~@forms))
(defmacro on-mouse-dragged [& forms] `(add-action ~limelight.ui.events.panel.MouseDraggedEvent ~@forms))
(defmacro on-mouse-wheel [& forms] `(add-action ~limelight.ui.events.panel.MouseWheelEvent ~@forms))
(defmacro on-key-pressed [& forms] `(add-action ~limelight.ui.events.panel.KeyPressedEvent ~@forms))
(defmacro on-key-released [& forms] `(add-action ~limelight.ui.events.panel.KeyReleasedEvent ~@forms))
(defmacro on-char-typed [& forms] `(add-action ~limelight.ui.events.panel.CharTypedEvent ~@forms))
(defmacro on-focus-gained [& forms] `(add-action ~limelight.ui.events.panel.FocusGainedEvent ~@forms))
(defmacro on-focus-lost [& forms] `(add-action ~limelight.ui.events.panel.FocusLostEvent ~@forms))
(defmacro on-button-pushed [& forms] `(add-action ~limelight.ui.events.panel.ButtonPushedEvent ~@forms))
(defmacro on-value-changed [& forms] `(add-action ~limelight.ui.events.panel.ValueChangedEvent ~@forms))
(defmacro on-illuminated [& forms] `(add-action ~limelight.ui.events.panel.IlluminatedEvent ~@forms))
(defmacro on-scene-opened [& forms] `(add-action ~limelight.ui.events.panel.SceneOpenedEvent ~@forms))
(defmacro on-cast [& forms] `(add-action :on-cast ~@forms))