package com.dxmovie.dxbase.binding.command

import com.dxmovie.dxbase.utils.extention.yes

/**
 *@desc dataBinding click command  带有返回值
 */
class BindingCommandWithParams<T> {
    /** action */
    private var execute: ((T) -> Unit)? = {}

    /** check action */
    private var checkAction: (() -> Boolean)? = { true }

    /** @param execute
     * @return T类型 */
    constructor(execute: ((T) -> Unit)) {
        this.execute = execute
    }

    /** @param execute
     * @param checkAction 判断是否可以执行
     * @return T类型 */
    constructor(execute: ((T) -> Unit), checkAction: (() -> Boolean) = { true }) {
        this.execute = execute
        this.checkAction = checkAction
    }

    /** 执行带参数的action */
    fun executeAction(params: T) {
        if (checkAction == null) {
            execute?.invoke(params)
        } else {
            checkAction?.let {
                it().yes {
                    execute?.invoke(params)
                }
            }
        }
    }
}