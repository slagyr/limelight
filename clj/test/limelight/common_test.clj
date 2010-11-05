(ns limelight.common-test
  (:use
    [limelight.common]
    [limelight.prop-building :only (!)]
    [limelight.scene :only (new-scene)]
    [lazytest.describe :only (describe testing it with given before using)]
    [lazytest.context :only (fn-context)]
    ))

(defn build-tree []
  (let [scene (new-scene {:id "root-id" :name "root"})]
    (add-props scene
      (! 'child {:id "child1"}
        (! 'grand-child {:id "grand-child1"}
          (! 'great-grand-child {:id "great-grand-child1"})
          (! 'great-grand-child {:id "great-grand-child2"}))
        (! 'grand-child {:id "grand-child2"}
          (! 'great-grand-child {:id "great-grand-child3"})))
      (! 'child {:id "child2"}
        (! 'grand-child {:id "grand-child3"})))
    (.setCastingDirector @(.peer scene) (limelight.model.api.FakeCastingDirector.))
    (.illuminate @(.peer scene))
    scene))

(describe "Common: Finding Props"
  (given [scene (build-tree)]

    (testing "find root by name"
      (given [roots (find-props-named scene "root")]
        (it "has 1 result" (= 1 (count roots)))
        (it "is the scene" (identical? scene (first roots)))))

    (testing "finding children by name"
      (given [children (find-props-named scene "child")]
        (it "found 2" (= 2 (count children)))
        (it "has right props" (= ["child1" "child2"] (map prop-id children)))))

    (testing "finding grand children by name"
      (given [grand-children (find-props-named scene "grand-child")]
        (it "found 3" (= 3 (count grand-children)))
        (it "has right props" (= ["grand-child1" "grand-child2" "grand-child3"] (map prop-id grand-children)))))

    (testing "finding great grand children by name"
      (given [great-grand-children (find-props-named scene "great-grand-child")]
        (it "found 3" (= 3 (count great-grand-children)))
        (it "has right props" (= ["great-grand-child1" "great-grand-child2" "great-grand-child3"] (map prop-id great-grand-children)))))

    (testing "finding root by id"
      (given [result (find-prop scene "root-id")]
        (it "is the scene" (identical? scene result))))

    (testing "finding child1 by id"
      (given [result (find-prop scene "child1")]
        (it "is a child" (= "child" (prop-name result)))
        (it "scene is parent" (= scene (parent-prop result)))))

    (testing "finding great-grand-child3 by id"
      (given [result (find-prop scene "great-grand-child3")]
        (it "is a great-grand-child" (= "great-grand-child" (prop-name result)))
        (it "has grand-child2 as parent" (= "grand-child2" (prop-id (parent-prop result))))
        (it "has child1 as grand parent" (= "child1" (prop-id (parent-prop (parent-prop result)))))))
    ))