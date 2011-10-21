;- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
;- Limelight and all included source files are distributed under terms of the MIT License.

(ns limelight.theater-spec
  (:use
    [speclj.core]
    [limelight.spec-helper]
    [limelight.theater])
  (:import [limelight.theater Theater]))

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


