(ns pages
  (:require 
   [rf]
   ))

;; this ns is to be used by users to create pages
;; [pages/page-link "user1" "demo1"]

(defn goto-page [user page]
  (rf/dispatch [:bidi/goto :user-page
                :user user :page page
                :query-params {}]))

(defn link [user page]
  [:p.m-2.border.bg-blue-500.w-32
   [:a {:on-click #(goto-page user page)}
    (str "! " user "/" page)]])