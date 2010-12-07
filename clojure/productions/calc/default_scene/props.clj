[:lcd {:text "()" :id "lcd"}]
(for [label "123+456-789*c0 /"]
  [:calc-button {:text (str label)}])
[:calc-button {:text "enter" :width "100%"}]