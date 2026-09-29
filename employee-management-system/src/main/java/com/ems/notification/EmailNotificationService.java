package com.ems.notification;

import com.ems.model.Employee;

public class EmailNotificationService implements NotificationService {

    @Override
    public void sendNotification(Employee employee, String message) {
        System.out.printf(
                "[EMAIL to %s <%s>] %s%n",
                employee.getName(), employee.getEmail(), message
        );
    }
}
