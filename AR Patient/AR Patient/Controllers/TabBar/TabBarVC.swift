//
//  TabBarVC.swift
//  AR Patient
//
//  Created by Silicon on 29/04/20.
//  Copyright © 2020 Silicon. All rights reserved.
//

import UIKit

class TabBarVC: UITabBarController {

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        UITabBar.appearance().tintColor = UIColor.init(named: "Color_LightBlue")
        UITabBar.appearance().backgroundColor = UIColor.white
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
