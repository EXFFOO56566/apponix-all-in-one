package com.tochy.browser.interfaces;


import com.tochy.browser.Model.FBStoryModel.NodeModel;
import com.tochy.browser.Model.story.TrayModel;

public interface UserListInterface {
    void userListClick(int position, TrayModel trayModel);

    void fbUserListClick(int position, NodeModel trayModel);
}
