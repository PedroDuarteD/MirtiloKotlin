package com.pedroduarte.mirtiloapp.model

data class RecordModel (
    var id: Int,
    var date_initial: String,
    var date_final: String,
    var state: Int,
    var recetor: String,
    var productor: String,
    var box: Int,
    var type: String,
)