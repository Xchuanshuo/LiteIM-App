package com.legend.liteim.ui.session.fragment;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.legend.im.client.IMClient;
import com.legend.liteim.R;
import com.legend.liteim.bean.Session;
import com.legend.liteim.common.adapter.OnItemSwipeAdapter;
import com.legend.liteim.common.base.mvp.fragment.BaseFragment;
import com.legend.liteim.contract.message.SessionContract;
import com.legend.liteim.db.SessionHelper;
import com.legend.liteim.presenter.session.SessionPresenter;
import com.legend.liteim.ui.session.adapter.SessionItemAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.legend.liteim.common.base.mvp.StatusType.REFRESH_SUCCESS;

/**
 * @author Legend
 * @data by on 19-9-7.
 * @description
 */
public class SessionFragment extends BaseFragment<SessionContract.Presenter, SessionItemAdapter>
    implements SessionContract.View {

    private List<Session> sessionList = new ArrayList<>();

    @Override
    protected SessionContract.Presenter initPresenter() {
        return new SessionPresenter(this);
    }

    @Override
    protected SessionItemAdapter getAdapter() {
        return new SessionItemAdapter(R.layout.item_session, sessionList);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_session;
    }

    @Override
    protected void initWidget() {
        setEnableLoaderMore(false);
        super.initWidget();
        getEmptyTextView().setText(getString(R.string.empty_session_list));
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mAdapter);
        itemDragAndSwipeCallback.setSwipeMoveFlags(ItemTouchHelper.START);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        // 开启拖拽
        mAdapter.enableDragItem(itemTouchHelper, R.id.lay_session, true);
        mAdapter.setOnItemDragListener(new OnItemDragListener() {
            @Override
            public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {

            }

            @Override
            public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {

            }

            @Override
            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {

            }
        });

        // 开启滑动删除
        mAdapter.enableSwipeItem();
        mAdapter.setOnItemSwipeListener(new OnItemSwipeAdapter() {
            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
                long id = Objects.requireNonNull(mAdapter.getItem(pos)).getId();
                SessionHelper.getInstance().removeById(id);
            }
        });
    }

    @Override
    public void refreshData() {
        mPresenter.pullSessionList();
        if (IMClient.getInstance().isClosed()) {
            // 被动关闭的情况直接尝试重连
            IMClient.getInstance().reconnect();
        }
    }

    @Override
    public void showNewSession(Session session) {
        sessionList.add(0, session);
        mAdapter.notifyItemInserted(0);
    }

    @Override
    public void showSessionList(List<Session> sessions) {
        sessionList.clear();
        sessionList.addAll(sessions);
        setLoadDataResult(sessionList, REFRESH_SUCCESS);
    }
}
