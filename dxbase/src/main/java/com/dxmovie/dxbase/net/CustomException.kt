package com.dxmovie.dxbase.net

import java.lang.Exception

/**
 *@desc
 */
class CustomException : Exception {

    constructor() : super()

    constructor(message: String) : super(message) {
        this.errorMessage = message
    }

    constructor(message: String, code: Int, data: String) : super(message) {
        this.code = code
        this.errorMessage = message
    }

    var code: Int = 0
    var errorMessage: String = ""
}