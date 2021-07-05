package com.kingkong.module_main.fragment

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dxmovie.dxbase.fragment.DXBaseFragment
import com.kingkong.module_main.BR
import com.kingkong.module_main.R
import com.kingkong.module_main.databinding.MainFragmentHomeBinding
import com.kingkong.module_main.viewmodel.HomeModel
import com.kingkong.module_main.widget.MagicTabPagerLinkage
import kotlinx.android.synthetic.main.main_fragment_home.*
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator

class HomeFragment : DXBaseFragment<MainFragmentHomeBinding?, HomeModel>() {


    companion object {
        val Arg = HomeFragment::class.java.name

        @JvmStatic
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    var tabs = listOf("推荐", "电视剧", "电影", "综艺", "动漫", "动漫", "动漫")

    val fragments: MutableList<HomeItemFragment> = mutableListOf()
    var linkage: MagicTabPagerLinkage? = null

    override fun getContentViewLayout(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.main_fragment_home
    }

    override fun getViewModelId(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): HomeModel {
        return createViewModel(HomeModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        linkage = MagicTabPagerLinkage(this)
        linkage!!.setContainer(viewPager)
                .setMagicIndicator(magicMI)
                .setTitleSize(20)
                .setTitleNormalColor(Color.parseColor("#999999"))
                .setTitleSelectedColor(Color.parseColor("#367DFD"))
                .setTextStyle(Typeface.BOLD)
                .setTitleScale(true)
                .setIndicatorBounce(false)
                .setIndicatorMode(LinePagerIndicator.MODE_EXACTLY)
                .setIndicatorWidthDP(15)
                .setIndicatorYOffsetDp(0)
                .setIndicatorHeightDP(3)
                .setIndicatorColor(Color.parseColor("#FFFFFF"))
                .addOnTabSelectedListener { index: Int ->
                    getViewModel().setSearchSwitch(index == 0)
                    linkage!!.select(index)
                }

        for (tab in tabs) {
            var bundle = Bundle()
            bundle.putString(HomeItemFragment.Arg, tab)
            var item: HomeItemFragment = HomeItemFragment.newInstance(bundle)
            fragments.add(item)
        }

        linkage!!.initStringTabs(tabs).initFragmentPages(fragments as List<Fragment>?).build().select(0)
    }

    private fun notifyDataSetChange() {
        setOffscreenPageLimit()
        viewPager.adapter?.notifyDataSetChanged()
    }

    private fun setOffscreenPageLimit() {
        viewPager.offscreenPageLimit = if (fragments.size > 1) fragments.size - 1 else fragments.size
    }
}
