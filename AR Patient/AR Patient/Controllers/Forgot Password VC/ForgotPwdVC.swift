//
//  ForgotPwdVC.swift
//  AR Patient
//
//  Created by Krupali on 27/04/20.
//  Copyright © 2020 Silicon. All rights reserved.
//

import UIKit

class ForgotPwdVC: UIViewController {
    
    @IBOutlet weak var txtMail: IPTextField!
    @IBOutlet weak var btnSubmit: IPButton!
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.setNeedsStatusBarAppearanceUpdate()
        
        txtMail.placeholder = "KeylblLSEmail".localize
        self.txtMail.text = ""
        self.navigationController?.navigationBar.isHidden = true
        setLanguageText()
    }
    
    // MARK: - 
    // MARK: - BUTTON ACTION METHODS
    @IBAction func btnBack(_ sender: Any) {
        self.view.endEditing(true)
        self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func btnSubmit(_ sender: Any) {
        self.view.endEditing(true)
        self.validateInformation()
    }
    
    //MARK:- CUSTOM METHODS
    func setLanguageText() -> Void {
        self.txtMail.delegate = self
        
        txtMail.placeholder = "KeylblLSEmail".localize
        
        txtMail.withImage(direction: .Left, image: #imageLiteral(resourceName: "Email"), colorSeparator: UIColor.clear, colorBorder: UIColor.clear)
        
        btnSubmit.setTitle("KeybtnResetPassword".localize.uppercased(), for: .normal)
    }
    
    func validateInformation() {
        let strEmail = txtMail.text!.trimmingCharacters(in: .whitespaces)
        
        guard strEmail.count > 0 else{
            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "KeyEmailUserValidation".localize) { (index, title) in }
            return
        }
        guard strEmail.isEmail else{
            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "KeyValidEmailValidation".localize) { (index, title) in }
            return
        }
        
        self.call_Forgot()
    }
    
    // MARK: - 
    // MARK: - API CALLER METHODS
    func call_Forgot() {
        let strEmail = txtMail.text!.trimmingCharacters(in: .whitespaces)
        
        var paramer: [String: Any] = [:]
        paramer["email"] = strEmail
        
        WebService.call.POST(filePath: APIConstant.Request.forgotPassword, params: paramer, enableInteraction: false, showLoader: true, viewObj: self.view, onSuccess: { (result, success) in
            //message
            if let Dict = result as? [String:Any] {
                
                if let str = Dict["code"] as? Int , str == 0{
                    AJAlertController.initialization().showAlertWithOkButton(aStrMessage: LocalizeHelper().localizedString(forKey: "KeylblCheckEmail")) { (index, title) in
                        let vc = UIStoryboard.init(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "ResetPwdVC") as? ResetPwdVC
                        vc!.strEmail = strEmail
                        self.navigationController?.pushViewController(vc!, animated: true)
                        
                        self.txtMail.text = ""
                    }
                }else{
                    
                    AJAlertController.initialization().showAlertWithOkButton(aStrMessage: Dict["message"] as? String ?? "") { (index, title) in
                        
                    }
                }
            }
        }) {
            print("Error \(self.description)")
        }
    }
    
}

extension ForgotPwdVC : UITextFieldDelegate {
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }
}
