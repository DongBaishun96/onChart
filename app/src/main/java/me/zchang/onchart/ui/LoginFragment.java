package me.zchang.onchart.ui;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import me.zchang.onchart.R;

/*
 *    Copyright 2015 Zhehua Chang
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

/**
 * Login Fragment, for test only.
 */
public class LoginFragment extends DialogFragment {

    private LoginListener listener = null;
    private EditText usrNumInput;
    private EditText pswInput;

    public LoginFragment() {

    }

    public void setListener(LoginListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.fragment_login, null);
        usrNumInput = (EditText) rootView.findViewById(R.id.et_num);
        pswInput = (EditText) rootView.findViewById(R.id.et_pwd);


        final AlertDialog dialog =  new AlertDialog.Builder(getActivity())
                .setPositiveButton(getString(R.string.login_submit), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onLoginInputFinish(usrNumInput.getText().toString(), pswInput.getText().toString());
                        }
                    }
                })
                .setView(rootView)
                .create();
        pswInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
                return false;
            }
        });
        return dialog;
    }

    public interface LoginListener {
        void onLoginInputFinish(String usrNum, String psw);
    }
}
