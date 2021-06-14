package com.vlabs.arc.states.issues.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class IssuesStateMachineConfigTest {

    @Autowired
    @Qualifier("issuesStateMachineFactory")
    private StateMachineFactory<IssuesStates, IssuesTransition> stateMachineFactory;

    private StateMachine<IssuesStates, IssuesTransition> stateMachine;

    @BeforeEach
    public void setUp() {
        stateMachine = stateMachineFactory.getStateMachine();
    }

    @Test
    public void contextLoads() {
        assertThat(stateMachine).isNotNull();
    }

    @Test
    public void initialState() {
        assertThat(stateMachine.getInitialState().getId()).isEqualTo(IssuesStates.START);
    }

    @Test
    public void create() {
        stateMachine.sendEvent(IssuesTransition.CREATE);
        assertThat(stateMachine.getState().getId()).isEqualTo(IssuesStates.BACKLOG);
    }

    @Test
    public void beginAnalysis() {
        stateMachine.sendEvent(IssuesTransition.CREATE);
        stateMachine.sendEvent(IssuesTransition.BEGIN_ANALYSIS);
        assertThat(stateMachine.getState().getId()).isEqualTo(IssuesStates.ANALYSIS);
    }

    @Test
    public void fastTrackToDev() {
        stateMachine.sendEvent(IssuesTransition.CREATE);
        stateMachine.sendEvent(IssuesTransition.FAST_TRACK_TO_DEV);
        assertThat(stateMachine.getState().getId()).isEqualTo(IssuesStates.IN_DEVELOPMENT);
    }

    @Test
    public void returnToBacklog() {
        stateMachine.sendEvent(IssuesTransition.CREATE);
        stateMachine.sendEvent(IssuesTransition.BEGIN_ANALYSIS);
        stateMachine.sendEvent(IssuesTransition.RETURN_TO_BACKLOG);
        assertThat(stateMachine.getState().getId()).isEqualTo(IssuesStates.BACKLOG);
    }

    @Test
    public void analysisComplete() {
        stateMachine.sendEvent(IssuesTransition.CREATE);
        stateMachine.sendEvent(IssuesTransition.BEGIN_ANALYSIS);
        stateMachine.sendEvent(IssuesTransition.ANALYSIS_COMPLETE);
        assertThat(stateMachine.getState().getId()).isEqualTo(IssuesStates.READY_FOR_DEV);
    }

    @Test
    public void needMoreInfo() {
        stateMachine.sendEvent(IssuesTransition.CREATE);
        stateMachine.sendEvent(IssuesTransition.BEGIN_ANALYSIS);
        stateMachine.sendEvent(IssuesTransition.ANALYSIS_COMPLETE);
        stateMachine.sendEvent(IssuesTransition.NEED_MORE_INFO);
        assertThat(stateMachine.getState().getId()).isEqualTo(IssuesStates.ANALYSIS);
    }

    @Test
    public void beginDevelopment() {
        stateMachine.sendEvent(IssuesTransition.CREATE);
        stateMachine.sendEvent(IssuesTransition.BEGIN_ANALYSIS);
        stateMachine.sendEvent(IssuesTransition.ANALYSIS_COMPLETE);
        stateMachine.sendEvent(IssuesTransition.BEGIN_DEVELOPMENT);
        assertThat(stateMachine.getState().getId()).isEqualTo(IssuesStates.IN_DEVELOPMENT);
    }

    @Test
    public void stopDevelopment() {
        stateMachine.sendEvent(IssuesTransition.CREATE);
        stateMachine.sendEvent(IssuesTransition.BEGIN_ANALYSIS);
        stateMachine.sendEvent(IssuesTransition.ANALYSIS_COMPLETE);
        stateMachine.sendEvent(IssuesTransition.BEGIN_DEVELOPMENT);
        stateMachine.sendEvent(IssuesTransition.STOP_DEVELOPMENT);
        assertThat(stateMachine.getState().getId()).isEqualTo(IssuesStates.READY_FOR_DEV);
    }

    @Test
    public void fastTrackToRelease() {
        stateMachine.sendEvent(IssuesTransition.CREATE);
        stateMachine.sendEvent(IssuesTransition.BEGIN_ANALYSIS);
        stateMachine.sendEvent(IssuesTransition.ANALYSIS_COMPLETE);
        stateMachine.sendEvent(IssuesTransition.BEGIN_DEVELOPMENT);
        stateMachine.sendEvent(IssuesTransition.FAST_TRACK_TO_RELEASE);
        assertThat(stateMachine.getState().getId()).isEqualTo(IssuesStates.READY_FOR_RELEASE);
    }

    @Test
    public void sendToQA() {
        stateMachine.sendEvent(IssuesTransition.CREATE);
        stateMachine.sendEvent(IssuesTransition.BEGIN_ANALYSIS);
        stateMachine.sendEvent(IssuesTransition.ANALYSIS_COMPLETE);
        stateMachine.sendEvent(IssuesTransition.BEGIN_DEVELOPMENT);
        stateMachine.sendEvent(IssuesTransition.SEND_TO_QA);
        assertThat(stateMachine.getState().getId()).isEqualTo(IssuesStates.READY_FOR_QA);
    }

    @Test
    public void beginTesting() {
        stateMachine.sendEvent(IssuesTransition.CREATE);
        stateMachine.sendEvent(IssuesTransition.BEGIN_ANALYSIS);
        stateMachine.sendEvent(IssuesTransition.ANALYSIS_COMPLETE);
        stateMachine.sendEvent(IssuesTransition.BEGIN_DEVELOPMENT);
        stateMachine.sendEvent(IssuesTransition.SEND_TO_QA);
        stateMachine.sendEvent(IssuesTransition.BEGIN_TESTING);
        assertThat(stateMachine.getState().getId()).isEqualTo(IssuesStates.IN_QA);
    }

    @Test
    public void qaPassed() {
        stateMachine.sendEvent(IssuesTransition.CREATE);
        stateMachine.sendEvent(IssuesTransition.BEGIN_ANALYSIS);
        stateMachine.sendEvent(IssuesTransition.ANALYSIS_COMPLETE);
        stateMachine.sendEvent(IssuesTransition.BEGIN_DEVELOPMENT);
        stateMachine.sendEvent(IssuesTransition.SEND_TO_QA);
        stateMachine.sendEvent(IssuesTransition.BEGIN_TESTING);
        stateMachine.sendEvent(IssuesTransition.QA_PASSED);
        assertThat(stateMachine.getState().getId()).isEqualTo(IssuesStates.READY_FOR_RELEASE);
    }

    @Test
    public void qaFailed() {
        stateMachine.sendEvent(IssuesTransition.CREATE);
        stateMachine.sendEvent(IssuesTransition.BEGIN_ANALYSIS);
        stateMachine.sendEvent(IssuesTransition.ANALYSIS_COMPLETE);
        stateMachine.sendEvent(IssuesTransition.BEGIN_DEVELOPMENT);
        stateMachine.sendEvent(IssuesTransition.SEND_TO_QA);
        stateMachine.sendEvent(IssuesTransition.BEGIN_TESTING);
        stateMachine.sendEvent(IssuesTransition.QA_FAILED);
        assertThat(stateMachine.getState().getId()).isEqualTo(IssuesStates.READY_FOR_DEV);
    }

    @Test
    public void released() {
        stateMachine.sendEvent(IssuesTransition.CREATE);
        stateMachine.sendEvent(IssuesTransition.BEGIN_ANALYSIS);
        stateMachine.sendEvent(IssuesTransition.ANALYSIS_COMPLETE);
        stateMachine.sendEvent(IssuesTransition.BEGIN_DEVELOPMENT);
        stateMachine.sendEvent(IssuesTransition.SEND_TO_QA);
        stateMachine.sendEvent(IssuesTransition.BEGIN_TESTING);
        stateMachine.sendEvent(IssuesTransition.QA_PASSED);
        stateMachine.sendEvent(IssuesTransition.RELEASED);
        assertThat(stateMachine.getState().getId()).isEqualTo(IssuesStates.RESOLVED);
    }

    @Test
    public void reopen() {
        stateMachine.sendEvent(IssuesTransition.CREATE);
        stateMachine.sendEvent(IssuesTransition.BEGIN_ANALYSIS);
        stateMachine.sendEvent(IssuesTransition.ANALYSIS_COMPLETE);
        stateMachine.sendEvent(IssuesTransition.BEGIN_DEVELOPMENT);
        stateMachine.sendEvent(IssuesTransition.SEND_TO_QA);
        stateMachine.sendEvent(IssuesTransition.BEGIN_TESTING);
        stateMachine.sendEvent(IssuesTransition.QA_PASSED);
        stateMachine.sendEvent(IssuesTransition.RELEASED);
        stateMachine.sendEvent(IssuesTransition.REOPEN);
        assertThat(stateMachine.getState().getId()).isEqualTo(IssuesStates.BACKLOG);
    }
}