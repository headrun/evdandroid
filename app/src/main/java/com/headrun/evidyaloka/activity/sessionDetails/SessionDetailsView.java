package com.headrun.evidyaloka.activity.sessionDetails;

import com.headrun.evidyaloka.model.*;
import com.headrun.evidyaloka.model.SessionDetails;

/**
 * Created by sujith on 6/3/17.
 */

public interface SessionDetailsView {

    public void setSessionData(SessionDetails mSessionDeatils);

    public void updateSessionDetialsCount(int count);

    public void movetoSessionScreen();
    public void showProcessingBar();
    public void hideProcessingBar();


}
