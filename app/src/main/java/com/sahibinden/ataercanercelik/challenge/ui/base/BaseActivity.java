package com.sahibinden.ataercanercelik.challenge.ui.base;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.sahibinden.ataercanercelik.challenge.TwitterSearchApplication;
import com.sahibinden.ataercanercelik.challenge.di.ApplicationComponent;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity
public abstract class BaseActivity extends AppCompatActivity{
    @AfterInject
    protected void init() {
        if (getApplicationComponent() != null)
            getApplicationComponent().inject(this);

    }

    @AfterViews
    protected void initViews() {

    }


    public ApplicationComponent getApplicationComponent() {
        return ((TwitterSearchApplication) getApplication()).getApplicationComponent();
    }

    public void showToast(String message, Context context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
