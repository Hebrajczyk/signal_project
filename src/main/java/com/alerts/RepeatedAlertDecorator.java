package com.alerts;

/**
 * Decorator that marks an alert as repeated.
 */
public class RepeatedAlertDecorator extends AlertDecorator {

    public RepeatedAlertDecorator(AlertInterface decoratedAlert) {
        super(decoratedAlert);
    }

    @Override
    public String getCondition() {
        return decoratedAlert.getCondition() + " [Repeated]";
    }
}
