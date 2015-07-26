(ns beam-diagnostic-system.util.definitions)

(def steelQuality "calidadAcero")
(def concreteQuality "calidadHormigon")
(def beamLength "longitud")
(def steelSection "areaAcero")
(def height "altura")
(def width "ancho")
(def supportPointAmount "cantidadPuntosApoyo")
(def lastBendingMoment "momentoFlectorUltimo")
(def nominalBendingMoment "momentoFlectorNominal")
(def lastShearTension "corteUltimo")
(def nominalShearTension "corteNominal")
(def longitudinalBarDiameter "diametroBarrasLongitudinales")
(def stirrupBarDiameter "diametroBarrasEstribos")
(def concreteCover "recubrimientoHormigon")
(def defaultBeamInputDataFile "src/beam_diagnostic_system/resources/beamDataFile.xlsx")
(def defaultSheet "Hoja1")
(def OKHEIGHT "verifica altura")
(def OKSECTIONLIMIT "verifica limite de sección")
(def OKSECTION "verifica sección")
(def OKSHEAR "verifica corte")
(def OKBINDING "verifica flexión")
(def OKBEAM "VIGA BIEN DIMENSIONADA. NO HACE FALTA REFUERZO")
(def cliOptions
  [
    ["-f" "--file" "REQUIRED: Beam 's data file name"
    :default defaultBeamInputDataFile]
    ["-h" "--help" "Show help"
    :flag true
    :default false]]
)
(defn inputBeamDataFile [arguments]
  (if (= (first arguments) nil) defaultBeamInputDataFile (first arguments))
)
