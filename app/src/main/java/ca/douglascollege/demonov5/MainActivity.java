package ca.douglascollege.demonov5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnImport = findViewById(R.id.buttonGetData);

        final ListView listViewItems  = findViewById(R.id.listViewData);

        createDB();
        createTables();

        btnImport.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            List<String[]> myStudents = ReadCSV();
            for(int i = 1; i < myStudents.size(); i++){
                String id = myStudents.get(i)[0];
                String name = myStudents.get(i)[1];
                String major = myStudents.get(i)[2] ;
                addStudentProfile(id, name, major);
            }
            List<String[]> myDBItems = browseStudentRecs();
            listViewItems.setAdapter(new MyBaseAdapter(myDBItems));

            addStudentGrade("312345", 98.8);
            addStudentGrade("300123", 80.6);
            startActivity(new Intent(MainActivity.this, TextViewOutput.class));
        }
    });

   }
   private List ReadCSV(){
            List<String[]> resultList = new ArrayList<>();
            InputStream inputStream = getResources().openRawResource(R.raw.students);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String csvLine;
            try {
                while ((csvLine = reader.readLine()) != null){
                    String[] row = csvLine.split(",");
                    resultList.add(row);

            }}catch(Exception ex){
                    throw new RuntimeException("Error reading fkjdf" + ex);
            }
            finally{
                try {
                    inputStream.close();
                }
                catch (IOException ex){
                    throw new RuntimeException("erer" + ex);
                }
            }
        return resultList;
    }

   private void createDB(){
        try {
            db = openOrCreateDatabase("Student.db", Context.MODE_PRIVATE, null);
            Toast.makeText(this, "Database Ready", Toast.LENGTH_SHORT).show();
        }catch (Exception ex){
            Log.e("DB DEMO", ex.getMessage());
        }
    }
    private void createTables(){
        try {
            String dropStudentsTable = "DROP TABLE IF EXISTS students;";
            String dropGradesTable = "DROP TABLE IF EXISTS grades;";

            String createStudents = "CREATE TABLE students " +
                    "(studentid TEXT PRIMARY KEY, name TEXT, major TEXT);";
            String createGrades = "CREATE TABLE grades " +
                    "(studentid TEXT, grade DECIMAL);";

            db.execSQL(dropStudentsTable);
            db.execSQL(dropGradesTable);
            db.execSQL(createStudents);
            db.execSQL(createGrades);
            Toast.makeText(this, "Tables ready", Toast.LENGTH_SHORT).show();
        }catch (Exception ex){
            Log.e("DB DEMO", "Error in creating tables" + ex.getMessage());
        }
    }

    private void addStudentGrade(String id, Double grade){
        long result = 0;
        ContentValues val = new ContentValues();
        val.put("studentid", id);
        val.put("grade", grade);
        result = db.insert("grades",null, val);

        if (result != -1){
            Log.e("DB DEMO:", "Added grade for student " + id);
        }else{
            Log.e("DB DEMO:", "Error adding grade for student" + id);
        }
    }
    private void addStudentProfile(String id, String name, String major){
        long result;
        ContentValues val = new ContentValues();
        val.put("studentid", id);
        val.put("name", name);
        val.put("major", major);

        result = db.insert("students",null, val);
        if (result != -1){
            Log.e("DB DEMO:", "Added item" + id);
        }else{
            Log.e("DB DEMO:", "Error adding item" + id);
        }
    }
    private List<String[]> browseStudentRecs(){
        List<String[]> studentList = new ArrayList<>();
        String query = "SELECT * FROM students;";
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor != null){
                cursor.moveToFirst();
                while (!cursor.isAfterLast()){
                    String[] eachRecArray = new String[3];
                    eachRecArray[0] = cursor.getString(0);
                    eachRecArray[1] = cursor.getString(1);
                    eachRecArray[2] = cursor.getString(2);
                    studentList.add(eachRecArray);
                    cursor.moveToNext();
                }

            }
        }
        catch(Exception ex){
            Log.e("DB DEMO", ex.getMessage());
        }
        return studentList;
    }

}

