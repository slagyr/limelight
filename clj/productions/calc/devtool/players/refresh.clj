(on-mouse-clicked
  (fn [e]
    (let [scene (.getRoot (.getRecipient e))
          stage (.getStage scene)
          production (.getProduction scene)
          stages (.getStages (.getTheater production))]
      (println stages)
      (doall (map
        (fn [stage]
          (println "stage: " stage)
          (.openScene production (.getName (.getScene stage)) stage (limelight.util.OptionsMap. {})))
        stages))
      )))
;  production.theater.stages.each do |stage|
;    if stage.current_scene
;      production.producer.open_scene(stage.current_scene.name, stage)
;    end
;  end
;end
