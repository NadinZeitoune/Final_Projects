//
//  DatesDataSource.swift
//  IOSProject_DatesReminder_App
//
//  Created by hackeru on 07/03/2019.
//  Copyright © 2019 hackeru. All rights reserved.
//

import UIKit

class DatesDataSource: NSObject, UITableViewDataSource {
    
    let months: [String] = ["January","February","March","April","May","June","July","August","September","October","November","December"]
    
    var dates: [Event]!
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 12
    }
    
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // check the number of dates belong to this month
        return 5
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        //
        return UITableViewCell(frame: CGRect(x: 0, y: 0, width: 5, height: 5))
    }
    
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        return months[section]
    }
}
