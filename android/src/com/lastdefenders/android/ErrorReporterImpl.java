package com.lastdefenders.android;

import com.lastdefenders.util.ErrorReporter;
import org.acra.ACRA;

public class ErrorReporterImpl implements ErrorReporter {

    @Override
    public void reportError(Throwable e) {
        ACRA.getErrorReporter().handleSilentException(e);
    }
}
