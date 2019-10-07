package com.legend.liteim.bean;

import android.text.TextUtils;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;
import com.legend.liteim.common.net.FinderApiService;
import com.legend.liteim.ui.contacts.adapter.ContactsAdapter;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * @author Legend
 * @data by on 19-9-10.
 * @description
 */

public class ChatGroup extends LitePalSupport implements MultiItemEntity, Serializable {

    /**
     * id : 1
     * name : Java交流群
     * ownerId : 1
     * owner : atur. Excep
     * portrait :
     * description :  ex ea commodo consequat. Duis aute irure dolor in reprehenderit
     * totalNumber : 0
     * numberLimit : 1
     * createTime : 1991-10-26T15:29:17
     * joined : false
     */
    @SerializedName("id")
    private Long groupId;
    private String name;
    private long ownerId;
    private String owner;
    private String portrait;
    private String description;
    private int totalNumber;
    private int numberLimit;
    private String createTime;
    private boolean joined;

    @Override
    public int getItemType() {
        return ContactsAdapter.TYPE_GROUP;
    }


    public Long getId() {
        return groupId;
    }

    public void setId(long groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPortrait() {
        if (!TextUtils.isEmpty(portrait)
                && !portrait.startsWith("http")) {
            setPortrait(FinderApiService.BASE_URL + portrait);
        }
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public int getNumberLimit() {
        return numberLimit;
    }

    public void setNumberLimit(int numberLimit) {
        this.numberLimit = numberLimit;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public boolean isJoined() {
        return joined;
    }

    public void setJoined(boolean joined) {
        this.joined = joined;
    }
}
