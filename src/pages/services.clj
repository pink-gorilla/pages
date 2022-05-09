(ns pages.services
  (:require
   [clojure.edn :as edn]
   [taoensso.timbre :refer [trace debug info warn error]]
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
   (let [p (-> (some-> (db/q db/load-page user page)
                       :page/content
                       edn/read-string)
               (or (unknown-user-page user page)))]
     (info "sending " user "/"page "content: " p)
     p))

(s/add {:pages/page get-page
        :pages/users db/users
        :pages/user-pages db/user-pages
        })

(comment
  (db/connect! (fn [] (println "schema was added!")))
  (db/connect! add-seed)

  (get-page "daslu" "demo1")

  (get-page "daslu" "demo11")
  
;
  )
