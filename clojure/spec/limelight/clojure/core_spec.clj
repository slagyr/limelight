;- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
;- Limelight and all included source files are distributed under terms of the MIT License.

(ns limelight.clojure.core-spec
  (:use
    [speclj.core]
    [limelight.clojure.spec-helper]
    [limelight.clojure.core]
    [limelight.clojure.prop-building :only (->props)]
    [limelight.clojure.scene :only (new-scene)]
    [limelight.clojure.prop :only (new-prop)]
    [limelight.clojure.production :only (new-production)]
    [limelight.clojure.util :only (map-for-java)]))

(def SLASH (.separator (limelight.Context/fs)))

(defn build-tree []
  (let [scene (new-scene {:id "root-id" :name "root" :path "root"})]
    (add scene (->props
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

    (it "find-by-name takes Named types as name"
      (should= 1 (count (find-by-name @root :root)))
      (should= 2 (count (find-by-name @root :child)))
      (should= 3 (count (find-by-name @root 'grand-child))))

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

    (it "find-by-id takes Names types as id"
      (should= @root (find-by-id @root :root-id))
      (should= "child" (name (find-by-id @root :child1))))
    )

  (context "with scene"
    (with root (build-tree))

    (it "gets the scene from the scene"
      (should= @root (scene @root)))

    (it "gets the scene from a prop"
      (should= @root (scene (find-by-id @root "child1"))))

    (it "gets the prop from a prop"
      (let [prop1 (find-by-id @root "child1")]
        (should= prop1 (prop prop1))))

    (it "gets the prop from the scene"
      (should= @root (prop @root)))

    (it "can evaluate code inside the scene ns"
      (let [[scn sns] (let-in [s @root] [s clojure.core/*ns*])]
        (should= @root scn)
        (should= (._ns @root) sns)))

    (context "backstage"

      (it "props have a backstage"
        (let [backstage1 (backstage (find-by-id @root "child1"))
              backstage2 (backstage (find-by-id @root "child2"))]
          (should-not-be-same backstage1 backstage2)
          (should= 0 (count backstage1))))

      (it "props can put thing in and get thing out"
        (let [prop (find-by-id @root "child1")]
          (should= nil (backstage-get prop "foo"))
          (should= nil (backstage-get prop :fizz))
          (backstage-put prop "foo" "bar")
          (backstage-put prop :fizz "bang")
          (should= "bar" (backstage-get prop :foo))
          (should= "bang" (backstage-get prop "fizz"))))

      (it "scenes can put thing in and get thing out"
        (should= nil (backstage-get @root "foo"))
        (should= nil (backstage-get @root :fizz))
        (backstage-put @root "foo" "bar")
        (backstage-put @root :fizz "bang")
        (should= "bar" (backstage-get @root :foo))
        (should= "bang" (backstage-get @root "fizz")))
      )

    (context "with production"

      (with peer-production (limelight.model.FakeProduction. (str "some" SLASH "path")))
      (with production1 (new-production @peer-production))
      (before (.setProduction (.getPeer @root) @peer-production))

      (it "gets the name of the production"
        (should= "path" (name @production1)))

      (it "loads the production from the scene"
        (should= @production1 (production @root)))

      (it "loads the production from a prop"
        (let [aprop (find-by-id @root "child1")]
          (should= @production1 (production aprop))))

      (it "loads the production from a production"
        (should= @production1 (production @production1)))

      (it "loads the production from a theater"
        (should= @production1 (production (theater @production1))))

      (it "get the production from a prop event"
        (should= @production1 (production (limelight.ui.events.panel.IlluminatedEvent. (peer @root)))))

      (it "gets the production from a production event"
        (let [event (limelight.model.events.ProductionOpenedEvent.)]
          (.dispatch event (peer @production1))
          (should= @production1 (production event))))

      (unless-headless
        (it "loads the production from a stage"
          (should= @production1 (production (default-stage (theater @production1)))))

        (it "get the scene from a stage"
          (let [stage1 (default-stage (theater @production1))]
            (should= nil (scene stage1))
            (.setScene (peer stage1) (peer @root))
            (should= @root (scene stage1)))))

      (it "gets the path of a production"
        (should= (str "some" SLASH "path") (path @production1)))

      (it "gets the path of a scene"
        (.setProduction (peer @root) (peer @production1))
        (should= (str "some" SLASH "path" SLASH "root") (path @root)))

      (it "can evaluate code inside the production ns"
        (let [[prod pns] (let-in [p @production1] [p clojure.core/*ns*])]
          (should= @production1 prod)
          (should= @(._ns @production1) pns)))

      (context "backstage"

        (it "productions have a backstage"
          (let [backstage1 (backstage @production1)]
            (should= 0 (count backstage1))))

        (it "productions can put thing in and get thing out"
          (should= nil (backstage-get @production1 "foo"))
          (should= nil (backstage-get @production1 :fizz))
          (backstage-put @production1 "foo" "bar")
          (backstage-put @production1 :fizz "bang")
          (should= "bar" (backstage-get @production1 :foo))
          (should= "bang" (backstage-get @production1 "fizz")))
        )

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
              (should= (peer stage1) (.getDefaultStage (peer theater1)))))

          (it "gets the stages from the theater"
            (let [theater1 (theater @production1)]
              (should= true (seq? (stages theater1)))
              (should= 0 (count (stages theater1)))
              (let [stage1 (build-and-add-stage theater1 "new-stage" :some :option)]
                (should= [stage1] (stages theater1)))))

          (context "with scene on stage"

            (with stage1 (default-stage (theater @production1)))
            (before (.setStage (peer @root) (peer @stage1)))

            (it "gets the stage from the stage"
              (should= @stage1 (stage @stage1)))

            (it "gets the stage from a scene"
              (should= @stage1 (stage @root)))

            (it "gets the stage from a prop"
              (should= @stage1 (stage (find-by-id @root "child1"))))

            (it "get the stage from a prop event"
              (should= @stage1 (stage (limelight.ui.events.panel.IlluminatedEvent. (peer @root)))))

            )
          )
        )
      )

    (context "panel events"

      (with event (limelight.ui.events.panel.IlluminatedEvent. (peer @root)))

      (it "can get the prop"
        (should= @root (prop @event)))

      (it "can get the scene"
        (should= @root (scene @event)))
      )

    (context "styles"
      (with child (find-by-id @root "child1"))
      (with s (style @child))

      (it "can get style from prop"
        (let [prop (find-by-id @root "child1")]
          (should= (.getStyle (peer prop)) (peer (style prop)))))

      (it "can pull values from style"
        (should= "auto" (:width @s))
        (should= "0" (get @s :top-border-width)))

      (it "can get any style attribute"
        (doseq [descriptor limelight.styles.Style/STYLE_LIST]
          (let [kw (keyword (limelight.util.StringUtil/spearCase (.getName descriptor)))]
            (should= (.toString (.getDefaultValue descriptor)) (kw @s)))))

      (it "can set a style attribute"
        (should= "auto" (style @child :width))
        (should= "auto" (style @child :height))
        (style= @child :width 123)
        (style= @child :height 456)
        (should= "123" (style @child :width))
        (should= "456" (style @child :height)))

      (it "can set multiple style attributes"
        (style= @child :width 123 :height 456)
        (should= "123" (style @child :width))
        (should= "456" (style @child :height)))
      )

    (context "tree manipulation"

      (it "can remove all the child props"
        (remove-all @root)
        (should= 0 (count (children @root))))

      (it "can remove one child"
        (let [child (find-by-id @root "child1")]
          (remove-child @root child)
          (should= 1 (count (children @root)))))

      (it "can add a child"
        (let [child (new-prop {:name "new" :id "new"})]
          (add-child @root child)
          (should= 3 (count (children @root)))
          (should= child (last (children @root)))))

      (it "can add a child at an index"
        (let [child (new-prop {:name "new" :id "new"})]
          (add-child @root child 0)
          (should= 3 (count (children @root)))
          (should= child (first (children @root)))))

      (it "can build props"
        (build @root [:new {:id "new"} [:new-child {:id "new-child"}]])
        (should= 3 (count (children @root)))
        (should= "new" (name (find-by-id @root "new")))
        (should= "new-child" (name (find-by-id @root "new-child")))
        (should= "new-child" (name (first (children (last (children @root)))))))
      )

    )
  )

(run-specs :stacktrace true)