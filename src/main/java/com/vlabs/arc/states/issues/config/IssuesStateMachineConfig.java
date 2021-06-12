package com.vlabs.arc.states.issues.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

import java.util.Optional;


@Slf4j
@EnableStateMachineFactory(name = "issuesStateMachineFactory")
@Configuration
public class IssuesStateMachineConfig extends EnumStateMachineConfigurerAdapter<IssuesStates, IssuesTransition> {

    @Autowired
    public ApplicationContext applicationContext;

    @Override
    public void configure(StateMachineConfigurationConfigurer<IssuesStates, IssuesTransition> config) throws Exception {
        config.withConfiguration()
                .listener(listener())
                .autoStartup(true);
    }

    private StateMachineListener<IssuesStates, IssuesTransition> listener() {
        return new StateMachineListenerAdapter<>() {
            @Override
            public void transition(Transition<IssuesStates, IssuesTransition> transition) {
                log.warn("move from :: {} to {}",
                        ofNullableState(transition.getSource()),
                        ofNullableState(transition.getTarget()));
            }

            private Object ofNullableState(State state) {
                return Optional.ofNullable(state)
                        .map(State::getId)
                        .orElse(null);
            }


            @Override
            public void eventNotAccepted(Message<IssuesTransition> event) {
                log.error("event not accepted :: {}", event);
            }
        };
    }

    @Override
    public void configure(StateMachineStateConfigurer<IssuesStates, IssuesTransition> states) throws Exception {
        states.withStates()
                .initial(IssuesStates.START)
                .state(IssuesStates.BACKLOG)
                .state(IssuesStates.ANALYSIS)
                .state(IssuesStates.READY_FOR_DEV)
                .state(IssuesStates.IN_DEVELOPMENT)
                .state(IssuesStates.READY_FOR_QA)
                .state(IssuesStates.IN_QA)
                .state(IssuesStates.READY_FOR_RELEASE)
                .state(IssuesStates.RESOLVED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<IssuesStates, IssuesTransition> transitions) throws Exception {
        transitions.withExternal()
                .event(IssuesTransition.CREATE)
                .source(IssuesStates.START)
                .target(IssuesStates.BACKLOG)
                .action(createIssue())

                .and()

                .withExternal()
                .event(IssuesTransition.BEGIN_ANALYSIS)
                .source(IssuesStates.BACKLOG)
                .target(IssuesStates.ANALYSIS)
                .action(beginAnalysis())

                .and()

                .withExternal()
                .event(IssuesTransition.FAST_TRACK_TO_DEV)
                .source(IssuesStates.BACKLOG)
                .target(IssuesStates.IN_DEVELOPMENT)
                .action(fastTrackToDev())

                .and()

                .withExternal()
                .event(IssuesTransition.RETURN_TO_BACKLOG)
                .source(IssuesStates.ANALYSIS)
                .target(IssuesStates.BACKLOG)
                .action(returnToBacklog())

                .and()

                .withExternal()
                .event(IssuesTransition.ANALYSIS_COMPLETE)
                .source(IssuesStates.ANALYSIS)
                .target(IssuesStates.READY_FOR_DEV)
                .action(analysisComplete())

                .and()

                .withExternal()
                .event(IssuesTransition.NEED_MORE_INFO)
                .source(IssuesStates.READY_FOR_DEV)
                .target(IssuesStates.ANALYSIS)
                .action(needMoreInfo())

                .and()

                .withExternal()
                .event(IssuesTransition.BEGIN_DEVELOPMENT)
                .source(IssuesStates.READY_FOR_DEV)
                .target(IssuesStates.IN_DEVELOPMENT)
                .action(beginDevelopment())

                .and()

                .withExternal()
                .event(IssuesTransition.STOP_DEVELOPMENT)
                .source(IssuesStates.IN_DEVELOPMENT)
                .target(IssuesStates.READY_FOR_DEV)
                .action(stopDevelopment())

                .and()

                .withExternal()
                .event(IssuesTransition.FAST_TRACK_TO_RELEASE)
                .source(IssuesStates.IN_DEVELOPMENT)
                .target(IssuesStates.READY_FOR_RELEASE)
                .action(fastTrackToRelease())

                .and()

                .withExternal()
                .event(IssuesTransition.SEND_TO_QA)
                .source(IssuesStates.IN_DEVELOPMENT)
                .target(IssuesStates.READY_FOR_QA)
                .action(sendToQA())

                .and()

                .withExternal()
                .event(IssuesTransition.BEGIN_TESTING)
                .source(IssuesStates.READY_FOR_QA)
                .target(IssuesStates.IN_QA)
                .action(beginTesting())

                .and()

                .withExternal()
                .event(IssuesTransition.QA_PASSED)
                .source(IssuesStates.IN_QA)
                .target(IssuesStates.READY_FOR_RELEASE)
                .action(qaPassed())

                .and()

                .withExternal()
                .event(IssuesTransition.QA_FAILED)
                .source(IssuesStates.IN_QA)
                .target(IssuesStates.READY_FOR_DEV)
                .action(qaFailed())

                .and()

                .withExternal()
                .event(IssuesTransition.RELEASED)
                .source(IssuesStates.READY_FOR_RELEASE)
                .target(IssuesStates.RESOLVED)
                .action(released())

                .and()

                .withExternal()
                .event(IssuesTransition.REOPEN)
                .source(IssuesStates.RESOLVED)
                .target(IssuesStates.BACKLOG)
                .action(reopen());
    }

    private Action<IssuesStates, IssuesTransition> reopen() {
        return null;
    }

    private Action<IssuesStates, IssuesTransition> released() {
        return null;
    }

    private Action<IssuesStates, IssuesTransition> qaFailed() {
        return null;
    }

    private Action<IssuesStates, IssuesTransition> qaPassed() {
        return null;
    }

    private Action<IssuesStates, IssuesTransition> beginTesting() {
        return null;
    }

    private Action<IssuesStates, IssuesTransition> sendToQA() {
        return null;
    }

    private Action<IssuesStates, IssuesTransition> fastTrackToRelease() {
        return null;
    }

    private Action<IssuesStates, IssuesTransition> stopDevelopment() {
        return null;
    }

    private Action<IssuesStates, IssuesTransition> beginDevelopment() {
        return null;
    }

    private Action<IssuesStates, IssuesTransition> needMoreInfo() {
        return null;
    }

    private Action<IssuesStates, IssuesTransition> analysisComplete() {
        return null;
    }

    private Action<IssuesStates, IssuesTransition> returnToBacklog() {
        return null;
    }

    private Action<IssuesStates, IssuesTransition> fastTrackToDev() {
        return null;
    }

    private Action<IssuesStates, IssuesTransition> beginAnalysis() {
        return null;
    }

    private Action<IssuesStates, IssuesTransition> createIssue() {
        return null;
    }
}
