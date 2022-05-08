(ns pages.seed
  (:require 
   [pages.db :refer [add-user add-page]]))

(def vega-heatmap
  '[user/vegalite
    {:box :sm
     :spec {:$schema "https://vega.github.io/schema/vega-lite/v5.json"
            :data {:values [{:from :a :to :b :cor 0.6}
                            {:from :a :to :c :cor 0.2}
                            {:from :a :to :d :cor -0.1}
                            {:from :a :to :e :cor 0.0}
                            {:from :b :to :c :cor 0.3}
                            {:from :b :to :d :cor 0.7}
                            {:from :b :to :e :cor 0.0}
                            {:from :c :to :d :cor 0.4}
                            {:from :c :to :e :cor 0.8}
                            {:from :d :to :e :cor -0.2}]}
            :mark "rect"
            :width 600
            :height 400
            :encoding {:x {:field "to"
                           :type "ordinal"}
                       :y {:field "from"
                           :type "ordinal"}
                       :color {;:value "blue"
                               :field "cor"
                               :type "quantitative"}}
            :config {:view {:stroke "transparent"}}}}])

(defn add-seed []
  (add-user "seed" "mysecretPassword!!1")
  
  (add-page "seed" "index" vega-heatmap)

  (add-page "seed" "demo3" vega-heatmap)

  (add-page "seed" "demo3" vega-heatmap)
  
  
  )