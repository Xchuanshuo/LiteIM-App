package com.worldtreestd.finder.ui.release.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.worldtreestd.finder.R;
import com.worldtreestd.finder.common.utils.GlideUtil;
import com.zhihu.matisse.internal.entity.Item;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Legend
 * @data by on 18-8-23.
 * @description
 */
public class GridViewAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private List<Item> dataList = new ArrayList<>();
    private int maxCount = 9;
    private AddClickListener addClickListener;
    private ItemClickListener itemClickListener;

    public GridViewAdapter(Context context) {
        this.mContext = context;
        inflater = LayoutInflater.from(context);
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setAddClickListener(AddClickListener addClickListener) {
        this.addClickListener = addClickListener;
    }

    public List<Item> getData() {
        return dataList;
    }

    public void setData(List<Item> list) {
        this.dataList = list;
        notifyDataSetChanged();
    }

    public void addData(List<Item> list) {
        dataList.addAll(list);
        notifyDataSetChanged();
    }

    public void updateData(int position, Item item) {
        if (dataList.size()>position) {
            dataList.set(position, item);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        int count = dataList.size()==0? 1: dataList.size()+1;
        return count>maxCount? maxCount : count;
    }

    @Override
    public Item getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_release_picture, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (position<dataList.size()) {
            final File file = new File(dataList.get(position).getPath());
            GlideUtil.loadImage(mContext, file, viewHolder.mImageView);
            viewHolder.mDeleteButton.setVisibility(View.VISIBLE);
            viewHolder.mImageView.setOnClickListener(v -> itemClickListener.onItemClickListener(position));
            viewHolder.mDeleteButton.setOnClickListener(v -> {
                dataList.remove(position);
                if (file.exists()) {
                    file.delete();
                }
                notifyDataSetInvalidated();
            });
        } else {
            if (viewHolder != null) {
                GlideUtil.loadImage(mContext, R.mipmap.ic_add, viewHolder.mImageView);
                viewHolder.mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                viewHolder.mDeleteButton.setVisibility(View.GONE);
                viewHolder.mImageView.setOnClickListener(v -> addClickListener.onAddClickListener(v));
            }
        }
        return convertView;
    }

    public interface AddClickListener {
        void onAddClickListener(View view);
    }

    public interface ItemClickListener {
        void onItemClickListener(int position);
    }

    static class ViewHolder {
        AppCompatImageView mImageView;
        AppCompatButton mDeleteButton;

        public ViewHolder(View view) {
            mImageView = view.findViewById(R.id.iv_image);
            mDeleteButton = view.findViewById(R.id.btn_delete);
        }
    }
}
