;- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
;- Limelight and all included source files are distributed under terms of the MIT License.

(ns limelight.clojure.prop
  (:use
    [limelight.clojure.util :only (map-for-clojure)]))

(def prop-ns (the-ns 'limelight.clojure.prop))

(defn- apply-options [prop options]
  (loop [keys (keys options) options options]
    (if (not (seq keys))
      options
      (let [key (first keys)]
        (if-let [fn-var (ns-resolve prop-ns (symbol key))]
          (do
            (@fn-var prop (get options key))
            (recur (rest keys) (dissoc options key)))
          (recur (rest keys) options))))))

(deftype Prop [_peer]
  limelight.model.api.PropProxy
  (applyOptions [this options]
    (let [opts (map-for-clojure options)]
      (apply-options this opts))
    options)
  (getPeer [this] @_peer))

(defn new-prop [options]
  (let [prop (Prop. (atom nil))
        peer (limelight.ui.model.PropPanel. prop)]
    (swap! (._peer prop) (fn [_] peer))
    (.addOptions peer options)
    prop))

(defn- ->event-action [action]
  (cond
    (fn? action) (reify limelight.events.EventAction (invoke [this e] (action e)))
    (seq? action) (reify limelight.events.EventAction (invoke [this e] (eval action)))
    (string? action) (recur (read (java.io.PushbackReader. (java.io.StringReader. action))))
    :else (throw (limelight.LimelightException. (str "Don't know how to create EventAction from: " (class action) ": " action)))))

(defn- add-event-action [prop event-class action]
  (let [event-action (->event-action action)
        event-handler (.getEventHandler @(._peer prop))]
    (.add event-handler event-class event-action)))

(defn on-mouse-clicked [prop action] (add-event-action prop limelight.ui.events.panel.MouseClickedEvent action))
(defn on-mouse-pressed [prop action] (add-event-action prop limelight.ui.events.panel.MousePressedEvent action))
(defn on-mouse-released [prop action] (add-event-action prop limelight.ui.events.panel.MouseReleasedEvent action))
(defn on-mouse-entered [prop action] (add-event-action prop limelight.ui.events.panel.MouseEnteredEvent action))
(defn on-mouse-exited [prop action] (add-event-action prop limelight.ui.events.panel.MouseExitedEvent action))
(defn on-mouse-moved [prop action] (add-event-action prop limelight.ui.events.panel.MouseMovedEvent action))
(defn on-mouse-dragged [prop action] (add-event-action prop limelight.ui.events.panel.MouseDraggedEvent action))
(defn on-mouse-wheel [prop action] (add-event-action prop limelight.ui.events.panel.MouseWheelEvent action))
(defn on-key-pressed [prop action] (add-event-action prop limelight.ui.events.panel.KeyPressedEvent action))
(defn on-key-released [prop action] (add-event-action prop limelight.ui.events.panel.KeyReleasedEvent action))
(defn on-char-typed [prop action] (add-event-action prop limelight.ui.events.panel.CharTypedEvent action))
(defn on-focus-gained [prop action] (add-event-action prop limelight.ui.events.panel.FocusGainedEvent action))
(defn on-focus-lost [prop action] (add-event-action prop limelight.ui.events.panel.FocusLostEvent action))
(defn on-button-pushed [prop action] (add-event-action prop limelight.ui.events.panel.ButtonPushedEvent action))
(defn on-value-changed [prop action] (add-event-action prop limelight.ui.events.panel.ValueChangedEvent action))
(defn on-illuminated [prop action] (add-event-action prop limelight.ui.events.panel.IlluminatedEvent action))
(defn on-scene-opened [prop action] (add-event-action prop limelight.ui.events.panel.SceneOpenedEvent action))