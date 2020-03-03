package com.example.workshop.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.workshop.Models.ResponseMessage;
import com.example.workshop.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Credentials;

import static com.example.workshop.MainActivity.basicT;
import static com.example.workshop.MainActivity.hideSoftKeyboard;
import static com.example.workshop.MainActivity.retrofitService;
import static com.example.workshop.MainActivity.isConnected;
import static com.example.workshop.MainActivity.role;
import static com.example.workshop.MainActivity.serviceId;

public class LoginFragment extends Fragment implements View.OnClickListener
{
    private TextInputEditText name;
    private TextInputEditText password;
    private Button loginButton, registerButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView =inflater.inflate(R.layout.login_fragment, container, false);
        name = rootView.findViewById(R.id.name_editText);
        password = rootView.findViewById(R.id.password_editText);
        registerButton = rootView.findViewById(R.id.register_button);
        loginButton = rootView.findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);

        return rootView;
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
                    public void onNext(ResponseMessage s)
                    {
                        isConnected = true;
                        role = s.getMessage().substring(0,s.getMessage().lastIndexOf("]")+1);
                        basicT = authToken;
                        serviceId = s.getMessage().contains("service:")?Long.valueOf(s.getMessage().substring(s.getMessage().lastIndexOf(":")+1)):0L;
                        if (getFragmentManager().getBackStackEntryCount() > 0)
                        {
                            getFragmentManager().popBackStack(null,
                                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        }
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        if (getActivity() !=null)
                        {
                        Snackbar.make(getActivity().findViewById(R.id.fragment_container), e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onComplete()
                    {
                        if (getActivity() !=null)
                        {
                        Snackbar.make(getActivity().findViewById(R.id.fragment_container), "Yupi jajaja Yupi jajaja", Snackbar.LENGTH_SHORT).show();
                        }
                        name.setText("");
                        password.setText("");
                    }
                });
    }
    private void registerUser(String name, String password)
    {

        retrofitService.registerUser(name, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseMessage>() {
                    @Override
                    public void onSubscribe(Disposable d)
                    {

                    }

                    @Override
                    public void onNext(ResponseMessage s) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Snackbar.make(getActivity().findViewById(R.id.fragment_container), e.getMessage(), Snackbar.LENGTH_SHORT).show(); //snackbar nie działa do poprawy
                    }

                    @Override
                    public void onComplete()
                    {
                        isConnected = true;
                        role = "[ROLE_USER]";
                        basicT = Credentials.basic(name, password);
                        if (getFragmentManager().getBackStackEntryCount() > 0)
                        {
                            getFragmentManager().popBackStack(null,
                                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        }
                        Snackbar.make(getActivity().findViewById(R.id.fragment_container), "Success", Snackbar.LENGTH_SHORT).show(); //snackbar nie działa do poprawy
                    }
                });
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.login_button:
                if (!TextUtils.isEmpty(name.getText()) && !TextUtils.isEmpty(password.getText()))
                {
                    loginUser(Credentials.basic(String.valueOf(name.getText()), String.valueOf(password.getText())));
                    hideSoftKeyboard(getActivity());
                }
                if(TextUtils.isEmpty(name.getText()))
                {
                    name.setError(getString(R.string.login_empty));
                }
                if(TextUtils.isEmpty(password.getText()))
                {
                    password.setError(getString(R.string.login_empty));
                }
                break;
            case R.id.register_button:
                if (!TextUtils.isEmpty(name.getText()) && !TextUtils.isEmpty(password.getText()))
                {
                    registerUser(String.valueOf(name.getText()), String.valueOf(password.getText()));
                    hideSoftKeyboard(getActivity());
                }
                break;
        }

    }
}
