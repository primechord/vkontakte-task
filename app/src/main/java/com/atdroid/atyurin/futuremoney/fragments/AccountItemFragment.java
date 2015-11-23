package com.atdroid.atyurin.futuremoney.fragments;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.atdroid.atyurin.futuremoney.R;
import com.atdroid.atyurin.futuremoney.dao.AccountsDAO;
import com.atdroid.atyurin.futuremoney.serialization.Account;
import com.atdroid.atyurin.futuremoney.utils.FragmentContainer;
import com.atdroid.atyurin.futuremoney.utils.NumberTextWatcher;

/**
 * Created by atdroid on 14.09.2015.
 *
 * elements description
 * 0 - name (EditText)
 * 1 - amount (EditText)
 * 2 - type (LinearLayout{Text, Spinner})
 * 3 - single date (Date Picker)
 * 4 - begin date (Date Picker)
 * 5 - end date (Date Picker)
 * 6 - period (LinearLayout{Text, Spinner, EditText(optional - depende on spinner)})
 *
 */


public class AccountItemFragment extends Fragment {
    final static String KEY_ACCOUNT = "key_account";
    final static String KEY_NEW_ITEM = "key_new_item";
    final static String LOG_TAG = "AccountItemFragment";
    Account account;
    boolean isNewItem = true;
    EditText etName, etAmount;
    TextView tvNameTitle, tvAmountTitle;
    NumberTextWatcher ntw;
    public static AccountItemFragment newInstance() {
        Log.d("Accountr fragment", "newInstance");

        AccountItemFragment accountItemFragment = new AccountItemFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_ACCOUNT, new Account());
        args.putBoolean(KEY_NEW_ITEM, true);
        accountItemFragment.setArguments(args);
        return accountItemFragment;
    }

    public static AccountItemFragment newInstance(Account account) {
        Log.d("Accountr fragment", "newInstance");
        AccountItemFragment accountItemFragment = new AccountItemFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_ACCOUNT, account);
        args.putBoolean(KEY_NEW_ITEM, false);
        accountItemFragment.setArguments(args);
        return accountItemFragment;
    }

    public AccountItemFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);//switch off menu for fragment
        this.account = (Account) this.getArguments().getSerializable(KEY_ACCOUNT);
        this.isNewItem = this.getArguments().getBoolean(KEY_NEW_ITEM);
        FragmentContainer.setCurentFragment(this.getClass().toString());
        View rootView =  inflater.inflate(R.layout.fragment_account_item, container, false);
        //name
        tvNameTitle = (TextView) rootView.findViewById(R.id.tv_account_name_title);
        etName = (EditText) rootView.findViewById(R.id.et_account_name_value);
        if (account.getName().length() > 0){
            etName.setText(account.getName());
            tvNameTitle.setVisibility(View.VISIBLE);
        }
        etName.addTextChangedListener(nameTextWatcher);
        //amount
        tvAmountTitle = (TextView) rootView.findViewById(R.id.tv_account_amount_title);
        etAmount = (EditText) rootView.findViewById(R.id.et_account_amount_value);
        ntw = new NumberTextWatcher(etAmount, tvAmountTitle);
        etAmount.setRawInputType(Configuration.KEYBOARD_QWERTY);
        if (account.getValue() > 0){
            ntw.setValue(account.getValue());
            ntw.formatText();
            tvAmountTitle.setVisibility(View.VISIBLE);
        }
        return rootView;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
//        Log.d("AccountItemFragment", "onCreateOptionsMenu");
        inflater.inflate(R.menu.menu_budget_item_new_item, menu);
    }

    public void onPrepareOptionsMenu(Menu menu) {
//        Log.d("AccountItemFragment", "onPrepareOptionsMenu");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("AccountItemFragment", "onOptionsItemSelected");
        if (item.getItemId() == R.id.action_btn_add_budget_item_confirm) {
            if (account.getName().length() < 1){
                Toast.makeText(getActivity().getBaseContext(), R.string.msg_budget_item_empty_name, Toast.LENGTH_LONG).show();
                return false;
            }
            account.setValue(ntw.getValue());
            if (account.getValue() <= 0){
                Toast.makeText(getActivity().getBaseContext(), R.string.msg_budget_item_empty_amount, Toast.LENGTH_LONG).show();
                return false;
            }
            AccountsDAO dao = new AccountsDAO(getActivity().getBaseContext());
            dao.openWritable();
            if (isNewItem){
                if (dao.addAccount(account)){
                    Toast.makeText(getActivity().getBaseContext(), getResources().getString(R.string.msg_budget_item_success), Toast.LENGTH_LONG).show();
                };
            }else{
                if (dao.updateAccount(account)){
                    Toast.makeText(getActivity().getBaseContext(), getResources().getString(R.string.msg_budget_item_success), Toast.LENGTH_LONG).show();
                }
            }
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, AccountsFragment.newInstance(getActivity(), getFragmentManager()))
                    .commit();
            dao.close();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private TextWatcher nameTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            account.setName(charSequence.toString());
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() > 0){
                tvNameTitle.setVisibility(View.VISIBLE);
            }else{
                tvNameTitle.setVisibility(View.GONE);
            }
        }
    };

    private TextWatcher amountTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable editable) {
            Log.d(LOG_TAG, editable.toString());
            if (editable.length() > 0){
                account.setValue(Double.valueOf(editable.toString()));
            }
            if (editable.length() > 0){
                tvAmountTitle.setVisibility(View.VISIBLE);
            }else{
                tvAmountTitle.setVisibility(View.GONE);
            }
        }
    };


}