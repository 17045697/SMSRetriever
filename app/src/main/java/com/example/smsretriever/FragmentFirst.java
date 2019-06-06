package com.example.smsretriever;


import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.PermissionChecker;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentFirst extends Fragment {

    Button btnSearchText, btnEmail;
    TextView tvFrag2;
    EditText etNumber;


    public FragmentFirst() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first, container,false);
        tvFrag2 = view.findViewById(R.id.tvFrag2);
        btnSearchText = view.findViewById(R.id.btnRetrieveText);
        etNumber = view.findViewById(R.id.etNumber);
        btnEmail = view.findViewById(R.id.btnEmail);

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL  , new String[] { "someemail@gmail.com" });
                intent.putExtra(Intent.EXTRA_SUBJECT, "SMS content");
                String text1 = tvFrag2.getText().toString();
                intent.putExtra(Intent.EXTRA_TEXT, text1 );

                startActivity(Intent.createChooser(intent, "Email via..."));

            }
        });
        btnSearchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("content://sms");
                String[] reqCols = new String[]{"date", "address", "body", "type"};
                ContentResolver cr = getActivity().getContentResolver();
                String filter = "address LIKE ?";
                int filterNumber = Integer.parseInt(etNumber.getText().toString());
                String[] filterArgs = {"%" + filterNumber + "%"};
                Cursor cursor = cr.query(uri, reqCols, filter, filterArgs, null);
                String smsBody = "";
                if (cursor.moveToFirst()) {
                    do {
                        long dateInMillis = cursor.getLong(0);
                        String date = (String) DateFormat.format("dd MMM yyyy h:mm:ss aa",dateInMillis);
                        String address = cursor.getString(1);
                        String body = cursor.getString(2);
                        String type = cursor.getString(3);
                        if (type.equalsIgnoreCase("1")) {
                            type = "Inbox";
                        } else {
                            type = "Sent";
                        }

                        smsBody += type + " " + address + "\n at " + date + "\n\"" + body + "\"\n\n";
                    }
                    while (cursor.moveToNext());
                }
                tvFrag2.setText(smsBody);
            }
        });
        return view;
    }

}
