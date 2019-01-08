package com.worldtreestd.finder.ui.moreinfo.viewholder;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.worldtreestd.finder.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Legend
 * @data by on 18-7-15.
 * @description
 */
public class DetailViewHolder extends BaseViewHolder {

    @BindView(R.id.from_Nick)
    TextView name;

    public DetailViewHolder(View view) {
        super(view);
        ButterKnife.bind(view);
    }
}
