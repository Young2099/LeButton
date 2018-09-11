package so.chinaso.com.voicemodule.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import so.chinaso.com.voicemodule.entity.RawMessage;

/**
 * Created by yf on 2018/9/10.
 */
@Dao
public interface MessageDao {
    @Insert
    void addMessage(RawMessage msg);

    @Update
    void updateMessage(RawMessage msg);

    @Query("select * from RawMessage")
    LiveData<List<RawMessage>> getAllMessage();
}
