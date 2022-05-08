(ns page.services
  (:require [goldly.service.core :as s]
            [page.db :as db]))

(db/connect!)

(comment
  (db/transact [{:user/name "daslu"
                 :user/password "mysecretPassword!!1"}])

  (db/q [])
  )

(def user-query
  '[:find
    [pull ?id [*]]
    ...])

(defn get-page
  [user page]
  (case [user page]
    ["daslu" "demo1"] [:p "hello world"]
    ["daslu" "demo2"] [:p "vega study"]
    [:p "unknown page"]))

(s/add {:page/get get-page})
