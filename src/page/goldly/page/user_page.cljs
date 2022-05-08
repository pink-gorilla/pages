

(defn user-nav []
  (into [:div]
        (for [i [1 2 3]]
          [page-link "daslu" (str "demo" i)])))

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

(defn default-page [_]
  [show-page "admin" "index"])

(add-page default-page :default-page)
