//
//  Event.swift
//  IOSProject_DatesReminder_App
//
//  Created by hackeru on 04/03/2019.
//  Copyright © 2019 hackeru. All rights reserved.
//

import Foundation

class Event {
    static let dateTypes: [DateType] = [DateType.birthday, DateType.wedding, DateType.deathDay, DateType.anniversary]
    static let personTypes: [PersonType] = [PersonType.family, PersonType.friends, PersonType.coWorkers, PersonType.differant]
    
    var dateType: DateType!
    var names: [String] = ["",""]
    var gregorianDate: Date!
    var hebrewDate: Date!
    var personType: PersonType!
    var isNotifyH: Bool = false
    var isNotifyG: Bool = false
    var yearsPass: Int = 0
    
    /*let dateCurrent = datePick.date
     let calendar = Calendar.init(identifier: .hebrew)
     let components = calendar.dateComponents([Calendar.Component.day, Calendar.Component.month, Calendar.Component.year], from: dateCurrent)
     let datefor = DateFormatter()
     datefor.calendar = Calendar.init(identifier: .hebrew)
     datefor.dateFormat = "MMMM"
     print("Day:\(components.day!) Month:\(datefor.string(from: dateCurrent)) Year:\(components.year!)")*/
    
    // Get the string of the hebrew month.
    func getHebMonthString() -> String {
        let dateFormmater = DateFormatter()
        dateFormmater.calendar = Calendar.init(identifier: .hebrew)
        dateFormmater.dateFormat = "MMMM"
        return dateFormmater.string(from: hebrewDate)
    }
}

enum DateType: String{
    case birthday = "Birthday"
    case wedding = "Wedding"
    case deathDay = "DeathDay"
    case anniversary = "Anniversary" // == Else?
}

enum PersonType: String{
    case family = "Family"
    case friends = "Friends"
    case coWorkers = "Co-Workers"
    case differant = "Differant"
}
