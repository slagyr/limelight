;- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
;- Limelight and all included source files are distributed under terms of the MIT License.

(ns limelight.clojure.stage-building-spec
  (:use
    [speclj.core]
    [limelight.clojure.spec-helper]
    [limelight.clojure.stage-building]
    [limelight.clojure.production :only (new-production)]))

(describe "Stage building"
  (with peer-production (limelight.model.FakeProduction. "Mock Production"))
  (with production (new-production @peer-production))
  (with theater @(._theater @production))

  (unless-headless

    (it "can build one stage"
      (let [result (build-stages @theater "(stage \"One\")" "stages.clj")]
        (should= 1 (count (.getStages (._peer @theater))))))

    (it "applies options when building a stage"
      (let [result (build-stages @theater "(stage \"One\" {:title \"Super Stage\"})" "stages.clj")]
        (should= "Super Stage"
          (-> @theater
            (._peer)
            (.get "One")
            (.getTitle)))))

    (it "can make multiple stages"
      (let [result (build-stages @theater "(stage \"One\" {:title \"Super Stage\"})(stage \"Two\")" "stages.clj")]
        (should= 2 (count (.getStages (._peer @theater))))))
    )
  )

(run-specs)