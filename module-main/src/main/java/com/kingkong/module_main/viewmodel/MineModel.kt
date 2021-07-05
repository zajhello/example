package com.kingkong.module_main.viewmodel

import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel
import com.dxmovie.dxbase.request.BaseRequest
import com.dxmovie.dxbase.response.BaseResponse
import com.dxmovie.dxbase.utils.RxUtils
import com.dxmovie.dxbase.utils.ToastUtils
import com.dxmovie.dxbase.utils.extention.otherwise
import com.dxmovie.dxbase.utils.extention.yes
import com.dxmovie.dxbase.viewmodel.BaseViewModel
import com.dxmovie.dxbase.widget.StatusViewLayout
import com.kingkong.common_library.repository.PrecondRepository
import com.kingkong.common_library.request.SecretKeyRequest
import com.kingkong.common_library.response.SecretKeyModel
import com.kingkong.common_library.response.SecretKeyResponse
import com.kingkong.common_library.utils.EncryUtil
import com.kingkong.common_library.utils.StatusViewUtils

class MineModel : BaseViewModel() {
    override fun onCreate() {
        super.onCreate()

        ToastUtils.showShort("MineModel getSecretKet")
        PrecondRepository.getInstance()
                .getSecretKet(BaseRequest(SecretKeyRequest()))
                .`as`(RxUtils.bindLifecycle(this))
                .subscribe({ baseResponse: SecretKeyResponse ->
                    val secret = baseResponse.datas!!.secret
                    EncryUtil.syncDecryptKey(secret)
                    ToastUtils.showShort(secret)
                    secret.isNotEmpty().yes {
                        statusObservable.set(StatusViewLayout.STATUS_CONTENT)
                    }.otherwise {
                        statusObservable.set(StatusViewLayout.STATUS_EMPTY)
                    }
                })
                { throwable: Throwable ->
                    ToastUtils.showShort(throwable.message)
                    StatusViewUtils.showError(statusObservable, throwable)
                }
    }



    var statusObservable: ObservableInt = ObservableInt(StatusViewLayout.STATUS_LOADING)
}