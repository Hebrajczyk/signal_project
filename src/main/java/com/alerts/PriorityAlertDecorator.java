package com.alerts;

/**
 * Decorator that adds priority information to an alert
 */
public class PriorityAlertDecorator extends AlertDecorator {
    private final String priority;

    public PriorityAlertDecorator(AlertInterface decoratedAlert, String priority) {
        super(decoratedAlert);
        this.priority = priority;
    }

    @Override
    public String getCondition() {
        return decoratedAlert.getCondition() + " [Priority: " + priority + "]";
    }
}
