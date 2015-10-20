package com.atdroid.atyurin.futuremoney.fragments;

import android.app.ListFragment;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.atdroid.atyurin.futuremoney.R;
import com.atdroid.atyurin.futuremoney.dao.AccountsDAO;
import com.atdroid.atyurin.futuremoney.serialization.Account;

import java.util.ArrayList;

/**
 * Created by atdroid on 14.09.2015.
 */

public class AccountItemFragment extends ListFragment {
    private AccountItemListAdapter mAdapter;
    Account account;
    boolean isNewItem = true;
    final static String LOG_TAG = "AccountItemFragment";

    public static AccountItemFragment newInstance() {
        AccountItemFragment accountItemFragment = new AccountItemFragment();
        accountItemFragment.account = new Account();
        return accountItemFragment;
    }

    public static AccountItemFragment newInstance(Account account) {
        AccountItemFragment accountItemFragment = new AccountItemFragment();
        accountItemFragment.account = account;
        accountItemFragment.isNewItem = false;
        return accountItemFragment;
    }

    public AccountItemFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);//switch off menu for fragment

        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        mAdapter = new AccountItemListAdapter();
        mAdapter.addItem(AccountItemListAdapter.ITEM_NAME);
        mAdapter.addItem(AccountItemListAdapter.ITEM_AMOUNT);

        setListAdapter(mAdapter);

        return rootView;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_budget_item_new_item, menu);
    }

    public void onPrepareOptionsMenu(Menu menu) {
//        Log.d("IncomeItemFragment", "onPrepareOptionsMenu");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(LOG_TAG, "onOptionsItemSelected");
        if (item.getItemId() == R.id.action_btn_add_budget_item_confirm) {
            if (account.getName().length() < 1){
                Toast.makeText(getActivity().getBaseContext(), R.string.msg_budget_item_empty_name, Toast.LENGTH_LONG).show();
                return false;
            }
            if (account.getValue() < 1){
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

    public class AccountItemListAdapter extends BaseAdapter {
        /**
         * position description
         * 0 - name (EditText)
         * 1 - amount (EditText)
         * */

        private static final int ITEM_NAME = 0;
        private static final int ITEM_AMOUNT = 1;
        private static final int ITEM_MAX_COUNT = ITEM_AMOUNT + 1;
        Context context = getActivity().getBaseContext();

        private ArrayList<Integer> itemsTypes = new ArrayList<Integer>();
        private LayoutInflater mInflater;

        public AccountItemListAdapter() {
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void addItem(final int itemType) {
            itemsTypes.add(itemType);
            notifyDataSetChanged();
        }

        public void addUniqItem(final int itemType) {
            boolean isExist = false;
            for (int index = 0; index <  itemsTypes.size(); index++){
                if (itemsTypes.get(index) == itemType){
                    isExist = true;
                }
            }
            if (!isExist) itemsTypes.add(itemType);
            notifyDataSetChanged();
        }

        public void deleteItem(final int itemType){
            for (int index = 0; index <  itemsTypes.size(); index++){
                if (itemsTypes.get(index) == itemType){
                    itemsTypes.remove(index);
                }
            }
        }

        @Override
        public int getItemViewType(int position) {
            return itemsTypes.get(position);
        }

        @Override
        public int getViewTypeCount() {
            return ITEM_MAX_COUNT;
        }

        @Override
        public int getCount() {
            return itemsTypes.size();
        }

        @Override
        public Integer getItem(int position) {
            return itemsTypes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            int type = getItemViewType(position);
            System.out.println("getView " + position + " " + convertView + " type = " + type);
            if (convertView == null) {
                holder = new ViewHolder();
                switch (type) {
                    case ITEM_NAME:
                        convertView = getNameView();
                        holder.editText = (EditText) convertView;
                        break;
                    case ITEM_AMOUNT:
                        convertView = getAmountView();
                        holder.editText = (EditText) convertView;
                        break;

                }
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            return convertView;
        }

        private View getNameView (){
            EditText editText = (EditText) mInflater.inflate(R.layout.list_item_edit_text, null);
            editText.setHint(R.string.budget_item_name);
            if (account.getName().length() > 0){
                editText.setText(account.getName());
            }
            editText.addTextChangedListener(nameTextWatcher);

            return editText;
        }

        private View getAmountView (){
            EditText editText = (EditText) mInflater.inflate(R.layout.list_item_edit_text, null);
            editText.setRawInputType(Configuration.KEYBOARD_QWERTY);
            editText.setHint(R.string.budget_item_amount);
            if (account.getValue() > 0){
                editText.setText(Double.toString(account.getValue()));
            }
            editText.addTextChangedListener(amountTextWatcher);

            return editText;
        }



        private TextWatcher nameTextWatcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                account.setName(charSequence.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {}
        };

        private TextWatcher amountTextWatcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0){
                    account.setValue(Double.valueOf(charSequence.toString()));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {}
        };
    }

    public static class ViewHolder {
        EditText editText;
    }
}
