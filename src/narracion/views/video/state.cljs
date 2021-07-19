(ns narracion.views.video.state
  (:require [narracion.components.video :as video]
            [re-frame.core :as re-frame]))

;; ## Interceptors

(def ^:private interceptors
  [(re-frame/path :video)])

;; ## Events

;; ### Current Status

(re-frame/reg-event-db
  ::player-ready
  interceptors
  (fn [db [_ player]]
    (assoc db :player player)))

(re-frame/reg-event-db
  ::set-current-time
  interceptors
  (fn [db [_ time-seconds]]
    (assoc db :current-time time-seconds)))

;; ### Interactions

(re-frame/reg-event-fx
  ::toggle-play
  interceptors
  (fn [{{:keys [player]} :db} _]
    (when player
      {::toggle-play {:player player}})))

(re-frame/reg-event-fx
  ::forward
  interceptors
  (fn [{{:keys [current-time player]} :db} _]
    (when player
      {::set-video-time
       {:player player
        :time-seconds (+ (or current-time 0) 0.5)}})))

(re-frame/reg-event-fx
  ::backward
  interceptors
  (fn [{{:keys [current-time player]} :db} _]
    (when player
      {::set-video-time
       {:player player
        :time-seconds (- (or current-time 0) 0.5)}})))

;; ## Effects

(re-frame/reg-fx
  ::toggle-play
  (fn [{:keys [player]}]
    (video/toggle-play player)))

(re-frame/reg-fx
  ::set-video-time
  (fn [{:keys [player time-seconds]}]
    (video/set-current-time player time-seconds)))

;; ## Subscriptions

(re-frame/reg-sub
  ::current-time
  (fn [db _]
    (or (get-in db [:video :current-time]) 0.0)))
