package com.vlabs.arc.states.issues.config;

public enum IssuesTransition {
    CREATE,

    BEGIN_ANALYSIS,
    FAST_TRACK_TO_DEV,
    RETURN_TO_BACKLOG,

    ANALYSIS_COMPLETE,
    NEED_MORE_INFO,

    BEGIN_DEVELOPMENT,
    FAST_TRACK_TO_RELEASE,
    STOP_DEVELOPMENT,

    SEND_TO_QA,

    BEGIN_TESTING,
    QA_FAILED,

    QA_PASSED,

    RELEASED,

    REOPEN
}
