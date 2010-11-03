(ns limelight.casting-director
  (:use [limelight.player]))

(declare *prop*)

;(defn install-player [prop player-path]
;  (let [player-content (.readTextFile (limelight.Context/fs) player-path)]
;    (binding [*ns* (the-ns 'limelight.casting-director)
;              *prop* prop]
;      (load-string player-content)
;      prop)))

(defn install-player [prop player-path]
  (let [player-content (.readTextFile (limelight.Context/fs) player-path)]
    (binding [*ns* (create-ns (symbol "foo"))
              *prop* prop]
      ;      (require [limelight.player])
      (use 'clojure.core)
      (use '[limelight.player])
      (def event-actions (atom {}))
      (binding [limelight.player/*action-cache* event-actions]
        (load-string player-content))
      prop)))

(defn install-player-from [prop player-path resource-loader]
  (if (.exists (limelight.Context/fs) (.pathTo resource-loader player-path))
    (install-player prop (.pathTo resource-loader player-path))
    nil))

(deftype CastingDirector []
  limelight.model.api.CastingDirector
  (castPlayer [this prop player-name]
    (let [player-path (str "players/" (limelight.util.StringUtil/underscore player-name) ".clj")]
      (if (install-player-from prop player-path (.getResourceLoader (.getRoot @(.peer prop))))
        true
        (install-player-from prop player-path (.getResourceLoader (.getProduction (.getRoot @(.peer prop)))))))))

;(defn on-mouse-clicked [action]
;  (.add (.getEventHandler @(.peer *prop*))
;    limelight.ui.events.panel.MouseClickedEvent
;    (reify limelight.events.EventAction
;      (invoke [this e] (action e)))))




