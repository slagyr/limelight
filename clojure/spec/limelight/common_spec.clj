(ns limelight.common-spec
  (:use
    [speclj.core]
    [limelight.common]
    [limelight.prop-building :only (to-props)]
    [limelight.scene :only (new-scene)]))

(defn build-tree []
  (let [scene (new-scene {:id "root-id" :name "root"})]
    (add-props scene (to-props
      [[:child {:id "child1"}
        [:grand-child {:id "grand-child1"}
         [:great-grand-child {:id "great-grand-child1"}]
         [:great-grand-child {:id "great-grand-child2"}]]
        [:grand-child {:id "grand-child2"}
         [:great-grand-child {:id "great-grand-child3"}]]]
       [:child {:id "child2"}
        [:grand-child {:id "grand-child3"}]]]))
    (.setCastingDirector @(.peer scene) (limelight.model.api.FakeCastingDirector.))
    (.illuminate @(.peer scene))
    scene))

(describe "Common: Finding Props"
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

(run-specs)