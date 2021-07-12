package com.dxmovie.dxbase.binding.viewadapter.viewgroup

import androidx.databinding.ViewDataBinding

interface IBindingItemViewModel<V: ViewDataBinding> {
    fun injecDataBinding(binding: V)
}