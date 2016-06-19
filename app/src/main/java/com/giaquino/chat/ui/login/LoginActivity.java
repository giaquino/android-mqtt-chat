package com.giaquino.chat.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import autovalue.shaded.com.google.common.common.collect.Lists;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.giaquino.chat.ChatApplication;
import com.giaquino.chat.R;
import com.giaquino.chat.common.app.BaseActivity;
import com.giaquino.chat.flux.action.ProfileActionCreator;
import com.giaquino.chat.flux.store.ProfileStore;
import com.giaquino.chat.ui.chat.ChatActivity;
import java.util.List;
import javax.inject.Inject;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/11/16
 */
public class LoginActivity extends BaseActivity implements FacebookCallback<LoginResult> {

    private static final List<String> READ_PERMISSIONS = Lists.newArrayList("public_profile");

    @Inject ProfileActionCreator profileActionCreator;
    @Inject ProfileStore profileStore;

    @OnClick(R.id.chat_activity_login_button) public void onLoginClick() {
        LoginManager.getInstance().logInWithReadPermissions(this, READ_PERMISSIONS);
    }

    private CallbackManager callbackManager;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity_login);
        ButterKnife.bind(this);
        ChatApplication.get(this).applicationComponent().inject(this);
        initialize();
    }

    public void initialize() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, this);
        addSubscriptionToUnsubscribe(profileStore.asObservable().subscribe(store -> {
            startActivity(new Intent(getApplication(), ChatActivity.class));
            finish();
        }));
    }

    @Override public void onSuccess(LoginResult loginResult) {
        Profile profile = Profile.getCurrentProfile();
        profileActionCreator.setProfile(profile.getId(), profile.getFirstName(),
            profile.getLastName(), profile.getProfilePictureUri(300, 300).toString());
    }

    @Override public void onCancel() {
        Snackbar.make(ButterKnife.findById(this, android.R.id.content), "Login cancelled",
            Snackbar.LENGTH_SHORT).show();
    }

    @Override public void onError(FacebookException error) {
        Snackbar.make(ButterKnife.findById(this, android.R.id.content), error.getMessage(),
            Snackbar.LENGTH_SHORT).show();
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
