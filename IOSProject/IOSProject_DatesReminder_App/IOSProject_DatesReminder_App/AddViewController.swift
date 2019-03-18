//
//  AddViewController.swift
//  IOSProject_DatesReminder_App
//
//  Created by hackeru on 11/03/2019.
//  Copyright © 2019 hackeru. All rights reserved.
//

import UIKit

class AddViewController: UIViewController, UIPickerViewDataSource, UIPickerViewDelegate{
    
    var pickerContainer: UIView!
    var dateType: UIButton!
    var confirmBtn: UIButton!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        initScreen()
        
        pickerContainer = UIView(frame: view.frame)
        
        createConfirmBtn()
    }
    
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
        dateType = UIButton(frame: CGRect(x: 0, y: title.frame.maxY + 10, width: view.frame.width, height: 30))
        let label = UILabel(frame: CGRect(x: 0, y: 0, width: 0, height: 30))
        label.text = "Date type: "
        label.font = UIFont.boldSystemFont(ofSize: 20)
        label.sizeToFit()
        label.adjustsFontSizeToFitWidth = true
        dateType.addTarget(self, action: #selector(handleDateTypeClick(sender:)), for: .touchUpInside)
        dateType.addSubview(label)
        view.addSubview(dateType)
    }
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
        // Open autometiclly the dateTypePicker
        handleDateTypeClick(sender: dateType)
    }
    
    func createConfirmBtn() {
        // Create confirmation button:
        confirmBtn = UIButton(type: .system)
        confirmBtn.setTitle("Confirm", for: .normal)
        confirmBtn.addTarget(self, action: #selector(handleConfirmationBtnClick(sender:)), for: .touchUpInside)
    }
    
    func createPicker(forTag: Int) {
        let typePicker = UIPickerView()
        typePicker.center = view.center
        typePicker.dataSource = self
        typePicker.delegate = self
        typePicker.backgroundColor = UIColor.lightText
        pickerContainer.addSubview(typePicker)
        
        confirmBtn.frame = CGRect(x: 0, y: typePicker.frame.maxY, width: view.frame.width, height: 30)
        pickerContainer.addSubview(confirmBtn)
        view.addSubview(pickerContainer)
    }
    
    @objc func handleDateTypeClick(sender: UIButton){
        // Create the picker:
        createPicker(forTag: 0)
    }
    
    @objc func handlePersonTypeClick(sender: UIButton){
        // Create the picker:
        createPicker(forTag: 1)
    }
    
    @objc func handleConfirmationBtnClick(sender: UIButton){
        pickerContainer.removeFromSuperview()
        
        if pickerContainer.subviews[0] is UIPickerView {
            let typePicker = pickerContainer.subviews[0] as! UIPickerView
            let choice = typePicker.selectedRow(inComponent: 0)
            
            // array of all type strings, this way we can use this in titleForRow + selected row?????
        }
        
        // Restart container.
        pickerContainer = UIView(frame: pickerContainer.frame)
    }
    
    // Picker view- DataSource & Delegate
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return pickerView.tag == 0 ? 4 : 4 //   ? date type : person type
    }
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        switch row {
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
        }
    }
}
