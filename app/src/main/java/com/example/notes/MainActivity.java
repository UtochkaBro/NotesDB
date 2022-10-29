package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewNotes;
    public static final ArrayList<Note> notes = new ArrayList<>();
    private NotesAdapter adapter;
    private NotesDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerViewNotes = findViewById(R.id.recyclersViewNotes);
        dbHelper = new NotesDBHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        if (notes.isEmpty()) {
            notes.add(new Note("Слон", "нет счастливее слона на свете", "Понедельник", 2));
            notes.add(new Note("Кот", "нет счастливее слона на свете", "Вторник", 3));
            notes.add(new Note("Собака", "нет счастливее слона на свете", "Среда", 3));
            notes.add(new Note("Мышь", "нет счастливее слона на свете", "Четверг", 1));
            notes.add(new Note("Хрюпс", "нет счастливее слона на свете", "Пятница", 1));
            notes.add(new Note("Слон", "нет счастливее слона на свете", "Суббота", 2));
        }
        for (Note note : notes) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(NotesContract.NotesEntry.COLUMN_TITLE, note.getTitle());
            contentValues.put(NotesContract.NotesEntry.COLUMN_DESCRIPTION, note.getDescription());
            contentValues.put(NotesContract.NotesEntry.COLUMN_DAY_OF_WEEK, note.getDayOfWeek());
            contentValues.put(NotesContract.NotesEntry.COLUMN_PRIORITY, note.getPriority());
            database.insert(NotesContract.NotesEntry.TABLE_NAME, null, contentValues);
        }

        ArrayList<Note> notesFromDB = new ArrayList<>();
        Cursor cursor = database.query(NotesContract.NotesEntry.TABLE_NAME, null,null,null,null,null,null);
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex(NotesContract.NotesEntry.COLUMN_TITLE));
            String description = cursor.getString(cursor.getColumnIndex(NotesContract.NotesEntry.COLUMN_DESCRIPTION));
            String dayOfWeek = cursor.getString(cursor.getColumnIndex(NotesContract.NotesEntry.COLUMN_DAY_OF_WEEK));
            int priority = cursor.getInt(cursor.getColumnIndex(NotesContract.NotesEntry.COLUMN_PRIORITY));

            Note note = new Note(title, description, dayOfWeek, priority);
            notesFromDB.add(note);
        }
        cursor.close();

        NotesAdapter adapter = new NotesAdapter(notesFromDB);
        recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNotes.setAdapter(adapter);
        adapter.setOnNoteClickListener(new NotesAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(int position) {

            }

            @Override
            public void longLongClick(int position) {
                remove(position);
            }
        });
    }

    private void remove(int position) {
        notes.remove(position);
        adapter.notifyDataSetChanged();
    }

    public void onClickAddNote(View view){
        Intent intent = new Intent(this, AddNoteActivity.class);
        startActivity(intent);
    }
}