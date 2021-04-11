package com.example.userschool.view.fragment;import android.content.BroadcastReceiver;import android.content.Context;import android.content.Intent;import android.content.IntentFilter;import android.content.SharedPreferences;import android.os.Bundle;import android.util.Log;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.ImageView;import android.widget.ProgressBar;import android.widget.TableLayout;import androidx.annotation.NonNull;import androidx.fragment.app.Fragment;import androidx.recyclerview.widget.LinearLayoutManager;import androidx.recyclerview.widget.RecyclerView;import com.example.userschool.R;import com.example.userschool.connectdb.ConnectDB;import com.example.userschool.model.Absece;import com.example.userschool.query.StudentQuery;import com.example.userschool.view.adapter.AbseceStudentRecyclerAdapter;import com.example.userschool.view.dialog.DateDialog;import com.google.android.material.floatingactionbutton.FloatingActionButton;import org.greenrobot.eventbus.EventBus;import org.greenrobot.eventbus.Subscribe;import org.greenrobot.eventbus.ThreadMode;import java.util.ArrayList;import java.util.List;import static android.content.Context.MODE_PRIVATE;public class AbseceStudentFragment extends Fragment {    private TableLayout tableLayoutAbseceFragment;    private ProgressBar progressBarAbseceStudentFragment;    private ImageView imgAbseceStudentFragmentEmpty;    private FloatingActionButton floatingActionFragmentAbseceByDate;    private RecyclerView recyclerFragmentAbseceStudent;    private RecyclerView.LayoutManager layoutManagerAbsece;    private List<Absece> abseces;    private AbseceStudentRecyclerAdapter abseceStudentRecyclerAdapter;    private ConnectDB connectDB;    private StudentQuery studentQuery;    private String date;    private SharedPreferences sharedPreferences;    private int studentID;    public View onCreateView(@NonNull LayoutInflater inflater,                             ViewGroup container,                             Bundle savedInstanceState) {        View view = inflater.inflate( R.layout.fragment_absence_student, container, false );        sharedPreferences = getActivity().getSharedPreferences( "studentID", MODE_PRIVATE );        studentID = sharedPreferences.getInt( "studentID", 0 );        connectDB = new ConnectDB();        studentQuery = new StudentQuery( getContext() );        tableLayoutAbseceFragment = view.findViewById( R.id.tableLayoutAbseceFragment );        floatingActionFragmentAbseceByDate = view.findViewById( R.id.floatingActionFragmentAbseceByDate );        progressBarAbseceStudentFragment = view.findViewById( R.id.progressBarAbseceStudentFragment );        imgAbseceStudentFragmentEmpty = view.findViewById( R.id.imgAbseceStudentFragmentEmpty );        recyclerFragmentAbseceStudent = view.findViewById( R.id.recyclerFragmentAbseceStudent );        //recycler for show studnet Absece        recyclerFragmentAbseceStudent.setHasFixedSize( true );        layoutManagerAbsece = new LinearLayoutManager( getActivity(), RecyclerView.VERTICAL, false );        recyclerFragmentAbseceStudent.setLayoutManager( layoutManagerAbsece );        abseces = new ArrayList<>();        studentQuery.callTaskGetAbsece( studentID );        abseceStudentRecyclerAdapter = new AbseceStudentRecyclerAdapter( getActivity(), abseces );        recyclerFragmentAbseceStudent.setAdapter( abseceStudentRecyclerAdapter );        progressBarAbseceStudentFragment.setVisibility( View.VISIBLE );        IntentFilter intentFilterAbsece = new IntentFilter( "listAbsece" );        requireActivity().registerReceiver( broadcastReceiverAbsece, intentFilterAbsece );        floatingActionFragmentAbseceByDate.setOnClickListener( new View.OnClickListener() {            @Override            public void onClick(View view) {                DateDialog dateDialog = new DateDialog();                dateDialog.show( getActivity().getSupportFragmentManager(), "DateDialog" );            }        } );        return view;    }    BroadcastReceiver broadcastReceiverAbsece = new BroadcastReceiver() {        @Override        public void onReceive(Context context, Intent intent) {            if (connectDB.CONN() == null) {                progressBarAbseceStudentFragment.setVisibility( View.GONE );                imgAbseceStudentFragmentEmpty.setImageResource( R.drawable.ic_disconnect );                imgAbseceStudentFragmentEmpty.setVisibility( View.VISIBLE );                tableLayoutAbseceFragment.setVisibility( View.GONE );                return;            }            abseces = studentQuery.getAbseces();            if (abseces.size() == 0) {                imgAbseceStudentFragmentEmpty.setVisibility( View.VISIBLE );            } else {                imgAbseceStudentFragmentEmpty.setVisibility( View.GONE );            }            abseceStudentRecyclerAdapter.setData( abseces );            progressBarAbseceStudentFragment.setVisibility( View.GONE );        }    };    //for get Absece By date    BroadcastReceiver broadcastReceiverAbseceByDate = new BroadcastReceiver() {        @Override        public void onReceive(Context context, Intent intent) {            if (connectDB.CONN() == null) {                progressBarAbseceStudentFragment.setVisibility( View.GONE );                imgAbseceStudentFragmentEmpty.setImageResource( R.drawable.ic_disconnect );                imgAbseceStudentFragmentEmpty.setVisibility( View.VISIBLE );                tableLayoutAbseceFragment.setVisibility( View.GONE );                return;            }            abseces = studentQuery.getAbseceByDate();            if (abseces.size() == 0) {                imgAbseceStudentFragmentEmpty.setVisibility( View.VISIBLE );            } else {                imgAbseceStudentFragmentEmpty.setVisibility( View.GONE );            }            abseceStudentRecyclerAdapter.setData( abseces );            progressBarAbseceStudentFragment.setVisibility( View.GONE );        }    };    @Subscribe(threadMode = ThreadMode.MAIN)    public void onResultReceived(String result) {        Log.d( "dateAAA",result );        studentQuery.callTaskGetAbseceByDate( studentID, result );        progressBarAbseceStudentFragment.setVisibility( View.VISIBLE );        IntentFilter intentFilterAbseceByDate = new IntentFilter( "listAbseceByDate" );        requireActivity().registerReceiver( broadcastReceiverAbseceByDate, intentFilterAbseceByDate );    }    @Override    public void onStart() {        super.onStart();        if (!EventBus.getDefault().isRegistered(this))            EventBus.getDefault().register(this);    }    @Override    public void onDestroy() {        super.onDestroy();        EventBus.getDefault().unregister(this);    }}