(ns page.services
  (:require [goldly.service.core :as s]))

(defn get-page
  [user page]
  (case [user page]
    ["daslu" "demo1"] [:p "hello world"]
    ["daslu" "demo2"] [:p "vega study"]
    [:p "unknown page"]))

(s/add {:page/get get-page})
