(ns limelight.casting-director)

(declare *prop*)

(deftype CastingDirector []
  limelight.model.api.CastingDirector
  (castPlayer [this prop player-name]
    (let [player-path (str "players/" (limelight.util.StringUtil/underscore player-name) ".clj")
          resource-loader (.getResourceLoader (.getRoot @(.peer prop)))]
      (if (.exists (limelight.Context/fs) (.pathTo resource-loader player-path))
        (let [player-content (.readText resource-loader player-path)]
          (binding [*ns* (the-ns 'limelight.casting-director)
                    *prop* prop]
            (load-string player-content)))))))

(defn on-mouse-clicked [action]
  (.add (.getEventHandler @(.peer *prop*))
    limelight.ui.events.panel.MouseClickedEvent
    (reify limelight.events.EventAction
      (invoke [this e] (action e)))))


