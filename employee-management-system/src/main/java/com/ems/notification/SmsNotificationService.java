package com.ems.notification;

import com.ems.model.Employee;

public class SmsNotificationService implements NotificationService {

    @Override
    public void sendNotification(Employee employee, String message) {
        System.out.printf(
                "[SMS to %s <%s>] %s%n",
                employee.getName(), employee.getPhone(), message
        );
    }
}
