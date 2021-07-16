(ns ^:figwheel-hooks narracion.core
  (:require [narracion.components.video :as video]
            [goog.dom :as gdom]
            [reagent-hotkeys.core :as hotkeys]
            [reagent.core :as r]
            [reagent.dom :as rdom]))

;; ## Video Player

(def +sources
  [{:src "//vjs.zencdn.net/v/oceans.mp4"
    :type "video/mp4"}])

;; define your app data so that it doesn't get over-written on reload
(defonce app-state (r/atom {:text "Hello world!"}))

(defn get-app-element []
  (gdom/getElement "app"))

(defn hello-world []
  (let [t (r/atom 0)
        p (r/atom nil)]
    (fn []
      [:div
       [:h1 "Current time: " @t]
       [:div {:data-vjs-player "data-vjs-player"}
        [video/video-player
         {:ref #(reset! p %)
          :on-time-update #(reset! t %)
          :sources +sources}]]
       [hotkeys/hotkeys
        {:keys {"right" {:handler #(swap! t inc)}
                "left" {:handler #(swap! t dec)}
                "space" {:handler #(video/toggle-play @p)}
                }}]])))

(defn mount [el]
  (rdom/render [hello-world] el))

(defn mount-app-element []
  (when-let [el (get-app-element)]
    (mount el)))

;; conditionally start your application based on the presence of an "app" element
;; this is particularly helpful for testing this ns without launching the app
(mount-app-element)

;; specify reload hook with ^;after-load metadata
(defn ^:after-load on-reload []
  (mount-app-element)
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )
