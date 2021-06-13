package com.vlabs.arc.states.issues.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
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
class IssuesStateMachineConfigTest {

    @Qualifier("issuesStateMachineFactory")
    @Autowired
    private StateMachineFactory<IssuesStates, IssuesTransition> stateMachineFactory;

    private StateMachine<IssuesStates, IssuesTransition> stateMachine;

    @BeforeEach
    void setUp() {
        stateMachine = stateMachineFactory.getStateMachine();
    }

    @AfterEach
    void tearDown() {
        stateMachine = null;
        stateMachineFactory = null;
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
    public void firstStepTest() {
        stateMachine.sendEvent(IssuesTransition.CREATE);
        assertThat(stateMachine.getState().getId()).isEqualTo(IssuesStates.BACKLOG);
    }
}