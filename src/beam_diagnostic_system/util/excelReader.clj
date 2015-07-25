(ns  beam-diagnostic-system.util.excelReader
  (:use [beam-diagnostic-system.util.definitions]
    [dk.ative.docjure.spreadsheet] :reload-all)
)
(defn storeInputToMap[inputBeamDataFile]
  (def cells (->>(load-workbook inputBeamDataFile)
      (select-sheet defaultSheet)
      (select-columns {:A :name, :B :value})
  ))
  (zipmap
    (map #(% :name) cells)
    (map #(% :value) cells)
  )
)
