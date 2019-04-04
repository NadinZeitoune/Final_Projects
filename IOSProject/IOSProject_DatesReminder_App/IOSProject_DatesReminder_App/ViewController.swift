//
//  ViewController.swift
//  IOSProject_DatesReminder_App
//
//  Created by hackeru on 04/03/2019.
//  Copyright © 2019 hackeru. All rights reserved.
//

import UIKit

class ViewController: UIViewController, UITableViewDelegate{
    
    var datesList: UITableView!
    var addDateBtn: UIButton!
    var datesDataSource: DatesDataSource!
    var actionSheet: UIAlertController!

    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = UIColor.white
        
        // If there is a file- load data for the tableView(global) and put it in the array.
        datesDataSource = DatesDataSource()
        initScreen()
        createActionSheet()
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
        datesList.delegate = self
        datesList.register(UITableViewCell.classForCoder(), forCellReuseIdentifier: "datesList")
        view.addSubview(datesList)
        
        // Create add btn:
        addDateBtn = UIButton(type: .contactAdd)
        addDateBtn.frame = CGRect(x: view.frame.maxX - 30, y: title.frame.maxY + 10, width: 30, height: 30)
        addDateBtn.tintColor = UIColor.darkText
        addDateBtn.addTarget(self, action: #selector(handleAddBtnClick(sender:)), for: .touchUpInside)
        view.addSubview(addDateBtn)
    }
    
    func createActionSheet(){
        actionSheet = UIAlertController(title: "Please choose what to do", message: nil, preferredStyle: .actionSheet)
        
        let actionCancel = UIAlertAction(title: "Cancel", style: .cancel, handler: nil)
        actionSheet.addAction(actionCancel)
        
        let actionShow = UIAlertAction(title: "Show event", style: .default) { (action: UIAlertAction) in // !!
            // Open new screen with the details shows nice.
            // Edit btn -> open exactly the same as actionEdit.
            // Delete btn -> exactly as actionDelete.
        }
        actionSheet.addAction(actionShow)
        
        let actionEdit = UIAlertAction(title: "Edit event", style: .default) { (action: UIAlertAction) in // !!
            // Open editing screen with all the details
            // Exit btn -> delete changes
            // Save btn -> Save details (new func: change calendar, save changes to list, refresh data source)
        }
        actionSheet.addAction(actionEdit)
        
        let actionDelete = UIAlertAction(title: "Delete event", style: .destructive) { (action: UIAlertAction) in
            
            let confirmationAlert = UIAlertController(title: "Are you sure?", message: "Deletion cannot be undone!", preferredStyle: .alert)
            
            let delete = UIAlertAction(title: "Yes", style: .destructive, handler: { (action: UIAlertAction) in //!!
                // Delete the reminder from the calendar
                // Delete the event from the list
                // Refresh the data source
            })
            confirmationAlert.addAction(delete)
            
            let cancel = UIAlertAction(title: "No", style: .cancel, handler: nil)
            confirmationAlert.addAction(cancel)
            
            self.present(confirmationAlert, animated: true, completion: nil)
        }
        actionSheet.addAction(actionDelete)
    }
    
    @objc func handleAddBtnClick(sender: UIButton){
        let addContorller = AddViewController()
        addContorller.viewController = self
        present(addContorller, animated: true, completion: nil)
    }
    
    // TableViewDelagate:
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        // Open action sheet with three actions.
        present(actionSheet, animated: true, completion: nil)
    }
}

