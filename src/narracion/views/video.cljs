(ns narracion.views.video
  (:require [narracion.views.video.state :as state]
            [narracion.components.video :as video]
            [reagent-hotkeys.core :as hotkeys]
            [re-frame.core :as re-frame]
            [goog.string :as gs]))


(def +sources
  [{:src "//vjs.zencdn.net/v/oceans.mp4"
    :type "video/mp4"}])

;; ## Hotkeys

(defn- hotkeys
  []
  [hotkeys/hotkeys
   {:keys {"right" {:handler #(re-frame/dispatch [::state/forward])}
           "left"  {:handler #(re-frame/dispatch [::state/backward])}
           "space" {:handler #(re-frame/dispatch [::state/toggle-play])}}}])

;; ## View

(defn render
  []
  (let [current-time (re-frame/subscribe [::state/current-time])]
    [:section.video
     [video/video-player
      {:on-ready       #(re-frame/dispatch [::state/player-ready %])
       :on-time-update #(re-frame/dispatch [::state/set-current-time %])
       :sources        +sources}]
     [:p (gs/format "Time: %.2fs" @current-time)]
     [hotkeys]]))
