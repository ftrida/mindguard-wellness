package com.mindguard.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.mindguard.data.local.entity.MoodEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class MoodDao_Impl implements MoodDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MoodEntity> __insertionAdapterOfMoodEntity;

  public MoodDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMoodEntity = new EntityInsertionAdapter<MoodEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `mood_entries` (`id`,`moodScore`,`notes`,`entryTime`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MoodEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getMoodScore());
        if (entity.getNotes() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getNotes());
        }
        if (entity.getEntryTime() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getEntryTime());
        }
      }
    };
  }

  @Override
  public Object insertMood(final MoodEntity mood, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMoodEntity.insert(mood);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<MoodEntity>> getAllMoods() {
    final String _sql = "SELECT * FROM mood_entries ORDER BY entryTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"mood_entries"}, new Callable<List<MoodEntity>>() {
      @Override
      @NonNull
      public List<MoodEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMoodScore = CursorUtil.getColumnIndexOrThrow(_cursor, "moodScore");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfEntryTime = CursorUtil.getColumnIndexOrThrow(_cursor, "entryTime");
          final List<MoodEntity> _result = new ArrayList<MoodEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MoodEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final int _tmpMoodScore;
            _tmpMoodScore = _cursor.getInt(_cursorIndexOfMoodScore);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final String _tmpEntryTime;
            if (_cursor.isNull(_cursorIndexOfEntryTime)) {
              _tmpEntryTime = null;
            } else {
              _tmpEntryTime = _cursor.getString(_cursorIndexOfEntryTime);
            }
            _item = new MoodEntity(_tmpId,_tmpMoodScore,_tmpNotes,_tmpEntryTime);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
