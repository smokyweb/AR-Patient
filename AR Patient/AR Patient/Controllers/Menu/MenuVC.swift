//
//  MenuVC.swift
//  AR Patient
//
//  Created by Silicon on 29/04/20.
//  Copyright © 2020 Silicon. All rights reserved.
//

import UIKit
import SDWebImage

class MenuVC: UIViewController {
    
    @IBOutlet weak var imgAvatar: UIImageView!
    @IBOutlet weak var lblName: IPAutoScalingLabel!
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var heightTableView: NSLayoutConstraint!
    
    var arrayMenu:[MenuData] = []

    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.imgAvatar.layer.cornerRadius = self.imgAvatar.frame.height / 2
        self.imgAvatar.layer.masksToBounds = true
        self.imgAvatar.layer.borderWidth = 2
        self.imgAvatar.layer.borderColor = UIColor.init(named: "Color_White")!.cgColor

        // Do any additional setup after loading the view.
        //self.generateMenu(menu: "Edit Profile", icon: "Profile", selected: false, tag: 1)
        //self.generateMenu(menu: "Change Password", icon: "Password", selected: false, tag: 11)
        self.generateMenu(menu: "News Feed", icon: "News", selected: false, tag: 2)
        self.generateMenu(menu: "Settings", icon: "Setting", selected: false, tag: 3)
        self.generateMenu(menu: "F.A.Q.", icon: "FAQ", selected: false, tag: 5)
        self.generateMenu(menu: "About Us", icon: "About", selected: false, tag: 6)
        self.generateMenu(menu: "Terms & Conditions", icon: "Terms", selected: false, tag: 7)
        self.generateMenu(menu: "Privacy Policy", icon: "Privacy", selected: false, tag: 10)
        self.generateMenu(menu: "Contact Us", icon: "Contact", selected: false, tag: 4)
        //self.generateMenu(menu: "Delete Accout", icon: "Delete", selected: false, tag: 8)
        self.generateMenu(menu: "Logout", icon: "Logout", selected: false, tag: 9)
        
        tableView.register(UINib(nibName: "MenuCell", bundle: nil), forCellReuseIdentifier: "MenuCell")
        
        tableView.separatorStyle = .none
        tableView.delegate = self
        tableView.dataSource = self
        
        self.heightTableView.constant = CGFloat(60*self.arrayMenu.count)
    }
    
    override func viewDidAppear(_ animated: Bool) {
        self.imgAvatar.layer.cornerRadius = 20
        self.imgAvatar.layer.masksToBounds = true
        self.imgAvatar.layer.borderWidth = 2
        self.imgAvatar.layer.borderColor = UIColor.init(named: "Color_White")!.cgColor
        
        let avatar = "\(APIConstant.mediaURL)\(Singleton.shared.retriveFromUserDefaults(key: kUser.avatar)!)"
        self.imgAvatar.contentMode = .scaleAspectFill
        self.imgAvatar.clipsToBounds = true
        self.imgAvatar.sd_setImage(with: URL(string: avatar), placeholderImage: UIImage(named: "Avatar"), options: .refreshCached)
        
        self.lblName.text = Singleton.shared.retriveFromUserDefaults(key: kUser.fullName)!
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        for i in 0..<self.arrayMenu.count {
            self.arrayMenu[i].selected = false
        }
        self.tableView.reloadData()
    }
    
    func generateMenu(menu: String, icon: String, selected: Bool, tag: Int) {
        let menuData = MenuData()
        menuData.menu = menu
        menuData.icon = icon
        menuData.selected = selected
        menuData.tag = tag
        
        self.arrayMenu.append(menuData)
    }
}

extension MenuVC: UITableViewDelegate, UITableViewDataSource {
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
        if(menuData.selected) {
            cell.imgMenu.image = UIImage.init(named: "\(menuData.icon) Selected")
            let font = UIFont.init(name: Theme.Font.SemiBold, size: Theme.Font.size.TxtSize_14)
            font!.withSize(((UIScreen.main.bounds.size.width) * font!.pointSize) / 320)
            cell.lblMenu.font = font
            cell.viewBackground.backgroundColor = UIColor.init(named: "Color_DarkBlue")
        } else {
            cell.imgMenu.image = UIImage.init(named: "\(menuData.icon) Unselected")
            let font = UIFont.init(name: Theme.Font.Regular, size: Theme.Font.size.TxtSize_14)
            font!.withSize(((UIScreen.main.bounds.size.width) * font!.pointSize) / 320)
            cell.lblMenu.font = font
            cell.viewBackground.backgroundColor = UIColor.clear
        }
        
        cell.viewBackground.layer.frame = CGRect(x: 0, y: 0, width: UIScreen.main.bounds.size.width, height: 60)
        cell.viewBackground.roundCorners([.bottomLeft, .topRight], radius: 10)
        
        cell.selectionStyle = .none
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        self.manageSelection(indexPath: indexPath)
        self.tableView.reloadData()
        
        let menuData = self.arrayMenu[indexPath.row]
        let tag = menuData.tag
        
        Global().delay(delay: 0.25, closure: {
            if(tag == 1) {
                let vc = UIStoryboard.init(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "EditProfileVC") as? EditProfileVC
                self.navigationController?.pushViewController(vc!, animated: true)
            } else if(tag == 2) {
                let vc = UIStoryboard.init(name: "Development", bundle: Bundle.main).instantiateViewController(withIdentifier: "NewsFeedVC") as? NewsFeedVC
                self.navigationController?.pushViewController(vc!, animated: true)
            } else if(tag == 3) {
               let vc = UIStoryboard.init(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "SettingsVC") as? SettingsVC
               self.navigationController?.pushViewController(vc!, animated: true)
            } else if(tag == 4) {
                let vc = UIStoryboard.init(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "ContactUsVC") as? ContactUsVC
                self.navigationController?.pushViewController(vc!, animated: true)
            } else if(tag == 5) {
                let vc = UIStoryboard.init(name: "Development", bundle: Bundle.main).instantiateViewController(withIdentifier: "FaqVC") as? FaqVC
                self.navigationController?.pushViewController(vc!, animated: true)
            } else if(tag == 6) {
                let vc = UIStoryboard.init(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "WebViewController") as? WebViewController
                vc?.cms_id = Singleton.shared.cms_id_aboutUs
                self.navigationController?.pushViewController(vc!, animated: true)
            } else if(tag == 7) {
                let vc = UIStoryboard.init(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "WebViewController") as? WebViewController
                vc?.cms_id = Singleton.shared.cms_id_terms
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
            } else if(tag == 10) {
                let vc = UIStoryboard.init(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "WebViewController") as? WebViewController
                vc?.cms_id = Singleton.shared.cms_id_privacy
                self.navigationController?.pushViewController(vc!, animated: true)
            } else if(tag == 11) {
                let vc = UIStoryboard.init(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "ChangePwdVC") as? ChangePwdVC
                self.navigationController?.pushViewController(vc!, animated: true)
            }
        })
    }
    
    func manageSelection(indexPath: IndexPath) {
        for i in 0..<self.arrayMenu.count {
            self.arrayMenu[i].selected = false
        }
        self.arrayMenu[indexPath.row].selected = true
    }
}

extension MenuVC {
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
