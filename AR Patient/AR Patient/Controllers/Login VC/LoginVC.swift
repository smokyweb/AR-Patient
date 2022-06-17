//
//  LoginVC.swift
//  AR Patient
//
//  Created by Knoxweb on 27/04/20.
//  Copyright © 2020 Knoxweb. All rights reserved.
//

import UIKit
import SwiftyJSON

class LoginVC: UIViewController {
    
    @IBOutlet weak var txtMail: IPTextField!
    @IBOutlet weak var txtPassword: passwordTextField!
    @IBOutlet weak var btnLogin: IPButton!
    @IBOutlet weak var btnRegister: IPButton!
    @IBOutlet weak var btnForgotPassword: IPButton!
    @IBOutlet weak var imgLogo: UIImageView!
    
    let userDefaults = UserDefaults.standard
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.setNeedsStatusBarAppearanceUpdate()
        self.navigationController?.navigationBar.isHidden = true
        //self.btnRegistration.titleLabel?.textColor = Theme.color.btnText
        
        self.txtMail.text = ""
        self.txtPassword.text = ""
        setLanguageText()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        self.txtPassword.addRightView(img: #imageLiteral(resourceName: "HideEye"), selected: #imageLiteral(resourceName: "Eye"))
        self.txtPassword.layer.cornerRadius = self.txtPassword.frame.height/2
        self.txtPassword.layer.masksToBounds = true
    }
    
    
    
    // MARK: - 
    // MARK: - BUTTON ACTION METHODS
    @IBAction func btnLogin(_ sender: Any) {
        self.view.endEditing(true)
        self.validateInformation()
    }
    
    @IBAction func btnRegister(_ sender: Any) {
        self.view.endEditing(true)
        let vc = UIStoryboard.init(name: "Development", bundle: Bundle.main).instantiateViewController(withIdentifier: "RegistrationVC") as? RegistrationVC
        self.navigationController?.pushViewController(vc!, animated: true)
    }
    
    @IBAction func btnForgotPassword(_ sender: Any) {
        self.view.endEditing(true)
        let vc = UIStoryboard.init(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "ForgotPwdVC") as? ForgotPwdVC
        self.navigationController?.pushViewController(vc!, animated: true)
    }
    
    //MARK:- CUSTOM METHODS
    func setLanguageText() -> Void {
        self.txtMail.delegate = self
        self.txtPassword.delegate = self
        
        txtMail.placeholder = "KeylblLSEmail".localize
        txtPassword.placeholder = "keylblPassword".localize
        
        txtMail.withImage(direction: .Left, image: #imageLiteral(resourceName: "Password"), colorSeparator: UIColor.clear, colorBorder: UIColor.clear)
        txtPassword.withImage(direction: .Left, image: #imageLiteral(resourceName: "User"), colorSeparator: UIColor.clear, colorBorder: UIColor.clear)
        
        btnLogin.setTitle("KeybtnLogin".localize, for: .normal)
        btnForgotPassword.setTitle("KeybtnForgot".localize, for: .normal)
        
        self.txtMail.placeHolderColor = UIColor.init(named: "Color_White")!
        self.txtPassword.placeHolderColor = UIColor.init(named: "Color_White")!
    }
    
    func validateInformation() {
        let strEmail = txtMail.text!.trimmingCharacters(in: .whitespaces)
        let strPassword = txtPassword.text!.trimmingCharacters(in: .whitespaces)
        
        guard strEmail.isEmail else{
            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "KeyValidEmailValidation".localize) { (index, title) in }
            return
        }
        
        guard strPassword.count > 0 else{
            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "keyPasswordValidation".localize) { (index, title) in }
            return
        }
        guard strPassword.count >= 8 else {
            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "KeyPassCharacterValidation".localize) { (index, title) in }
            return
        }
        
        self.call_Login()
    }
    
    // MARK: - 
    // MARK: - API CALLER METHODS
    func call_Login() {
        var paramer: [String: Any] = [:]
        paramer["username"] = txtMail.text!
        paramer["password"] = txtPassword.text!
        paramer["push_id"] = Singleton.shared.retriveFromUserDefaults(key: Global.g_UserDefaultKey.DeviceToken)!
        paramer["device_token"] = UIDevice.current.identifierForVendor?.uuidString
        paramer["device_type"] = "2"
        
        WebService.call.POST(filePath: APIConstant.Request.login, params: paramer, enableInteraction: false, showLoader: true, viewObj: view, onSuccess: { (result, success) in
            
            if let Dict = result as? [String:Any] {
                if let str = Dict["code"] as? Int , str == 0{
                    Global().delay(delay: 1.5, closure: {
                        let avatar = JSON(Dict["avatar"]!).stringValue
                        let email = JSON(Dict["email"]!).stringValue
                        let name = JSON(Dict["name"]!).stringValue
                        let session_id = JSON(Dict["session_id"]!).stringValue
                        let user_id = JSON(Dict["user_id"]!).stringValue
                        let role = JSON(Dict["role"]!).stringValue
                        let is_public = JSON(Dict["is_public"]!).stringValue
                        
                        Singleton.shared.saveToUserDefaults(value: "1" , forKey: kUser.IsLoggedIn)
                        Singleton.shared.saveToUserDefaults(value: avatar, forKey: kUser.avatar)
                        Singleton.shared.saveToUserDefaults(value: email, forKey: kUser.email)
                        Singleton.shared.saveToUserDefaults(value: name, forKey: kUser.fullName)
                        Singleton.shared.saveToUserDefaults(value: session_id, forKey: kUser.session_id)
                        Singleton.shared.saveToUserDefaults(value: user_id, forKey: kUser.User_Id)
                        Singleton.shared.saveToUserDefaults(value: role, forKey: kUser.role)
                        Singleton.shared.saveToUserDefaults(value: is_public, forKey: kUser.is_public)
                        
                        self.txtMail.text = ""
                        self.txtPassword.text = ""
                        
                        Global.appDelegate.setnavigation(viewController: "TabBarVC")
                    })
                }
            }
            
        }) {
            print("Error \(self.description)")
        }
    }
}

extension LoginVC : UITextFieldDelegate {
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        if textField == txtMail {
            let str = (textField.text! + string)
            if str.count <= 100 {
                return string != " "
            }
            else{
                return false
            }
        }
        else if textField == txtPassword {
            let str = (textField.text! + string)
            if str.count <= 20 {
                return string != " "
            }
            else{
                return false
            }
        }
        else {
            return true
        }
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        if textField == txtMail{
            txtPassword.becomeFirstResponder()
        } else{
            textField.resignFirstResponder()
        }
        return true
    }
}
