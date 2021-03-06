//
//  DatesDataSource.swift
//  IOSProject_DatesReminder_App
//
//  Created by Nadin Zeitoune on 07/03/2019.
//

import UIKit

class DatesDataSource: NSObject, UITableViewDataSource{
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
    
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        return months[section]
    }
    
    static func generateStringFromDate(_ date: Date, WithCalendar calendar: Calendar) -> String{
        let formatter = DateFormatter()
        formatter.dateStyle = DateFormatter.Style.short
        formatter.calendar = calendar
        return formatter.string(from: date)
    }
    
    static func sortDatesArray(){
        // For every sub array - sort.
        for i in 0 ..< dates.count{
            if dates[i].count == 0{
                continue
            }
            
            // Sort:
            bubbleSort(arr: &dates[i]) { (event1: Event, event2: Event) -> Bool in
                if event1.getDay() >= event2.getDay(){
                    return true
                }else{
                    return false
                }
            }
        }
    }
    
    static func swap<T>(arr: inout[T], i: Int, j: Int) {
        let temp = arr[i]
        arr[i] = arr[j]
        arr[j] = temp
    }
    
    static func bubbleSort<T>(arr: inout[T], compare:(_ x: T, _ y: T) -> Bool) {
        var isSorted = false
        var upTo = arr.count - 1
        
        while !isSorted {
            isSorted = true
            
            for i in 0..<upTo {
                if compare(arr[i], arr[i + 1]) {
                    swap(arr: &arr, i:i, j: i + 1)
                    isSorted = false
                }
            }
            upTo -= 1
        }
    }
}
