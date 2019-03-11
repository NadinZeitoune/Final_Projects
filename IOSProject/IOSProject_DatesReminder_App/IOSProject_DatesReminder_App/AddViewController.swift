//
//  AddViewController.swift
//  IOSProject_DatesReminder_App
//
//  Created by hackeru on 11/03/2019.
//  Copyright © 2019 hackeru. All rights reserved.
//

import UIKit

class AddViewController: UIViewController {
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        initScreen()
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
    }
}
