package com.dxmovie.dxbase.dialog;


/**
 * @desc
 */
public interface IBaseDialog {
    /**
     * 返回dialog的布局文件
     *
     * @return
     */
    int initLayout();

    /**
     * 初始化view事件
     */
    void initView();

    /**
     * 是否可以取消
     *
     * @return
     */
    boolean isCancel();

    /**
     * 点击外部是否可以取消
     *
     * @return
     */
    boolean isCancelOutside();

    /**
     * 自定义Layout
     *
     * @return
     */
    int getCustomLayout();

    /**
     * 是否全屏
     *
     * @return
     */
    boolean isFullScreen();

}
