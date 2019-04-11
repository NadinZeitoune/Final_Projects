//
//  ShowViewController.swift
//  IOSProject_DatesReminder_App
//
//  Created by Nadin Zeitoune on 10/04/2019.
//

import UIKit

class ShowViewController: UIViewController {
    let margin: CGFloat = 20
    
    weak var viewController: ViewController!
    var event: Event!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        view.backgroundColor = UIColor.white
        
        initButtons()
        
        initEvent()
    }
    
    func initEvent(){
        // Names:
        let title = UILabel(frame: CGRect(x: 0, y: 10, width: 1, height: 35))
        title.text = event.dateType == .wedding ? "\(event.names[0]) & \(event.names[1])" : "\(event.names[0])"
        title.font = UIFont.boldSystemFont(ofSize: 35)
        title.sizeToFit()
        title.adjustsFontSizeToFitWidth = true
        title.center.x = view.center.x
        view.addSubview(title)
        
        // Type:
        let type = createLabel(WithTitle: "Date type: \(event.dateType.rawValue)", Underlabel: title)
        view.addSubview(type)
        
        // Person:
        let person = createLabel(WithTitle: "Person type: \(event.personType.rawValue)", Underlabel: type)
        view.addSubview(person)
        
        // Dates:
        let dateG = createLabel(WithTitle: "Gregorian: \(DatesDataSource.generateStringFromDate(event.gregorianDate, WithCalendar: .init(identifier: .gregorian)))", Underlabel: person)
        view.addSubview(dateG)
        
        let dateH = createLabel(WithTitle: "Hebrew: \(DatesDataSource.generateStringFromDate(event.hebrewDate, WithCalendar: .init(identifier: .hebrew)))", Underlabel: dateG)
        view.addSubview(dateH)
        
        // Does notify (Yes / No):
        let notifyG = createLabel(WithTitle: "Gregorian notification: \(event.isNotifyG ? "ON" : "OFF")", Underlabel: dateH)
        view.addSubview(notifyG)
        
        let notifyH = createLabel(WithTitle: "Hebrew notification: \(event.isNotifyH ? "ON" : "OFF")", Underlabel: notifyG)
        view.addSubview(notifyH)
        
        let pic = UIImageView(frame: CGRect(x: 0, y: 0, width: 150, height: 150))
        pic.frame.origin.y = view.frame.maxY - pic.frame.height
        pic.center.x = view.center.x
        pic.image = UIImage(named: event.dateType.rawValue)
        view.addSubview(pic)
    }
    
    func createLabel(WithTitle title: String, Underlabel upLabel:UILabel) -> UILabel{
        let label = UILabel(frame: CGRect(x: 0, y: 0, width: 0, height: 30))
        label.text = title
        label.font = UIFont.boldSystemFont(ofSize: 25)
        label.sizeToFit()
        label.adjustsFontSizeToFitWidth = true
        label.frame.origin.y = upLabel.frame.maxY + margin
        return label
    }
    
    func initButtons() {
        // Exit Btn.
        let exitBtn = UIButton(type: .system)
        exitBtn.frame = CGRect(x: 0, y: 0, width: 25, height: 25)
        exitBtn.setTitle("X", for: .normal)
        exitBtn.titleLabel!.font = UIFont.boldSystemFont(ofSize: 25)
        exitBtn.frame.origin.x = view.frame.maxX - exitBtn.frame.width
        exitBtn.frame.origin.y = exitBtn.frame.height
        exitBtn.addTarget(self, action: #selector(handleExitBtnClick(sender:)), for: .touchUpInside)
        view.addSubview(exitBtn)
        
        // DeleteBtn.
        let deleteBtn = UIButton(type: .system)
        deleteBtn.frame = CGRect(x: 0, y: 0, width: 80, height: 30)
        deleteBtn.setTitle("Delete", for: .normal)
        deleteBtn.titleLabel!.font = UIFont.boldSystemFont(ofSize: 26)
        deleteBtn.frame.origin.y = view.frame.maxY - deleteBtn.frame.height * 1.5
        deleteBtn.addTarget(self, action: #selector(handleDeleteBtnClick(sender:)), for: .touchUpInside)
        view.addSubview(deleteBtn)
        
        // EditBtn.
        let editBtn = UIButton(type: .system)
        editBtn.frame = CGRect(x: 0, y: 0, width: 80, height: 30)
        editBtn.setTitle("Edit", for: .normal)
        editBtn.titleLabel!.font = UIFont.boldSystemFont(ofSize: 28)
        editBtn.frame.origin.x = view.frame.maxX - editBtn.frame.width
        editBtn.frame.origin.y = view.frame.maxY - editBtn.frame.height * 1.5
        editBtn.addTarget(self, action: #selector(handleEditBtnClick(sender:)), for: .touchUpInside)
        view.addSubview(editBtn)
    }
    
    @objc func handleEditBtnClick(sender: UIButton){
        // Open the add contorller with event.
        dismiss(animated: true, completion: nil)
        self.viewController.openAddViewController(EditEvent: true, With: self.event)
    }
    
    @objc func handleDeleteBtnClick(sender: UIButton){
        // Add confirmation alert.
        let confirmationAlert = UIAlertController(title: "Are you sure?", message: "Deletion cannot be undone!", preferredStyle: .alert)
         
         let delete = UIAlertAction(title: "Yes", style: .destructive, handler: { (action: UIAlertAction) in
            self.dismiss(animated: true, completion: nil)
            self.viewController.deleteEvent()
         })
         confirmationAlert.addAction(delete)
         
         let cancel = UIAlertAction(title: "No", style: .cancel, handler: nil)
         confirmationAlert.addAction(cancel)
        
        present(confirmationAlert, animated: true, completion: nil)
    }
    
    @objc func handleExitBtnClick(sender: UIButton){
        dismiss(animated: true, completion: nil)
    }
}
