(defn show-hiccup [h & args]
  (let [h-fn (render-vizspec h)]
    ))

(def page-state (r/atom {:page [:p "loading ..."]}))

;; eval

(defn goto-page [user page]
  (rf/dispatch [:bidi/goto :user-page
                :user user :page page
                :query-params {}]))

(defn page-link [user page]
  [:p.m-2.border.bg-blue-500.w-32
   [:a {:on-click #(goto-page user page)}
    (str "go to " user "/" page)]])

(defn request-page [user page]
  (let [{:keys [r-user r-page]} @page-state]
    (when-not (= [r-user r-page]
                 [user page])
      (swap! page-state
             assoc
             :r-user user
             :r-page page)
      (run-a page-state [:page] :page/get user page))))

(defn user-page [{:keys [route-params]}]
  (let [{:keys [user page]} route-params]
    (request-page user page)
    [:div.w-full.h-full.m-0.p-5
     [:p "user-page"]
     [:p "user: " user]
     [:p "page: " page]
     (:page @page-state)
     (into [:div]
           (for [page ["demo1" "demo2"]]
             [page-link "daslu" page]))]))

(add-page user-page :user-page)
