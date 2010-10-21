(ns limelight.prop-building
  (:use [limelight.prop :only (new-prop add-children)]))

(defn add-child-props [parent children]
  (if children
    (add-children parent children)))

(defn ! [name & args]
  (let [root (new-prop {})]
    (if (map? (first args))
      (do (.addOptions @(.peer root) (limelight.util.OptionsMap. (assoc (first args) :name name)))
        (add-child-props root (rest args)))
      (do (.addOptions @(.peer root) (limelight.util.OptionsMap. (hash-map :name name)))
        (add-child-props root args)))
    root))

(defn build-props [root input]
  (binding [*ns* (the-ns 'limelight.prop-building)]
    (let [props (load-string (str "[" input "]"))]
      (add-children root props)
      root)))
