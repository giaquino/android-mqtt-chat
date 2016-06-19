package com.giaquino.chat.common.app;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import butterknife.BindView;
import com.giaquino.chat.ChatApplication;
import com.giaquino.chat.R;
import java.util.ArrayList;
import java.util.List;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/10/16
 */
public abstract class BaseActivity extends AppCompatActivity {

    private final static Handler MAIN_THREAD_HANDLER = new Handler(Looper.getMainLooper());

    @Nullable @BindView(R.id.chat_coordinator_layout) protected CoordinatorLayout coordinatorLayout;
    @Nullable @BindView(R.id.chat_appbar_layout) protected AppBarLayout appBarLayout;
    @Nullable @BindView(R.id.chat_toolbar) protected Toolbar toolbar;

    private CompositeSubscription subscriptions = new CompositeSubscription();
    private List<Runnable> runnables = new ArrayList<>();
    private volatile boolean isRunning;

    public void addSubscriptionToUnsubscribe(@NonNull Subscription subscription) {
        subscriptions.add(subscription);
    }

    protected void runOnUIThreadIfAlive(@NonNull final Runnable runnable) {
        if (!isAlive()) {
            Timber.d("Activity not alive, defer running runnable.");
            return;
        }
        if (!isRunning) {
            runnables.add(runnable);
            Timber.d("Activity is not running, caching runnable. cache count %d", runnables.size());
            return;
        }
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
            return;
        }
        MAIN_THREAD_HANDLER.post(runnable::run);
    }

    @SuppressWarnings("Convert2streamapi") @Override protected void onResume() {
        super.onResume();
        isRunning = true;
        for (Runnable runnable : runnables) {
            runnable.run();
        }
        runnables.clear();
    }

    @Override protected void onPause() {
        super.onPause();
        isRunning = false;
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        subscriptions.clear();
        ChatApplication.get(this).refWatcher().watch(this);
    }

    protected boolean isAlive() {
        return !isFinishing();
    }
}
