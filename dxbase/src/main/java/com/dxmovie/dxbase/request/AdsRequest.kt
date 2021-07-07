package com.dxmovie.dxbase.request

import java.io.Serializable

data class AdsRequest(
        val project: Int = 2, //2 视频组-袋熊视频
        val app_flag: String = "daixiongshipin.sharesplit.com@1.5.8",//渠道标识@版本号
        val phone_type: String = "android",
        val ad_type: String,/** @link{com.kingkong.common_library.constants.Constants.ADS}*/
        val group: String,//广告分组，如果adtype字段为空，group=1,将会对所有类型的广告进行分组输出
        val sign: String = "android",
        val timestamp: Long = System.currentTimeMillis(),
        val appkey: String = "dxsp",
        val other: String
) : Serializable
