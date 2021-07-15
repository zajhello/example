package com.kingkong.common_library.source

object Api {
    const val searchResultSecret = "/app/lookup/searchResultSecret"

    const val getSecret = "/user/v1/getSecret"
    const val searchAppSwitchConfig = "/lookup/searchAppSwitchConfig"
    const val searchUserCenterUrlConfig = "/lookup/searchUserCenterUrlConfig"
    const val upgrade = "/edition/v1/upgrade"
    const val category = "/movie/v1/category"
    const val advList = "/subject/v1/advList"
    const val movieInfoList = "/subject/v1/movieInfoList"
    const val advInfo = "/movie/adv/advInfo"
    const val adv1 = "/php/api/ad/v1"


    /**
     * 是否需要加密
     */
    @JvmStatic
    fun need2Encry(urlPart: String): Boolean {
        return (urlPart != getSecret
                && urlPart != searchAppSwitchConfig
                && urlPart != searchUserCenterUrlConfig
                && urlPart != upgrade
                && urlPart != category
                && urlPart != advList
                && urlPart != movieInfoList
                && urlPart != advInfo
                && urlPart != adv1)
    }
}