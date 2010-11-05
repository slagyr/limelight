(ns limelight.prop-building
  (:use
    [limelight.common]
    [limelight.prop :only (new-prop)]))

(defn ! [name & args]
  (let [root (new-prop {})]
    (if (map? (first args))
      (do (.addOptions @(.peer root) (limelight.util.OptionsMap. (assoc (first args) :name name)))
        (add-props root (rest args)))
      (do (.addOptions @(.peer root) (limelight.util.OptionsMap. (hash-map :name name)))
        (add-props root args)))
    root))

(defn build-props [root input]
  (binding [*ns* (the-ns 'limelight.prop-building)]
    (let [props (load-string (str "[" input "]"))]
      (add-props root props)
      root)))