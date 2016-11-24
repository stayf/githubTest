package com.stayfprod.github.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.stayfprod.github.R;
import com.stayfprod.github.databinding.ItemSearchBinding;
import com.stayfprod.github.model.SearchItem;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolderItem> {

    private List<SearchItem> mItemList = new ArrayList<SearchItem>();
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public SearchAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void cleanList() {
        mItemList.clear();
        notifyDataSetChanged();
    }

    public boolean isEmptyList() {
        return mItemList.isEmpty();
    }

    @Override
    public ViewHolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchAdapter.ViewHolderItem(DataBindingUtil.inflate(mLayoutInflater, R.layout.item_search, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolderItem holder, int position) {
        SearchItem item = mItemList.get(position);
        holder.bind.title.setText(item.fullName);
        holder.bind.desc.setText(item.description);
        holder.bind.image.setImageURI(Uri.parse(item.owner.avatarUrl));
    }

    public void updateList(List<SearchItem> items) {
        int currSize = items.size();
        mItemList.addAll(items);

        if (currSize == 0) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeInserted(currSize, items.size());
        }
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    class ViewHolderItem extends RecyclerView.ViewHolder {

        ItemSearchBinding bind;

        ViewHolderItem(ItemSearchBinding bind) {
            super(bind.getRoot());
            this.bind = bind;
        }
    }
}
