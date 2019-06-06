package com.example.smsretriever;


import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
public class FragmentSecond extends Fragment {

    Button btnSearchText;
    TextView tvFrag2;
    EditText etNumber;


    public FragmentSecond() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_second, container,false);
        tvFrag2 = view.findViewById(R.id.tvFrag2);
        btnSearchText = view.findViewById(R.id.btnRetrieve);
        etNumber = view.findViewById(R.id.etWord);
        btnSearchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("content://sms");
                String[] reqCols = new String[]{"date", "address", "body", "type"};
                ContentResolver cr = getActivity().getContentResolver();
                String filter = "body LIKE ?";
                //String filterNumber = etNumber.getText().toString();
                //String[] filterArgs = {"%" + filterNumber + "%"};

                String word = etNumber.getText().toString();
                String[] arr = word.split(" ");
                String[] filterargs = new String[arr.length];

                for (int i = 0; i < arr.length ;i++ ){
                    filterargs[i] = "%" + arr[i] + "%";

                    filter+= "OR body LIKE ?";
                }
                Cursor cursor = cr.query(uri, reqCols, filter, filterargs, null);
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
