package com.dxmovie.dxbase.request

import java.io.Serializable

/**
 * 请求基类
 */
data class BaseRequest<T> (var param: T): Serializable {

     var userID = 0L
     var token: String? = null
     var deviceType = 2
     var appVersion = "daixiongshipin.sharesplit.com@1.5.8"
     var appNameCN = "袋熊视频 测试 1.5.8"
     var secret: String? = null
     var devicenId = "02:15:B2:00:00:00"
     var wechatId = "wx62d9790635beb080"
     var taskVersion : String? = null
}