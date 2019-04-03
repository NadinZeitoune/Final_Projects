//
//  Event.swift
//  IOSProject_DatesReminder_App
//
//  Created by hackeru on 04/03/2019.
//  Copyright © 2019 hackeru. All rights reserved.
//

import Foundation

class Event {
    static let hebMonthStrings = ["Tishrei", "Cheshvan", "Kislev", "Tevet", "Shvat", "Adar1", "Adar2", "Nisan", "Iyyar", "Sivan", "Tamuz", "Av", "Elul"]
    static let dateTypes: [DateType] = [DateType.birthday, DateType.wedding, DateType.deathDay, DateType.anniversary]
    static let personTypes: [PersonType] = [PersonType.family, PersonType.friends, PersonType.coWorkers, PersonType.differant]
    
    var dateType: DateType!
    var names: [String] = ["",""]
    var gregorianDate: Date!
    var hebrewDate: Date!
    var personType: PersonType!
    var isNotifyH: Bool = false
    var isNotifyG: Bool = false
    var month: Int!
    var yearsPass: Int = 0
    
    
    
    // Get the string of the hebrew month.
    func getHebMonthString() -> String {
        let dateFormmater = DateFormatter()
        dateFormmater.calendar = Calendar.init(identifier: .hebrew)
        dateFormmater.dateFormat = "MMMM"
        return dateFormmater.string(from: hebrewDate)
    }
    
    func createDateFromDictionary(_ dictionary: [String:Any], PutInGreg toGreg:Bool){
        //create Date from DateComponents:
        let calendar = Calendar.init(identifier: toGreg ? .gregorian : .hebrew)
        var components = DateComponents()
        if toGreg {
            components.day = dictionary["gd"] as! Int
            components.month = dictionary["gm"] as! Int
            components.year = dictionary["gy"] as! Int
            self.gregorianDate = calendar.date(from: components)
        }
        else{
            // Get the number of the month.
            for i in 0 ..< Event.hebMonthStrings.count{
                if Event.hebMonthStrings[i] == dictionary["hm"] as! String{
                    components.month = i + 1
                    break
                }
            }
            
            components.day = dictionary["hd"] as! Int
            components.year = dictionary["hy"] as! Int
            self.hebrewDate = calendar.date(from: components)
        }
    }
    
    // Get the month of the event.
    func getMonth(){
        let date = self.gregorianDate
        let calendar = Calendar.init(identifier: .gregorian)
        let components = calendar.dateComponents([Calendar.Component.month], from: date!)
        self.month = components.month
    }
    
    // Get the years pass.
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
