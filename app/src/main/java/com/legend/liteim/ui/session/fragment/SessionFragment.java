package com.legend.liteim.ui.session.fragment;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.legend.im.client.IMClient;
import com.legend.liteim.R;
import com.legend.liteim.bean.Session;
import com.legend.liteim.common.base.mvp.fragment.BaseFragment;
import com.legend.liteim.contract.message.SessionContract;
import com.legend.liteim.presenter.session.SessionPresenter;
import com.legend.liteim.ui.session.adapter.SessionItemAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.legend.liteim.common.base.mvp.StatusType.REFRESH_SUCCESS;

/**
 * @author Legend
 * @data by on 19-9-7.
 * @description
 */
public class SessionFragment extends BaseFragment<SessionContract.Presenter>
    implements SessionContract.View {

    private List<Session> sessionList = new ArrayList<>();

    @Override
    protected SessionContract.Presenter initPresenter() {
        return new SessionPresenter(this);
    }

    @Override
    protected BaseQuickAdapter getAdapter() {
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
