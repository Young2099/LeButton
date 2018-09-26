package so.chinaso.com.voicemodule.chat;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.iflytek.location.LocationListener;
import com.iflytek.location.PosLocator;
import com.iflytek.location.result.GPSLocResult;
import com.iflytek.location.result.LocResult;
import com.iflytek.location.result.NetLocResult;

import javax.inject.Inject;
import javax.inject.Singleton;


/**
 * 位置Repo
 */
@Singleton
public class LocationRepository {
    private Context mContext;
    private SingleLiveEvent<NetLocResult> mNetLocData = new SingleLiveEvent<>();
    private SingleLiveEvent<GPSLocResult> mGPSLocData = new SingleLiveEvent<>();

    @Inject
    public LocationRepository(Context context) {
        mContext = context;
    }

    public LiveData<NetLocResult> getNetLoc() {
        PosLocator.getInstance(mContext).asyncGetLocation(PosLocator.TYPE_NET_LOCATION, new LocationListener() {
            @Override
            public void onResult(LocResult locResult) {
                mNetLocData.postValue((NetLocResult) locResult);
            }
        });

        return mNetLocData;
    }

    public LiveData<GPSLocResult> getGPSLoc() {
        PosLocator.getInstance(mContext).asyncGetLocation(PosLocator.TYPE_GPS_LOCATION, new LocationListener() {
            @Override
            public void onResult(LocResult locResult) {
                mGPSLocData.postValue((GPSLocResult) locResult);
            }
        });

        return mGPSLocData;
    }

    public void stopLocate() {
        PosLocator.getInstance(mContext).asyncDestroy();
    }

}
