//
//  AddViewController.swift
//  IOSProject_DatesReminder_App
//
//  Created by hackeru on 11/03/2019.
//  Copyright © 2019 hackeru. All rights reserved.
//

import UIKit

class AddViewController: UIViewController, UIPickerViewDataSource, UIPickerViewDelegate{
    
    let margin: CGFloat = 10
    
    var pickerContainer: UIView!
    var confirmBtn: UIButton!
    
    var dateType: UIButton!
    var isFirstTimeDatePickerShowed = true
    
    var namesView: UIView!
    var names: [UITextField] = []
    
    //var gregDate: ?כפתור רדיו עם צ׳ק בוקס
    //var hebDate: ?כפתור רדיו עם צ׳ק בוקס
    var personType: UIButton!
    // check boxs - does notify heb /+ greg ?

    var addEventBtn: UIButton!
    var newEvent: Event!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        initScreen()
        
        pickerContainer = UIView(frame: view.frame)
        
        createConfirmBtn()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
        // Open autometiclly the dateTypePicker
        handleDateTypeClick(sender: dateType)
    }
    
    //!!
    func initScreen() {
        view.backgroundColor = UIColor.white
        
        // Title:
        let title = UILabel(frame: CGRect(x: 0, y: 20, width: 0, height: 0))
        title.text = "Add event"
        title.font = UIFont.boldSystemFont(ofSize: 35)
        title.sizeToFit()
        title.adjustsFontSizeToFitWidth = true
        title.center.x = view.center.x
        view.addSubview(title)
        
        // Date type:
        dateType = UIButton(frame: CGRect(x: 0, y: title.frame.maxY + margin, width: view.frame.width, height: 30))
        
        let dateTitle = UILabel(frame: CGRect(x: 0, y: 0, width: 0, height: 30))
        dateTitle.text = "Date type: "
        dateTitle.font = UIFont.boldSystemFont(ofSize: 22)
        dateTitle.sizeToFit()
        dateTitle.adjustsFontSizeToFitWidth = true
        dateType.addTarget(self, action: #selector(handleDateTypeClick(sender:)), for: .touchUpInside)
        dateType.addSubview(dateTitle)
        
        let datePick = UILabel(frame: CGRect(x: dateTitle.frame.maxX + margin, y: 2, width: 0, height: 30))
        datePick.tag = 0
        dateType.addSubview(datePick)
        
        view.addSubview(dateType)
        
        // Names view:
        namesView = UIView(frame: CGRect(x: 0, y: dateType.frame.maxY + margin, width: view.frame.width, height: 30))
        
        let namesTitle = UILabel(frame: CGRect(x: 0, y: 0, width: 0, height: 30))
        namesTitle.text = "Name: "
        namesTitle.font = UIFont.boldSystemFont(ofSize: 22)
        namesTitle.sizeToFit()
        namesTitle.adjustsFontSizeToFitWidth = true
        namesView.addSubview(namesTitle)
        let firstName = UITextField(frame: CGRect(x: namesTitle.frame.maxX + margin, y: 0, width: (namesView.frame.width - namesTitle.frame.width) / 2.5, height: 30))
        names.append(firstName)
        
        // Event:
        newEvent = Event()
    }
    
    func createConfirmBtn() {
        // Create confirmation button:
        confirmBtn = UIButton(type: .system)
        confirmBtn.setTitle("Confirm", for: .normal)
        confirmBtn.addTarget(self, action: #selector(handleConfirmationBtnClick(sender:)), for: .touchUpInside)
    }
    
    func createPicker(forTag: Int, goToRow: Int) {
        let typePicker = UIPickerView()
        typePicker.center = view.center
        typePicker.dataSource = self
        typePicker.delegate = self
        typePicker.backgroundColor = UIColor.lightText
        typePicker.tag = forTag
        typePicker.selectRow(goToRow, inComponent: 0, animated: false)
        pickerContainer.addSubview(typePicker)
        
        confirmBtn.frame = CGRect(x: 0, y: typePicker.frame.maxY, width: view.frame.width, height: 30)
        pickerContainer.addSubview(confirmBtn)
        view.addSubview(pickerContainer)
    }
    
    //!!
    func initRestScreen() {
        // Change amount of name text fields acoording to the date type.
        changeNamesCount()
        
        // Init Rest of the screen.
        if isFirstTimeDatePickerShowed {
            isFirstTimeDatePickerShowed = false
            
            // Init rest of the screen
            
        }
    }
    
    //!!
    func changeNamesCount(){
        let delimiterWidth = names[0].frame.width
        let nameDelimiter = UILabel(frame: CGRect(x: names[0].frame.maxX + margin, y: 0, width: delimiterWidth, height: 30))
        
        if names.count == 2{
            if newEvent.dateType! != .wedding{
                names.remove(at: names.count - 1)
            }
        }else{
            if newEvent.dateType! == .wedding{
                names.append(UITextField(frame: CGRect(x: nameDelimiter.frame.maxX + margin, y: 0, width: names[0].frame.width, height: 30)))
            }
        }
        
        namesView.addSubview(names[0])
        
        // If there is two text fields.
        if names.count == 2{
            namesView.addSubview(nameDelimiter)
            namesView.addSubview(names[1])
        }
        
        // Show on screen the text fields
        view.addSubview(namesView)
    }
    
    @objc func handleDateTypeClick(sender: UIButton){
        // Create the picker:
        let row = sender.subviews[sender.subviews.count - 1] as! UILabel
        createPicker(forTag: 0, goToRow: row.tag)
    }
    
    @objc func handlePersonTypeClick(sender: UIButton){
        // Create the picker:
        let row = sender.subviews[sender.subviews.count - 1] as! UILabel
        createPicker(forTag: 1, goToRow: row.tag)
    }
    
    @objc func handleConfirmationBtnClick(sender: UIButton){
        pickerContainer.removeFromSuperview()
        
        if pickerContainer.subviews[0] is UIPickerView {
            let typePicker = pickerContainer.subviews[0] as! UIPickerView
            let choice = typePicker.selectedRow(inComponent: 0)
            let typeLabel: UILabel
            
            // DateType picker.
            if typePicker.tag == 0{
                typeLabel = dateType.subviews[dateType.subviews.count - 1] as! UILabel
                typeLabel.text = Event.dateTypes[choice].rawValue
                newEvent.dateType = Event.dateTypes[choice]
                
                initRestScreen()
            }
            // PersonType picker.
            else{
                typeLabel = personType.subviews[personType.subviews.count - 1] as! UILabel
                typeLabel.text = Event.personTypes[choice].rawValue
                newEvent.personType = Event.personTypes[choice]
            }
            
            typeLabel.tag = choice
            typeLabel.font = UIFont.boldSystemFont(ofSize: 20)
            typeLabel.sizeToFit()
            typeLabel.adjustsFontSizeToFitWidth = true
        }
        
        // Restart container.
        pickerContainer = UIView(frame: pickerContainer.frame)
    }
    
    // Picker view- DataSource & Delegate
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return pickerView.tag == 0 ? Event.dateTypes.count : Event.personTypes.count
    }
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        /*switch row {
            case 0:
                return pickerView.tag == 0 ? DateType.birthday.rawValue : PersonType.family.rawValue
            case 1:
                return pickerView.tag == 0 ? DateType.wedding.rawValue : PersonType.friends.rawValue
            case 2:
                return pickerView.tag == 0 ? DateType.deathDay.rawValue : PersonType.coWorkers.rawValue
            case 3:
                return pickerView.tag == 0 ? DateType.anniversary.rawValue : PersonType.differant.rawValue
            default:
                return ""
        }*/
        return pickerView.tag == 0 ? Event.dateTypes[row].rawValue : Event.personTypes[row].rawValue
    }
}
