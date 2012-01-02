;- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
;- Limelight and all included source files are distributed under terms of the MIT License.

(ns limelight.clojure.scene-spec
  (:use
    [speclj.core]
    [limelight.clojure.spec-helper]
    [limelight.clojure.scene])
  (:import
    [limelight.clojure.scene Scene]
    [limelight.clojure.casting PlayerRecruiter]))

(describe "Scene"

  (it "has limelight lineage"
    (should (isa? Scene limelight.model.api.SceneProxy)))

  (it "can be created"
    (let [scene (new-scene (hash-map :name "Bill"))]
      (should-not= nil @(._peer scene))
      (should= @(._peer scene) (.getPeer scene))
      (should= limelight.ui.model.ScenePanel (type @(._peer scene)))
      (should= PlayerRecruiter (type @(._player-recruiter scene)))
      (should= @(._player-recruiter scene) (.getPlayerRecruiter @(._peer scene)))))
  )

(run-specs)

