/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.google.android.gms.ads.AdView;
import com.ninexe.ui.R;
import com.ninexe.ui.adapters.NavigationDrawerAdapter;
import com.ninexe.ui.analytics.AnalyticsConstants;
import com.ninexe.ui.analytics.NixonTracker;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.fragments.BaseFragment;
import com.ninexe.ui.fragments.HomeFragment;
import com.ninexe.ui.fragments.LoginFragment;
import com.ninexe.ui.fragments.RetryFragment;
import com.ninexe.ui.fragments.TabSectionFragment;
import com.ninexe.ui.models.Article;
import com.ninexe.ui.models.GenericResponse;
import com.ninexe.ui.models.HomeData;
import com.ninexe.ui.models.Menu;
import com.ninexe.ui.models.NotificationSubscriptionModel;
import com.ninexe.ui.models.NotificationSubscriptionResponse;
import com.ninexe.ui.models.PollDataHandler;
import com.ninexe.ui.network.NetworkAdapter;
import com.ninexe.ui.network.ResponseCallback;
import com.ninexe.ui.network.RestError;
import com.ninexe.ui.utils.DateTimeUtils;
import com.ninexe.ui.utils.DialogUtils;
import com.ninexe.ui.utils.EmailUtils;
import com.ninexe.ui.utils.LogUtils;
import com.ninexe.ui.utils.NavigationUtils;
import com.ninexe.ui.utils.NetworkCheckUtility;
import com.ninexe.ui.utils.PreferenceManager;
import com.ninexe.ui.utils.RateAppUtil;
import com.ninexe.ui.utils.ShareUtils;
import com.ninexe.ui.utils.ViewUtils;
import com.ninexe.ui.utils.dataproviders.HomeDataProvider;
import com.ninexe.ui.utils.dataproviders.NavigationDrawerDataProvider;
import com.ninexe.ui.utils.dataproviders.OfflineArticleDataHandler;
import com.ninexe.ui.utils.dataproviders.SettingsDataProvider;
import com.ninexe.ui.utils.dataproviders.UserDataProvider;

import java.util.ArrayList;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by nagesh on 7/10/15.
 */
public class HomeActivity extends BaseActivity implements
        TabSectionFragment.OnTabSectionFragmentInteractionListener,
        RetryFragment.OnRetryInteractionListener {

    private static final String TAG = HomeActivity.class.getSimpleName();

    private final float LEFT_DRAWER_PERCENT = 0.55f;
    private static final int READ_PHONE_STATE_PERMISSION = 1;

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Bind(R.id.nav_drawer_list)
    ExpandableListView mNavigationDrawerList;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.left_drawer)
    View mLeftDrawerContainer;

    @Bind(R.id.adView)
    AdView mAdView;

    private int mCurrentMenuDrawerSelectedPosition;
    private NavigationDrawerAdapter mNavigationDrawerAdapter;
    private ArrayList<Menu> mNavigationMenuArrayList = new ArrayList<>();
    private View mView;

    /**
     * Start of Activity life cycle methods
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            if (null == HomeDataProvider.getInstance().getHomeData())
                HomeDataProvider.getInstance().setHomeData((HomeData) savedInstanceState.getParcelable("home_data"));
        }
        registerFragmentBackStackChangeListener();
        if (getIntent().hasExtra(Constants.EXTRA_DEEPLINK)) {
            loadArticle(getIntent().getStringExtra(Constants.EXTRA_ARTICLE_ID),
                    getIntent().getStringExtra(Constants.EXTRA_ARTICLE_TYPE),
                    getIntent().getStringExtra(Constants.EXTRA_ARTICLE_TITLE));
        } else
            displayHomeScreenData();
        setTextSizeDefaultZoom();
        loadAd(mAdView);
        if (!PreferenceManager.isSubscribedToNotifications(this)) {
            if (PackageManager.PERMISSION_DENIED != ContextCompat.checkSelfPermission(HomeActivity.this,
                    Manifest.permission.READ_PHONE_STATE)) {
                subscribeForNotifications();
            } else {
                ActivityCompat.requestPermissions(HomeActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        READ_PHONE_STATE_PERMISSION);
            }
        }
    }

    private void loadArticle(String articleID, String type, String title) {
        if (NetworkCheckUtility.isNetworkAvailable(this)) {
            if (null != type && type.equalsIgnoreCase(Constants.PHOTO)) {
                NavigationUtils.startPhotoGalleryActivityForResult(this, articleID);
            } else if (null != type && (type.equalsIgnoreCase(Constants.QUIZ) || type.equalsIgnoreCase(Constants.PERSONALITY_TEST) ||
                    type.equalsIgnoreCase(Constants.CONTEST) || type.equalsIgnoreCase(Constants.POLL))) {
                if (type.equalsIgnoreCase(Constants.POLL)) {
                    if (PollDataHandler.isPollAttempted(articleID, this)) {
                        initToolbar();
                        fetchSections();
                        DialogUtils.showSingleButtonAlertDialog(this, "", getString(R.string.poll_attemted), getString(R.string.ok));
                        return;
                    }
                }
                NavigationUtils.startQuizActivityForResult(this, articleID, type);
            } else {
                NavigationUtils.startArticleDetailActivityForResult(this, articleID, title);
            }
        } else {
            if (OfflineArticleDataHandler.isArticleCached(articleID, this)) {
                NavigationUtils.startArticleDetailActivityForResult(this, articleID, title);
            } else {
                DialogUtils.showAlertDialogWithCallBack(this, "", getString(R.string.no_network), getString(R.string.ok),
                        "", false, new DialogUtils.DialogInterfaceCallBack() {
                            @Override
                            public void positiveButtonClick(DialogInterface dialog) {
                                finish();
                            }

                            @Override
                            public void negativeButtonClick(DialogInterface dialog) {

                            }
                        }, false);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!getIntent().hasExtra(Constants.EXTRA_DEEPLINK))
            updateDrawerItems();
    }

    private boolean isLogOutAdded() {
        boolean isLogOutAdded = false;
        for (Menu menu : mNavigationMenuArrayList) {
            if (menu.getTitle().equals(Constants.MENU_LOGOUT)) {
                isLogOutAdded = true;
            }
        }
        return isLogOutAdded;
    }

    private void fetchSections() {
        DialogUtils.showCustomProgressDialog(this, true);
        if (NetworkCheckUtility.isNetworkAvailable(this)) {
            ResponseCallback<HomeData> responseCallback = new ResponseCallback<HomeData>() {
                @Override
                public void success(HomeData homeData) {
                    HomeDataProvider.getInstance().setHomeData(homeData);
                    if (OfflineArticleDataHandler.isOfflineCacheEnabled(HomeActivity.this)) {
                        OfflineArticleDataHandler.setHomeResponse(HomeActivity.this, homeData);
                    }
                    DialogUtils.cancelProgressDialog();
                    displayHomeScreenData();
                    updateDrawerItems();

                }

                @Override
                public void failure(RestError error) {
                    DialogUtils.cancelProgressDialog();
                    displayHomeScreenData();
                    updateDrawerItems();
                }
            };

            NetworkAdapter.get(this).getSections(responseCallback);
        } else {
            DialogUtils.cancelProgressDialog();
            if (OfflineArticleDataHandler.isOfflineCacheEnabled(HomeActivity.this) &&
                    OfflineArticleDataHandler.isHomeScreenCached(HomeActivity.this)) {
                HomeDataProvider.getInstance().setHomeData(OfflineArticleDataHandler.getHomeResponse(this));
                displayHomeScreenData();
                updateDrawerItems();
            }
        }
    }

    private void updateDrawerItems() {
        setNavigationHeaderData();
        ViewUtils.setThemeBackground(mToolbar);
        ViewUtils.setThemeBackground(findViewById(R.id.navigation_header_container));
        mNavigationDrawerList.setSelection(0);
        if (UserDataProvider.getInstance().isLoggedIn(this) && !isLogOutAdded()) {
            Menu logout = new Menu();
            logout.setTitle(Constants.MENU_LOGOUT);
            mNavigationMenuArrayList.add(logout);
            mNavigationDrawerAdapter.notifyDataSetChanged();
        }
        DateTimeUtils.isTimeWithInNotificationDNDRange();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        LogUtils.LOGD("BackStackEntryCount: from onBackPressed", fm.getBackStackEntryCount() + "");
        HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(HomeFragment.HOME_FRAGMENT);
        if (null != mDrawerLayout && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            closeNavigationDrawer();
        } else if (homeFragment != null && homeFragment.isVisible() && fm.getBackStackEntryCount() == 0) {
            DialogUtils.showAppQuitDialog(this);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * End of Activity life cycle methods
     */


    /**
     * Start of Member Variable related methods
     */
    private void setCurrentMenuDrawerSelectedPosition(int mCurrentMenuDrawerSelectedPosition) {
        this.mCurrentMenuDrawerSelectedPosition = mCurrentMenuDrawerSelectedPosition;
    }

    private int getCurrentMenuDrawerSelectedPosition() {
        return mCurrentMenuDrawerSelectedPosition;
    }

    public ArrayList<Menu> getNavigationMenuList() {
        return mNavigationMenuArrayList;
    }

    public void setNavigationMenuArrayList(ArrayList<Menu> mNavigationMenuArrayList) {
        this.mNavigationMenuArrayList = mNavigationMenuArrayList;
    }

/**
 * end of Member Variable related methods
 */

    /**
     * Start of toolbar related methods
     */
    private void initToolbar() {
        setToolbar(mToolbar);
        setNavigationIcon(R.drawable.menu_icn);
        displayHeaderLogo();
    }

    @OnClick(R.id.navigation_icon)
    void onNavigationIconClick() {
        NixonTracker.get().logScreenVisits(AnalyticsConstants.EVENT_TAP_MENU, null);
        openNavigationDrawer();
    }

    @OnClick(R.id.right_icon)
    void onSearchClick() {
        if (NetworkCheckUtility.isNetworkAvailable(this)) {
            NixonTracker.get().logScreenVisits(AnalyticsConstants.EVENT_TAP_SEARCH, null);
            NavigationUtils.startSearchActivity(this);
        } else {
            DialogUtils.showNoNetworkDialog(this);
        }
    }

    /**
     * End of toolbar related methods
     */


    /**
     * Start of Navigation Drawer related methods
     */

    private void initNavigationDrawer() {
        if (mView == null)
            mView = LayoutInflater.from(this).inflate(R.layout.navigation_drawer_header, null);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UserDataProvider.getInstance().isLoggedIn(HomeActivity.this)) {
                    closeNavigationDrawer();
                    NavigationUtils.startLoginActivity(HomeActivity.this);
                }
            }
        });
        setNavigationHeaderData();
        mNavigationDrawerList.addHeaderView(mView);
        mNavigationMenuArrayList.clear();
        mNavigationMenuArrayList.addAll(NavigationDrawerDataProvider.getInstance().getMenuList(this));
        mNavigationDrawerAdapter = new NavigationDrawerAdapter(this, mNavigationMenuArrayList);
        mNavigationDrawerList.setAdapter(mNavigationDrawerAdapter);
        handleNavigationDrawerListOnClickListeners();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        TypedValue outValue = new TypedValue();
        getResources().getValue(R.dimen.navigation_drawer_width, outValue, true);
        float navigationDrawerWidth = outValue.getFloat();
        mLeftDrawerContainer.getLayoutParams().width = (int) (metrics.widthPixels * navigationDrawerWidth);
    }

    private void setNavigationHeaderData() {
        ImageView profileImageView = (ImageView) mView.findViewById(R.id.profile_image);
        TextView userName = (TextView) mView.findViewById(R.id.user_name);
        BitmapPool bitmapPool = Glide.get(this).getBitmapPool();
        if (UserDataProvider.getInstance().isLoggedIn(HomeActivity.this)) {
            Glide.with(HomeActivity.this)
                    .load(UserDataProvider.getInstance().getProfilePictureUrl(HomeActivity.this))
                    .bitmapTransform(new CropCircleTransformation(bitmapPool))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.placeholder_pro_pic)
                    .crossFade()
                    .into(profileImageView);
            userName.setText(UserDataProvider.getInstance().getName(HomeActivity.this));
        } else {
            //Guest User
            userName.setText(R.string.guest_user);
        }
    }

    private void handleNavigationDrawerListOnClickListeners() {
        mNavigationDrawerList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                onNavigationDrawerGroupClick(parent, groupPosition);
                return true;
            }
        });

        mNavigationDrawerList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                onNavigationDrawerChildClick(parent, groupPosition, childPosition);
                return true;
            }
        });

    }

    private void onNavigationDrawerChildClick(ExpandableListView parent, int groupPosition, int childPosition) {
        NavigationDrawerAdapter navigationDrawerAdapter = getNavigationAdapter();
        Menu menu = navigationDrawerAdapter.getGroup(groupPosition);
        parent.collapseGroup(groupPosition);
        parent.setSelection(0);
        closeNavigationDrawer();

        if (NetworkCheckUtility.isNetworkAvailable(this) || OfflineArticleDataHandler.isSectionCached(this, menu.getSubMenu().get(childPosition).getId())) {
            if (TextUtils.equals(menu.getSubMenu().get(childPosition).getType(), Constants.LIVE_TV)) {
                Intent intent = new Intent(this, LiveTVActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.EXTRA_SUB_MENU, menu.getSubMenu().get(childPosition));
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                NavigationUtils.startSectionDetailActivity(this, menu.getSubMenu(), childPosition, menu.getTitle());
            }
        } else {
            DialogUtils.showNoNetworkDialog(this);
        }
    }

    private void onNavigationDrawerGroupClick(ExpandableListView parent, int groupPosition) {
        NavigationDrawerAdapter navigationDrawerAdapter = getNavigationAdapter();

        if (navigationDrawerAdapter.getChildrenCount(groupPosition) == 0) {
            closeNavigationDrawer();
            Menu menu = navigationDrawerAdapter.getGroup(groupPosition);
            switch (menu.getTitle().toLowerCase()) {
                case Constants.MENU_HOME:
                    if (groupPosition != getCurrentMenuDrawerSelectedPosition()) {
                        loadHomeFragment();
                        setCurrentMenuDrawerSelectedPosition(groupPosition);
                    }
                    break;
                case Constants.MENU_HELLO_EDITOR:
                    sendEmailToHelloEditor();
                    break;
                case Constants.MENU_NOTIFICATION_HUB:
                    closeNavigationDrawer();
                    if (NetworkCheckUtility.isNetworkAvailable(this)) {
                        NavigationUtils.startNotificationHubActivity(this);
                    } else {
                        DialogUtils.showNoNetworkDialog(this);
                    }
             /*       if (groupPosition != getCurrentMenuDrawerSelectedPosition()) {
                        loadNotificationHubFragment();
                        setCurrentMenuDrawerSelectedPosition(groupPosition);
                    }*/
                    break;
                case Constants.MENU_NEWS_LETTER:
             /*       if (groupPosition != getCurrentMenuDrawerSelectedPosition()) {
                        loadNewsLetterFragment();
                        setCurrentMenuDrawerSelectedPosition(groupPosition);
                    }*/
                    closeNavigationDrawer();
                    NavigationUtils.startNewsLetterActivity(this);
                    break;
                case Constants.MENU_SHARE_THE_APP:
                    NixonTracker.get().logScreenVisits(AnalyticsConstants.EVENT_TAP_APP_SHARE, null);
                    if (NetworkCheckUtility.isNetworkAvailable(this)) {
                        if (null != SettingsDataProvider.getInstance().getStaticUrlResponse()) {
                            ShareUtils.share(SettingsDataProvider.getInstance().getStaticUrlResponse().getData()
                                    .getAndroidAppStoreUrl(), HomeActivity.this);
                        }

                    } else {
                        DialogUtils.showNoNetworkDialog(this);
                    }
                    break;
                case Constants.MENU_SETTINGS:
                    NixonTracker.get().logScreenVisits(AnalyticsConstants.EVENT_TAP_SETTINGS, null);
/*                    if (groupPosition != getCurrentMenuDrawerSelectedPosition()) {
                        loadSettingsFragment();
                        setCurrentMenuDrawerSelectedPosition(groupPosition);
                    }*/
                    closeNavigationDrawer();
                    NavigationUtils.startSettingsActivity(this);
                    break;
                case Constants.MENU_LOGOUT:
                    showLogoutConfirmationDialog();
                    break;
            }
            navigationDrawerAdapter.setCurrentSelectedPosition(getCurrentMenuDrawerSelectedPosition());
        } else {
            if (!parent.isGroupExpanded(groupPosition)) {
                parent.expandGroup(groupPosition);
            } else {
                parent.collapseGroup(groupPosition);
            }
            int len = navigationDrawerAdapter.getGroupCount();

            for (int i = 0; i < len; i++) {
                if (i != groupPosition) {
                    parent.collapseGroup(i);
                }
            }
        }
        parent.setSelection(groupPosition);
    }

    private void showLogoutConfirmationDialog() {
        DialogUtils.showAlertDialogWithCallBack(HomeActivity.this, "", getString(R.string.logout_confirmation),
                getString(R.string.yes), getString(R.string.no),
                true, new DialogUtils.DialogInterfaceCallBack() {
                    @Override
                    public void positiveButtonClick(DialogInterface dialog) {
                        PreferenceManager.clearPreference(HomeActivity.this);
                        NavigationUtils.startLoginActivity(HomeActivity.this);
                        finish();
                    }

                    @Override
                    public void negativeButtonClick(DialogInterface dialog) {

                    }
                });
    }

    private void sendEmailToHelloEditor() {
        if (null != SettingsDataProvider.getInstance().getStaticUrlResponse()) {
            EmailUtils.sendEmail(this,
                    SettingsDataProvider.getInstance().getStaticUrlResponse().getData().getHelloEditorEmail(),
                    getString(R.string.hello_editor_subject),
                    getString(R.string.email_chooser_text));
        }
    }

    private NavigationDrawerAdapter getNavigationAdapter() {
        return mNavigationDrawerAdapter;
    }

    private void openNavigationDrawer() {
        if (null != mDrawerLayout && !mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }


    private void
    closeNavigationDrawer() {
        if (null != mDrawerLayout && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    /**
     * End of Navigation Drawer related methods
     */


    /**
     * Start of Web Service related methods
     */

    private void displayHomeScreenData() {
        initToolbar();
        loadHomeFragment();
        initNavigationDrawer();
    }

    /**
     * End of Web Service related methods
     */


    /**
     * Start of Fragment Transaction Methods
     */

    private void registerFragmentBackStackChangeListener() {
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                FragmentManager fm = getSupportFragmentManager();
                LogUtils.LOGD("BackStackEntryCount: from backstack change listener", fm.getBackStackEntryCount() + "");
            }
        });
    }

    private void loadHomeFragment() {
        HomeFragment homeFragment =
                HomeFragment.newInstance();
        loadFragment(R.id.drawer_base_container, homeFragment, HomeFragment.HOME_FRAGMENT, 0, 0,
                BaseFragment.FragmentTransactionType.CLEAR_BACK_STACK_AND_REPLACE);
    }
    /**
     * End of  Fragment Transaction Methods
     */


    /**
     * Start of  webview text zoom
     * used to set article webview text zoom level
     */

    private void setTextSizeDefaultZoom() {
        if (-1 == PreferenceManager.getWebViewTextSize(this)) {
            PreferenceManager.storeWebViewTextSize(this,
                    getResources().getInteger(R.integer.web_view_text_size_100));
        }
    }

    /**
     * End of  webview text zoom
     */

    /**
     * Start of Fragment Interaction Listener Methods
     */

    @Override
    public void onArticleSelection(Article article, String title) {
        LogUtils.LOGD(TAG, "selected article id " + article.getId());
        if (NetworkCheckUtility.isNetworkAvailable(this)) {
            if (article.isPhoto()) {
                NavigationUtils.startPhotoGalleryActivity(this, article.getId());
            } else if (article.isQuiz()) {
                if (TextUtils.equals(article.getType(), Constants.POLL)) {
                    if (PollDataHandler.isPollAttempted(article.getId(), this)) {
                        DialogUtils.showSingleButtonAlertDialog(this, "", getString(R.string.poll_attemted), getString(R.string.ok));
                        return;
                    }
                }
                NavigationUtils.startQuizActivity(this, article.getId(), article.getType());
            } else {
                NavigationUtils.startArticleDetailActivity(this, article.getId(), title);
            }
        } else {
            if (OfflineArticleDataHandler.isArticleCached(article.getId(), this)) {
                NavigationUtils.startArticleDetailActivity(this, article.getId(), title);
            } else {
                DialogUtils.showNoNetworkDialog(this);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null == HomeDataProvider.getInstance().getHomeData())
            fetchSections();
        else {
            displayHomeScreenData();
            updateDrawerItems();
        }
    }

    @Override
    public void retry() {
        displayHomeScreenData();
    }

    /**
     * End of Fragment Interaction Listener Methods
     */

    /**
     * Start of Subscribe Notifications API
     */
    private void subscribeForNotifications() {
        if (NetworkCheckUtility.isNetworkAvailable(this) && !PreferenceManager.isSubscribedToNotifications(this)) {
            String deviceId;
            String gcmToken = PreferenceManager.getGCMToken(this);

            TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
            String tmDevice, tmSerial, androidId;
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
            androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

            UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
            deviceId = deviceUuid.toString();

            PreferenceManager.storeGCMDeviceId(this, deviceId);


            if (null != gcmToken && null != deviceId) {

                NotificationSubscriptionModel notificationSubscriptionModel = new NotificationSubscriptionModel();
                notificationSubscriptionModel.setDeviceId(deviceId);
                notificationSubscriptionModel.setDeviceToken(gcmToken);
                notificationSubscriptionModel.setPlatform(Constants.PLATFORM_ANDROID);

                ResponseCallback<GenericResponse<NotificationSubscriptionResponse>>
                        responseCallback = new ResponseCallback<GenericResponse<NotificationSubscriptionResponse>>() {
                    @Override
                    public void success(GenericResponse<NotificationSubscriptionResponse> notificationSubscriptionResponseGenericResponse) {
                        LogUtils.LOGD(Constants.APP_TAG, "GCM registration successful");
                        PreferenceManager.storeIsSubscribedToNotifications(HomeActivity.this, true);
                        PreferenceManager.storeShowNotificationsEnableStatus(HomeActivity.this, true);
                    }

                    @Override
                    public void failure(RestError error) {
                        LogUtils.LOGD(Constants.APP_TAG, "GCM registration failure");
                        PreferenceManager.storeIsSubscribedToNotifications(HomeActivity.this, false);
                    }
                };
                NetworkAdapter.get(this).subscribeForNotifications(notificationSubscriptionModel, responseCallback);

            }

        }
    }

    /**
     * End of Subscribe Notifications API
     */

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (null != HomeDataProvider.getInstance().getHomeData()) {
            outState.putParcelable("home_data", HomeDataProvider.getInstance().getHomeData());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (null == HomeDataProvider.getInstance().getHomeData())
            HomeDataProvider.getInstance().setHomeData((HomeData) savedInstanceState.getParcelable("home_data"));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case READ_PHONE_STATE_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    subscribeForNotifications();
                } else {
                    DialogUtils.cancelProgressDialog();
                }
                break;
        }
    }
}
