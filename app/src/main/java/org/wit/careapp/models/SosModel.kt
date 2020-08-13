package org.wit.careapp.models

data class SosModel(
    var id: String = "",
    var userId: String = "",
    var source: String = "SOS Button",
    var alertDate: String = "",
    var alertTime: String = ""
)