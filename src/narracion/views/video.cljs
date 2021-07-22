(ns narracion.views.video
  (:require [narracion.views.video.state :as state]
            [narracion.components.video :as video]
            [narracion.components.text-element :as text-element]
            [reagent-hotkeys.core :as hotkeys]
            [re-frame.core :as re-frame]
            [goog.string :as gs]))


(def +sources
  [{:src "//vjs.zencdn.net/v/oceans.mp4"
    :type "video/mp4"}])

;; ## Hotkeys

(defn- hotkeys
  []
  (let [mode (re-frame/subscribe [::state/mode])]
    [hotkeys/hotkeys
     {:keys
      (if (= @mode :normal)
        {"right"       {:handler #(re-frame/dispatch [::state/skip-video 1])}
         "left"        {:handler #(re-frame/dispatch [::state/skip-video -1])}
         "shift-right" {:handler #(re-frame/dispatch [::state/skip-video 0.5])}
         "shift-left"  {:handler #(re-frame/dispatch [::state/skip-video -0.5])}
         "space"       {:handler #(re-frame/dispatch [::state/toggle-play])}
         "i"           {:handler #(re-frame/dispatch [::state/insert-mode])}}
        {"esc"         {:handler #(re-frame/dispatch [::state/normal-mode])}})}]))

;; ## View

(defn render
  []
  (let [current-time  (re-frame/subscribe [::state/current-time])
        current-entry (re-frame/subscribe [::state/current-entry])
        mode          (re-frame/subscribe [::state/mode])]
    [:section.video
     [video/video-player
      {:on-ready       #(re-frame/dispatch [::state/player-ready %])
       :on-time-update #(re-frame/dispatch [::state/set-current-time %])
       :sources        +sources}]
     [:p (gs/format "Time: %.2fs" @current-time)]
     (when-let [{:keys [timestamp text]} @current-entry]
       [text-element/text-element
        {:mode      @mode
         :timestamp timestamp
         :text      text
         :on-blur   #(re-frame/dispatch [::state/normal-mode])}])
     [hotkeys]]))
