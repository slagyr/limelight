;- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
;- Limelight and all included source files are distributed under terms of the MIT License.

(ns limelight.clojure.core-spec
  (:use
    [speclj.core]
    [limelight.clojure.spec-helper]
    [limelight.clojure.core]
    [limelight.clojure.prop-building :only (to-props)]
    [limelight.clojure.scene :only (new-scene)]
    [limelight.clojure.production :only (new-production)]))

(defn build-tree []
  (let [scene (new-scene {:id "root-id" :name "root" :path "root"})]
    (add-props scene (to-props
                       [[:child {:id "child1"}
                         [:grand-child {:id "grand-child1"}
                          [:great-grand-child {:id "great-grand-child1"}]
                          [:great-grand-child {:id "great-grand-child2"}]]
                         [:grand-child {:id "grand-child2"}
                          [:great-grand-child {:id "great-grand-child3"}]]]
                        [:child {:id "child2"}
                         [:grand-child {:id "grand-child3"}]]]))
    (.setPlayerRecruiter @(._peer scene) (limelight.model.api.FakePlayerRecruiter.))
    (.illuminate @(._peer scene))
    scene))

(describe "Core"

  (before-all (boot-limelight))

  (context "finding props"

    (with scene (build-tree))

    (it "finds root by name"
      (let [roots (find-props-named @scene "root")]
        (should= 1 (count roots))
        (should (identical? @scene (first roots)))))

    (it "finds children by name"
      (let [children (find-props-named @scene "child")]
        (should= 2 (count children))
        (should= ["child1" "child2"] (map prop-id children))))

    (it "finds grand children by name"
      (let [grand-children (find-props-named @scene "grand-child")]
        (should= 3 (count grand-children))
        (should= ["grand-child1" "grand-child2" "grand-child3"] (map prop-id grand-children))))

    (it "finds great grand children by name"
      (let [great-grand-children (find-props-named @scene "great-grand-child")]
        (should= 3 (count great-grand-children))
        (should= ["great-grand-child1" "great-grand-child2" "great-grand-child3"] (map prop-id great-grand-children))))

    (it "finds root by id"
      (let [result (find-prop @scene "root-id")]
        (should (identical? @scene result))))

    (it "finds child1 by id"
      (let [result (find-prop @scene "child1")]
        (should= "child" (prop-name result))
        (should= @scene (parent-prop result))))

    (it "finds great-grand-child3 by id"
      (let [result (find-prop @scene "great-grand-child3")]
        (should= "great-grand-child" (prop-name result))
        (should= "grand-child2" (prop-id (parent-prop result)))
        (should= "child1" (prop-id (parent-prop (parent-prop result))))))
    )

  (context "with production"

    (with scene (build-tree))

    (with peer-production (limelight.model.FakeProduction. "some/path"))
    (with production1 (new-production @peer-production))
    (before (.setProduction (.getPeer @scene) @peer-production))

    (it "loads the production from the scene"
      (should= @production1 (production @scene)))

    (it "loads the production from a prop"
      (let [aprop (find-prop @scene "child1")]
        (should= @production1 (production aprop))))

    (context "scene opening"

      (with scene-stub (limelight.ui.model.FakeScene.))
      (before
        (do
          (set! (.stubbedScene @peer-production) @scene-stub)
          (.loadProduction @peer-production)
          (.setProxy (.getTheater (._peer @production1)) (limelight.model.api.MockTheaterProxy.))))

      (it "on default stage"
        (let [result (open-scene @production1 "foo")
              stage (.getDefaultStage (.getTheater (._peer @production1)))]
          (should= @scene-stub result)
          (should= @scene-stub (.getScene stage))
          (should= "foo" (.loadedScenePath @peer-production))))

      (it "on specified stage"
        (let [stage (limelight.ui.model.MockStage. "mock")
              _ (.add (.getTheater (._peer @production1)) stage)
              result (open-scene @production1 "foo" "mock")]
          (should= @scene-stub result)
          (should= @scene-stub (.getScene stage))
          (should= "foo" (.loadedScenePath @peer-production))))
      )

    (context "theater"

      (it "gets the theater from the production"
        (should= @(._theater @production1) (theater @production1)))

      (it "gets the theater from a stage"
        (let [theater1 (theater @production1)
              stage1 (create-stage theater1 "new-stage" :some :option)]
          (should= theater1 (theater stage1))
          (should= "new-stage" (name stage1))
          (should= stage1 (get-stage theater1 "new-stage"))))


      )
    )
  )

(run-specs)