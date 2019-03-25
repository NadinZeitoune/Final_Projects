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
    
    var dateSeg: UISegmentedControl!
    var dateBtn: UIButton!
    var datePick: UIDatePicker!
    var isGregDateEve: UISwitch!
    
    var personType: UIButton!
    // check boxs - does notify heb /+ greg ?

    var addEventBtn: UIButton!
    var exitBtn: UIButton!
    var newEvent: Event!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        initScreen()
        
        createPickerContainer()
        
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
        
        // Exit button:
        exitBtn = UIButton(type: .roundedRect)
        exitBtn.frame = CGRect(x: view.frame.maxX - 20, y: title.center.y, width: 20, height: 20)
        exitBtn.setTitle("X", for: .normal)
        exitBtn.titleLabel?.font = UIFont.boldSystemFont(ofSize: 22)
        exitBtn.addTarget(self, action: #selector(handleExitBtnClick(sender:)), for: .touchUpInside)
        view.addSubview(exitBtn)
        
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
        styleNameTextField(firstName)
        names.append(firstName)
        
        // Event:
        newEvent = Event()
    }
    
    func createPickerContainer(){
        pickerContainer = UIView(frame: view.frame)
        pickerContainer.backgroundColor = UIColor.gray.withAlphaComponent(0.75)
    }
    
    func createConfirmBtn() {
        // Create confirmation button:
        confirmBtn = UIButton(type: .system)
        confirmBtn.setTitle("Confirm", for: .normal)
        confirmBtn.titleLabel?.backgroundColor = UIColor.lightText
        confirmBtn.titleLabel?.font = UIFont.boldSystemFont(ofSize: 22)
        confirmBtn.addTarget(self, action: #selector(handleConfirmationBtnClick(sender:)), for: .touchUpInside)
    }
    
    func createPicker(forTag: Int, goToRow: Int) {
        let typePicker = UIPickerView()
        typePicker.center = pickerContainer.center
        typePicker.backgroundColor = UIColor.lightText
        typePicker.dataSource = self
        typePicker.delegate = self
        typePicker.tag = forTag
        typePicker.selectRow(goToRow, inComponent: 0, animated: false)
        pickerContainer.addSubview(typePicker)
        
        confirmBtn.frame = CGRect(x: 0, y: typePicker.frame.maxY - 2, width: view.frame.width, height: 30)
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
            
            createDateSegAndBtn()
            
        }
    }
    
    func createDateSegAndBtn(){
        // Create segmented controll.
        dateSeg = UISegmentedControl(items: ["Gregorian date","Hebrew date"])
        dateSeg.frame.origin.y = namesView.frame.maxY + margin
        dateSeg.center.x = view.center.x
        dateSeg.addTarget(self, action: #selector(handleDateSegChanged(sender:)), for: .valueChanged)
        view.addSubview(dateSeg)
        
        // Create button to open the picker.
        dateBtn = UIButton(type: .system)
        dateBtn.frame = CGRect(x: 0, y: dateSeg.frame.maxY + margin, width: 0, height: 30)
        dateBtn.setTitle("Date:", for: .normal)
        dateBtn.sizeToFit()
        dateBtn.titleLabel?.adjustsFontSizeToFitWidth = true
        dateBtn.addTarget(self, action: #selector(handleDateBtnClick(sender:)), for: .touchUpInside)
        view.addSubview(dateBtn)
        
        // Create picker.
        datePick = UIDatePicker()
        datePick.datePickerMode = .date
        datePick.frame.origin.y = dateSeg.frame.maxY + margin
        datePick.center.x = view.center.x
        datePick.backgroundColor = UIColor.gray
        let confirm = UIButton(type: .system)
        confirm.setTitle("Confirm", for: .normal)
        confirm.frame = CGRect(x: 0, y: datePick.frame.maxY, width: 50, height: 30)
        // I'm here - need to add target & change Y frame
        datePick.addSubview(confirm)
        
        dateSeg.selectedSegmentIndex = 0
        handleDateSegChanged(sender: dateSeg)
    }
    
    func changeNamesCount(){
        let nameDelimiter = UILabel(frame: CGRect(x: names[0].frame.maxX, y: 0, width: 0, height: 30))
        nameDelimiter.text = " & "
        nameDelimiter.font = UIFont.boldSystemFont(ofSize: 24)
        nameDelimiter.sizeToFit()
        
        if names.count == 2{
            if newEvent.dateType! != .wedding{
                names.remove(at: names.count - 1)
            }
        }else{
            if newEvent.dateType! == .wedding{
                names.append(UITextField(frame: CGRect(x: nameDelimiter.frame.maxX, y: 0, width: names[0].frame.width, height: 30)))
                styleNameTextField(names[names.count - 1])
            }
        }
        
        // Reset namesView
        var subviewNum = namesView.subviews.count
        while subviewNum > 1{
            namesView.subviews[subviewNum - 1].removeFromSuperview()
            subviewNum -= 1
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
    
    func styleNameTextField(_ field: UITextField) {
        field.borderStyle = .roundedRect
    }
    
    @objc func handleDateBtnClick(sender: UIButton){
        view.addSubview(datePick)
    }
    
    @objc func handleDateSegChanged(sender: UISegmentedControl){
        switch sender.selectedSegmentIndex {
            case 0: // Gregorian choice.
                datePick.calendar = Calendar.init(identifier: Calendar.Identifier.gregorian)
            
            case 1: // Hebrew choice.
                datePick.calendar = Calendar.init(identifier: Calendar.Identifier.hebrew)
            
            default:
                break
        }
    }
    
    @objc func handleExitBtnClick(sender: UIButton){
        
        dismiss(animated: true, completion: nil)
        //present(ViewController(), animated: true, completion: nil)
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
        createPickerContainer()
    }
    
    //!!
    @objc func handleAddEventBtnClick(sender: UIButton){
        // put all details we have in newEvent
        // connect to API to get missing details.
        // Put new details in newEvent
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
