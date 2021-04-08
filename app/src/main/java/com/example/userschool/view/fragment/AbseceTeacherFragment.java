package com.example.userschool.view.fragment;import android.content.BroadcastReceiver;import android.content.Context;import android.content.Intent;import android.content.IntentFilter;import android.content.SharedPreferences;import android.os.Bundle;import android.util.Log;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.ImageButton;import android.widget.ImageView;import android.widget.ProgressBar;import android.widget.TableLayout;import android.widget.TextView;import androidx.annotation.NonNull;import androidx.fragment.app.Fragment;import androidx.recyclerview.widget.LinearLayoutManager;import androidx.recyclerview.widget.RecyclerView;import com.example.userschool.R;import com.example.userschool.connectdb.ConnectDB;import com.example.userschool.listener.AbseceListner;import com.example.userschool.model.Absece;import com.example.userschool.model.Student;import com.example.userschool.model.StudentFound;import com.example.userschool.query.TeacherQuery;import com.example.userschool.view.activity.TeacherActivity;import com.example.userschool.view.adapter.AbseceTeacherRecyclerAdapter;import java.text.SimpleDateFormat;import java.util.ArrayList;import java.util.Calendar;import java.util.Date;import java.util.List;import java.util.Locale;import static android.content.Context.MODE_PRIVATE;public class AbseceTeacherFragment extends Fragment implements AbseceListner {    private boolean state = true;    private TextView txtAbseceTeacherFragment;    private ProgressBar progressBarAbseceTeacherFragment;    private ImageView imgAbseceTeacherFragmentEmpty;    private ImageButton imgBtnAbseceTeacherFragmentNew;    private TableLayout tableLayoutAbseceTeacherFragment;    private RecyclerView recyclerAbseceTeacherFragment;    private RecyclerView.LayoutManager layoutManagerAbsece;    private List<Student> students;    private AbseceTeacherRecyclerAdapter abseceTeacherRecyclerAdapter;    private List<Absece> abseces;    private List<StudentFound> studentFounds;    private int subjectID;    private ConnectDB connectDB;    private TeacherQuery teacherQuery;    private SharedPreferences sharedPreferences;    public View onCreateView(@NonNull LayoutInflater inflater,                             ViewGroup container, Bundle savedInstanceState) {        View view = inflater.inflate( R.layout.fragment_absece_teacher, container, false );        sharedPreferences = getActivity().getSharedPreferences( "subjectID", MODE_PRIVATE );        subjectID = sharedPreferences.getInt( "subjectID", 0 );        Date date = Calendar.getInstance().getTime();        SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd", Locale.getDefault() );        String formattedDate = df.format( date );        connectDB = new ConnectDB();        teacherQuery = new TeacherQuery( getContext() );        abseces = new ArrayList<>();        studentFounds = new ArrayList<>();        txtAbseceTeacherFragment = view.findViewById( R.id.txtAbseceTeacherFragment );        tableLayoutAbseceTeacherFragment = view.findViewById( R.id.tableLayoutAbseceTeacherFragment );        progressBarAbseceTeacherFragment = view.findViewById( R.id.progressBarAbseceTeacherFragment );        imgAbseceTeacherFragmentEmpty = view.findViewById( R.id.imgAbseceTeacherFragmentEmpty );        imgBtnAbseceTeacherFragmentNew = view.findViewById( R.id.imgBtnAbseceTeacherFragmentNew );        recyclerAbseceTeacherFragment = view.findViewById( R.id.recyclerAbseceTeacherFragment );        //recycler for show studnet Absece        recyclerAbseceTeacherFragment.setHasFixedSize( true );        layoutManagerAbsece = new LinearLayoutManager( getActivity(), RecyclerView.VERTICAL, false );        recyclerAbseceTeacherFragment.setLayoutManager( layoutManagerAbsece );        students = new ArrayList<>();        abseceTeacherRecyclerAdapter = new AbseceTeacherRecyclerAdapter( getActivity(), students, studentFounds, this::onAbseceChange );        recyclerAbseceTeacherFragment.setAdapter( abseceTeacherRecyclerAdapter );        tableLayoutAbseceTeacherFragment.setVisibility( View.GONE );        imgAbseceTeacherFragmentEmpty.setImageResource( R.drawable.ic_add_file );        imgAbseceTeacherFragmentEmpty.setVisibility( View.VISIBLE );        imgBtnAbseceTeacherFragmentNew.setOnClickListener( new View.OnClickListener() {            @Override            public void onClick(View view) {                if (state == true) {                    progressBarAbseceTeacherFragment.setVisibility( View.VISIBLE );                    imgAbseceTeacherFragmentEmpty.setVisibility( View.GONE );                    teacherQuery.callTaskGetStudentByClassID( ((TeacherActivity) getActivity()).getClassID() );                    IntentFilter intentFilterStudentByClassID = new IntentFilter( "studentsByClassID" );                    requireActivity().registerReceiver( broadcastReceiverStudentByClassID, intentFilterStudentByClassID );                    state = false;                } else if (state == false) {                    tableLayoutAbseceTeacherFragment.setVisibility( View.GONE );                    recyclerAbseceTeacherFragment.setVisibility( View.GONE );                    progressBarAbseceTeacherFragment.setVisibility( View.VISIBLE );                    for (int i = 0; i < students.size(); i++) {                        abseces.add( new Absece( 0, studentFounds.get( i ).getStudentID(), subjectID, studentFounds.get( i ).isFound(), formattedDate ) );                    }                    teacherQuery.callTaskInsertAbseces( abseces );                    IntentFilter intentFilterInsertAbsece = new IntentFilter( "InsertAbseceState" );                    requireActivity().registerReceiver( broadcastReceiverInsertAbsece, intentFilterInsertAbsece );                    state = true;                }            }        } );        return view;    }    // get student By classID    final BroadcastReceiver broadcastReceiverStudentByClassID = new BroadcastReceiver() {        @Override        public void onReceive(Context context, Intent intent) {            if (connectDB.CONN() == null) {                progressBarAbseceTeacherFragment.setVisibility( View.GONE );                imgAbseceTeacherFragmentEmpty.setImageResource( R.drawable.ic_disconnect );                imgAbseceTeacherFragmentEmpty.setVisibility( View.VISIBLE );                tableLayoutAbseceTeacherFragment.setVisibility( View.GONE );                return;            }            students = teacherQuery.getStudents();            if (students.size() == 0) {                imgAbseceTeacherFragmentEmpty.setVisibility( View.VISIBLE );            } else {                imgAbseceTeacherFragmentEmpty.setVisibility( View.GONE );            }            abseceTeacherRecyclerAdapter.setData( students );            for (Student student : students) {                studentFounds.add( new StudentFound( student.getId(), 0 ) );            }            progressBarAbseceTeacherFragment.setVisibility( View.GONE );            tableLayoutAbseceTeacherFragment.setVisibility( View.VISIBLE );            imgBtnAbseceTeacherFragmentNew.setBackgroundResource( R.drawable.ic_check );        }    };    //for Insert List Absece to dataBase    BroadcastReceiver broadcastReceiverInsertAbsece = new BroadcastReceiver() {        @Override        public void onReceive(Context context, Intent intent) {            if (connectDB.CONN() == null) {                progressBarAbseceTeacherFragment.setVisibility( View.GONE );                imgAbseceTeacherFragmentEmpty.setImageResource( R.drawable.ic_disconnect );                imgAbseceTeacherFragmentEmpty.setVisibility( View.VISIBLE );                tableLayoutAbseceTeacherFragment.setVisibility( View.GONE );                return;            }            if (teacherQuery.getStateInsertAbsece()) {                imgAbseceTeacherFragmentEmpty.setImageResource( R.drawable.ic_done );                imgAbseceTeacherFragmentEmpty.setVisibility( View.VISIBLE );            } else {                imgAbseceTeacherFragmentEmpty.setImageResource( R.drawable.ic_cancel );                imgAbseceTeacherFragmentEmpty.setVisibility( View.VISIBLE );            }            imgBtnAbseceTeacherFragmentNew.setBackgroundResource( R.drawable.ic_add );        }    };    @Override    public void onAbseceChange(List<StudentFound> studentFounds) {        this.studentFounds = studentFounds;    }}