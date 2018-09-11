package so.chinaso.com.voicemodule.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import so.chinaso.com.voicemodule.entity.RawMessage;


/**
 * 聊天消息Room数据库类定义
 */
@Database(entities = {RawMessage.class}, version = 1, exportSchema = false)
//@TypeConverters({Converters.class})
public abstract class MessageDB extends RoomDatabase {
    public abstract MessageDao messageDao();

    private static MessageDB INSTANCE;
    private static final Object sLock = new Object();

    public static MessageDB getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE =
                        Room.databaseBuilder(context.getApplicationContext(), MessageDB.class, "message.db")
                                .build();
            }
            return INSTANCE;
        }
    }

}
