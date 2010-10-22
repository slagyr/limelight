(ns limelight.casting-director)

(declare *prop*)

(deftype CastingDirector [loader]
  limelight.model.api.CastingDirector
  (castPlayer [this prop player-name]
    (let [prop-name (.getName @(.peer prop))
          player-path (str "players/" prop-name ".clj")
          resource-loader (.getResourceLoader (.getRoot @(.peer prop)))]
      (if (limelight.io.FileUtil/exists (.pathTo resource-loader player-path))
        (let [player-content (.readText resource-loader (str "players/" prop-name ".clj"))]
          (binding [*ns* (the-ns 'limelight.casting-director)
                    *prop* prop]
            (load-string player-content)))))))

(defn on-mouse-clicked [action]
  (.add (.getEventHandler @(.peer *prop*))
    limelight.ui.events.panel.MouseClickedEvent
    (reify limelight.events.EventAction
      (invoke [this e] (action e)))))
