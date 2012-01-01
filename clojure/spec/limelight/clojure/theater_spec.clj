;- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
;- Limelight and all included source files are distributed under terms of the MIT License.

(ns limelight.clojure.theater-spec
  (:use
    [speclj.core]
    [limelight.clojure.spec-helper]
    [limelight.clojure.theater])
  (:import [limelight.clojure.theater Theater]))

(describe "Theater"
  (with theater (Theater. :peer :production))

  (it "has Java lineage"
    (should (.isInstance limelight.model.api.TheaterProxy @theater)))

  (it "is contructed properly"
    (should= :peer (.peer @theater))
    (should= :production (.production @theater)))

  (unless-headless

    (it "can build a stage"
      (let [stage (.buildStage @theater "Limelight" {})]
        (should (.isInstance limelight.model.Stage stage))
        (should= "Limelight" (.getName stage))))
    )
  )


