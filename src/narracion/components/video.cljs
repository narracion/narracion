(ns narracion.components.video
  (:require [reagent.core :as r]
            [reagent.dom :as rdom]))

;; Player

(defrecord PlayerRef [player])

(defn- apply-player
  "Apply a function to the underlying videojs player."
  [{:keys [player]} f & args]
  (apply f player args))

(defn toggle-play
  "Play/Pause."
  [^PlayerRef player-ref]
  (apply-player player-ref #(if (.paused %) (.play %) (.pause %))))

;; ## Component

(defn- attach-handlers!
  "Attach the following handlers to the videojs player:
   - `:on-time-update`
   - `:on-play`
   - `:on-pause`
  "
  [player {:keys [on-time-update
                  on-play
                  on-pause]}]
  (when on-time-update
    (.on player "timeupdate" #(on-time-update (.currentTime player))))
  (when on-play
    (.on player "play" #(on-play)))
  (when on-pause
    (.on player "pause" #(on-pause))))

(defn- handle-component-did-mount
  "DID MOUNT: Initialise videojs, propagate reference."
  [{:keys [ref sources] :as props} player-atom]
  (fn [this]
    (let [node   (rdom/dom-node this)
          player (doto (js/videojs node #js {:controls true
                                             :autoPlay false
                                             :muted    true
                                             :width    640
                                             :sources  (clj->js sources)})
                   (attach-handlers! props))]
      (reset! player-atom player)
      (when ref
        (.ready player #(ref (PlayerRef. player)))))))

(defn- handle-component-will-unmount
  "WILL UNMOUNT: Dispose of the player."
  [_ player-atom]
  (fn [_]
    (some-> @player-atom .dispose)))

(defn video-player
  "Create a videojs component, initialise it with the given `:sources`."
  [props]
  (let [player-atom (atom nil)]
    (r/create-class
      {:component-did-mount    (handle-component-did-mount props player-atom)
       :component-will-unmount (handle-component-will-unmount props player-atom)
       :reagent-render         (fn [_] [:video.video-js])})))
