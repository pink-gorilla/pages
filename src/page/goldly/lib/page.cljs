(defn safe-resolve [s]
  (try
    (resolve-symbol-sci s)
    (catch :default e
      (println "renderer not found: " s)
      nil)))

(defn render-vizspec2 [h]
  ;(println "rendering vizspec: " h)
  ;(println "first item in vec:" (first h) "type: " (type (first h)))
  ;(println "render fn:" (get-symbol-value (first h)))
  ;(println "now showing..")
  (let [h-fn (pinkie/show safe-resolve h)]
    ;(println "rendered spec: " (pr-str h-fn))
    h-fn))

(defn goto-page [user page]
  (rf/dispatch [:bidi/goto :user-page
                :user user :page page
                :query-params {}]))

(defn page-link [user page]
  [:p.m-2.border.bg-blue-500.w-32
   [:a {:on-click #(goto-page user page)}
    (str "! " user "/" page)]])

(defn request-page [page-state user page]
  (let [{:keys [r-user r-page]} @page-state]
    (when-not (= [r-user r-page]
                 [user page])
      (swap! page-state
             assoc
             :r-user user
             :r-page page)
      (run-a page-state [:page] :page/get user page))))

(defn show-page [user page]
  (let [page-state (r/atom {:page [:p.bg-red-500.m-1 "loading ..."]})]
    (fn [user page]
      (request-page page-state user page)
      [:div
       [:p "user page: " user "-" page]
       (-> @page-state :page render-vizspec2)])))

