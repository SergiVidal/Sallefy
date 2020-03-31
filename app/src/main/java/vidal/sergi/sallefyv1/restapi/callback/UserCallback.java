package vidal.sergi.sallefyv1.restapi.callback;

import java.util.List;

import vidal.sergi.sallefyv1.model.Playlist;
import vidal.sergi.sallefyv1.model.User;
import vidal.sergi.sallefyv1.model.UserToken;

public interface UserCallback extends FailureCallback {
    void onLoginSuccess(UserToken userToken);
    void onLoginFailure(Throwable throwable);
    void onRegisterSuccess();
    void onRegisterFailure(Throwable throwable);
    void onUserInfoReceived(User userData);
    void onUsersReceived(List<User> users);
    void onUsersFailure(Throwable throwable);;

}
