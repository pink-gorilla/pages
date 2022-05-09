(ns pages.user-page
  (:require
    [pages]
    [page :refer [show-page]]
    [user :refer [add-page]]
   )
  )

(defn user-nav []
  (into [:div]
        (for [i [1 2 3]]
          [pages/link "daslu" (str "demo" i)])))

(defn user-page-wrapped [user page]
  [:div.w-full.h-full.m-0.p-5
   [:p.bg-blue-300 "user: " user " page: " page]
   [show-page user page]
   [user-nav]
     ;; (-> @page-state :page pr-str)
   ])

(defn user-page [{:keys [route-params]}]
  (let [{:keys [user page]} route-params]
    [user-page-wrapped user page]))

(add-page user-page :user-page)


(defn user-page-index [{:keys [route-params]}]
  (let [{:keys [user]} route-params]
    [user-page-wrapped user "index"]))

(add-page user-page :user-page-index)



(defn default-page [_]
  [show-page "seed" "index"])

(add-page default-page :default-page)

