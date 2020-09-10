package com.okwy.roomwordsample.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.okwy.roomwordsample.Dao.WordDao;
import com.okwy.roomwordsample.Database.WordRoomDatabase;
import com.okwy.roomwordsample.Entity.Word;

import java.util.List;

public class WordRepository {
    private WordDao mWordDao;
    private LiveData<List<Word>> mAllWords;

    public WordRepository(Application application) {
        WordRoomDatabase db = WordRoomDatabase.getDatabase(application);
        mWordDao = db.wordDao();
        mAllWords = mWordDao.getAllWords();
    }

    public LiveData<List<Word>> getAllWords() {
        return mAllWords;
    }

    public void insert(Word word) {
        new insertAsyncTask(mWordDao).execute(word);
    }

    public static class insertAsyncTask extends AsyncTask<Word, Void, Void> {

        private WordDao asyncWordDao;

        public insertAsyncTask(WordDao asyncWordDao) {
            this.asyncWordDao = asyncWordDao;
        }

        @Override
        protected Void doInBackground(final Word... words) {
            asyncWordDao.insert(words[0]);
            return null;
        }
    }
}
