//
//  SettingsVC.swift
//  AR Patient
//
//  Created by Silicon on 31/07/20.
//  Copyright © 2020 Silicon. All rights reserved.
//

import UIKit

class SettingsVC: UIViewController {

    @IBOutlet weak var viewHeader: UIView!
    @IBOutlet weak var btnBack: UIButton!
    @IBOutlet weak var lblTitle: IPAutoScalingLabel!
    @IBOutlet weak var tableView: UITableView!
    
    var arrayMenu:[MenuData] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        self.viewHeader.layer.frame = CGRect(x: 0, y: 0, width: UIScreen.main.bounds.size.width, height: 60)
        self.viewHeader.roundCorners([.bottomLeft, .bottomRight], radius: 25)
        
        self.arrayMenu.removeAll()
        
        self.generateMenu(menu: "Edit Profile", icon: "Profile", selected: false, tag: 1)
        self.generateMenu(menu: "Change Password", icon: "Password", selected: false, tag: 11)
        self.generateMenu(menu: "Delete Accout", icon: "Delete", selected: false, tag: 8)
        self.generateMenu(menu: "Logout", icon: "Logout", selected: false, tag: 9)
        
        tableView.register(UINib(nibName: "MenuCell", bundle: nil), forCellReuseIdentifier: "MenuCell")
        
        //tableView.separatorStyle = .none
        tableView.delegate = self
        tableView.dataSource = self
        tableView.tableFooterView = UIView()
    }
    
    func generateMenu(menu: String, icon: String, selected: Bool, tag: Int) {
        let menuData = MenuData()
        menuData.menu = menu
        menuData.icon = icon
        menuData.selected = selected
        menuData.tag = tag
        
        self.arrayMenu.append(menuData)
    }
    

    @IBAction func btnBack(_ sender: Any) {
        self.view.endEditing(true)
        self.navigationController?.popViewController(animated: true)
    }

}

extension SettingsVC: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 60.0
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.arrayMenu.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "MenuCell", for: indexPath) as! MenuCell
        
        let menuData = self.arrayMenu[indexPath.row]
        
        cell.lblMenu.text = menuData.menu
        cell.lblMenu.textColor = UIColor.init(named: "Color_11306A")
        cell.imgMenu.image = UIImage.init(named: "\(menuData.icon) Unselected")
        let font = UIFont.init(name: Theme.Font.SemiBold, size: Theme.Font.size.TxtSize_14)
        font!.withSize(((UIScreen.main.bounds.size.width) * font!.pointSize) / 320)
        cell.lblMenu.font = font
        //cell.viewBackground.backgroundColor = UIColor.init(named: "Color_DarkBlue")
        
        cell.viewBackground.layer.frame = CGRect(x: 0, y: 0, width: UIScreen.main.bounds.size.width, height: 60)
        cell.viewBackground.roundCorners([.bottomLeft, .topRight], radius: 10)
        
        cell.selectionStyle = .none
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {        
        let menuData = self.arrayMenu[indexPath.row]
        let tag = menuData.tag
        
        if(tag == 1) {
            let vc = UIStoryboard.init(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "EditProfileVC") as? EditProfileVC
            self.navigationController?.pushViewController(vc!, animated: true)
        } else if(tag == 8) {
            AJAlertController.initialization().showAlert(aStrMessage: "Are you sure want to delete account?", aCancelBtnTitle: "Yes", aOtherBtnTitle: "No", completion: { (index, title) in
                if index == 0 {
                    self.userDeleteAPICall()
                } else {
                    for i in 0..<self.arrayMenu.count {
                        self.arrayMenu[i].selected = false
                    }
                    self.tableView.reloadData()
                }
            })
        } else if(tag == 9) {
            AJAlertController.initialization().showAlert(aStrMessage: "KeylblLogoutMessage".localize, aCancelBtnTitle: "Yes", aOtherBtnTitle: "No", completion: { (index, title) in
                if index == 0 {
                    self.call_logout()
                } else {
                    for i in 0..<self.arrayMenu.count {
                        self.arrayMenu[i].selected = false
                    }
                    self.tableView.reloadData()
                }
            })
        } else if(tag == 11) {
            let vc = UIStoryboard.init(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "ChangePwdVC") as? ChangePwdVC
            self.navigationController?.pushViewController(vc!, animated: true)
        }
    }
}

extension SettingsVC {
    func call_logout(){
        var paramer: [String: Any] = [:]
        paramer["user_id"] = Global.kretriveUserData().User_Id!
        
        WebService.call.POST(filePath: APIConstant.Request.logout, params: paramer, enableInteraction: false, showLoader: true, viewObj: (Global.appDelegate.window?.rootViewController!.view)!, onSuccess: { (result, success) in
            print("result-----\(result)")
            if let Dict = result as? [String:Any] {
                if let str = Dict["code"] as? Int , str == 0 {
                    Global.appDelegate.logoutUser()
                }
                
            }
            
        }) {
            print("Error \(self.description)")
        }
    }
    
    func userDeleteAPICall(){
        var paramer: [String: Any] = [:]
        
        paramer["user_id"] = Global.kretriveUserData().User_Id!
        
        WebService.call.POST(filePath: APIConstant.Request.delete, params: paramer, enableInteraction: false, showLoader: true, viewObj: (Global.appDelegate.window?.rootViewController!.view)!, onSuccess: { (result, success) in
            print("result-----\(result)")
            if let Dict = result as? [String:Any]
            {
                if let str = Dict["code"] as? Int , str == 0 {
                    Global.appDelegate.logoutUser()
                }
                
            }
            
        }) {
            print("Error \(self.description)")
        }
    }
}
