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
    public void firstStepTest() {
        stateMachine.sendEvent(IssuesTransition.CREATE);
        assertThat(stateMachine.getState().getId()).isEqualTo(IssuesStates.BACKLOG);
    }
}