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
    // var gregorianDate:
    // var hebrewDate:
    var personType: PersonType!
    var isNotifyH: Bool = false
    var isNotifyG: Bool = false
    var yearsPass: Int = 0
    // nextGregorianDate
    // nextHebrewDate
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
