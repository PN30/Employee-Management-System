package com.ems.notification;

import com.ems.model.Employee;

public interface NotificationService {
    void sendNotification(Employee employee, String message);
}
