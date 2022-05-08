(ns pages.services
  (:require
   [clojure.edn :as edn]
   [goldly.service.core :as s]
   [pages.db :as db]
   [pages.seed :refer [add-seed]]))

(db/connect! add-seed)

(defn unknown-user-page [user page]
  [:div.border.border-solid.border-red-500.bg-blue-200.p-5.m-5
   [:p.text-bold.text-xl "Page not found!"]
   [:p "User: " user]
   [:p "Page: " page]])

(defn get-page
  [user page]
  (-> (some-> (db/q db/load-page user page)
              :page/content
              edn/read-string)
      (or (unknown-user-page user page))))

(s/add {:page/get get-page})

(comment
  (get-page "daslu" "demo1")
  (get-page "daslu" "demo11"))
