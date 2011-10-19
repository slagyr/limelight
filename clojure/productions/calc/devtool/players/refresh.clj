(on-mouse-clicked [e]
  (let [scene (.getRoot (.getRecipient e))
        stage (.getStage scene)
        production (.getProduction scene)
        stages (.getStages (.getTheater production))]
    (println stages)
    (doall (map
      (fn [stage]
        (println "stage: " stage)
        (.openScene production (.getName (.getScene stage)) stage (limelight.util.Opts. {})))
      stages))
    ))