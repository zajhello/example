package com.dxmovie.dxbase.binding.viewadapter.recyclerview

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class LineManagers constructor() {

    interface LineManagerFactory {
        fun create(recyclerView: RecyclerView): ItemDecoration?
    }

    companion object {

        fun both(): LineManagerFactory? {
            return object : LineManagerFactory {
                override fun create(recyclerView: RecyclerView): ItemDecoration? {
                    return DividerLine(recyclerView.context, DividerLine.LineDrawMode.BOTH)
                }
            }
        }

        fun horizontal(): LineManagerFactory? {
            return object : LineManagerFactory {
                override fun create(recyclerView: RecyclerView): ItemDecoration? {
                    return DividerLine(recyclerView.context, DividerLine.LineDrawMode.HORIZONTAL)
                }
            }
        }

        fun vertical(): LineManagerFactory? {
            return object : LineManagerFactory {
                override fun create(recyclerView: RecyclerView): ItemDecoration? {
                    return DividerLine(recyclerView.context, DividerLine.LineDrawMode.VERTICAL)
                }
            }
        }
    }
}