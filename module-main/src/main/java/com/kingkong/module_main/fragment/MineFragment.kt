package com.kingkong.module_main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dxmovie.dxbase.fragment.DXBaseFragment
import com.dxmovie.dxbase.utils.extention.setBottomMargin
import com.kingkong.module_main.BR
import com.kingkong.module_main.R
import com.kingkong.module_main.databinding.MainMineFragmentBinding
import com.kingkong.module_main.viewmodel.MineModel
import kotlinx.android.synthetic.main.main_mine_fragment.*

class MineFragment : DXBaseFragment<MainMineFragmentBinding, MineModel>() {

    companion object {
        val Arg = MineFragment::class.java.name

        @JvmStatic
        fun newInstance(): MineFragment = MineFragment()
    }

    override fun getContentViewLayout(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.main_mine_fragment
    }

    override fun getViewModelId(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): MineModel {
        return createViewModel(MineModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        srlRefresh.setEnableAutoLoadMore(true)
    }
}