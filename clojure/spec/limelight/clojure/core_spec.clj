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
    (add scene (to-props
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

    (with root (build-tree))

    (it "finds root by name"
      (let [roots (find-by-name @root "root")]
        (should= 1 (count roots))
        (should (identical? @root (first roots)))))

    (it "finds children by name"
      (let [children (find-by-name @root "child")]
        (should= 2 (count children))
        (should= ["child1" "child2"] (map id children))))

    (it "finds grand children by name"
      (let [grand-children (find-by-name @root "grand-child")]
        (should= 3 (count grand-children))
        (should= ["grand-child1" "grand-child2" "grand-child3"] (map id grand-children))))

    (it "finds great grand children by name"
      (let [great-grand-children (find-by-name @root "great-grand-child")]
        (should= 3 (count great-grand-children))
        (should= ["great-grand-child1" "great-grand-child2" "great-grand-child3"] (map id great-grand-children))))

    (it "finds root by id"
      (let [result (find-by-id @root "root-id")]
        (should (identical? @root result))))

    (it "finds child1 by id"
      (let [result (find-by-id @root "child1")]
        (should= "child" (name result))
        (should= @root (parent result))))

    (it "finds great-grand-child3 by id"
      (let [result (find-by-id @root "great-grand-child3")]
        (should= "great-grand-child" (name result))
        (should= "grand-child2" (id (parent result)))
        (should= "child1" (id (parent (parent result))))))
    )

  (context "getting scene"
    (with root (build-tree))

    (it "gets the scene from the scene"
      (should= @root (scene @root)))

    (it "gets the scene from a prop"
      (should= @root (scene (find-by-id @root "child1"))))
    )

  (context "with production"

    (with root (build-tree))

    (with peer-production (limelight.model.FakeProduction. "some/path"))
    (with production1 (new-production @peer-production))
    (before (.setProduction (.getPeer @root) @peer-production))

    (it "loads the production from the scene"
      (should= @production1 (production @root)))

    (it "loads the production from a prop"
      (let [aprop (find-by-id @root "child1")]
        (should= @production1 (production aprop))))

    (it "loads the production from a production"
      (should= @production1 (production @production1)))

    (it "loads the production from a theater"
      (should= @production1 (production (theater @production1))))

    (unless-headless
      (it "loads the production from a stage"
        (should= @production1 (production (default-stage (theater @production1)))))

      (it "get the scene from a stage"
        (let [stage1 (default-stage (theater @production1))]
          (should= nil (scene stage1))
          (.setScene (peer stage1) (peer @root))
          (should= @root (scene stage1)))))

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
          (should= (.getProxy @scene-stub) result)
          (should= @scene-stub (.getScene stage))
          (should= "foo" (.loadedScenePath @peer-production))))

      (it "on specified stage"
        (let [stage (limelight.ui.model.MockStage. "mock")
              _ (.add (.getTheater (._peer @production1)) stage)
              result (open-scene @production1 "foo" "mock")]
          (should= (.getProxy @scene-stub) result)
          (should= @scene-stub (.getScene stage))
          (should= "foo" (.loadedScenePath @peer-production))))
      )

    (context "theater"

      (it "gets the theater from the production"
        (should= @(._theater @production1) (theater @production1)))

      (unless-headless
        (it "gets the theater from a stage"
          (let [theater1 (theater @production1)
                stage1 (build-and-add-stage theater1 "new-stage" :some :option)]
            (should= theater1 (theater stage1))
            (should= "new-stage" (name stage1))
            (should= stage1 (get-stage theater1 "new-stage"))))

        (it "gets the default-stage"
          (let [theater1 (theater @production1)
                stage1 (default-stage theater1)]
            (should-not= nil stage1)
            (should= (peer stage1) (.getDefaultStage (peer theater1))))))

      )
    )
  )

(run-specs)