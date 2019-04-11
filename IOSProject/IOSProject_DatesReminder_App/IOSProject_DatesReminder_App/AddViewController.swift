//
//  AddViewController.swift
//  IOSProject_DatesReminder_App
//
//  Created by Nadin Zeitoune on 11/03/2019.
//

import UIKit

class AddViewController: UIViewController, UIPickerViewDataSource, UIPickerViewDelegate{
    
    let httpBaseString = "https://www.hebcal.com/converter/?cfg=json&"
    let margin: CGFloat = 10
    let spaceMargin: CGFloat = 30
    
    var pickerContainer: UIView!
    var confirmBtn: UIButton!
    
    var dateType: UIButton!
    var personType: UIButton!
    var typePickers: [UIPickerView] = [UIPickerView(), UIPickerView()]
    
    var namesView: UIView!
    var names: [UITextField] = []
    
    var dateSeg: UISegmentedControl!
    var dateBtn: UIButton!
    var datePick: UIDatePicker!
    var tagDate: Date = Date()
    var isGregDate: Bool = true // to know which date selected
    var isBeforeAfterDate: Bool = false
    var isBeforeAfterView: UIView!
    
    var doesNotifyGreg: UIView!
    var doesNotifyHeb: UIView!

    var addEventBtn: UIButton!
    var exitBtn: UIButton!
    var newEvent: Event!
    
    var toEdit: Bool = false
    
    weak var viewController: ViewController!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        initScreen()
        
        createPickerContainer()
        
        createPickers()
        
        // Few changes for editing.
        if toEdit{
            // Change title to "Edit Event"
            let title = view.subviews[0] as! UILabel
            title.text = "Edit Event"
            
            initPickerAccordingToEvent()
            
            // Add the names
            names[0].text = newEvent.names[0]
            if names.count == 2{
                names[1].text = newEvent.names[1]
            }
            
            // Add the notify switchs position
            var switchNotify = doesNotifyGreg.subviews[doesNotifyGreg.subviews.count - 1] is UISwitch ? doesNotifyGreg.subviews[doesNotifyGreg.subviews.count - 1] as! UISwitch : nil
            if switchNotify != nil{
                switchNotify!.isOn = newEvent.isNotifyG
            }
            switchNotify = doesNotifyHeb.subviews[doesNotifyHeb.subviews.count - 1] is UISwitch ? doesNotifyHeb.subviews[doesNotifyHeb.subviews.count - 1] as! UISwitch : nil
            if switchNotify != nil{
                switchNotify!.isOn = newEvent.isNotifyH
            }
            
            // Change addBtn title to "Save"
            addEventBtn.setTitle("Save", for: .normal)
        }
        
        // Show basic details on screen.
        commitChangesToTextAccordingTo(datePick)
        commitChangesToTextAccordingTo(typePickers[0])
        commitChangesToTextAccordingTo(typePickers[1])
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
        // Open keyboard automatic.
        names[0].becomeFirstResponder()
        
        // Add tap to the screen.
        let tapRecognizer = UITapGestureRecognizer(target: self, action: #selector(handleTapOnScreen(sender:)))
        view.addGestureRecognizer(tapRecognizer)
    }
    
    func initScreen() {
        view.backgroundColor = UIColor.white
        
        // Event:
        if !toEdit{
            newEvent = Event()
            newEvent.dateType = .birthday
        }
        
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
        
        // Names view:
        namesView = UIView(frame: CGRect(x: 0, y: title.frame.maxY + spaceMargin, width: view.frame.width, height: 30))
        
        var namesTitle = UILabel(frame: CGRect(x: 0, y: 0, width: 0, height: 30))
        namesTitle = styleLabel(namesTitle, withTitle: "Name: ")
        namesView.addSubview(namesTitle)
        let firstName = UITextField(frame: CGRect(x: namesTitle.frame.maxX + margin, y: 0, width: (namesView.frame.width - namesTitle.frame.width) / 2.5, height: 30))
        styleNameTextField(firstName)
        names.append(firstName)
        changeNamesCount()
        
        // Date type:
        dateType = UIButton(frame: CGRect(x: 0, y: namesView.frame.maxY + spaceMargin, width: view.frame.width, height: 30))
        
        var dateTitle = UILabel(frame: CGRect(x: 0, y: 0, width: 0, height: 30))
        dateTitle = styleLabel(dateTitle, withTitle: "Date type: ")
        dateType.addTarget(self, action: #selector(handleDateTypeClick(sender:)), for: .touchUpInside)
        dateType.addSubview(dateTitle)
        
        let datePick = UILabel(frame: CGRect(x: dateTitle.frame.maxX + margin, y: 2, width: 0, height: 30))
        datePick.tag = 0
        dateType.addSubview(datePick)
        
        view.addSubview(dateType)
        
        // Date btn.
        createDateBtn()
        
        // Create person type picker.
        personType = UIButton(frame: CGRect(x: 0, y: dateBtn.frame.maxY + spaceMargin, width: view.frame.width, height: 30))
        var personTitle = UILabel(frame: CGRect(x: 0, y: 0, width: 0, height: 30))
        personTitle = styleLabel(personTitle, withTitle: "Person type: ")
        personType.addTarget(self, action: #selector(handlePersonTypeClick(sender:)), for: .touchUpInside)
        personType.addSubview(personTitle)
        
        let personPick = UILabel(frame: CGRect(x: personTitle.frame.maxX + margin, y: 2, width: 0, height: 30))
        personPick.tag = 0
        personType.addSubview(personPick)
        view.addSubview(personType)
        
        // Create notification switches.
        var notifyTitle = UILabel(frame: CGRect(x: 0, y: personType.frame.maxY + spaceMargin, width: 0, height: 30))
        notifyTitle = styleLabel(notifyTitle, withTitle: "Does notify:")
        view.addSubview(notifyTitle)
        
        doesNotifyGreg = createSwitchView(State: false, WithTitle: "Gregorian date?", AndOrigin: (margin * 5, notifyTitle.frame.maxY + spaceMargin), WithTag: 2)
        view.addSubview(doesNotifyGreg)
        
        doesNotifyHeb = createSwitchView(State: false, WithTitle: "Hebrew date?", AndOrigin: (margin * 5, doesNotifyGreg.frame.maxY + margin), WithTag: 3)
        view.addSubview(doesNotifyHeb)
        
        // Create add btn.
        addEventBtn = UIButton(type: .system)
        addEventBtn.frame = CGRect(x: 0, y: doesNotifyHeb.frame.maxY + margin * 8, width: 0, height: 30)
        addEventBtn.setTitle("Add Event", for: .normal)
        addEventBtn.titleLabel?.font = UIFont.boldSystemFont(ofSize: 30)
        addEventBtn.sizeToFit()
        addEventBtn.center.x = view.center.x
        addEventBtn.addTarget(self, action: #selector(handleAddEventBtnClick(sender:)), for: .touchUpInside)
        view.addSubview(addEventBtn)
    }
    
    func createPickerContainer(){
        pickerContainer = UIView(frame: view.frame)
        pickerContainer.backgroundColor = UIColor.lightGray.withAlphaComponent(0.75)
    }
    
    func createConfirmBtn(ToType isType: Bool) {
        // Create confirmation button:
        confirmBtn = UIButton(type: .system)
        confirmBtn.setTitle("Confirm", for: .normal)
        confirmBtn.backgroundColor = UIColor.lightGray
        confirmBtn.titleLabel?.font = UIFont.boldSystemFont(ofSize: 22)
        confirmBtn.addTarget(self, action: #selector(handleConfirmationBtnClick(sender:)), for: .touchUpInside)
        confirmBtn.frame = CGRect(x: 0, y: isType ? typePickers[0].frame.maxY : isBeforeAfterView.frame.maxY, width: view.frame.width, height: 30)
    }
    
    func createPickers(){
        // Create date type picker.
        createPicker(forTag: 0, goToRow: 0)
        
        // Create person type picker.
        createPicker(forTag: 1, goToRow: 0)
        
        // Create date picker.
        createPicker(forTag: 3, goToRow: Date())
    }
    
    func createPicker(forTag: Int, goToRow: Any){
        // Date picker:
        if goToRow is Date{
            let date = goToRow as! Date
            
            // Create picker.
            datePick = UIDatePicker()
            datePick.tag = forTag
            datePick.datePickerMode = .date
            datePick.center = pickerContainer.center
            datePick.backgroundColor = UIColor.lightGray
            datePick.setDate(date, animated: false)
            
            // Create switch.
            isBeforeAfterView = createSwitchView(State: isBeforeAfterDate, WithTitle: "", AndOrigin: (0, datePick.frame.maxY), WithTag: 1)
            modifyDateSwitchView()
            
            // Create segmented controll.
            dateSeg = UISegmentedControl(items: ["Gregorian date","Hebrew date"])
            dateSeg.frame.origin.y = datePick.frame.minY - dateSeg.frame.height
            dateSeg.center.x = pickerContainer.center.x
            dateSeg.addTarget(self, action: #selector(handleDateSegChanged(sender:)), for: .valueChanged)
            dateSeg.selectedSegmentIndex = 0
            handleDateSegChanged(sender: dateSeg)
        }else{
            let row = goToRow as! Int
            let typePicker = UIPickerView()
            typePicker.center = pickerContainer.center
            typePicker.backgroundColor = UIColor.lightGray
            typePicker.dataSource = self
            typePicker.delegate = self
            typePicker.tag = forTag
            typePicker.selectRow(row, inComponent: 0, animated: false)
            typePickers[forTag] = typePicker
        }
    }
    
    func initPickerAccordingToEvent(){
        // Init date picker.
        if newEvent.isGregorian_isBeforeAfter.0{
            datePick.setDate(newEvent.gregorianDate, animated: false)
            
        }else{
            // Seg points to heb date.
            dateSeg.selectedSegmentIndex = 1
            handleDateSegChanged(sender: dateSeg)
            datePick.setDate(newEvent.hebrewDate, animated: false)
        }
        // Change "isBeforeAfter"
        var switchNotify = isBeforeAfterView.subviews[isBeforeAfterView.subviews.count - 1] is UISwitch ? isBeforeAfterView.subviews[isBeforeAfterView.subviews.count - 1] as! UISwitch : nil
        if switchNotify != nil{
            switchNotify!.isOn = newEvent.isGregorian_isBeforeAfter.1
        }
        
        // Init date type & person type pickers.
        for j in 0 ..< 2{
            for i in 0 ..< Event.dateTypes.count{
                if newEvent.dateType == Event.dateTypes[i]{
                    typePickers[j].selectRow(i, inComponent: 0, animated: false)
                }
            }
        }
    }
    
    func modifyDateSwitchView() {
        isBeforeAfterView.center.x = pickerContainer.center.x
        isBeforeAfterView.backgroundColor = UIColor.lightText
    }
    
    func createSwitchView(State state:Bool, WithTitle title: String, AndOrigin origin:(CGFloat, CGFloat), WithTag tag: Int) -> UIView{
        
        // Create text.
        var titleLabel = UILabel(frame: CGRect(x: 0, y: 0, width: 0, height: 30))
        titleLabel = styleLabel(titleLabel, withTitle: title)
        
        // Create Switch.
        let tempSwitch = UISwitch(frame: CGRect(x: titleLabel.frame.maxX + margin, y: 0, width: 0, height: 0))
        tempSwitch.center.y = titleLabel.center.y
        tempSwitch.onTintColor = UIColor.blue
        tempSwitch.isOn = state
        tempSwitch.tag = tag
        tempSwitch.addTarget(self, action: #selector(handleSwitchBtnChange(sender:)), for: .valueChanged)
        
        let height = titleLabel.frame.height >= tempSwitch.frame.height ? titleLabel.frame.height : tempSwitch.frame.height
        let view = UIView(frame: CGRect(x: origin.0, y: origin.1, width: titleLabel.frame.width + margin + tempSwitch.frame.width, height: height))
        
        view.addSubview(titleLabel)
        view.addSubview(tempSwitch)
        return view
    }
    
    func createDateBtn(){
        // Create button to open the date picker.
        dateBtn = UIButton(frame: CGRect(x: 0, y: dateType.frame.maxY + spaceMargin, width: view.frame.width, height: 30))
        var dateTitle = UILabel(frame: CGRect(x: 0, y: 0, width: 0, height: 30))
        dateTitle = styleLabel(dateTitle, withTitle: "Date: ")
        dateBtn.addSubview(dateTitle)
        dateBtn.addTarget(self, action: #selector(handleDateBtnClick(sender:)), for: .touchUpInside)
        let datePick = UILabel(frame: CGRect(x: dateTitle.frame.maxX + margin, y: 2, width: 0, height: 30))
        dateBtn.addSubview(datePick)
        view.addSubview(dateBtn)
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
    
    func styleLabel(_ label: UILabel, withTitle title: String) -> UILabel{
        label.text = title
        label.font = UIFont.boldSystemFont(ofSize: 22)
        label.sizeToFit()
        label.adjustsFontSizeToFitWidth = true
        
        return label
    }
    
    func confirmAllDetailsAreGiven() -> Bool{
        // Check datetype:
        if dateType.subviews[dateType.subviews.count - 1] is UILabel {
            let label = dateType.subviews[dateType.subviews.count - 1] as! UILabel
            if label.text == nil || label.text!.isEmpty{
                return false
            }
        }
        
        // Check names:
        if names[0].text == nil || names[0].text!.isEmpty{
            return false
        }
        if names.count == 2{
            if names[1].text == nil || names[1].text!.isEmpty{
                return false
            }
        }
        
        // Check date: always != nil !!!! check the option of putting the date already on screen when entering the controller
        if tagDate == nil{
            return false
        }
        
        // Check person type:
        if personType.subviews[personType.subviews.count - 1] is UILabel {
            let label = personType.subviews[personType.subviews.count - 1] as! UILabel
            if label.text == nil || label.text!.isEmpty{
                return false
            }
        }
        
        return true
    }
    
    func getOppositeDate(From date: Date, WhatToDo action: @escaping (Data) -> Void){
        // Show loading circle on bar
        UIApplication.shared.isNetworkActivityIndicatorVisible = true
        
        DispatchQueue.global().async {
            var extendURL = self.httpBaseString
            
            let calendar = Calendar.init(identifier: self.isGregDate ? .gregorian : .hebrew)
            var components = calendar.dateComponents([Calendar.Component.day, Calendar.Component.month, Calendar.Component.year], from: date)
            
            // Get the specific URL.
            if self.isGregDate{
                extendURL.append("gy=\(components.year!)&gm=\(components.month!)&gd=\(components.day!)&g2h=1")
                if self.isBeforeAfterDate{
                    extendURL.append("&gs=on")
                }
            }else{
                if self.isBeforeAfterDate{
                    components.day! -= 1
                }
                extendURL.append("hy=\(components.year!)&hm=\(Event.hebMonthStrings[components.month! - 1])&hd=\(components.day!)&h2g=1")
            }
            
            // connect to API
            self.sendHttpRequest(WithUrl: extendURL, WhatToDo: action)
        }
    }
    
    func sendHttpRequest(WithUrl urlS: String, WhatToDo action: @escaping (Data) -> Void){
        let url = URL(string: urlS)
        let urlSession = URLSession(configuration: URLSessionConfiguration.default)
        let urlRequest = URLRequest(url: url!)
        let task = urlSession.dataTask(with: urlRequest) { (data: Data?, response: URLResponse?, error: Error?) in
            if error == nil{
                if let theData = data{
                    if theData.count > 0{
                        DispatchQueue.main.async {
                            // Close loading circle.
                            UIApplication.shared.isNetworkActivityIndicatorVisible = false
                        }
                        action(theData)
                    }
                }
            }
        }
        task.resume()
    }
    
    //!!!
    func finalActions(){
        // add newEvent to data source
        if toEdit{
            // remove reminder/s
            
            // Remove the previous form of this event.
            DatesDataSource.dates[viewController.index.section].remove(at: viewController.index.row)
        }
        DatesDataSource.dates[newEvent.month - 1].append(newEvent)
        DatesDataSource.sortDatesArray()
        
        // add reminder/s
        Reminders.event = newEvent
        Reminders.confirmAccess(forAdding: true)
        
        // Pop success alert.
        let title = toEdit ? "Event Changed!" : "Event added!"
        let successAlert = UIAlertController(title: title, message: "Press OK to reload list", preferredStyle: .alert)
        let actionOK = UIAlertAction(title: "OK", style: .default) { (action: UIAlertAction) in
            // Close addViewController
            self.dismiss(animated: true) {
                self.viewController.datesList.reloadData()
            }
        }
        successAlert.addAction(actionOK)
        self.present(successAlert, animated: true, completion: nil)
    }
    
    func getJSONFromData(data: Data) -> [String : Any]{
        do{
            let json = try JSONSerialization.jsonObject(with: data, options: .allowFragments) as! [String : Any]
            return json
        }catch let e{
            return [:]
        }
    }
    
    func addPickerContainerToScreen(){
        handleTapOnScreen(sender: UITapGestureRecognizer())
        pickerContainer.addSubview(confirmBtn)
        view.addSubview(pickerContainer)
    }
    
    func commitChangesToTextAccordingTo(_ picker: UIView){
        if picker is UIPickerView {
            let typePicker = picker as! UIPickerView
            let choice = typePicker.selectedRow(inComponent: 0)
            let typeLabel: UILabel
            
            // DateType picker.
            if typePicker.tag == 0{
                typeLabel = dateType.subviews[dateType.subviews.count - 1] as! UILabel
                typeLabel.text = Event.dateTypes[choice].rawValue
                newEvent.dateType = Event.dateTypes[choice]
                
                changeNamesCount()
            }
            // PersonType picker.
            else{
                typeLabel = personType.subviews[personType.subviews.count - 1] as! UILabel
                typeLabel.text = Event.personTypes[choice].rawValue
                newEvent.personType = Event.personTypes[choice]
            }
            
            typeLabel.font = UIFont.boldSystemFont(ofSize: 20)
            typeLabel.sizeToFit()
            typeLabel.adjustsFontSizeToFitWidth = true
        }
        // Date picker:
        else if(picker is UIDatePicker){
            let dateLabel: UILabel
            tagDate = datePick.date
            dateLabel = dateBtn.subviews[dateBtn.subviews.count - 1] as! UILabel
            dateLabel.text = DatesDataSource.generateStringFromDate(datePick.date, WithCalendar: datePick.calendar)
            dateLabel.font = UIFont.boldSystemFont(ofSize: 20)
            dateLabel.sizeToFit()
            dateLabel.adjustsFontSizeToFitWidth = true
        }
    }
    
    @objc func handleTapOnScreen(sender: UITapGestureRecognizer){
        names[0].resignFirstResponder()
        
        if names.count == 2 {
            names[1].resignFirstResponder()
        }
    }
    
    @objc func handleSwitchBtnChange(sender: UISwitch){
        switch sender.tag {
        case 1: // Date
            isBeforeAfterDate = sender.isOn
        case 2: // Greg
            newEvent.isNotifyG = sender.isOn
        case 3: // Heb
            newEvent.isNotifyH = sender.isOn
        default:
            break
        }
    }
    
    @objc func handleDateBtnClick(sender: UIButton){
        pickerContainer.addSubview(datePick)
        pickerContainer.addSubview(isBeforeAfterView)
        pickerContainer.addSubview(dateSeg)
        createConfirmBtn(ToType: false)
        addPickerContainerToScreen()
    }
    
    @objc func handleDateSegChanged(sender: UISegmentedControl){
        let dateSwitchTitlles = ["Is after sundown?", "Is before midnight?"]
        isBeforeAfterView.removeFromSuperview()
        isBeforeAfterView = createSwitchView(State: false, WithTitle: dateSwitchTitlles[sender.selectedSegmentIndex], AndOrigin: (isBeforeAfterView.frame.origin.x, isBeforeAfterView.frame.origin.y), WithTag: 1)
        isBeforeAfterDate = false
        modifyDateSwitchView()
        
        switch sender.selectedSegmentIndex {
            case 0: // Gregorian choice.
                datePick.calendar = Calendar.init(identifier: Calendar.Identifier.gregorian)
                isGregDate = true
            case 1: // Hebrew choice.
                datePick.calendar = Calendar.init(identifier: Calendar.Identifier.hebrew)
                isGregDate = false
            default:
                break
        }
        
        pickerContainer.addSubview(isBeforeAfterView)
    }
    
    @objc func handleExitBtnClick(sender: UIButton){
        // Makw sure user want to leave.
        let alert = UIAlertController(title: "Are you sure?", message: "If you leave, data will not be saved", preferredStyle: .alert)
        
        let exitAction = UIAlertAction(title: "Leave", style: .destructive) { (action: UIAlertAction) in
            self.dismiss(animated: true, completion: nil)
        }
        
        let cancelAction = UIAlertAction(title: "Cancel", style: .cancel, handler: nil)
        alert.addAction(exitAction)
        alert.addAction(cancelAction)
        present(alert, animated: true, completion: nil)
    }
    
    @objc func handleDateTypeClick(sender: UIButton){
        // Create the picker:
        pickerContainer.addSubview(typePickers[0])
        createConfirmBtn(ToType: true)
        addPickerContainerToScreen()
    }
    
    @objc func handlePersonTypeClick(sender: UIButton){
        // Create the picker:
        pickerContainer.addSubview(typePickers[1])
        createConfirmBtn(ToType: true)
        addPickerContainerToScreen()
    }
    
    @objc func handleConfirmationBtnClick(sender: UIButton){
        pickerContainer.removeFromSuperview()
        
        commitChangesToTextAccordingTo(pickerContainer.subviews[0])
        
        // Restart container.
        createPickerContainer()
    }
    
    @objc func handleAddEventBtnClick(sender: UIButton){
        handleTapOnScreen(sender: UITapGestureRecognizer())
        
        // Make sure all details are given.
        if confirmAllDetailsAreGiven() {
            // Put all details we have in newEvent
            // Names:
            newEvent.names.removeAll()
            newEvent.names.append(names[0].text!)
            if names.count == 2{
                newEvent.names.append(names[1].text!)
            }
            // Types are in. func handleConfirmationClick
            // Notifys are in. func handleSwitchChanged
            
            // Date:
            if isGregDate{
                newEvent.gregorianDate = tagDate
            }else{
                newEvent.hebrewDate = tagDate
            }
            getOppositeDate(From: tagDate, WhatToDo: { (data: Data) in
                let dictionary = self.getJSONFromData(data: data)
                if dictionary.count != 0{
                    self.newEvent.createDateFromDictionary(dictionary, PutInGreg: self.isGregDate ? false : true)
                    // Get the month.
                    self.newEvent.getMonth()
                    
                    self.finalActions()
                }
            })
            
            // Get the differance of dates.
            newEvent.isGregorian_isBeforeAfter.0 = isGregDate
            newEvent.isGregorian_isBeforeAfter.1 = isBeforeAfterDate
        }else{
            // Stop the user.
            let alert = UIAlertController(title: "Hold on!", message: "You forgot to give all the details", preferredStyle: .alert)
            let okAction = UIAlertAction(title: "OK", style: .default, handler: nil)
            alert.addAction(okAction)
            present(alert, animated: true, completion: nil)
        }
    }
    
    // Picker view- DataSource & Delegate
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return pickerView.tag == 0 ? Event.dateTypes.count : Event.personTypes.count
    }
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return pickerView.tag == 0 ? Event.dateTypes[row].rawValue : Event.personTypes[row].rawValue
    }
}
