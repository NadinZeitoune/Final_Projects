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
                let notification = createNotification(toGreg: true, calendar: cal)
                
                // Save the notification.
                do{
                    try eventStore.save(notification, span: .futureEvents, commit: true)
                }catch{print("error: \(error)")}
            }
            
            // Heb alarm.
            if event.isNotifyH{
                let notification = createNotification(toGreg: false, calendar: cal)
                
                // Save the notification.
                do{
                    try eventStore.save(notification, span: .futureEvents, commit: true)
                }catch{print("error: \(error)")}
            }
        }
    }
    
    static func createNotification(toGreg: Bool, calendar: EKCalendar) -> EKEvent{
        let notification = EKEvent(eventStore: eventStore)
        notification.title = event.dateType == .wedding ? "\(event.names[0]) & \(event.names[1]) wedding day" : "\(event.names[0]) \(event.dateType.rawValue)"
        notification.notes = ""
        notification.calendar = calendar
        notification.isAllDay = true
        notification.startDate = toGreg ? event.gregorianDate : event.hebrewDate
        notification.endDate = toGreg ? event.gregorianDate : event.hebrewDate
        
        let alarm = EKAlarm(relativeOffset: 3600 * 9) // 9:00 AM on the event day
        notification.addAlarm(alarm)
        
        // Add recurrency to the notification.
        notification.addRecurrenceRule(EKRecurrenceRule(recurrenceWith: .yearly, interval: 1, end: nil))
        
        return notification
    }
    
    static private func deleteNotification() {
        
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
