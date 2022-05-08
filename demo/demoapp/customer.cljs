


(defn customer [{:keys [first last]}]
  [:div.bg-blue-500.m-5
   [:p.text-bold.text-red-500 first]
   [:p.text-bold.text-red-500 last]])