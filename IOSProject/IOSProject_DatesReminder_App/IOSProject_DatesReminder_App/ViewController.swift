//
//  ViewController.swift
//  IOSProject_DatesReminder_App
//
//  Created by Nadin Zeitoune on 04/03/2019.
//

import UIKit

class ViewController: UIViewController, UITableViewDelegate{
    
    var datesList: UITableView!
    var addDateBtn: UIButton!
    var datesDataSource: DatesDataSource!
    var actionSheet: UIAlertController!
    var index: IndexPath!

    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = UIColor.white
        
        datesDataSource = DatesDataSource()
        
        // If there is a file- load data for the tableView and put it in the array.
        ViewController.loadFileData()
        
        initScreen()
        createActionSheet()
    }
    
    static func loadFileData(){
        let fileDirectory = try! FileManager().url(for: .documentDirectory, in: .userDomainMask, appropriateFor: nil, create: true).appendingPathComponent(AppDelegate.myFileName)
        
        if FileManager.default.fileExists(atPath: fileDirectory.path){
            do{
                let eventsString = try String(contentsOf: fileDirectory)
                
                // Depart the events.
                let events = eventsString.components(separatedBy: AppDelegate.delimiter)
                
                for i in 0 ..< events.count{
                    // Create and add event.
                    let event = Event(eventAsString: events[i])
                    DatesDataSource.dates[event.month - 1].append(event)
                }
            }catch{}
        }
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
            let showController = ShowViewController()
            showController.viewController = self
            showController.event = DatesDataSource.dates[self.index.section][self.index.row]
            self.present(showController, animated: true, completion: nil)
        }
        actionSheet.addAction(actionShow)
        
        let actionEdit = UIAlertAction(title: "Edit event", style: .default) { (action: UIAlertAction) in // !!
            // Open editing screen with all the details
            self.openAddViewController(EditEvent: true, With: DatesDataSource.dates[self.index.section][self.index.row])
        }
        actionSheet.addAction(actionEdit)
        
        let actionDelete = UIAlertAction(title: "Delete event", style: .destructive) { (action: UIAlertAction) in
            
            let confirmationAlert = UIAlertController(title: "Are you sure?", message: "Deletion cannot be undone!", preferredStyle: .alert)
            
            let delete = UIAlertAction(title: "Yes", style: .destructive, handler: { (action: UIAlertAction) in self.deleteEvent()})
            confirmationAlert.addAction(delete)
            
            let cancel = UIAlertAction(title: "No", style: .cancel, handler: nil)
            confirmationAlert.addAction(cancel)
            
            self.present(confirmationAlert, animated: true, completion: nil)
        }
        actionSheet.addAction(actionDelete)
    }
    
    func deleteEvent() {
        // Delete the reminder from the calendar
        Reminders.event = DatesDataSource.dates[self.index.section][self.index.row]
        Reminders.confirmAccess(forAdding: false)
        
        // Delete the event from the list.
        DatesDataSource.dates[self.index.section].remove(at: self.index.row)
        
        // Refresh the data source
        self.datesList.reloadData()
    }
    
    func openAddViewController(EditEvent toEdit: Bool, With event: Event?){
        let addContorller = AddViewController()
        addContorller.viewController = self
        addContorller.newEvent = toEdit ? event! : nil
        addContorller.toEdit = toEdit        
        present(addContorller, animated: true, completion: nil)
    }
    
    @objc func handleAddBtnClick(sender: UIButton){
        openAddViewController(EditEvent: false, With: nil)
    }
    
    // TableViewDelagate:
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        // Open action sheet with three actions.
        self.index = indexPath
        present(actionSheet, animated: true, completion: nil)
    }
}

