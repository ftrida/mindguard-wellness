package com.mindguard.data.local.db;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.mindguard.data.local.dao.JournalDao;
import com.mindguard.data.local.dao.JournalDao_Impl;
import com.mindguard.data.local.dao.LifestyleDao;
import com.mindguard.data.local.dao.LifestyleDao_Impl;
import com.mindguard.data.local.dao.MoodDao;
import com.mindguard.data.local.dao.MoodDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class MindGuardDatabase_Impl extends MindGuardDatabase {
  private volatile LifestyleDao _lifestyleDao;

  private volatile MoodDao _moodDao;

  private volatile JournalDao _journalDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `daily_lifestyle_logs` (`id` INTEGER NOT NULL, `sleepHours` REAL NOT NULL, `screenTimeHours` REAL NOT NULL, `activeMinutes` INTEGER NOT NULL, `caffeineIntakeMg` INTEGER NOT NULL, `waterIntakeLiters` REAL NOT NULL, `logDate` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `mood_entries` (`id` INTEGER NOT NULL, `moodScore` INTEGER NOT NULL, `notes` TEXT, `entryTime` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `journal_entries` (`id` INTEGER NOT NULL, `title` TEXT NOT NULL, `content` TEXT NOT NULL, `sentimentScore` REAL, `createdAt` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'f2c7de58d24f9880ff4ea16cfb522cf7')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `daily_lifestyle_logs`");
        db.execSQL("DROP TABLE IF EXISTS `mood_entries`");
        db.execSQL("DROP TABLE IF EXISTS `journal_entries`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsDailyLifestyleLogs = new HashMap<String, TableInfo.Column>(7);
        _columnsDailyLifestyleLogs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDailyLifestyleLogs.put("sleepHours", new TableInfo.Column("sleepHours", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDailyLifestyleLogs.put("screenTimeHours", new TableInfo.Column("screenTimeHours", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDailyLifestyleLogs.put("activeMinutes", new TableInfo.Column("activeMinutes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDailyLifestyleLogs.put("caffeineIntakeMg", new TableInfo.Column("caffeineIntakeMg", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDailyLifestyleLogs.put("waterIntakeLiters", new TableInfo.Column("waterIntakeLiters", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDailyLifestyleLogs.put("logDate", new TableInfo.Column("logDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDailyLifestyleLogs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDailyLifestyleLogs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDailyLifestyleLogs = new TableInfo("daily_lifestyle_logs", _columnsDailyLifestyleLogs, _foreignKeysDailyLifestyleLogs, _indicesDailyLifestyleLogs);
        final TableInfo _existingDailyLifestyleLogs = TableInfo.read(db, "daily_lifestyle_logs");
        if (!_infoDailyLifestyleLogs.equals(_existingDailyLifestyleLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "daily_lifestyle_logs(com.mindguard.data.local.entity.DailyLogEntity).\n"
                  + " Expected:\n" + _infoDailyLifestyleLogs + "\n"
                  + " Found:\n" + _existingDailyLifestyleLogs);
        }
        final HashMap<String, TableInfo.Column> _columnsMoodEntries = new HashMap<String, TableInfo.Column>(4);
        _columnsMoodEntries.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMoodEntries.put("moodScore", new TableInfo.Column("moodScore", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMoodEntries.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMoodEntries.put("entryTime", new TableInfo.Column("entryTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMoodEntries = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMoodEntries = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMoodEntries = new TableInfo("mood_entries", _columnsMoodEntries, _foreignKeysMoodEntries, _indicesMoodEntries);
        final TableInfo _existingMoodEntries = TableInfo.read(db, "mood_entries");
        if (!_infoMoodEntries.equals(_existingMoodEntries)) {
          return new RoomOpenHelper.ValidationResult(false, "mood_entries(com.mindguard.data.local.entity.MoodEntity).\n"
                  + " Expected:\n" + _infoMoodEntries + "\n"
                  + " Found:\n" + _existingMoodEntries);
        }
        final HashMap<String, TableInfo.Column> _columnsJournalEntries = new HashMap<String, TableInfo.Column>(5);
        _columnsJournalEntries.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsJournalEntries.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsJournalEntries.put("content", new TableInfo.Column("content", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsJournalEntries.put("sentimentScore", new TableInfo.Column("sentimentScore", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsJournalEntries.put("createdAt", new TableInfo.Column("createdAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysJournalEntries = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesJournalEntries = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoJournalEntries = new TableInfo("journal_entries", _columnsJournalEntries, _foreignKeysJournalEntries, _indicesJournalEntries);
        final TableInfo _existingJournalEntries = TableInfo.read(db, "journal_entries");
        if (!_infoJournalEntries.equals(_existingJournalEntries)) {
          return new RoomOpenHelper.ValidationResult(false, "journal_entries(com.mindguard.data.local.entity.JournalEntity).\n"
                  + " Expected:\n" + _infoJournalEntries + "\n"
                  + " Found:\n" + _existingJournalEntries);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "f2c7de58d24f9880ff4ea16cfb522cf7", "87ab08f03da42d27b184d837e63fb8d9");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "daily_lifestyle_logs","mood_entries","journal_entries");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `daily_lifestyle_logs`");
      _db.execSQL("DELETE FROM `mood_entries`");
      _db.execSQL("DELETE FROM `journal_entries`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(LifestyleDao.class, LifestyleDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MoodDao.class, MoodDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(JournalDao.class, JournalDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public LifestyleDao lifestyleDao() {
    if (_lifestyleDao != null) {
      return _lifestyleDao;
    } else {
      synchronized(this) {
        if(_lifestyleDao == null) {
          _lifestyleDao = new LifestyleDao_Impl(this);
        }
        return _lifestyleDao;
      }
    }
  }

  @Override
  public MoodDao moodDao() {
    if (_moodDao != null) {
      return _moodDao;
    } else {
      synchronized(this) {
        if(_moodDao == null) {
          _moodDao = new MoodDao_Impl(this);
        }
        return _moodDao;
      }
    }
  }

  @Override
  public JournalDao journalDao() {
    if (_journalDao != null) {
      return _journalDao;
    } else {
      synchronized(this) {
        if(_journalDao == null) {
          _journalDao = new JournalDao_Impl(this);
        }
        return _journalDao;
      }
    }
  }
}
