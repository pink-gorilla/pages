(ns page.services
  (:require [goldly.service.core :as s]
            [page.db :as db]
            [clojure.edn :as edn]))

(db/connect!)

(defn get-page
  [user page]
  (-> (some-> (db/q db/load-page user page)
              :page/content
              edn/read-string)
      (or [:p "unknown page"])))

(s/add {:page/get get-page})

(comment
  (get-page "daslu" "demo1")
  (get-page "daslu" "demo11"))
