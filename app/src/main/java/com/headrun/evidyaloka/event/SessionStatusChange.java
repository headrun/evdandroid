package com.headrun.evidyaloka.event;

import com.headrun.evidyaloka.model.Sessions;

/**
 * Created by sujith on 16/2/17.
 */

public class SessionStatusChange {

    public final Sessions mSession;

    public SessionStatusChange(Sessions mSession) {
        this.mSession = mSession;
    }
}
