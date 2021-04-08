package com.example.userschool.view.activity;import androidx.appcompat.app.AppCompatActivity;import androidx.appcompat.app.AppCompatDelegate;import android.app.ProgressDialog;import android.content.Intent;import android.content.SharedPreferences;import android.os.AsyncTask;import android.os.Bundle;import android.util.Log;import android.view.View;import android.view.animation.Animation;import android.view.animation.AnimationUtils;import android.widget.Button;import android.widget.EditText;import android.widget.RadioButton;import android.widget.RadioGroup;import android.widget.RelativeLayout;import android.widget.Toast;import com.example.userschool.R;import com.example.userschool.connectdb.ConnectDB;import com.google.android.material.snackbar.Snackbar;import java.sql.Connection;import java.sql.ResultSet;import java.sql.ResultSetMetaData;import java.sql.Statement;public class MainActivity extends AppCompatActivity {    private Animation topAnimation;    private RelativeLayout layoutSingIn;    private EditText edtLogInName;    private EditText edtLogInpassword;    private RadioGroup radioGroupStudOrTeach;    private RadioButton radioButtonStudent;    private RadioButton radioButtonTeacher;    private Button btnLogInSingIn;    private ProgressDialog progressDialog;    private SharedPreferences sharedPreferences;    private ConnectDB connectDB;    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate( savedInstanceState );        AppCompatDelegate.setDefaultNightMode( AppCompatDelegate.MODE_NIGHT_NO );        setContentView( R.layout.activity_main );        // for remeber me when open        sharedPreferences = getSharedPreferences( "remeberMeAsStudent", MODE_PRIVATE );        if (sharedPreferences.getBoolean( "remeberMeAsStudent", false )) {            startActivity( new Intent( MainActivity.this, StudentActivity.class ) );            finish();            return;        }        sharedPreferences = getSharedPreferences( "remeberMeAsTeacher", MODE_PRIVATE );        if (sharedPreferences.getBoolean( "remeberMeAsTeacher", false )) {            startActivity( new Intent( MainActivity.this, ChoseClassByTeacherActivity.class ) );            finish();            return;        }        //for set animatoin for login Ui        topAnimation = AnimationUtils.loadAnimation( this, R.anim.top_animtion );        layoutSingIn = findViewById( R.id.layoutSingIn );        layoutSingIn.setAnimation( topAnimation );        connectDB = new ConnectDB();        progressDialog = new ProgressDialog( this );        // Process Login        edtLogInName = findViewById( R.id.edtLogInName );        edtLogInpassword = findViewById( R.id.edtLogInpassword );        radioGroupStudOrTeach = findViewById( R.id.radioGroupStudOrTeach );        radioButtonStudent = findViewById( R.id.radioButtonStudent );        radioButtonTeacher = findViewById( R.id.radioButtonTeacher );        btnLogInSingIn = findViewById( R.id.btnLogInSingIn );        btnLogInSingIn.setOnClickListener( new View.OnClickListener() {            @Override            public void onClick(View view) {                String name = edtLogInName.getText().toString().trim();                String password = edtLogInpassword.getText().toString().trim();                if (name.equals( "" ) || password.equals( "" )) {                    Toast.makeText( MainActivity.this, "Please fill all record and agine", Toast.LENGTH_SHORT ).show();                    return;                }                if (radioButtonStudent.isChecked()) {                    Toast.makeText( MainActivity.this, "student", Toast.LENGTH_SHORT ).show();                    logINStudent();                } else if (radioButtonTeacher.isChecked()) {                    Toast.makeText( MainActivity.this, "teacher", Toast.LENGTH_SHORT ).show();                    logINTeacher();                } else {                    Toast.makeText( MainActivity.this, "please select student or teacher", Toast.LENGTH_SHORT ).show();                }            }        } );    }    public void logINStudent() {        new TaskSelectStudnet().execute( "" );    }    public void logINTeacher() {        new TaskSelectTeacher().execute( "" );    }    //region Login teacher    public class TaskSelectTeacher extends AsyncTask<String, String, Integer> {        String namestr = edtLogInName.getText().toString();        String passwordstr = edtLogInpassword.getText().toString();        String state = "";        boolean isSuccess = false;        int teacherID;        String returnName = "";        String returnPassword = "";        int subjectID;        @Override        protected void onPreExecute() {            progressDialog.setMessage( "Loading..." );            progressDialog.show();        }        @Override        protected Integer doInBackground(String... strings) {            try {                Connection connection = connectDB.CONN();                if (connection == null) {                    Snackbar snackbar = Snackbar.make( findViewById( R.id.layoutSingIn ),                            "Please check your internet connection", Snackbar.LENGTH_LONG );                    snackbar.setAction( "RETRY", new View.OnClickListener() {                        @Override                        public void onClick(View view) {                        }                    } );                    snackbar.show();                } else {                    String query = "select id , name , password , subjectID " +                            "from teacher" +                            " where ( name='" + namestr + "') and (password='" + passwordstr + "')";                    Statement stmt1 = connection.createStatement();                    ResultSet resultSet = stmt1.executeQuery( query );                    ResultSetMetaData resultSetMetaData = resultSet.getMetaData();                    while (resultSet.next()) {                        teacherID = resultSet.getInt( 1 );                        returnName = resultSet.getNString( 2 );                        returnPassword = resultSet.getNString( 3 );                        subjectID = resultSet.getInt( 4 );                    }                    if (returnName.equals( "" ) || returnPassword.equals( "" )) {                        Snackbar snackbar = Snackbar.make( findViewById( R.id.layoutSingIn ),                                "Name or Password is Wrong", Snackbar.LENGTH_LONG );                        snackbar.setAction( "RETRY", new View.OnClickListener() {                            @Override                            public void onClick(View view) {                                edtLogInName.setText( "" );                                edtLogInpassword.setText( "" );                            }                        } );                        snackbar.show();                        isSuccess = false;                    } else {                        isSuccess = true;                    }                }            } catch (Exception ex) {                isSuccess = false;                state = "Exceptions" + ex;            }            return teacherID;        }        @Override        protected void onPostExecute(Integer teacherID) {            if (isSuccess) {                sharedPreferences = getSharedPreferences( "remeberMeAsTeacher", MODE_PRIVATE );                SharedPreferences.Editor editor = sharedPreferences.edit();                editor.putBoolean( "remeberMeAsTeacher", true );                editor.commit();                sharedPreferences = getSharedPreferences( "teacherID", MODE_PRIVATE );                SharedPreferences.Editor editorTeacherID = sharedPreferences.edit();                editorTeacherID.putInt( "teacherID", teacherID );                editorTeacherID.commit();                sharedPreferences = getSharedPreferences( "subjectID", MODE_PRIVATE );                SharedPreferences.Editor editorSubjectID = sharedPreferences.edit();                editorSubjectID.putInt( "subjectID", subjectID );                editorSubjectID.commit();                startActivity( new Intent( MainActivity.this, ChoseClassByTeacherActivity.class ) );                finish();            }            progressDialog.hide();        }    }    ///endregion    //region Login student    public class TaskSelectStudnet extends AsyncTask<String, String, Integer> {        String namestr = edtLogInName.getText().toString();        String passwordstr = edtLogInpassword.getText().toString();        String state = "";        boolean isSuccess = false;        String returnName = "";        String returnPassword = "";        int studentID;        @Override        protected void onPreExecute() {            progressDialog.setMessage( "Loading..." );            progressDialog.show();        }        @Override        protected Integer doInBackground(String... strings) {            try {                Connection connection = connectDB.CONN();                if (connection == null) {                    Snackbar snackbar = Snackbar.make( findViewById( R.id.layoutSingIn ),                            "Please check your internet connection", Snackbar.LENGTH_LONG );                    snackbar.setAction( "RETRY", new View.OnClickListener() {                        @Override                        public void onClick(View view) {                        }                    } );                    snackbar.show();                } else {                    String query = "select id , name , password from student" +                            " where ( name='" + namestr + "') and (password='" + passwordstr + "')";                    Statement stmt1 = connection.createStatement();                    ResultSet resultSet = stmt1.executeQuery( query );                    ResultSetMetaData resultSetMetaData = resultSet.getMetaData();                    while (resultSet.next()) {                        studentID = resultSet.getInt( 1 );                        returnName = resultSet.getNString( 2 );                        returnPassword = resultSet.getNString( 3 );                    }                    if (returnName.equals( "" ) || returnPassword.equals( "" )) {                        Snackbar snackbar = Snackbar.make( findViewById( R.id.layoutSingIn ),                                "Name or Password is Wrong", Snackbar.LENGTH_LONG );                        snackbar.setAction( "RETRY", new View.OnClickListener() {                            @Override                            public void onClick(View view) {                                edtLogInName.setText( "" );                                edtLogInpassword.setText( "" );                            }                        } );                        snackbar.show();                        isSuccess = false;                    } else {                        isSuccess = true;                    }                }            } catch (Exception ex) {                isSuccess = false;                state = "Exceptions" + ex;            }            return studentID;        }        @Override        protected void onPostExecute(Integer studentID) {            if (isSuccess) {                sharedPreferences = getSharedPreferences( "remeberMeAsStudent", MODE_PRIVATE );                SharedPreferences.Editor editor = sharedPreferences.edit();                editor.putBoolean( "remeberMeAsStudent", true );                editor.commit();                sharedPreferences = getSharedPreferences( "studentID", MODE_PRIVATE );                SharedPreferences.Editor editorStudentID = sharedPreferences.edit();                editorStudentID.putInt( "studentID", studentID );                editorStudentID.commit();                startActivity( new Intent( MainActivity.this, StudentActivity.class ) );                finish();            }            progressDialog.hide();        }    }    ///endregion}