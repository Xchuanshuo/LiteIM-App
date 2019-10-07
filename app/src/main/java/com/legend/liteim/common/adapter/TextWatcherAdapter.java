package com.legend.liteim.common.adapter;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * @author Legend
 * @data by on 19-9-13.
 * @description
 */
public abstract class TextWatcherAdapter implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
