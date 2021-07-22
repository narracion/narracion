(defproject narracion "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"
  :license {:name "MIT"
            :url "https://choosealicense.com/licenses/mit"
            :comment "MIT License"
            :year 2021
            :key "mit"}

  :min-lein-version "2.7.1"

  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.773"]
                 [org.clojure/tools.reader "1.3.3"]

                 ;; React
                 [reagent "1.0.0" ]
                 [kee-frame "1.1.2"]
                 [re-frame "1.2.0"]
                 [io.github.narracion/reagent-hotkeys "0.1.0-SNAPSHOT"]]

  :source-paths ["src"]

  :aliases {"fig"       ["trampoline" "run" "-m" "figwheel.main"]
            "fig:build" ["trampoline" "run" "-m" "figwheel.main" "-b" "dev" "-r"]
            "fig:min"   ["run" "-m" "figwheel.main" "-O" "advanced" "-bo" "dev"]
            "fig:test"  ["run" "-m" "figwheel.main" "-co" "test.cljs.edn" "-m" "narracion.test-runner"]}

  :profiles {:dev {:dependencies [[com.bhauman/figwheel-main "0.2.12"]
                                  [com.bhauman/rebel-readline-cljs "0.1.4"]
                                  [org.clojure/java.classpath "0.3.0"]
                                  [com.google.code.findbugs/jsr305 "3.0.2"]
                                  [args4j "2.33"]
                                  [ring "1.8.1"]
                                  [ring/ring-codec "1.1.2"]
                                  [commons-io "2.6"]
                                  [commons-fileupload "1.4"]
                                  [ns-tracker "0.4.0"]]

                   :resource-paths ["target"]
                   ;; need to add the compiled assets to the :clean-targets
                   :clean-targets ^{:protect false} ["target"]}}
  :pedantic? :abort)

