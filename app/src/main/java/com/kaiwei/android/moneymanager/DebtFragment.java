package com.kaiwei.android.moneymanager;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.kaiwei.android.moneymanager.utils.PhotoUtils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class DebtFragment extends Fragment {

    private Debt mDebt;
    private EditText mTitle;
    private Button mDateButton;
    private EditText mDescription;
    private EditText mAmount;
    private CheckBox mReturned;
    private ImageView mDebtPhoto;
    private ImageButton mAddPhotoButton;
    private Button mDebtorButton;
    private Button mSendMessageButton;
    private Button mCallDebtorButton;

    private static final String ARG_DEBT_ID = "debt_id";
    private static final String DIALOG_DATE = "DialogDate";

    private static final String TAKE_PHOTO = "Take Photo";
    private static final String CHOOSE_CATEGORY = "Choose from image category";
    private static final String CANCEL = "Cancel";

    private static final int REQUEST_DATE = 0;
    private static final int SELECT_IMAGE_REQUEST = 7;
    private static final int TAKE_PHOTO_REQUEST = 8;

    private static final int REQUEST_CONTACT = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 3;

    public static DebtFragment newInstance(UUID debtId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DEBT_ID, debtId);

        DebtFragment debtFragment = new DebtFragment();
        debtFragment.setArguments(args);
        return debtFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.debt_detail);
        UUID debtId = (UUID) getArguments().getSerializable(ARG_DEBT_ID);
        mDebt = DebtLab.get(getActivity()).getDebt(debtId);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        DebtLab.get(getActivity())
                .updateDebt(mDebt);
    }

    private void askForContactPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_CONTACTS},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_debt_management, container, false);
        getActivity().setTitle(R.string.debt_detail);

        mTitle = view.findViewById(R.id.et_debtTitle);
        mTitle.setText(mDebt.getDebtTitle());
        mTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mDebt.setDebtTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDateButton = (Button) view.findViewById(R.id.btn_debtDate);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(mDebt.getDebtDate());
                dialog.setTargetFragment(DebtFragment.this, REQUEST_DATE);
                dialog.show(fragmentManager, DIALOG_DATE);
            }
        });

        mDescription = (EditText) view.findViewById(R.id.et_debtDescription);
        mDescription.setText(mDebt.getDebtDescription());
        mDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mDebt.setDebtDescription(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mAmount = (EditText) view.findViewById(R.id.et_debtAmount);
        DecimalFormat precision = new DecimalFormat("0.00");
        mAmount.setText(precision.format(mDebt.getDebtAmount()));
        mAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString().trim())) {
                    mAmount.setText("0");
                } else {
                    mDebt.setDebtAmount(Double.valueOf(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mReturned = (CheckBox) view.findViewById(R.id.chckBob_returnMoney);
        mReturned.setChecked(mDebt.isReturned());
        mReturned.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mDebt.setReturned(isChecked);
            }
        });

        mDebtPhoto = (ImageView) view.findViewById(R.id.iv_debt);
        byte[] data = mDebt.getDebtPhotoFile();
        if (data != null) {
            Bitmap bitmap = PhotoUtils.getImage(data);
            mDebtPhoto.setImageBitmap(bitmap);
        }

        mAddPhotoButton = (ImageButton) view.findViewById(R.id.btn_debtPhoto);
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

        final Intent pickContact = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        mDebtorButton = (Button) view.findViewById(R.id.btn_chooseDebtor);
        mDebtorButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });
        if (mDebt.getDebtor() != null) {
            mDebtorButton.setText(mDebt.getDebtor());
        }

        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContact,
                PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mDebtorButton.setEnabled(false);
        }

        mSendMessageButton = view.findViewById(R.id.btn_sendMessage);
        mSendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getDebtReport());
                i.putExtra(Intent.EXTRA_SUBJECT,
                        getString(R.string.crime_report_subject));
                i = Intent.createChooser(i, getString(R.string.send_report));
                startActivity(i);
            }
        });

        askForContactPermission();

        mCallDebtorButton = (Button) view.findViewById(R.id.btn_call);
        mCallDebtorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri number = Uri.parse("tel:" + mDebt.getContact());
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                startActivity(callIntent);
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
                startActivityForResult(Intent.createChooser(intent, "Select Debt Picture"), SELECT_IMAGE_REQUEST);
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

    private void updateDate() {
        DateFormat df = new SimpleDateFormat("E, MMMM dd, yyyy");
        mDateButton.setText(df.format(mDebt.getDebtDate()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mDebt.setDebtDate(date);
            updateDate();
        } else if (requestCode == TAKE_PHOTO_REQUEST) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            mDebtPhoto.setImageBitmap(bitmap);
            mDebt.setDebtPhotoFile(PhotoUtils.getBytes(bitmap));
        } else if (requestCode == SELECT_IMAGE_REQUEST) {
            Uri selectedImage = data.getData();
            mDebtPhoto.setImageURI(selectedImage);
            Bitmap bitmap = ((BitmapDrawable) mDebtPhoto.getDrawable()).getBitmap();
            mDebt.setDebtPhotoFile(PhotoUtils.getBytes(bitmap));
        } else if (requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();

            String[] queryFields = new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME
            };

            Cursor c = getActivity().getContentResolver()
                    .query(contactUri, queryFields, null, null, null);
            try {

                if (c.getCount() == 0) {
                    return;
                }

                c.moveToFirst();
                String debtor = c.getString(0);
                mDebt.setDebtor(debtor);
                mDebtorButton.setText(debtor);
            } finally {
                c.close();
            }
        }
    }

    private String getDebtReport() {
        String clearedString = null;
        if (mDebt.isReturned()) {
            clearedString = getString(R.string.debt_report_returned);
        } else {
            clearedString = getString(R.string.debt_report_not_returned);
        }
        String dateFormat = "EEE, MMM dd";
        String dateString = android.text.format.DateFormat.format(dateFormat,mDebt.getDebtDate()).toString();
        String debtor = mDebt.getDebtor();
        if (debtor == null) {
            debtor = getString(R.string.debt_report_no_debtor);
        } else {
            debtor = getString(R.string.debt_report_debtor, debtor);
        }
        String report = getString(R.string.debt_report,
                mDebt.getDebtTitle(), dateString, clearedString, debtor);
        return report;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_delete, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_item:
                DebtLab.get(getActivity()).removeDebt(mDebt);
                getActivity().finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
