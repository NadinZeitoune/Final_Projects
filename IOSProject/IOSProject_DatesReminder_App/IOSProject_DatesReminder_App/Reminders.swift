//
//  Reminders.swift
//  IOSProject_DatesReminder_App
//
//  Created by Nadin Zeitoune on 11/04/2019.
//

import Foundation
import EventKit

class Reminders {
    static let calendarTitle = "Calendar"
    static var eventStore: EKEventStore!
    static var event: Event!
    
    static private func addNewNotification() {
        if let cal = getCalendar(){
            // Greg alarm.
            if event.isNotifyG{
                let notification = createNotification(calendar: cal)
                
                // Save the notification.
                do{
                    try eventStore.save(notification, span: .futureEvents, commit: true)
                }catch{print("error: \(error)")}
            }
        }
    }
    
    static private func createNotification(calendar: EKCalendar) -> EKEvent{
        let notification = EKEvent(eventStore: eventStore)
        notification.title = event.dateType == .wedding ? "\(event.names[0]) & \(event.names[1]) Wedding day" : "\(event.names[0]) \(event.dateType.rawValue)"
        notification.calendar = calendar
        notification.isAllDay = true
        notification.startDate = event.gregorianDate
        notification.endDate = event.gregorianDate
        notification.notes = "Gregorian date"
        let alarm = EKAlarm(relativeOffset: 3600 * 9) // 9:00 AM on the event day
        notification.addAlarm(alarm)
        
        // Add recurrency to the notification.
        notification.addRecurrenceRule(EKRecurrenceRule(recurrenceWith: .yearly, interval: 1, end: nil))
        
        return notification
    }
    
    static private func getEventTitle() -> String{
        var title = event.names[0]
        if event.names.count == 2 {
            title.append(" & \(event.names[1]) Wedding day")
        }else{
            title.append(" \(event.dateType.rawValue)")
        }
        return title
    }

    static private func deleteNotification() {
        fetchEvents(withTitle: getEventTitle())
    }
    
    static private func fetchEvents(withTitle title: String){
        if let cal = getCalendar(){
            let predicate = eventStore.predicateForEvents(withStart: self.event.gregorianDate, end: self.event.gregorianDate, calendars: [cal])
            DispatchQueue.global().async {
                self.eventStore.enumerateEvents(matching: predicate, using: { (event: EKEvent, stop: UnsafeMutablePointer<ObjCBool>) in
                    if let theTitle = event.title{
                        print(theTitle)
                        if theTitle == title{
                            do{
                                try eventStore.remove(event, span: .futureEvents, commit: true)
                            }catch{print("problem removing rec event")}
                        }
                    }
                })
            }
        }
    }
    
    static func confirmAccess(forAdding isAdd:Bool){
        eventStore = EKEventStore()
        eventStore.requestAccess(to: .event) { (permitted: Bool, error:Error?) in
            if error == nil{
                if permitted{
                    if isAdd{
                        addNewNotification()
                    }else{
                        deleteNotification()
                    }
                }else{
                    print("access not permitted")
                }
            }else{
                print("we have access error: \(error!)")
            }
        }
    }
    
    static func getCalendar() ->EKCalendar?{
        let calendars:[EKCalendar] = eventStore.calendars(for: .event)
        var calendar:EKCalendar? = nil
        for cal in calendars{
            if cal.title == calendarTitle{
                calendar = cal
                break
            }
        }
        return calendar
    }
}
