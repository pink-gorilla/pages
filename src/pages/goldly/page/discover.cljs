

 (require '[pages :refer [a-page]])

(defn show-user [user]
  [a-page {:user user :page "index"}
     [:p user]])


(defn users-view [users]
  (if users 
    (into [:div
            [:p "users"]]
          (map show-user users))
    [:p "loading users.."]))


(defn users [x]
  [url-loader {:fmt :clj
               :url :pages/users
               :arg-fetch nil}
   users-view
   ])


(defn show-user-page [user page]
  [a-page {:user user :page page}
   [:p page]])

(defn user-page-view [user pages] 
  [:div.bg-blue-500.m-5
   [:p.text-bold.text-red-500 
    (pr-str pages)]
    (into [:div]
      (map #(show-user-page user %) pages))
   ])



(defn user-page [user]
  [url-loader {:fmt :clj
               :url :pages/user-pages
               :arg-fetch user}
   #(user-page-view user %)])