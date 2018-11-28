package com.kaiwei.android.moneymanager;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.kaiwei.android.moneymanager.utils.PhotoUtils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ExpenseFragment extends Fragment {

    private Expense mExpense;
    private Button mDateButton;
    private Button mTimeButton;
    private EditText mAmountField;
    private Spinner mCategorySpinner;
    private ImageView mExpenseImageView;
    private Button mAddPhotoButton;
    private EditText mNoteField;

    private static final String ARG_EXPENSES_ID = "expenses_id";
    private static final String CATEGORY_TYPE = "Expense";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";

    private static final String TAKE_PHOTO = "Take Photo";
    private static final String CHOOSE_CATEGORY = "Choose from image category";
    private static final String CANCEL = "Cancel";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;
    private static final int SELECT_IMAGE_REQUEST = 7;
    private static final int TAKE_PHOTO_REQUEST = 8;

    public static ExpenseFragment newInstance(UUID expensesId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_EXPENSES_ID, expensesId);

        ExpenseFragment expensesFragment = new ExpenseFragment();
        expensesFragment.setArguments(args);
        return expensesFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID expenseId = (UUID) getArguments().getSerializable(ARG_EXPENSES_ID);
        mExpense = ExpenseLab.get(getActivity()).getExpense(expenseId);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPause() {
        super.onPause();

        ExpenseLab.get(getActivity())
                .updateExpense(mExpense);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expenses, container, false);
        getActivity().setTitle(R.string.expense);

        mDateButton = (Button) view.findViewById(R.id.expenses_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(mExpense.getExpensesDate());
                dialog.setTargetFragment(ExpenseFragment.this, REQUEST_DATE);
                dialog.show(fragmentManager, DIALOG_DATE);
            }
        });

        mTimeButton = (Button) view.findViewById(R.id.expenses_time);
        updateTime();
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment
                        .newInstance(mExpense.getExpensesTime());
                dialog.setTargetFragment(ExpenseFragment.this, REQUEST_TIME);
                dialog.show(fragmentManager, DIALOG_TIME);
            }
        });

        mAmountField = (EditText) view.findViewById(R.id.expense_total);
        DecimalFormat precision = new DecimalFormat("0.00");
        mAmountField.setText(precision.format(mExpense.getExpensesTotal()));
        mAmountField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString().trim())) {
                    mAmountField.setText("0");
                } else {
                    mExpense.setExpensesTotal(Double.valueOf(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mCategorySpinner = (Spinner) view.findViewById(R.id.spinner_expenseCategory);
        CategoryLab categoryLab = CategoryLab.get(getActivity());
        final List<String> categoryList = categoryLab.getCategoryForType(CATEGORY_TYPE);
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, categoryList);

        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        String selectedValue = mExpense.getExpensesCategory();
        mCategorySpinner.setAdapter(spinnerArrayAdapter);
        if (selectedValue != null) {
            int spinnerPosition = spinnerArrayAdapter.getPosition(selectedValue);
            mCategorySpinner.setSelection(spinnerPosition);
        }
        mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mExpense.setExpensesCategory(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mExpenseImageView = (ImageView) view.findViewById(R.id.iv_expensePhoto);
        byte[] data = mExpense.getExpensesPhotoFile();
        if(data != null){
            Bitmap bitmap = PhotoUtils.getImage(data);
            mExpenseImageView.setImageBitmap(bitmap);
        }

        mAddPhotoButton = (Button) view.findViewById(R.id.btn_addPhoto);
        mAddPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {TAKE_PHOTO, CHOOSE_CATEGORY, CANCEL};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.dialog_expense_photo);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals(TAKE_PHOTO)) {
                            takePhoto();
                        } else if (items[item].equals(CHOOSE_CATEGORY)) {
                            selectImage();
                        } else if (items[item].equals(CANCEL)) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });

        mNoteField = (EditText) view.findViewById(R.id.expense_note);
        mNoteField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mExpense.setExpensesNote(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return view;
    }

    private void selectImage() {
        try {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, SELECT_IMAGE_REQUEST);
            } else {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"), SELECT_IMAGE_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void takePhoto() {
        try {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, TAKE_PHOTO_REQUEST);
            } else {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(Intent.createChooser(intent, getString(R.string.take_photo_title)), TAKE_PHOTO_REQUEST);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

        private void updateDate () {
            DateFormat df = new SimpleDateFormat("E, MMMM dd, yyyy");
            mDateButton.setText(df.format(mExpense.getExpensesDate()));
        }

        private void updateTime () {
            DateFormat tf = new SimpleDateFormat("hh:mm a");
            mTimeButton.setText(tf.format(mExpense.getExpensesTime()));
        }

        @Override
        public void onActivityResult ( int requestCode, int resultCode, Intent data){
            if (resultCode != Activity.RESULT_OK) {
                return;
            }
            if (requestCode == REQUEST_DATE) {
                Date date = (Date) data
                        .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
                mExpense.setExpensesDate(date);
                updateDate();
            } else if (requestCode == REQUEST_TIME) {
                Date time = (Date) data
                        .getSerializableExtra(TimePickerFragment.EXTRA_TIME);
                mExpense.setExpensesTime(time);
                updateTime();
            } else if(requestCode == TAKE_PHOTO_REQUEST){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                mExpenseImageView.setImageBitmap(bitmap);
                mExpense.setExpensesPhotoFile(PhotoUtils.getBytes(bitmap));
            } else if(requestCode == SELECT_IMAGE_REQUEST){
                Uri selectedImage = data.getData();
                mExpenseImageView.setImageURI(selectedImage);
                Bitmap bitmap = ((BitmapDrawable) mExpenseImageView.getDrawable()).getBitmap();
                mExpense.setExpensesPhotoFile(PhotoUtils.getBytes(bitmap));
            }
        }

        @Override
        public void onCreateOptionsMenu (Menu menu, MenuInflater inflater){
            super.onCreateOptionsMenu(menu, inflater);
            inflater.inflate(R.menu.fragment_delete, menu);
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            switch (item.getItemId()) {
                case R.id.delete_item:
                    ExpenseLab.get(getActivity()).removeExpense(mExpense);
                    getActivity().finish();
                    return true;

                default:
                    return super.onOptionsItemSelected(item);
            }
        }
    }
