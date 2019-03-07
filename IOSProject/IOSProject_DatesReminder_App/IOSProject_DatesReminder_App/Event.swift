//
//  Event.swift
//  IOSProject_DatesReminder_App
//
//  Created by hackeru on 04/03/2019.
//  Copyright © 2019 hackeru. All rights reserved.
//

import Foundation

class Event {
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
    case birthday = "balloons"
    case wedding = "wedding"
    case deathDay = "tombstone"
    case anniversary = "calendar" // == Else?
}

enum PersonType: String{
    case family = "family"
    case friends = "friends"
    case coWorkers = "coWorkers"
    case differant = "differant"
}
