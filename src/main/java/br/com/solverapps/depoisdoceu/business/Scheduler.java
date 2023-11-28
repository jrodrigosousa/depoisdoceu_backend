package br.com.solverapps.depoisdoceu.business;

import br.com.solverapps.depoisdoceu.data.model.Message;
import br.com.solverapps.depoisdoceu.data.model.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Component
public class Scheduler {

    Map<Message, List<ScheduledFuture>> tasks = new HashMap<>();

    @Autowired
    org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler scheduler;

    public void schedule(Message message){
        List tasks = new ArrayList();
        ScheduledFuture task = scheduler.schedule(
                new SendMessage(message),
                message.getToSendDate().toInstant(ZoneOffset.of("-03:00")));
        tasks.add(task);
        for(Notification notification: message.getNotifications()){
            task = scheduler.schedule(
                    new SendNotification(notification),
                    notification.getToSendDate().toInstant(ZoneOffset.of("-03:00")));
            tasks.add(task);
        }
        this.tasks.put(message, tasks);
    }


    public void cancel(Message message){
        List<ScheduledFuture> tasks = this.tasks.get(message);
        if(tasks==null)
            return;
        for(ScheduledFuture task: tasks)
            task.cancel(false);
    }
}