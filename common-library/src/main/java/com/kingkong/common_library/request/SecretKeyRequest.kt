package com.kingkong.common_library.request

import java.io.Serializable

data class SecretKeyRequest(val classCode: String = "AppConfig", val itemCode: String = "RESULT_SECRET") : Serializable
