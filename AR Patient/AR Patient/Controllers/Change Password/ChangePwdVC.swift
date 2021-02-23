//
//  ChangePwdVC.swift
//  AR Patient
//
//  Created by Krupali on 30/04/20.
//  Copyright © 2020 Silicon. All rights reserved.
//

import UIKit

class ChangePwdVC: UIViewController {
    
    @IBOutlet weak var lblTitle: IPAutoScalingLabel!
    @IBOutlet weak var viewHeader: UIView!
    @IBOutlet weak var txtCurrentPwd: passwordTextField!
    @IBOutlet weak var txtNewPwd: passwordTextField!
    @IBOutlet weak var txtConfirmPwd: passwordTextField!
    @IBOutlet weak var btnChangePwd: IPButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        viewHeader.layer.frame = CGRect(x: 0, y: 0, width: UIScreen.main.bounds.size.width, height: 60)
        viewHeader.roundCorners([.bottomLeft, .bottomRight], radius: 25)
        setLanguageText() 
        
    }
    
    override func viewDidAppear(_ animated: Bool) {
        self.navigationController?.navigationBar.isHidden = true
        self.txtCurrentPwd.layer.cornerRadius = self.txtCurrentPwd.frame.height/2
        self.txtCurrentPwd.layer.masksToBounds = true
        self.txtNewPwd.layer.cornerRadius = self.txtNewPwd.frame.height/2
        self.txtNewPwd.layer.masksToBounds = true
        self.txtConfirmPwd.layer.cornerRadius = self.txtConfirmPwd.frame.height/2
        self.txtConfirmPwd.layer.masksToBounds = true
        
        self.txtCurrentPwd.addRightView(img: #imageLiteral(resourceName: "HideEyeBlack"), selected: #imageLiteral(resourceName: "EyeBlack"))
        self.txtNewPwd.addRightView(img: #imageLiteral(resourceName: "HideEyeBlack"), selected: #imageLiteral(resourceName: "EyeBlack"))
        self.txtConfirmPwd.addRightView(img: #imageLiteral(resourceName: "HideEyeBlack"), selected: #imageLiteral(resourceName: "EyeBlack"))
        setLanguageText()
    }
    
    // MARK: - 
    // MARK: - BUTTON ACTION METHODS
    @IBAction func btnBack(_ sender: Any) {
        self.view.endEditing(true)
        self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func btnChangePwd(_ sender: Any) {
        self.view.endEditing(true)
        self.validationInfo()
    }
    
    //MARK:- CUSTOM METHODS
    func setLanguageText(){
        txtCurrentPwd.placeholder = "KeylblCurrentPassword".localize
        txtNewPwd.placeholder = "KeylblNewPassword".localize
        txtConfirmPwd.placeholder = "KeylblConfirmNewPassword".localize
        lblTitle.text = "KeybtnChangePassword".localize
        btnChangePwd.setTitle("KeylblChangePassword".localize.uppercased(), for: .normal)
        
        txtCurrentPwd.delegate = self
        txtNewPwd.delegate = self
        txtConfirmPwd.delegate = self
        
        self.txtCurrentPwd.addRightView(img: #imageLiteral(resourceName: "HideEyeBlack"), selected: #imageLiteral(resourceName: "EyeBlack"))
        self.txtNewPwd.addRightView(img: #imageLiteral(resourceName: "HideEyeBlack"), selected: #imageLiteral(resourceName: "EyeBlack"))
        self.txtConfirmPwd.addRightView(img: #imageLiteral(resourceName: "HideEyeBlack"), selected: #imageLiteral(resourceName: "EyeBlack"))
        
        txtCurrentPwd.withoutImage(direction: .Left, colorSeparator: UIColor.clear, colorBorder: UIColor.clear)
        txtNewPwd.withoutImage(direction: .Left, colorSeparator: UIColor.clear, colorBorder: UIColor.clear)
        txtConfirmPwd.withoutImage(direction: .Left, colorSeparator: UIColor.clear, colorBorder: UIColor.clear)
        
        self.txtCurrentPwd.layer.cornerRadius = self.txtCurrentPwd.frame.height/2
        self.txtCurrentPwd.layer.masksToBounds = true
        self.txtNewPwd.layer.cornerRadius = self.txtNewPwd.frame.height/2
        self.txtNewPwd.layer.masksToBounds = true
        self.txtConfirmPwd.layer.cornerRadius = self.txtConfirmPwd.frame.height/2
        self.txtConfirmPwd.layer.masksToBounds = true
    }
    
    func validationInfo() {
        self.view.endEditing(true)
        let strCurrentPwd = txtCurrentPwd.text!.trimmingCharacters(in: .whitespaces)
        let strNewPwd = txtNewPwd.text!.trimmingCharacters(in: .whitespaces)
        let strConfirmPwd = txtConfirmPwd.text!.trimmingCharacters(in: .whitespaces)
        
        guard strCurrentPwd.count > 0 else{
            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "keyCurrentPasswordValidation".localize) { (index, title) in }
            return
        }
        
        guard strNewPwd.count > 0 else{
            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "keyNewPasswordValidation".localize) { (index, title) in }
            return
        }
        guard strNewPwd.count >= 8 else {
            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "KeyNewPassvalidation".localize) { (index, title) in }
            return
        }
        
        guard strConfirmPwd.count > 0 else{
            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "keyConfirmPasswordValidation".localize) { (index, title) in }
            return
        }
        
        guard strConfirmPwd == strNewPwd else {
            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "keySamePasswordValidation".localize) { (index, title) in }
            return
        }
        self.changePasswordAPICall()
    }
    
    // MARK: - 
    // MARK: - API CALLER METHODS
    private func changePasswordAPICall(){
        
        var paramer: [String: Any] = [:]
        paramer["user_id"] = Global.kretriveUserData().User_Id!
        paramer["new_password"] = self.txtConfirmPwd.text!
        paramer["old_password"] = self.txtCurrentPwd.text!
        
        WebService.call.POST(filePath: APIConstant.Request.changePassword, params: paramer, enableInteraction: false, showLoader: true, viewObj: self.view, onSuccess: { (result, success) in
            if let Dict = result as? [String:Any] {
                
                if let str = Dict["code"] as? Int , str == 0{
                    self.txtCurrentPwd.text = ""
                    self.txtNewPwd.text = ""
                    self.txtConfirmPwd.text = ""
                    
                    AJAlertController.initialization().showAlertWithOkButton(aStrMessage: LocalizeHelper().localizedString(forKey: "KeyChangePasswordSuccessMessage")) { (index, title) in
                    }
                } else{
                    AJAlertController.initialization().showAlertWithOkButton(aStrMessage: Dict["message"] as? String ?? "") { (index, title) in
                        
                    }
                }
            }

        }) {
            print("Error \(self.description)")
        }
    }
}

extension ChangePwdVC: UITextFieldDelegate{
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        if textField == txtCurrentPwd {
            let str = (textField.text! + string)
            if str.count <= 20 {
                return string != " "
            }
            else{
                return false
            }
        }
        else if textField == txtNewPwd {
            let str = (textField.text! + string)
            if str.count <= 20 {
                return string != " "
            }
            else{
                return false
            }
        }
        else if textField == txtConfirmPwd {
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
}
