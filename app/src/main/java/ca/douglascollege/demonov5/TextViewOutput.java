package ca.douglascollege.demonov5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TextViewOutput extends AppCompatActivity {

    SQLiteDatabase db;
    StringBuilder outputText = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_view_output);
        openDatabase();
        browseStudentRecs();
        browseGradeRercords();
        browseJoinStudentRecs();
        browseJoinStudentHighGrades(90);
        checkAndReplaceRecord("312345");
        TextView textViewOutput = findViewById(R.id.textViewOutput);
        textViewOutput.setText(outputText.toString());


    }
    private void openDatabase(){
        try {
            db = openOrCreateDatabase("Student.db", MODE_PRIVATE, null);
        }
        catch (Exception ex){
            Log.e("DB DEMO", ex.getMessage());
        }
    }
    private void browseStudentRecs(){
        String query = "SELECT * FROM students;";
        try {
            Cursor cursor = db.rawQuery(query, null);
            String headRecord = String.format("%-15s-%-15s%-15s\n", "StudentId", "StudentName", "StudentMajor");
            outputText.append(headRecord);
            if (cursor != null){
                cursor.moveToFirst();
                while (!cursor.isAfterLast()){
                    String eachRec = String.format("%-15s-%-15s%-15s\n",
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2));
                    outputText.append(eachRec);
                    cursor.moveToNext();
                }
            }
        }
        catch(Exception ex){
            Log.e("DB DEMO", ex.getMessage());
        }
    }
    private void browseGradeRercords(){
        String query = "SELECT * FROM grades;";
        try {
            Cursor cursor = db.rawQuery(query, null);
            //String headRecord = String.format("%-15s-%-15s%\n", "StudentId", "Grade");
            String headRecord = String.format("\n\n%-15s-%-15s%-15s\n", "StudentId", "StudentName", "StudentMajor");
            outputText.append(headRecord);
            if (cursor != null){
                cursor.moveToFirst();
                while (!cursor.isAfterLast()){
                    String eachRec = String.format("%-15s-%-15.2f\n",
                            cursor.getString(0),
                            cursor.getDouble(1));
                    outputText.append(eachRec);
                    cursor.moveToNext();
                }
            }
        }
        catch(Exception ex){
            Log.e("DB DEMO", ex.getMessage());
        }
    }
    private void browseJoinStudentHighGrades(int cutoffGrade) {
        String queryString = "SELECT name, grade FROM students " +
                "INNER JOIN grades ON " +
                "students.studentid = grades.studentid WHERE grade > ?";
        try {
            Cursor cursor = db.rawQuery(queryString, new String[]{String.valueOf(cutoffGrade)});
            //String headRecord = String.format("%-15s-%-15s%\n", "StudentId", "Grade");
            String headRecord = String.format("\n\n%-15s-%-15s\n", "StudentName", "Grade");
            outputText.append(headRecord);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    String eachRec = String.format("%-15s-%-15.2f\n",
                            cursor.getString(0),
                            cursor.getDouble(1));
                    outputText.append(eachRec);
                    cursor.moveToNext();
                }
            }
        } catch (Exception ex) {
            Log.e("DB DEMO", ex.getMessage());
        }
    }
    private void browseJoinStudentRecs(){
        String queryString = "SELECT name, grade FROM students " +
                "INNER JOIN grades ON " +
                "students.studentid = grades.studentid";
        try {
            Cursor cursor = db.rawQuery(queryString, null);
            //String headRecord = String.format("%-15s-%-15s%\n", "StudentId", "Grade");
            String headRecord = String.format("\n\n%-15s-%-15s\n", "StudentName", "Grade");
            outputText.append(headRecord);
            if (cursor != null){
                cursor.moveToFirst();
                while (!cursor.isAfterLast()){
                    String eachRec = String.format("%-15s-%-15.2f\n",
                            cursor.getString(0),
                            cursor.getDouble(1));
                    outputText.append(eachRec);
                    cursor.moveToNext();
                }
            }
        }
        catch(Exception ex){
            Log.e("DB DEMO", ex.getMessage());
        }
    }
    private boolean checkAndReplaceRecord(String id)
    {
        String queryStr = "SELECT studentid, grade FROM grades WHERE studentid = ?;";
        try {
            Cursor cursor = db.rawQuery(queryStr, new String[]{id});
            if (cursor.getCount() == 1){
                cursor.moveToFirst();
                double oldGrade = cursor.getDouble(1);
                ContentValues val = new ContentValues();
                val.put("studentid", id);
                val.put("grade", oldGrade*1.1);
                db.update("grades", val, "studentid = ?", new String[]{id});
                Log.e("DB DEMO", "Updated grade for " + id);
                return true;
            }
        }
        catch(Exception ex){
            Log.e("DB DEMO", ex.getMessage());

        }
        return false;
    }
}
