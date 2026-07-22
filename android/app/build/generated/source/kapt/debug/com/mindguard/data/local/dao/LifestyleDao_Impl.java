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
import com.mindguard.data.local.entity.DailyLogEntity;
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
public final class LifestyleDao_Impl implements LifestyleDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DailyLogEntity> __insertionAdapterOfDailyLogEntity;

  public LifestyleDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDailyLogEntity = new EntityInsertionAdapter<DailyLogEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `daily_lifestyle_logs` (`id`,`sleepHours`,`screenTimeHours`,`activeMinutes`,`caffeineIntakeMg`,`waterIntakeLiters`,`logDate`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DailyLogEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindDouble(2, entity.getSleepHours());
        statement.bindDouble(3, entity.getScreenTimeHours());
        statement.bindLong(4, entity.getActiveMinutes());
        statement.bindLong(5, entity.getCaffeineIntakeMg());
        statement.bindDouble(6, entity.getWaterIntakeLiters());
        if (entity.getLogDate() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getLogDate());
        }
      }
    };
  }

  @Override
  public Object insertLog(final DailyLogEntity log, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfDailyLogEntity.insert(log);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<DailyLogEntity>> getAllLogs() {
    final String _sql = "SELECT * FROM daily_lifestyle_logs ORDER BY logDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"daily_lifestyle_logs"}, new Callable<List<DailyLogEntity>>() {
      @Override
      @NonNull
      public List<DailyLogEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSleepHours = CursorUtil.getColumnIndexOrThrow(_cursor, "sleepHours");
          final int _cursorIndexOfScreenTimeHours = CursorUtil.getColumnIndexOrThrow(_cursor, "screenTimeHours");
          final int _cursorIndexOfActiveMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "activeMinutes");
          final int _cursorIndexOfCaffeineIntakeMg = CursorUtil.getColumnIndexOrThrow(_cursor, "caffeineIntakeMg");
          final int _cursorIndexOfWaterIntakeLiters = CursorUtil.getColumnIndexOrThrow(_cursor, "waterIntakeLiters");
          final int _cursorIndexOfLogDate = CursorUtil.getColumnIndexOrThrow(_cursor, "logDate");
          final List<DailyLogEntity> _result = new ArrayList<DailyLogEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DailyLogEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final float _tmpSleepHours;
            _tmpSleepHours = _cursor.getFloat(_cursorIndexOfSleepHours);
            final float _tmpScreenTimeHours;
            _tmpScreenTimeHours = _cursor.getFloat(_cursorIndexOfScreenTimeHours);
            final int _tmpActiveMinutes;
            _tmpActiveMinutes = _cursor.getInt(_cursorIndexOfActiveMinutes);
            final int _tmpCaffeineIntakeMg;
            _tmpCaffeineIntakeMg = _cursor.getInt(_cursorIndexOfCaffeineIntakeMg);
            final float _tmpWaterIntakeLiters;
            _tmpWaterIntakeLiters = _cursor.getFloat(_cursorIndexOfWaterIntakeLiters);
            final String _tmpLogDate;
            if (_cursor.isNull(_cursorIndexOfLogDate)) {
              _tmpLogDate = null;
            } else {
              _tmpLogDate = _cursor.getString(_cursorIndexOfLogDate);
            }
            _item = new DailyLogEntity(_tmpId,_tmpSleepHours,_tmpScreenTimeHours,_tmpActiveMinutes,_tmpCaffeineIntakeMg,_tmpWaterIntakeLiters,_tmpLogDate);
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
