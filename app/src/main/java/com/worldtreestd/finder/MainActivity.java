package com.worldtreestd.finder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.worldtreestd.finder.bean.User;
import com.worldtreestd.finder.common.base.mvp.activity.BaseActivity;
import com.worldtreestd.finder.common.utils.ScreenUtils;
import com.worldtreestd.finder.data.DBData;
import com.worldtreestd.finder.data.SharedData;
import com.worldtreestd.finder.ui.dynamic.fragment.DynamicFragment;
import com.worldtreestd.finder.ui.mainpage.fragment.HomeFragment;
import com.worldtreestd.finder.ui.moreinfo.fragment.MoreInfoFragment;
import com.worldtreestd.finder.ui.release.ReleaseActivity;
import com.worldtreestd.finder.ui.userinfo.UserInfoActivity;

import butterknife.BindView;
import cn.jzvd.JZVideoPlayer;

import static com.worldtreestd.finder.common.utils.Constant.LOOK_USER;
import static com.worldtreestd.finder.common.utils.Constant.POSITION;

/**
 * @author legend
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.float_button)
    FloatingActionButton mFloatButton;
    @BindView(R.id.container)
    FrameLayout mContainer;
    AppCompatTextView mReleasePicture;
    AppCompatTextView mReleaseVideo;
    private PopupWindow mPopupWindow;
    final HomeFragment mHomeFragment = new HomeFragment();
    final MoreInfoFragment mMoreInfoFragment = new MoreInfoFragment();
    final DynamicFragment mDynamicFragment = new DynamicFragment();
    User user  = DBData.getInstance().getCurrentUser();

    public static void come(Context context, Integer id) {
        Intent intent = new Intent(context, MainActivity.class);
        if (id != null) {
            intent.putExtra(POSITION, id);
        }
        context.startActivity(intent);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            item -> {
                loadFragment(item.getItemId());
                return true;
            };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        super.onCreate(savedInstanceState);
        // 设置不可滑动
        setSwipeBackEnable(false);
        loadMultipleRootFragment(R.id.container,0, mHomeFragment,
                mMoreInfoFragment, mDynamicFragment);
        Integer id = getIntent().getIntExtra(POSITION, -1);
        setSelected(id);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        mPortrait.setOnClickListener(v -> {
            String jwt = SharedData.getInstance().getJWT();
            if (TextUtils.isEmpty(jwt)) {
                LoginActivity.come(this);
                finish();
                return;
            }
            Bundle bundle = new Bundle();
            User user = DBData.getInstance().getCurrentUser();
            bundle.putSerializable(LOOK_USER, user);
            UserInfoActivity.come(this, bundle);
        });
        mFloatButton.setOnClickListener(view->jumpTop(bottomNavigationView.getSelectedItemId()));
    }

    View mView;

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        // 获取用户信息
        initPopupWindow();
        mWriteButton.setOnClickListener(v -> {
            int[] windowPos = ScreenUtils.calculatePopWindowPos(v, mView);
            mPopupWindow.showAtLocation(v, Gravity.TOP|Gravity.START
                    , windowPos[0], windowPos[1]+20);
        } );
    }

    private void initPopupWindow() {
        mView = LayoutInflater.from(this).inflate(R.layout.dynamic_popupwindow, null);
        mPopupWindow= new PopupWindow(mView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        //点击 back 键的时候，窗口会自动消失
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        mReleasePicture = mView.findViewById(R.id.tv_release_picture);
        mReleasePicture.setOnClickListener(v-> startActivity(new Intent().setClass(this, ReleaseActivity.class).putExtra("TYPE", "0")));
        mReleaseVideo = mView.findViewById(R.id.tv_release_video);
        mReleaseVideo.setOnClickListener(v-> startActivity(new Intent().setClass(this, ReleaseActivity.class).putExtra("TYPE", "1")));
    }

    private void loadFragment(int id) {
        switch (id) {
            case R.id.navigation_home:
                setToolbarTitle(getString(R.string.title_home));
                showHideFragment(mHomeFragment);
                mSearchButton.setVisibility(View.VISIBLE);
                mWriteButton.setVisibility(View.INVISIBLE);
                break;
            case R.id.navigation_more_info:
                setToolbarTitle(getString(R.string.title_dashboard));
                showHideFragment(mMoreInfoFragment);
                mSearchButton.setVisibility(View.VISIBLE);
                mWriteButton.setVisibility(View.INVISIBLE);
                break;
            case R.id.navigation_dynamic:
                setToolbarTitle(getString(R.string.title_notifications));
                showHideFragment(mDynamicFragment);
                mSearchButton.setVisibility(View.GONE);
                mWriteButton.setVisibility(View.VISIBLE);
                break;
            default: break;
        }
    }

    private void setSelected(int id) {
        if (id != -1) {
            bottomNavigationView.setSelectedItemId(id);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    private void jumpTop(int id) {
        switch (id) {
            case R.id.navigation_home:
                mHomeFragment.jumpTop();
                break;
            case R.id.navigation_more_info:
                mMoreInfoFragment.jumpTop();
                break;
            case R.id.navigation_dynamic:
                mDynamicFragment.jumpTop();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }
    //    private void request1() {
////        NetworkService.getInstance().getCircleList()
////                .compose(new NetworkService.NetworkTransformer<>())
////                .subscribe(new Observer<BaseResponse<List<HomeCircleBean>>>() {
////                    @Override
////                    public void onSubscribe(Disposable d) {
////                        Toast.makeText(MainActivity.this,"The request is runtime!!!",Toast.LENGTH_LONG).show();
////
////                    }
////
////                    @Override
////                    public void onNext(BaseResponse<List<HomeCircleBean>> listBaseResponse) {
////                        for (HomeCircleBean bean : listBaseResponse.getResults()) {
////                            textView.setText(textView.getText()+bean.getAdd_time()+bean.getImage()+bean.getDesc()+"\n");
////                        }
//////                        textView.setText(listBaseResponse.getResults().get(0).getImage());
////                    }
////
////                    @Override
////                    public void onError(Throwable e) {
////                        e.printStackTrace();
////                    }
////
////                    @Override
////                    public void onComplete() {
////                        Toast.makeText(MainActivity.this,"The request is finished!!!",Toast.LENGTH_LONG).show();
////                    }
////                });
//        NetworkService.getInstance().getCircleList()
//                .subscribeOn(Schedulers.io())
//                .map(listBaseResponse -> listBaseResponse.getResults().get(0).getId())
//                .flatMap(new Function<Integer, ObservableSource<HomeCircleBean>>() {
//                    @Override
//                    public ObservableSource<HomeCircleBean> apply(Integer o) throws Exception {
////                        Toast.makeText(MainActivity.this,
////                                o+"",Toast.LENGTH_LONG).show();
//                        return  NetworkService.getInstance().getCircleDetail(o.toString());
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<HomeCircleBean>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        Toast.makeText(MainActivity.this,"The request is runtime!!!",Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onNext(HomeCircleBean homeCircleBean) {
//                        textView.
//                                setText(textView.getText()+homeCircleBean.getAdd_time()+homeCircleBean.getDesc()+homeCircleBean.getImage()+"\n");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Toast.makeText(MainActivity.this,"The request is finished!!!",Toast.LENGTH_LONG).show();
//                    }
//                });
//    }
//
//    private void request2() {
//        NetworkService.getInstance().getCircleList()
//                .subscribeOn(Schedulers.io())
////                .map(new Function<BaseResponse<List<HomeCircleBean>>, String>() {
////                    @Override
////                    public String apply(BaseResponse<List<HomeCircleBean>> listBaseResponse) throws Exception {
////
////                        return String.valueOf(listBaseResponse.getResults().get(0).getId());
////                    }
////                })
//                .flatMap(new Function<BaseResponse<List<HomeCircleBean>>, ObservableSource<HomeCircleBean>>() {
//                    @Override
//                    public ObservableSource<HomeCircleBean> apply(BaseResponse<List<HomeCircleBean>> listBaseResponse) throws Exception {
//                        if (listBaseResponse != null && listBaseResponse.getResults().size() > 0) {
//                            Log.d("Tag",listBaseResponse.getResults().size()+"");
//                            for (int i=0;i < listBaseResponse.getCount();i++) {
//                                return NetworkService.getInstance().getCircleDetail(String.valueOf(5000));
//                            }
//                        }
//                        return io.reactivex.Observable.empty();
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(homeCircleBean ->
//                        textView.setText(textView.getText().toString() + homeCircleBean.getId() + homeCircleBean.getAdd_time() +
//                        homeCircleBean.getImage() + homeCircleBean.getDesc() + "\n"), throwable -> {
//                    if (throwable instanceof HttpException) {
//                        ResponseBody body = ((HttpException)throwable).response().errorBody();
//                        Log.d("CodeCode", body.string());
//                        int response = ((HttpException) throwable).code();
//                        Log.d("CodeCode", response+ "---");
//                    }
//                });
//
//    }
//
//    private void request3() {
//        NetworkService.getInstance().getCircleList()
//                .subscribeOn(Schedulers.io())
//                .compose(ProgressTransformer.appProgressBar(this))
//                .flatMap(listBaseResponse -> NetworkService.getInstance().getCircleDetail(String.valueOf(5000)))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new BaseObserve<HomeCircleBean>() {
//                    @Override
//                    public void onSuccess(HomeCircleBean data) {
//                        textView.setText(textView.getText().toString() + data.getId() + data.getAdd_time() +
//                                data.getImage() + data.getDesc() + "\n");
//                    }
//
//                });
//    }
}
