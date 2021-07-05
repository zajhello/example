package com.kingkong.module_main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.dxmovie.dxbase.fragment.DXBaseFragment
import com.kingkong.common_library.widget.BaseRecyclerView
import com.kingkong.module_main.BR
import com.kingkong.module_main.R
import com.kingkong.module_main.databinding.MainFragmentHomeItemBinding
import com.kingkong.module_main.viewmodel.HomeItemModel
import kotlinx.android.synthetic.main.main_fragment_home_item.*


class HomeItemFragment : DXBaseFragment<MainFragmentHomeItemBinding?, HomeItemModel>() {

    companion object {
        val Arg = HomeItemFragment::class.java.name

        @JvmStatic
        fun newInstance(bundle: Bundle?): HomeItemFragment {
            val fragment = HomeItemFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getContentViewLayout(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.main_fragment_home_item
    }

    override fun getViewModelId(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): HomeItemModel {
        return createViewModel(HomeItemModel::class.java)
    }

    override fun lazyLoad() {
        super.lazyLoad()
        initParams()
        loadData()
    }

    private fun initParams() {
        BaseRecyclerView.closeDefaultAnimator(recyclerView)
    }

    /**
     * 懒加载方法
     */
    fun loadData() {


    }


}