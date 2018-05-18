/*
 * Copyright 2015-2018 The twitlatte authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.moko256.twitlatte;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.github.moko256.twitlatte.database.CachedUsersSQLiteOpenHelper;
import com.github.moko256.twitlatte.entity.AccessToken;
import com.github.moko256.twitlatte.model.AccountsModel;
import com.github.moko256.twitlatte.text.TwitterStringUtils;
import com.github.moko256.twitlatte.widget.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 * Created by moko256 on 2015/11/08.
 *
 * @author moko256
 */
public class MainActivity extends AppCompatActivity implements BaseListFragment.GetSnackBar, BaseTweetListFragment.GetRecyclerViewPool, BaseUsersFragment.GetRecyclerViewPool {

    CompositeSubscription subscription;

    Toolbar toolbar;

    @Nullable
    DrawerLayout drawer;
    NavigationView navigationView;

    View headerView;
    TextView userNameText;
    TextView userIdText;
    ImageView userImage;
    ImageView userBackgroundImage;
    ImageView userToggleImage;
    RecyclerView accountListView;

    TabLayout tabLayout;

    boolean isDrawerAccountsSelection = false;

    RecyclerView.RecycledViewPool tweetListViewPool;
    RecyclerView.RecycledViewPool userListViewPool;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        subscription = new CompositeSubscription();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.getChildAt(0).setOnClickListener(v -> {
            Fragment fragment = getMainFragment();
            if (fragment instanceof MovableTopInterface){
                ((MovableTopInterface) fragment).moveToTop();
            }
        });

        drawer = findViewById(R.id.drawer_layout);

        if (drawer != null){
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {}

                @Override
                public void onDrawerOpened(@NonNull View drawerView) {}

                @Override
                public void onDrawerClosed(@NonNull View drawerView) {
                    if (isDrawerAccountsSelection){
                        changeIsDrawerAccountsSelection();
                    }
                }

                @Override
                public void onDrawerStateChanged(int newState) {}
            });
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.color_primary_dark));
        }

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item-> {
            int id = item.getItemId();

            if(!item.isChecked()){
                switch(id){
                    case R.id.nav_timeline:
                        replaceFragment(new HomeTimeLineFragment());
                        break;
                    case R.id.nav_mentions:
                        replaceFragment(new MentionsFragment());
                        break;
                    case R.id.nav_account:
                        startMyUserActivity();
                        break;
                    case R.id.nav_follow_and_follower:
                        replaceFragment(new MyFollowFollowerFragment());
                        break;
                    case R.id.nav_like:
                        replaceFragment(UserLikeFragment.newInstance(GlobalApplication.userId));
                        break;
                    case R.id.nav_settings:
                        startActivity(new Intent(this,SettingsActivity.class));
                        break;
                }
            }

            if (drawer != null) {
                drawer.closeDrawer(GravityCompat.START);
            }
            return (id != R.id.nav_settings)&&(id != R.id.nav_account);

        });

        headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);

        userNameText = headerView.findViewById(R.id.user_name);
        userIdText = headerView.findViewById(R.id.user_id);
        userImage = headerView.findViewById(R.id.user_image);
        userToggleImage = headerView.findViewById(R.id.toggle_account);
        userBackgroundImage = headerView.findViewById(R.id.user_bg_image);
        userBackgroundImage.setOnClickListener(v -> changeIsDrawerAccountsSelection());

        updateDrawerImage();

        accountListView = new RecyclerView(this);
        accountListView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        accountListView.setLayoutManager(new LinearLayoutManager(this));
        accountListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        accountListView.setVisibility(View.GONE);
        navigationView.addHeaderView(accountListView);

        SelectAccountsAdapter adapter = new SelectAccountsAdapter(this);
        adapter.onImageButtonClickListener = accessToken -> {
            if (drawer != null) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                changeIsDrawerAccountsSelection();
            }

            if (accessToken.getUserId() != GlobalApplication.userId) {
                PreferenceManager.getDefaultSharedPreferences(this)
                        .edit()
                        .putString("AccountKey", accessToken.getKeyString())
                        .apply();
                ((GlobalApplication) getApplication()).initTwitter(accessToken);
                updateDrawerImage();
                clearAndPrepareFragment();
            }
        };
        adapter.onAddButtonClickListener = v -> {
            PreferenceManager.getDefaultSharedPreferences(this)
                    .edit()
                    .putString("AccountKey", "-1")
                    .apply();
            GlobalApplication.twitter = null;
            startActivity(new Intent(this, OAuthActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        };
        adapter.onRemoveButtonClickListener = v -> new AlertDialog.Builder(this)
                .setMessage(R.string.confirm_logout)
                .setCancelable(true)
                .setPositiveButton(R.string.do_logout,
                        (dialog, i) -> {
                            SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

                            AccountsModel accountsModel = GlobalApplication.accountsModel;

                            AccessToken token = accountsModel.get(
                                    defaultSharedPreferences.getString("AccountKey", "-1")
                            );
                            accountsModel.delete(token);
                            adapter.removeAccessTokensAndUpdate(token);

                            int point = accountsModel.size() - 1;
                            if (point != -1) {
                                AccessToken accessToken = accountsModel.getAccessTokens().get(point);
                                defaultSharedPreferences
                                        .edit()
                                        .putString("AccountKey", accessToken.getKeyString())
                                        .apply();
                                ((GlobalApplication) getApplication()).initTwitter(accessToken);
                                updateDrawerImage();
                                clearAndPrepareFragment();
                            } else {
                                adapter.onAddButtonClickListener.onClick(v);
                            }
                        }
                )
                .setNegativeButton(android.R.string.cancel, null)
                .show();
        accountListView.setAdapter(adapter);

        subscription.add(
                Single.create(
                        singleSubscriber -> {
                            AccountsModel accountsModel = GlobalApplication.accountsModel;
                            List<AccessToken> accessTokens = accountsModel.getAccessTokens();

                            ArrayList<User> users = new ArrayList<>(accessTokens.size());
                            for (AccessToken accessToken : accessTokens){
                                long id = accessToken.getUserId();
                                CachedUsersSQLiteOpenHelper userHelper = new CachedUsersSQLiteOpenHelper(this, accessToken);
                                User user = userHelper.getCachedUser(id);
                                if (user == null){
                                    try {
                                        user = ((GlobalApplication) getApplication()).createTwitterInstance(accessToken).verifyCredentials();
                                        userHelper.addCachedUser(user);
                                    } catch (TwitterException e) {
                                        e.printStackTrace();
                                    } finally {
                                        userHelper.close();
                                    }
                                }
                                users.add(user);
                            }
                            singleSubscriber.onSuccess(new Pair<>(users, accessTokens));
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                o -> {
                                    Pair<ArrayList<User>, ArrayList<AccessToken>> pairs = (Pair<ArrayList<User>, ArrayList<AccessToken>>) o;
                                    adapter.addAndUpdate(pairs.first, pairs.second);
                                    adapter.notifyDataSetChanged();
                                },
                                Throwable::printStackTrace
                        )
        );

        findViewById(R.id.fab).setOnClickListener(v -> startActivity(new Intent(this, PostActivity.class)));

        tabLayout= findViewById(R.id.toolbar_tab);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {}

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Fragment fragment = ((FragmentPagerAdapter) ((UseTabsInterface) getMainFragment()).getTabsViewPager().getAdapter()).getFragment(tab.getPosition());
                if (fragment instanceof MovableTopInterface){
                    ((MovableTopInterface) fragment).moveToTop();
                }
            }
        });

        tweetListViewPool = new RecyclerView.RecycledViewPool();
        userListViewPool = new RecyclerView.RecycledViewPool();

        getSupportFragmentManager().addOnBackStackChangedListener(() -> attachFragment(getMainFragment()));

        if(savedInstanceState==null){
            prepareFragment();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
        subscription = null;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        attachFragment(getMainFragment());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_toolbar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.action_search){
            startActivity(new Intent(this, TrendsActivity.class));
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer!= null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_N){
            startActivity(new Intent(this, PostActivity.class));
        } else if (event.isShiftPressed() && keyCode == KeyEvent.KEYCODE_SLASH){
            new AlertDialog.Builder(this)
                    .setTitle("KeyBoard Shortcuts")
                    .setMessage("? : This Dialog\nn : New Post\nCtrl + Tab : Open Drawer\nCtrl+Enter : Post")
                    .show();
        } else if (drawer != null && event.isCtrlPressed() && keyCode == KeyEvent.KEYCODE_TAB){
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void changeIsDrawerAccountsSelection() {
        isDrawerAccountsSelection = !isDrawerAccountsSelection;

        accountListView.setVisibility(isDrawerAccountsSelection? View.VISIBLE: View.GONE);

        userToggleImage.setRotation(isDrawerAccountsSelection? 180: 0);

        navigationView.getMenu().setGroupVisible(R.id.drawer_menu_main, !isDrawerAccountsSelection);
        navigationView.getMenu().setGroupVisible(R.id.drawer_menu_settings, !isDrawerAccountsSelection);
    }

    private void startMyUserActivity() {
        ActivityOptionsCompat animation = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                userImage,
                "icon_image"
        );
        startActivity(ShowUserActivity.getIntent(this, GlobalApplication.userId), animation.toBundle());
    }

    private void addFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.mainLayout, fragment)
                .commit();
    }

    private void replaceFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,android.R.anim.fade_in,android.R.anim.fade_out)
                .replace(R.id.mainLayout, fragment)
                .commit();
    }

    private Fragment getMainFragment(){
        return getSupportFragmentManager().findFragmentById(R.id.mainLayout);
    }

    @Override
    public Snackbar getSnackBar(String string) {
        return Snackbar.make(findViewById(R.id.activity_main_coordinator_layout), string, Snackbar.LENGTH_LONG);
    }

    private void attachFragment(Fragment fragment){
        if (fragment != null) {
            if(fragment instanceof ToolbarTitleInterface){
                setTitle(((ToolbarTitleInterface)fragment).getTitleResourceId());
            }

            if(fragment instanceof NavigationPositionInterface){
                navigationView.setCheckedItem(((NavigationPositionInterface)fragment).getNavigationPosition());
            }

            if(fragment instanceof UseTabsInterface) {
                if(tabLayout.getVisibility()!=View.VISIBLE){
                    tabLayout.setVisibility(View.VISIBLE);
                }
                tabLayout.setupWithViewPager(((UseTabsInterface)fragment).getTabsViewPager());
            } else{
                if(tabLayout.getVisibility()!=View.GONE) {
                    tabLayout.setVisibility(View.GONE);
                }
            }
        }
    }

    private void updateDrawerImage(){
        subscription.add(
                Single.create(
                        subscriber->{
                            try {
                                User me = GlobalApplication.userCache.get(GlobalApplication.userId);
                                if (me == null){
                                    me = GlobalApplication.twitter.verifyCredentials();
                                    GlobalApplication.userCache.add(me);
                                }
                                subscriber.onSuccess(me);
                            } catch (TwitterException e) {
                                subscriber.onError(e);
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                result -> {
                                    User user = (User) result;
                                    TwitterStringUtils.plusAndSetMarks(
                                            user.getName(),
                                            userNameText,
                                            user.isProtected(),
                                            user.isVerified()
                                    );
                                    userIdText.setText(TwitterStringUtils.plusAtMark(user.getScreenName()));

                                    userImage.setOnClickListener(v -> startMyUserActivity());

                                    GlideRequests requests=GlideApp.with(this);

                                    requests.load(user.get400x400ProfileImageURLHttps())
                                            .circleCrop()
                                            .transition(DrawableTransitionOptions.withCrossFade())
                                            .into(userImage);
                                    requests.load(user.getProfileBannerRetinaURL())
                                            .centerCrop()
                                            .transition(DrawableTransitionOptions.withCrossFade())
                                            .into(userBackgroundImage);
                                },
                                Throwable::printStackTrace
                        )
        );

    }

    private void prepareFragment(){
        Fragment top=new HomeTimeLineFragment();
        addFragment(top);
        attachFragment(top);
    }

    private void clearAndPrepareFragment(){
        Fragment top=new HomeTimeLineFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager
                .beginTransaction()
                .replace(R.id.mainLayout, top)
                .commit();

        attachFragment(top);
    }

    @Override
    public RecyclerView.RecycledViewPool getTweetListViewPool() {
        return tweetListViewPool;
    }

    @Override
    public RecyclerView.RecycledViewPool getUserListViewPool() {
        return userListViewPool;
    }
}