package com.kingkong.common_library.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class CommonRecyclerAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    protected Context mContext;
    protected LayoutInflater mInflater;
    //数据怎么办？
    protected List<T> mData;
    private int mLayoutId;
    private Page page;

    // 多布局支持
    private MultiTypeSupport mMultiTypeSupport;

    public CommonRecyclerAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mData = new ArrayList<>();
        this.mLayoutId = -1;
    }

    public void setMultiTypeSupport(MultiTypeSupport multiTypeSupport) {
        this.mMultiTypeSupport = multiTypeSupport;
    }

    public void setLayoutId(int layoutId) {
        this.mLayoutId = layoutId;
    }


    public Page getPage() {
        if (page == null) {
            page = new Page();
        }
        return page;
    }

    /**
     * 增加Item
     *
     * @param t
     */
    public void add(int index, T t) {
        mData.add(index, t);
        notifyItemInserted(index);
    }

    /**
     * 替换Item
     *
     * @param t
     */
    public void replace(int index, T t) {
        mData.set(index, t);
        notifyItemChanged(index);
    }

    /**
     * 增加Item
     *
     * @param t
     */
    public void add(T t) {
        if (t != null) {
            mData.add(t);
            notifyItemInserted(mData.size() - 1);
        }
    }

    /**
     * 增加List、
     *
     * @param list
     */
    public void addAll(List<T> list) {
        if (list != null && list.size() > 0) {
            mData.addAll(list);
            notifyItemRangeInserted(mData.size() - list.size(), list.size());
        }
    }

    public void autoAddAll(List<T> list) {
        if (getPage().page == 1) {
            setList(list);
        } else {
            addAll(list);
        }
    }

    /**
     * 移除item
     *
     * @param t
     * @return
     */
    public boolean remove(T t) {
        boolean remove = mData.remove(t);
        notifyDataSetChanged();
        return remove;
    }

    public void remove(int index) {
        if (index < 0 || mData.size() < index + 1)
            return;
        mData.remove(index);
        notifyItemRemoved(index);
    }

    /**
     * 移除list
     *
     * @param list
     * @return
     */
    public boolean removeAll(List<T> list) {
        boolean b = mData.removeAll(list);
        notifyDataSetChanged();
        return b;
    }

    /**
     * 获取List
     *
     * @return
     */
    public List<T> getList() {
        return mData;
    }

    /**
     * 设置List
     *
     * @param list
     */
    public void setList(List<T> list) {
        mData.clear();
        mData.addAll(list);
        notifyDataSetChanged();
    }


    /**
     * 刷新数据
     *
     * @param list
     */
    public void refresh(List<T> list) {
        setList(list);
    }

    /**
     * 加载更多
     *
     * @param list
     */
    public void loadMore(List<T> list) {
        addAll(list);
    }

    /**
     * 根据当前位置获取不同的viewType
     */
    @Override
    public int getItemViewType(int position) {
        // 多布局支持
        if (mMultiTypeSupport != null) {
            return mMultiTypeSupport.getLayoutId(mData.get(position), position);
        }
        return super.getItemViewType(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 多布局支持
        if (mMultiTypeSupport != null) {
            mLayoutId = viewType;
        }
        // 先inflate数据
        View itemView = mInflater.inflate(mLayoutId, parent, false);
        // 返回ViewHolder
        ViewHolder holder = new ViewHolder(itemView);


        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // 设置点击和长按事件
        if (mItemClickListener != null) {

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(v, position);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mItemClickListener.onItemLongClick(v, position);
                }
            });

        }

        convert(holder, position, mData.get(position));
    }

    /**
     * 利用抽象方法回传出去，每个不一样的Adapter去设置
     *
     * @param item 当前的数据
     */
    public abstract void convert(ViewHolder holder, int position, T item);

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /***************
     * 设置条目点击和长按事件
     *********************/
    public OnItemClickListener mItemClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }


    public class Page {
        public Page() {
            this.page = 1;
            this.size = 20;
        }

        public Page(int page, int size) {
            this.page = page;
            this.size = size;
        }

        private int page;
        private int size;

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public void next() {
            this.page++;
        }

        public void reset() {
            this.page = 1;
        }

    }
}