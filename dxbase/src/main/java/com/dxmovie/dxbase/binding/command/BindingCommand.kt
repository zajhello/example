package com.dxmovie.dxbase.binding.command

import com.dxmovie.dxbase.utils.extention.yes


/**
 *@author Levi
 *@date 2020/5/10
 *@desc dataBinding click command
 */

class BindingCommand {
    /** action */
    private var execute: (() -> Unit)? = {}

    /** check action */
    private var checkAction: (() -> Boolean)? = { true }


    /** @param execute 无返回类型 */
    constructor(execute: () -> Unit) {
        this.execute = execute
    }

    /** @param execute 命令绑定
     * @param checkAction 检查判断是否需要执行 默认执行 */
    constructor(execute: () -> Unit, checkAction: () -> Boolean = { false }) {
        this.execute = execute
        this.checkAction = checkAction
    }

    /** 不带参数执行 */
    fun executeAction() {
        if (checkAction == null) {
            execute?.invoke()
        } else {
            checkAction?.let {
                it().yes {
                    execute?.invoke()
                }
            }
        }
    }
}