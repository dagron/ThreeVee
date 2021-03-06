(ns threevee.input.core
  (:require
   [boot.core :as c]
   [threevee.image.core :as img]))

;; Operating on Java filesa

(defn file->name [jfile]
  [(.getName jfile) (.toString jfile)])

(defn sorted-input-files [input-files]
  (sort-by #(.toString %) input-files))

(defn indexed-images [files]
  (->> (sorted-input-files files)
       (map-indexed
        (fn [i file]
          (let [[img-name img-path] (file->name file)]
            {:image-index i
             :image-name img-name
             :image (img/image-by-path img-path)})))))


(defn save-image-to-output-path [images-info]
  (doseq [{:keys [image output-path]} images-info]
    (println "saving to:" output-path)
    (img/save-to-path image output-path)))

;; Operating on Boot fileset & "input-files" (tmpfiles)

(defn input-files-by-re [fileset res]
  (c/by-re res
           (c/input-files fileset)))

(defn tmpfile->name [tfile]
  (-> tfile c/tmp-file file->name))

(defn print-names [tfiles]
  (doseq [tfile tfiles]
    (let [[name _] (tmpfile->name tfile)]
      (println "input file:" name))))


;; Face Extraction
(def INPUT-ROOT-DIR "FACES/INPUT/")
(def INPUT-GUEST-DIR (str INPUT-ROOT-DIR "GUESTS/"))
(def INPUT-GUEST-CROPPED-FACES (str INPUT-ROOT-DIR "GUESTS_CROPPED_FACES/"))
(def INPUT-ART-DIR (str INPUT-ROOT-DIR "ART/"))

(def OUTPUT-ROOT-DIR "FACES/OUTPUT/")
(def OUTPUT-GUEST-DIR (str OUTPUT-ROOT-DIR "GUESTS"))
(def OUTPUT-GUEST-FACE-EXTRACTIONS-DIR (str OUTPUT-GUEST-DIR "/EXTRACTIONS"))
(def OUTPUT-PREPROCESSED-GUEST-FACE-DIR (str OUTPUT-GUEST-DIR "/PREPROCESSED"))

(def OUTPUT-ART-DIR (str OUTPUT-ROOT-DIR "ART"))
(def OUTPUT-ART-FACE-EXTRACTIONS-DIR (str OUTPUT-ART-DIR "/EXTRACTIONS"))
(def OUTPUT-ART-FACE-EQ-EXTRACTIONS-DIR (str OUTPUT-ART-DIR "/EXTRACTIONS_EQ"))
(def OUTPUT-ART-FACE-DETECTIONS-DIR (str OUTPUT-ART-DIR "/DETECTIONS"))

(def IMG-PATTERN ".*\\.(gif|jpg|jpeg|tiff|png)$")
(def RE-GUEST
  (re-pattern (str INPUT-GUEST-DIR IMG-PATTERN)))
(def RE-GUEST-CROPPED-FACES
  (re-pattern (str INPUT-GUEST-CROPPED-FACES IMG-PATTERN)))
(def RE-ART
  (re-pattern (str INPUT-ART-DIR IMG-PATTERN)))

(defn guest-input-files [fileset]
  (input-files-by-re fileset [RE-GUEST]))

(defn guest-cropped-face-input-files [fileset]
  (input-files-by-re fileset [RE-GUEST-CROPPED-FACES]))

(defn art-input-files [fileset]
  (input-files-by-re fileset [RE-ART]))


;; Older fns using direct filesystem access.
;; (defn input-files [dir-name]
;;   (file-seq
;;    (clojure.java.io/file dir-name)))

;; (def map-file-name-xr
;;   (map file->name))

;; (defn input-file-names []
;;   (into [] map-file-name-xr (input-files INPUT-DIR)))

;; (def filter-pic-name-xr
;;   (filter (fn [[file-name _]]
;;             (re-find
;;              (re-pattern ".*\\.(gif|jpg|jpeg|tiff|png)$")
;;              file-name))))

;; (defn input-pic-names []
;;   (let [xr (comp
;;             map-file-name-xr
;;             filter-pic-name-xr)]
;;     (into [] xr (input-files INPUT-DIR))))

;; (defn output-pic-name
;;   ([input-pic-name]
;;    (output-pic-name input-pic-name nil))
;;   ([input-pic-name extra]
;;    (let [tag (if-not (nil? extra)
;;                (str extra "_")
;;                "")]
;;      (str OUTPUT-DIR "/output_" tag input-pic-name))))

