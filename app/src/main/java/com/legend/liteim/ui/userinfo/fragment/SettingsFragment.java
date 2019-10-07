package com.legend.liteim.ui.userinfo.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.bumptech.glide.Glide;
import com.legend.liteim.LoginActivity;
import com.legend.liteim.R;
import com.legend.liteim.common.utils.ActivityManager;
import com.legend.liteim.common.utils.DialogUtils;
import com.legend.liteim.data.GlobalData;

import java.util.Objects;

/**
 * @author Legend
 * @data by on 19-10-4.
 * @description
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    private Preference mSettingAutoUpdate, mCheckUpdate;
    private Preference mAbout, mClearCache, mLogout;

    public static final int CLEAR_GLIDE_CACHE_DONE = 1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case CLEAR_GLIDE_CACHE_DONE:
                    Snackbar.make(getActivity().getWindow().getDecorView(),"清除成功！", Snackbar.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_preference_fragment);

        mSettingAutoUpdate = findPreference("settingAutoUpdate");
        mCheckUpdate = findPreference("checkUpdate");
        mAbout = findPreference("about");
        mClearCache = findPreference("clearCache");
        mLogout = findPreference("logout");

        mClearCache.setOnPreferenceClickListener(preference -> {
            new Thread(() -> {
                Glide.get(getContext()).clearDiskCache();
                Message msg = new Message();
                msg.what = CLEAR_GLIDE_CACHE_DONE;
                handler.sendMessage(msg);
            }).start();
            Glide.get(getContext()).clearMemory();
            return false;
        });

        mLogout.setOnPreferenceClickListener(preference -> {
            logout();
            return false;
        });
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setTitle("您确认要退出登录吗？");
        builder.setMessage("此操作将会清除当前登录账号的所有信息");
        builder.setPositiveButton("确定", (dialog, which) ->{
            GlobalData.getInstance().logout();
            LoginActivity.show(getContext());
            DialogUtils.showToast("退出登录");
            ActivityManager.getInstance().finishAllActivity();
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }
}
