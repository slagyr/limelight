(! 'lcd {:text "()" :id "lcd"})
(map
  (fn [label] (! 'calc-button {:text label}))
  (map str (seq "123+456-789*c0 /")))
(! 'calc-button {:text "enter" :width "100%"})