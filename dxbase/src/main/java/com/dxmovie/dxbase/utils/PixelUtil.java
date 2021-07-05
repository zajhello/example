package com.dxmovie.dxbase.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;
import com.dxmovie.dxbase.base.AppManager;

/**
 * 代码中的单位转换 px dip sp
 *
 */
public class PixelUtil {
  public final static int PX = TypedValue.COMPLEX_UNIT_PX;
  public final static int DIP = TypedValue.COMPLEX_UNIT_DIP;
  public final static int SP = TypedValue.COMPLEX_UNIT_SP;

  /**
   *
   * @param unit
   *          单位 </br>
   *          0 px</br>
   *          1 dip</br>
   *          2 sp
   * @param value
   *          size 大小
   * @return
   */
  public static float getDimensionPixelSize(int unit, float value) {
    DisplayMetrics metrics = new DisplayMetrics();
    WindowManager wm = (WindowManager) AppManager.getsApplication()
        .getSystemService(Context.WINDOW_SERVICE);
    wm.getDefaultDisplay().getMetrics(metrics);
    switch (unit) {
      case PX:
        return value;
      case DIP:
      case SP:
        return TypedValue.applyDimension(unit, value, metrics);
      default:
        throw new IllegalArgumentException("unknow unix");
    }
  }

  /**
   * 根据手机的屏幕属性从 dip 的单位 转成为 px(像素)
   *
   * @param value
   * @return
   */
  public static int dip2px(float value) {
    DisplayMetrics metrics = AppManager.getsApplication().getResources().getDisplayMetrics();
    return (int) (value * metrics.density + 0.5f);
  }

  /**
   * 根据手机的屏幕属性从 px(像素) 的单位 转成为 dip
   * @param value
   * @return
   */
  public static float px2dip(float value) {
    DisplayMetrics metrics = AppManager.getsApplication().getResources().getDisplayMetrics();
    return value / metrics.density;
  }

  /**
   * 根据手机的屏幕属性从 sp的单位 转成为px(像素)
   *
   * @param value
   * @return
   */
  public static float sp2px(float value) {
    DisplayMetrics metrics = AppManager.getsApplication().getResources().getDisplayMetrics();
    return value * metrics.scaledDensity;
  }

  /**
   * 根据手机的屏幕属性从 px(像素) 的单位 转成为 sp
   *
   * @param value
   * @return
   */
  public static float px2sp(float value) {
    DisplayMetrics metrics = AppManager.getsApplication().getResources().getDisplayMetrics();
    return value / metrics.scaledDensity;
  }


  public static int getWidth() {
    DisplayMetrics metrics = AppManager.getsApplication().getResources().getDisplayMetrics();
    return metrics.widthPixels;
  }

  public static int getHeight() {
    DisplayMetrics metrics = AppManager.getsApplication().getResources().getDisplayMetrics();
    return metrics.heightPixels;
  }

  public static int getStatusBarHeight() {
    int height = 0;
    int resourceId = AppManager.getsApplication().getResources().getIdentifier("status_bar_height", "dimen", "android");
    if (resourceId > 0) {
      height = AppManager.getsApplication().getResources().getDimensionPixelSize(resourceId);
    }

    if (height == 0) {
//      height = context.getResources().getDimensionPixelOffset(R.dimen.statusbar_view_height);
    }
    return height;
  }
}
