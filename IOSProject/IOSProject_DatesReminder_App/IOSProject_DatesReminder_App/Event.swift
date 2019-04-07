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
    let delimiter = "&"
    
    var dateType: DateType!
    var names: [String] = ["",""]
    var gregorianDate: Date!
    var hebrewDate: Date!
    var personType: PersonType!
    var isNotifyH: Bool = false
    var isNotifyG: Bool = false
    var month: Int!
    //var yearsPass: Int = 0 // On hold
    
    convenience init(eventAsString: String) {
        self.init()
        // Depart string to subStrings.
        var parts = eventAsString.components(separatedBy: delimiter)
        
        if parts.count != 8 || parts.count != 9 {
            return
        }
        
        // Get dateType.
        self.dateType = Event.dateTypes[Int(parts[0])!]
        
        // Get personType.
        self.personType = Event.personTypes[Int(parts[1])!]
        
        // Get gregorianDate.
        self.gregorianDate = Date(timeIntervalSince1970: TimeInterval(exactly: Double(parts[2])!)!)
        
        // Get hebrewDate.
        self.hebrewDate = Date(timeIntervalSince1970: TimeInterval(exactly: Double(parts[3])!)!)
        
        // Get month.
        self.month = Int(parts[4])!
        
        // Get isNotifyG.
        self.isNotifyG = parts[5] == "1" ? true : false
        
        // Get isNotifyH.
        self.isNotifyH = parts[6] == "1" ? true : false
        
        // Get first name.
        self.names[0] = parts[7]
        
        // Check and get second name (if needed).
        if parts.count == 9 {
            self.names[1] = parts[8]
        }else{
            self.names.removeLast()
        }
    }
    
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
    
    // On hold - // Get the years pass.
    
    // Get event as String.
    func toString() -> String{
        var str: String = ""
        for i in 0 ..< Event.dateTypes.count {
            if self.dateType.rawValue == Event.dateTypes[i].rawValue{
                str.append("\(i)\(delimiter)")
                break
            }
        }
        for i in 0 ..< Event.personTypes.count {
            if self.personType.rawValue == Event.personTypes[i].rawValue{
                str.append("\(i)\(delimiter)")
                break
            }
        }
        
        str.append("\(self.gregorianDate.timeIntervalSince1970.binade)\(delimiter)")
        str.append("\(self.hebrewDate.timeIntervalSince1970.binade)\(delimiter)")
        str.append("\(self.month)&")
        str.append("\(self.isNotifyG ? 1 : 0)\(delimiter)")
        str.append("\(self.isNotifyH ? 1 : 0)\(delimiter)")
        str.append("\(self.names[0])")
        if self.names.count == 2 {
            str.append("\(delimiter)\(self.names[1])")
        }
        
        return str
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
