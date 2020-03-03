package com.example.workshop;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.workshop.ApiService.RetrofitService;

import androidx.preference.PreferenceManager;

import android.util.Log;
import android.view.Menu;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.example.workshop.Fragments.HomePageFragment;
import com.example.workshop.Fragments.LoginFragment;
import com.example.workshop.Fragments.PendingFragment;
import com.example.workshop.Fragments.ServiceAddDialog;
import com.example.workshop.Fragments.ServiceDetailFragment;
import com.example.workshop.Models.ResponseMessage;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{

    DrawerLayout drawer;
    ActionBarDrawerToggle navDrawerToggle;
    FragmentTransaction transaction;
    FragmentManager fragmentManager;
    Fragment homePageFragment, loginFragment, serviceDetailFragment, servicePendingFragment;
    MenuItem menuLogout, menuLogin, menuServicePending, menuProvider, menuAddService;
    NavigationView navigationView;
    public static String basicT = "";
    public static String role = "";
    public static Long serviceId =0L;
    public static boolean isConnected = false;
    public static RetrofitService retrofitService;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        transaction = getSupportFragmentManager().beginTransaction();
        fragmentManager = getSupportFragmentManager();
        if (homePageFragment == null)
        {
            homePageFragment = new HomePageFragment();
        }
        loginFragment = new LoginFragment();
        serviceDetailFragment = new ServiceDetailFragment();
        servicePendingFragment = new PendingFragment();
        transaction.replace(R.id.fragment_container, homePageFragment, "HomePageFragment").commit();
        new getSharedPreferences().execute();

        navDrawerToggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset)
            {
                if (slideOffset > 0.33)
                {
                    showHideMenuItems(isConnected, navigationView.getMenu());
                }
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };
        drawer.addDrawerListener(navDrawerToggle);
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        navDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

       retrofitService = RetrofitService.retrofit.create(RetrofitService.class);
    }

    public void showHideMenuItems(boolean isConnected, Menu menu)
    {

        menuLogout = menu.findItem(R.id.nav_logout);
        menuLogin = menu.findItem(R.id.nav_login);
        menuProvider = menu.findItem(R.id.nav_provider);
        menuServicePending = menu.findItem(R.id.nav_service_pending);
        menuAddService = menu.findItem(R.id.nav_service_add);

        if (isConnected)
        {
            menuLogin.setVisible(false);
            menuLogout.setVisible(true);
            menuServicePending.setVisible(true);
            if (role.equals("[ROLE_PROVIDER]") || role.equals("[ROLE_ADMIN]"))
            {
                menuProvider.setVisible(true);
                menuAddService.setVisible(false);
            }
            else
            {
                menuAddService.setVisible(true);
            }
        }

        else if (!isConnected)
        {
            menuLogin.setVisible(true);
            menuLogout.setVisible(false);
            menuProvider.setVisible(false);
            menuServicePending.setVisible(false);
        }
    }
    @Override
    public void onBackPressed()
    {
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0)
        {
            getSupportFragmentManager().popBackStack(null,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //noinspection SimplifiableIfStatement
        switch (item.getItemId())
        {
            case android.R.id.home:
                if (drawer.isDrawerOpen(GravityCompat.START))
                {
                    drawer.closeDrawer(GravityCompat.START);
                } else if (!drawer.isDrawerOpen(GravityCompat.START))
                {
                    drawer.openDrawer(GravityCompat.START);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.

        switch (item.getItemId())
        {
            case R.id.nav_provider_pending:
            case R.id.nav_service_pending:
                if (!servicePendingFragment.isAdded())
                {
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, servicePendingFragment, "ServicePendingFragment");
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                break;
            case R.id.nav_service_add:
                ServiceAddDialog serviceAddDialog = new ServiceAddDialog();
                transaction = getSupportFragmentManager().beginTransaction();
                serviceAddDialog.show(transaction, "serviceAddDialog");
                //transaction.addToBackStack(null);
                //transaction.commit();
                break;
            case R.id.nav_service_search:
                if (!homePageFragment.isAdded())
                {
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, homePageFragment, "HomePageFragment");
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                break;
            case R.id.nav_login:
                if (!loginFragment.isAdded())
                {
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, loginFragment, "LoginFragment");
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                break;
            case R.id.nav_logout:
                retrofitService.logoutUser().subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<ResponseMessage>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(ResponseMessage s) {

                            }

                            @Override
                            public void onError(Throwable e)
                            {
                                Log.e("Logout ERROR", " "  + e);
                                deleteToken();
                                isConnected = false;
                                role = "";
                                basicT = "";
                                serviceId = 0L;
                                if (getFragmentManager().getBackStackEntryCount() > 0)
                                {
                                    getFragmentManager().popBackStack(null,
                                            FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                }
                            }

                            @Override
                            public void onComplete()
                            {

                            }
                        });
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loginUser(String authToken)
    {
        retrofitService.loginUser(authToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseMessage>() {
                    @Override
                    public void onSubscribe(Disposable d)
                    {

                    }
                    @Override
                    public void onNext(ResponseMessage s) {
                        isConnected = true;
                        role = s.getMessage().substring(0,s.getMessage().lastIndexOf("]")+1);
                        basicT = authToken;
                        serviceId = s.getMessage().contains("service:")?Long.valueOf(s.getMessage().substring(s.getMessage().lastIndexOf(":")+1)):0L;
                    }

                    @Override
                    public void onError(Throwable e) {
                        Snackbar.make(findViewById(R.id.fragment_container), e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        Snackbar.make(findViewById(R.id.fragment_container), "Yupi jajaja Yupi jajaja", Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    public void deleteToken()
    {
        ((Runnable) () ->
        {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            SharedPreferences.Editor preferencesEditor = pref.edit();
            if (pref.contains("basicT"))
            {
                preferencesEditor.remove("basicT");
                preferencesEditor.apply();
            }

        }).run();
    }

    public class getSharedPreferences extends AsyncTask<Context, Void, String>
    {

        @Override
        protected String doInBackground(Context... contexts)
        {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            if (pref.contains("basicT"))
            {
                return pref.getString("basicT", "unauthorized");
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            if (s!= null)
            {
                loginUser(s);
            }
        }
    }

    public static void hideSoftKeyboard(Activity activity)
    {

            try
            {
                InputMethodManager inputMethodManager =
                        (InputMethodManager) activity.getSystemService(
                                Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(
                        activity.getCurrentFocus().getWindowToken(), 0);

            } catch(NullPointerException n)
                {

                }
    }
    private void saveToken(String token)
    {
        ((Runnable) () ->
        {
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor preferencesEditor = pref.edit();
                preferencesEditor.putString("basicT", token);
                preferencesEditor.apply();
        }).run();
    }
    @Override
    public void onPause()
    {
        if (!basicT.equals(""))
        {
            saveToken(basicT);
        }
        super.onPause();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }
}
