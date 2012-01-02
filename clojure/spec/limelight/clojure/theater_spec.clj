;- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
;- Limelight and all included source files are distributed under terms of the MIT License.

(ns limelight.clojure.theater-spec
  (:use
    [speclj.core]
    [limelight.clojure.spec-helper]
    [limelight.clojure.theater]
    [limelight.clojure.production :only (new-production)])
  (:import [limelight.clojure.theater Theater]))

(describe "Theater"
  (with peer-production (limelight.model.FakeProduction.))
  (with production (new-production @peer-production))
  (with peer (limelight.model.Theater. @peer-production))
  (with theater (new-theater @peer @production))

  (it "has Java lineage"
    (should (.isInstance limelight.model.api.TheaterProxy @theater)))

  (it "is contructed properly"
    (should= @peer (._peer @theater))
    (should= @theater (.getProxy (._peer @theater)))
    (should= @production (._production @theater)))

  (unless-headless

    (it "can build a stage"
      (let [stage (.buildStage @theater "Limelight" {})]
        (should (.isInstance limelight.model.Stage stage))
        (should= "Limelight" (.getName stage))))
    )
  )


