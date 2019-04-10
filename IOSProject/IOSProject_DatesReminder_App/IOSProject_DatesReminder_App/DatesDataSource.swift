//
//  DatesDataSource.swift
//  IOSProject_DatesReminder_App
//
//  Created by hackeru on 07/03/2019.
//  Copyright © 2019 hackeru. All rights reserved.
//

import UIKit

class DatesDataSource: NSObject, UITableViewDataSource{
    // !! 2D array??
    let months: [String] = ["January","February","March","April","May","June","July","August","September","October","November","December"]
    
    static var dates: [[Event]] = [[], [], [], [], [], [], [], [], [], [], [], []]
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return months.count
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        return DatesDataSource.dates[section].count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        var cell = tableView.dequeueReusableCell(withIdentifier: "datesList", for: indexPath)
        
        // Make space for more details.
        if cell.detailTextLabel == nil{
            cell = UITableViewCell(style: .value1, reuseIdentifier: "datesList")
        }
        
        let event = DatesDataSource.dates[indexPath.section][indexPath.row]
        
        // Add the name title.
        if event.dateType == .wedding && event.names.count != 1 && !event.names[1].isEmpty{
            cell.textLabel!.text = "\(event.names[0]) & \(event.names[1])"
        }else{
            cell.textLabel!.text = event.names[0]
        }
        
        // Add image.
        let img = UIImage(named: event.dateType.rawValue)
        cell.imageView!.image = img
        
        // Add date.
        cell.detailTextLabel!.text = DatesDataSource.generateStringFromDate(event.gregorianDate, WithCalendar: Calendar(identifier: .gregorian))
        
        return cell
    }
    
    static func generateStringFromDate(_ date: Date, WithCalendar calendar: Calendar) -> String{
        let formatter = DateFormatter()
        formatter.dateStyle = DateFormatter.Style.short
        formatter.calendar = calendar
        return formatter.string(from: date)
    }
    
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        return months[section]
    }
}
