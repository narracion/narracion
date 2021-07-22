(ns narracion.main
  (:require [narracion.views.video :as video]
            [kee-frame.core :as k]))

;; ## Router

(def ^:private routes
  [["/" :video]])

(defn- router
  []
  [k/switch-route
   (comp :name :data)
   :video [video/render]
   nil    [:div "Loading ..."]])


(defn start!
  []
  (k/start!
    {:routes         routes
     :root-component [router]
     :initial-db     {:mode :normal}}))
