//
//  MyProfileNav.swift
//  AR Patient
//
//  Created by Silicon on 06/05/20.
//  Copyright © 2020 Silicon. All rights reserved.
//

import UIKit

class MyProfileNav: UINavigationController {

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        let vc = UIStoryboard.init(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "MyProfileVC") as! MyProfileVC
        vc.isFromTab = true
        self.initRootViewController(vc: vc)
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
