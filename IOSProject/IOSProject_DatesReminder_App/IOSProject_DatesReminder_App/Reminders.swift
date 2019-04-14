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
                
                // Change the event start date.
                fetchEvents(withTitle: getEventTitle(), delete: false)
            }
        }
    }
    
    static private func createNotification(toGreg: Bool, calendar: EKCalendar) -> EKEvent{
        let notification = EKEvent(eventStore: eventStore)
        notification.title = event.dateType == .wedding ? "\(event.names[0]) & \(event.names[1]) Wedding day" : "\(event.names[0]) \(event.dateType.rawValue)"
        notification.notes = toGreg ? "Gregorian date" : "Hebrew date"
        notification.calendar = calendar
        notification.isAllDay = true
        notification.startDate = toGreg ? event.gregorianDate : event.hebrewDate
        notification.endDate = toGreg ? event.gregorianDate : event.hebrewDate
        notification.notes = toGreg ? "Gregorian date" : "Hebrew date"
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
        fetchEvents(withTitle: getEventTitle(), delete: true)
    }
    
    static private func fetchEvents(withTitle title: String, delete: Bool){
        if let cal = getCalendar(){
            let greg = Calendar(identifier: .gregorian)
            let today = Date()
            let startDate = greg.date(byAdding: DateComponents(month: -2), to: event.gregorianDate)!
            let endDate = greg.date(byAdding: DateComponents(year: 4), to: today)!
            let predicate = eventStore.predicateForEvents(withStart: startDate, end: endDate, calendars: [cal])
            DispatchQueue.global().async {
                var eventSN = 0
                var events = self.eventStore.events(matching: predicate)
                for i in 0 ..< events.count{
                    if let theTitle = events[i].title{
                        if theTitle == title{
                            if delete{
                                do{
                                    try eventStore.remove(events[i], span: .thisEvent, commit: true)
                                }catch{print("problem removing rec event")}
                            }else{
                                if events[i].notes == "Hebrew date"{
                                    // Change start date for the event.
                                    events[i].startDate = self.event.getHebrewDateAfter(years: eventSN)
                                    events[i].endDate = self.event.getHebrewDateAfter(years: eventSN)
                                    
                                    // Save changes.
                                    do{
                                        try eventStore.save(events[i], span: .thisEvent, commit: true)
                                    }catch{print("problem saving changes")}
                                }
                                print("\(events[i].title) \(eventSN)")
                                eventSN += 1
                            }
                        }
                    }
                }
                /*self.eventStore.enumerateEvents(matching: predicate, using: { (event: EKEvent, stop: UnsafeMutablePointer<ObjCBool>) in
                    if let theTitle = event.title{
                        if theTitle == title{
                            if delete{
                                do{
                                    try eventStore.remove(event, span: .thisEvent, commit: true)
                                }catch{print("problem removing rec event")}
                            }else{
                                if event.notes == "Hebrew date"{
                                    // Change start date for the event.
                                    event.startDate = self.event.getHebrewDateAfter(years: eventSN)
                                    event.endDate = self.event.getHebrewDateAfter(years: eventSN)
                                    
                                    // Save changes.
                                    do{
                                        try eventStore.save(event, span: .thisEvent, commit: true)
                                    }catch{print("problem saving changes")}
                                }
                                print("\(event.title) \(eventSN)")
                                eventSN += 1                                
                            }
                        }
                    }
                })*/
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
