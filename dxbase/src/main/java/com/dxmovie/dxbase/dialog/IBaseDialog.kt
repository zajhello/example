package com.dxmovie.dxbase.dialog

interface IBaseDialog {

    /**
     * 返回dialog的布局文件
     *
     * @return
     */
    fun initLayout(): Int

    /**
     * 初始化view事件
     */
    fun initView()

    /**
     * 是否可以取消
     *
     * @return
     */
    fun isCancel(): Boolean

    /**
     * 点击外部是否可以取消
     *
     * @return
     */
    fun isCancelOutside(): Boolean

    /**
     * 自定义Layout
     *
     * @return
     */
    fun getCustomLayout(): Int

    /**
     * 是否全屏
     *
     * @return
     */
    fun isFullScreen(): Boolean
}