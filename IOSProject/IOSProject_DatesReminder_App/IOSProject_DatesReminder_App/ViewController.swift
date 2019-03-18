//
//  ViewController.swift
//  IOSProject_DatesReminder_App
//
//  Created by hackeru on 04/03/2019.
//  Copyright © 2019 hackeru. All rights reserved.
//

import UIKit

class ViewController: UIViewController{
    
    var datesList: UITableView!
    var addDateBtn: UIButton!
    var datesDataSource: DatesDataSource!

    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = UIColor.white
        
        // If there is a file- load data for the tableView(global) and put it in the array.
        datesDataSource = DatesDataSource()
        initScreen()
    }

    func initScreen(){
        // Title:
        let title = UILabel(frame: CGRect(x: 0, y: 20, width: 0, height: 0))
        title.text = "Forever Board"
        title.font = UIFont.boldSystemFont(ofSize: 35)
        title.sizeToFit()
        title.adjustsFontSizeToFitWidth = true
        title.center.x = view.center.x
        view.addSubview(title)
     
        // Create the table view:
        datesList = UITableView(frame: CGRect(x: 0, y: title.frame.maxY + 10, width: view.frame.width, height: view.frame.height - title.frame.height - 10), style: .grouped)
        datesList.sectionFooterHeight = 0
        datesList.dataSource = datesDataSource
        //datesList.delegate =
        view.addSubview(datesList)
        
        // Create add btn:
        addDateBtn = UIButton(type: .contactAdd)
        addDateBtn.frame = CGRect(x: view.frame.maxX - 30, y: title.frame.maxY + 10, width: 30, height: 30)
        addDateBtn.tintColor = UIColor.darkText
        addDateBtn.addTarget(self, action: #selector(handleAddBtnClick(sender:)), for: .touchUpInside)
        view.addSubview(addDateBtn)
    }
    
    @objc func handleAddBtnClick(sender: UIButton){
        present(AddViewController(), animated: true, completion: nil)
    }
}

