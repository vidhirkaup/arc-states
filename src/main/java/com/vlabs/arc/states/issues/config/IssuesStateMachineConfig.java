package com.vlabs.arc.states.issues.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
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
@EnableStateMachineFactory(name = "issuesStateMachineConfig")
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
        super.configure(states);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<IssuesStates, IssuesTransition> transitions) throws Exception {
        super.configure(transitions);
    }
}
