package com.okwy.roomwordsample.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.okwy.roomwordsample.Dao.WordDao;
import com.okwy.roomwordsample.Entity.Word;

@Database(entities = {Word.class}, version = 1, exportSchema = false)
public abstract class WordRoomDatabase extends RoomDatabase {

    public abstract WordDao wordDao();

    private static WordRoomDatabase INSTANCE;

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new populateDbAsync(INSTANCE).execute();
        }
    };

    private static class populateDbAsync extends AsyncTask<Void, Void, Void> {

        private WordDao dao;
        String[] words = {"Kangaroo", "Donkey", "Phoenix"};

        public populateDbAsync(WordRoomDatabase db) {
            dao = db.wordDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Start the app with a clean database every time.
            // Not needed if you only populate the database
            // when it is first created
            dao.deleteAll();

            for (int i = 0; i < words.length; i++) {
                Word word = new Word(words[i]);
                dao.insert(word);
            }
            return null;
        }

    }

    public static WordRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WordRoomDatabase.class) {
                if (INSTANCE == null) {
                    //Create database here

                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), WordRoomDatabase.class, "word_database")
                            // Wipes and rebuilds instead of migrating if no Migration object is available
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }


}
