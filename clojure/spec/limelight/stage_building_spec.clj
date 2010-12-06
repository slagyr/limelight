(ns limelight.stage-building-spec
  (:use
    [speclj.core]
    [limelight.stage-building]
    [limelight.production :only (new-production)]))

(describe "Stage building"
  (with peer-production (limelight.model.FakeProduction. "Mock Production"))
  (with production (new-production @peer-production))
  (with theater @(.theater @production))

  (it "can build one stage"
    (let [result (build-stages @theater "(stage \"One\")" "stages.clj")]
      (should= 1 (count (.getStages (.peer @theater))))))

  (it "applies options when building a stage"
    (let [result (build-stages @theater "(stage \"One\" {:title \"Super Stage\"})" "stages.clj")]
      (should="Super Stage"
        (-> @theater
        (.peer)
        (.get "One")
        (.getTitle)))))

  (it "can make multiple stages"
    (let [result (build-stages @theater "(stage \"One\" {:title \"Super Stage\"})(stage \"Two\")" "stages.clj")]
      (should= 2 (count (.getStages (.peer @theater))))))
  )

(run-specs)