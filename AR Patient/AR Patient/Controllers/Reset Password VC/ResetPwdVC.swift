//
//  ResetPwdVC.swift
//  AR Patient
//
//  Created by Knoxweb on 27/04/20.
//  Copyright © 2020 Knoxweb. All rights reserved.
//

import UIKit

class ResetPwdVC: UIViewController {
    
    @IBOutlet weak var txtOTP: IPTextField!
    @IBOutlet weak var txtNewPwd: passwordTextField!
    @IBOutlet weak var txtConfirmPwd: passwordTextField!
    @IBOutlet weak var btnReset: IPButton!
    
    var strEmail = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.navigationController?.navigationBar.isHidden = true
        self.txtNewPwd.layer.cornerRadius = self.txtNewPwd.frame.height/2
        self.txtNewPwd.layer.masksToBounds = true
        self.txtConfirmPwd.layer.cornerRadius = self.txtConfirmPwd.frame.height/2
        self.txtConfirmPwd.layer.masksToBounds = true
        setLanguageText()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        self.txtNewPwd.layer.cornerRadius = self.txtNewPwd.frame.height/2
        self.txtNewPwd.layer.masksToBounds = true
        self.txtConfirmPwd.layer.cornerRadius = self.txtConfirmPwd.frame.height/2
        self.txtConfirmPwd.layer.masksToBounds = true
        self.txtNewPwd.addRightView(img: #imageLiteral(resourceName: "HideEye"), selected: #imageLiteral(resourceName: "Eye"))
        self.txtConfirmPwd.addRightView(img: #imageLiteral(resourceName: "HideEye"), selected: #imageLiteral(resourceName: "Eye"))
        self.txtNewPwd.backgroundColor = UIColor.init(named: "Color_DarkBlue")
        self.txtConfirmPwd.backgroundColor = UIColor.init(named: "Color_DarkBlue")
        
    }
    
    // MARK: - 
    // MARK: - BUTTON ACTION METHODS
    @IBAction func btnReset(_ sender: Any) {
        self.view.endEditing(true)
        self.validateInformation()
    }
    
    @IBAction func btnBack(_ sender: Any) {
        self.view.endEditing(true)
        self.navigationController?.popViewController(animated: true)
    }
    
    //MARK:- CUSTOM METHODS
    func setLanguageText() -> Void {
        self.txtOTP.delegate = self
        self.txtNewPwd.delegate = self
        self.txtConfirmPwd.delegate = self
        
        txtOTP.placeholder = "KeylblEnterOtp".localize
        txtNewPwd.placeholder = "KeylblNewPassword".localize
        txtConfirmPwd.placeholder = "KeylblConfirmNewPassword".localize
        
        txtOTP.withImage(direction: .Left, image: #imageLiteral(resourceName: "Password"), colorSeparator: UIColor.clear, colorBorder: UIColor.clear)
        txtNewPwd.withImage(direction: .Left, image: #imageLiteral(resourceName: "User"), colorSeparator: UIColor.clear, colorBorder: UIColor.clear)
        txtConfirmPwd.withImage(direction: .Left, image: #imageLiteral(resourceName: "User"), colorSeparator: UIColor.clear, colorBorder: UIColor.clear)
        
        btnReset.setTitle("KeylblResetPassword".localize.uppercased(), for: .normal)
        
        self.txtNewPwd.placeHolderColor = UIColor.init(named: "Color_White")!
        self.txtConfirmPwd.placeHolderColor = UIColor.init(named: "Color_White")!
        
        
    }
    
    private func validateInformation() {
        let strOtp = txtOTP.text!.trimmingCharacters(in: .whitespaces)
        let strPassword = txtNewPwd.text!.trimmingCharacters(in: .whitespaces)
        let strConfPassword = txtConfirmPwd.text!.trimmingCharacters(in: .whitespaces)
        
        guard strOtp.count > 0 else{
            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "keyOTPValidation".localize) { (index, title) in }
            return
        }
        
        guard strPassword.count > 0 else{
            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "keyNewPasswordValidation".localize) { (index, title) in }
            return
        }
        guard strPassword.count >= 8 else {
            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "KeyNewPassvalidation".localize) { (index, title) in }
            return
        }
        
        guard strConfPassword.count > 0 else{
            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "keyConfirmPasswordValidation".localize) { (index, title) in }
            return
        }
        guard strConfPassword == strPassword else {
            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "keySamePasswordValidation".localize) { (index, title) in }
            return
        }
        
        self.call_ResetPassword()
    }
    
    // MARK: - 
    // MARK: - API CALLER METHODS
    private func call_ResetPassword(){
        
        var paramer: [String: Any] = [:]
        paramer["email"] = self.strEmail
        paramer["otp"] = self.txtOTP.text!
        paramer["password"] = self.txtConfirmPwd.text!
        
        WebService.call.POST(filePath: APIConstant.Request.resetPassword, params: paramer, enableInteraction: false, showLoader: true, viewObj: self.view, onSuccess: { (result, success) in
            if let Dict = result as? [String:Any] {
                
                if let str = Dict["code"] as? Int , str == 0{
                    AJAlertController.initialization().showAlertWithOkButton(aStrMessage: LocalizeHelper().localizedString(forKey: "KeyChangePasswordSuccessMessage")) { (index, title) in
                        
                        Global().delay(delay: 0.5, closure: {
                            let viewControllers: [UIViewController] = self.navigationController!.viewControllers
                            for aViewController in viewControllers {
                                if aViewController is LoginVC {
                                    self.navigationController!.popToViewController(aViewController, animated: true)
                                }
                            }
                        })
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

extension ResetPwdVC : UITextFieldDelegate {
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        if textField == txtOTP {
            let str = (textField.text! + string)
            if str.count <= 6 {
                return string != " "
            }
            else{
                return false
            }
        }
        else if textField == txtNewPwd || textField == txtConfirmPwd {
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
        if textField == txtOTP{
            txtNewPwd.becomeFirstResponder()
        }else if textField == txtNewPwd{
            txtConfirmPwd.becomeFirstResponder()
        }
        else{
            textField.resignFirstResponder()
        }
        return true
    }
}
