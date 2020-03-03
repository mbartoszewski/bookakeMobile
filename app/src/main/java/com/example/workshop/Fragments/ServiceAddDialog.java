package com.example.workshop.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.example.workshop.Models.ResponseMessage;
import com.example.workshop.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.workshop.MainActivity.basicT;
import static com.example.workshop.MainActivity.retrofitService;
import static com.example.workshop.MainActivity.role;

public class ServiceAddDialog extends DialogFragment implements View.OnClickListener
{
    public ServiceAddDialog()
    {

    }

    EditText name, email, mobile, street, city, zip, state, aboutService;
    Button okButton, cancelButton;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.add_service_dialog, container);
        name = rootView.findViewById(R.id.service_name_edit);
        mobile = rootView.findViewById(R.id.service_mobile_edit);
        email = rootView.findViewById(R.id.service_email_edit);
        street = rootView.findViewById(R.id.service_street_edit);
        city = rootView.findViewById(R.id.service_city_edit);
        zip = rootView.findViewById(R.id.service_zip_edit);
        state = rootView.findViewById(R.id.service_state_edit);
        okButton = rootView.findViewById(R.id.ok_button);
        cancelButton = rootView.findViewById(R.id.cancel_button);
        aboutService = rootView.findViewById(R.id.service_description_edit);

        okButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onPause()
    {
        dismiss();
        super.onPause();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.ok_button:
                    //dodanie usługi
               addService(
                    basicT,
                    name.getText().toString(),
                    mobile.getText().toString(),
                    email.getText().toString(),
                    street.getText().toString(),
                    city.getText().toString(),
                    zip.getText().toString(),
                    state.getText().toString(),
                    aboutService.getText().toString(),
                       this
            );
               /*
                addService(
                        basicT,
                        "name",
                        "mobile",
                        "email",
                        "street 12",
                        "kleszczow",
                        "97-411",
                        "lodzkie"
                );

                */
                break;
            case R.id.cancel_button:
                this.dismiss();
                break;
        }
    }

    private void addService(String basicT, String name, String mobile, String email, String street, String city, String zip, String state, String aboutService, DialogFragment dialogFragment)
    {
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("mobile", mobile);
        map.put("email", email);
        map.put("street", street);
        map.put("city", city);
        map.put("zip", zip);
        map.put("addressState", state);
        map.put("description", aboutService);

       retrofitService.addService(basicT,1L, map)
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(new Observer<ResponseMessage>()
               {
                   @Override
                   public void onSubscribe(Disposable d)
                   {

                   }

                   @Override
                   public void onNext(ResponseMessage responseMessage)
                   {
                   }

                   @Override
                   public void onError(Throwable e)
                   {
                       Log.e("SERVICEADD", " nie udalo sie " + e.getMessage());
                   }

                   @Override
                   public void onComplete()
                   {
                       role = "[ROLE_PROVIDER]";
                       Snackbar.make(getActivity().findViewById(R.id.fragment_container),R.string.service_added, Snackbar.LENGTH_SHORT).show();
                       dialogFragment.dismiss();
                   }
               });
    }
}
