package bdnath.lictproject.info.ghur.Events;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import bdnath.lictproject.info.ghur.FireBasePojoClass.EventHandler;
import bdnath.lictproject.info.ghur.R;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddEventFragment extends Fragment {
    private EditText eventTitle;
    private EditText eventPlace;
    private EditText eventCost;
    private EditText eventDetail;
    private EditText eventStartDate;
    private EditText eventEndDate;

    private Button doneBTN;
    private Button cancleBTN;
    private int year, month, day, hour, minute;
    private Calendar calendar;

    private MainViewListener listener;
    private EventHandler eventHandler;

    private DatabaseReference roofRef;
    private DatabaseReference eventRef;
    private FirebaseUser user;
    private FirebaseAuth auth;
/*    private StorageReference storageReference;
    private StorageReference userStorageReference;*/

    /*private Uri proImagePath=null;
    private Bitmap proBitmap;
    private String profileImagePath;*/

    private String title;
    private String place;
    private float cost;
    private String startDate;
    private String endDate;
    private String detail;
    private String id;


    public AddEventFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_add_event, container, false);

        eventTitle=view.findViewById(R.id.eventTitle);
        eventPlace=view.findViewById(R.id.eventPlace);
        eventCost=view.findViewById(R.id.eventCost);
        eventStartDate=view.findViewById(R.id.startDateInputED);
        eventEndDate=view.findViewById(R.id.endDateInputED);
        eventDetail=view.findViewById(R.id.eventDetailED);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        listener= (MainViewListener) getActivity();
        eventHandler=new EventHandler();

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        roofRef= FirebaseDatabase.getInstance().getReference();
        eventRef=roofRef.child("Events");

        doneBTN=view.findViewById(R.id.doneBTN);
        cancleBTN=view.findViewById(R.id.canlceBTN);

        eventStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                                calendar.set(i,i1,i2);
                                String date = sdf.format(calendar.getTime());
                                eventStartDate.setText(date);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        eventEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                                calendar.set(i,i1,i2);
                                String date = sdf.format(calendar.getTime());
                                eventEndDate.setText(date);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });


        doneBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cost=0;
                title=eventTitle.getText().toString();
                place=eventPlace.getText().toString();
                try {
                    cost= Float.parseFloat(eventCost.getText().toString());
                }catch (Exception e){
                    Log.d("Cost: ",e.getMessage());
                }

                startDate=eventStartDate.getText().toString();
                endDate=eventEndDate.getText().toString();
                detail=eventDetail.getText().toString();
                id=eventRef.push().getKey();
                if (title.isEmpty()){
                    eventTitle.setError("Empty field");
                    return;
                }
                if (place.isEmpty()){
                    eventPlace.setError("Empty field");
                    return;
                }
                if (cost==0){
                    eventCost.setError("Empty field");
                    return;
                }
                if (startDate.isEmpty()){
                    eventStartDate.setError("Empty field");
                    return;
                }
                if (endDate.isEmpty()){
                    eventEndDate.setError("Empty field");
                    return;
                }
                if (detail.isEmpty()){
                    eventDetail.setError("Empty field");
                    return;
                }

                EventHandler handler=new EventHandler(id,title,place,cost,startDate,endDate,detail,0);
                eventRef.child(user.getUid()).child(id).setValue(handler);
                listener.goMainView();
            }
        });

        cancleBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.goMainView();
            }
        });

        return view;
    }

    public interface MainViewListener{
        public void goMainView();
    }

}
