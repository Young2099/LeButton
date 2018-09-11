package so.chinaso.com.voicemodule.voice;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.iflytek.location.LocationListener;
import com.iflytek.location.PosLocator;
import com.iflytek.location.result.GPSLocResult;
import com.iflytek.location.result.LocResult;
import com.iflytek.location.result.NetLocResult;


/**
 * 位置Repo
 */

public class LocationRepo {
    private Context mContext;
    private SingleLiveEvent<NetLocResult> mNetLocData = new SingleLiveEvent<>();
    private SingleLiveEvent<GPSLocResult> mGPSLocData = new SingleLiveEvent<>();

    public LocationRepo(Context context) {
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
