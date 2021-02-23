//
//  ContactUsVC.swift
//  AR Patient
//
//  Created by Krupali on 28/04/20.
//  Copyright © 2020 Silicon. All rights reserved.
//

import UIKit

class ContactUsVC: UIViewController {
    
    @IBOutlet weak var viewHeader: UIView!
    @IBOutlet weak var txtName: IPTextField!
    @IBOutlet weak var txtMail: IPTextField!
    @IBOutlet weak var txtPhone: IPTextField!
    @IBOutlet weak var txtMsg: IPTextView!
    @IBOutlet weak var btnSubmit: IPButton!
    @IBOutlet weak var btnBack: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        viewHeader.layer.frame = CGRect(x: 0, y: 0, width: UIScreen.main.bounds.size.width, height: 60)
        viewHeader.roundCorners([.bottomLeft, .bottomRight], radius: 25)
        setLanguageText() 
    }
    
    override func viewWillAppear(_ animated: Bool) {
    }
    
    override func viewDidAppear(_ animated: Bool) {
        self.navigationController?.navigationBar.isHidden = true
        self.txtMsg.textColor = UIColor.lightGray
        
        self.txtName.layer.cornerRadius = self.txtName.frame.height/2
        self.txtName.layer.masksToBounds = true
        
        self.txtMail.layer.cornerRadius = self.txtMail.frame.height/2
        self.txtMail.layer.masksToBounds = true
        
        self.txtPhone.layer.cornerRadius = self.txtPhone
            .frame.height/2
        self.txtPhone.layer.masksToBounds = true
        setLanguageText()   
    }
    
    // MARK: - 
    // MARK: - BUTTON ACTION METHODS
    @IBAction func btnBack(_ sender: Any) {
        self.view.endEditing(true)
        self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func btnContactUs(_ sender: Any) {
        self.view.endEditing(true)
        self.validationInfo()
    }
    
    
    //MARK:- CUSTOM METHODS
    func setLanguageText(){
        txtName.placeholder = "KeylblUserName".localize
        txtMail.placeholder = "KeylblEmail".localize
        txtPhone.placeholder = "KeylblPhone".localize
        txtMsg.text = "KeylblMessage".localize
        btnSubmit.setTitle("KeybtnResetPassword".localize.uppercased(), for: .normal)
        
        txtName.delegate = self
        txtMail.delegate = self
        txtPhone.delegate = self
        txtMsg.delegate = self
        
        txtMail.withoutImage(direction: .Left, colorSeparator: UIColor.clear, colorBorder: UIColor.clear)
        txtName.withoutImage(direction: .Left, colorSeparator: UIColor.clear, colorBorder: UIColor.clear)
        txtPhone.withoutImage(direction: .Left, colorSeparator: UIColor.clear, colorBorder: UIColor.clear)
        
        txtMsg.tag = 102
    }
    
    func validationInfo() {
        self.view.endEditing(true)
        let strEmail = txtMail.text!.trimmingCharacters(in: .whitespaces)
        let strName = txtName.text!.trimmingCharacters(in: .whitespaces)
        let strPhone = txtPhone.text!.trimmingCharacters(in: .whitespaces).digitsOnly()
        let strMessage = txtMsg.text!.trimmingCharacters(in: .whitespaces)
        
        guard strEmail.count > 0 else{
            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "KeyEmailUserValidation".localize) { (index, title) in }
            return
        }
        
        guard strEmail.isEmail else{
            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "KeyValidEmailValidation".localize) { (index, title) in }
            return
        }
        
        guard strName.count > 0 else{
            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "KeyFullNameValidation".localize) { (index, title) in }
            return
        }
        
        guard strPhone.isNumeric else{
            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "KeyValidPhoneValidation".localize) { (index, title) in }
            return
        }
        
        guard strPhone.count == 10 else{
            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "KeyValidPhoneValidation".localize) { (index, title) in }
            return
        }
        
        guard strMessage.count > 0 && strMessage != "Message" else{
            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "KeyMessageValidation".localize) { (index, title) in }
            return
        }
        
        self.contactUsAPI()
    }
}

extension ContactUsVC : UITextFieldDelegate, UITextViewDelegate {
    
    func textView(_ textView: UITextView, shouldChangeTextIn range: NSRange, replacementText text: String) -> Bool {
        return txtMsg.text.count + (text.count - range.length) <= 300
    }
    
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
        else if textField == txtName {
            let str = (textField.text! + string)
            if str.count <= 50 {
                return true//string != " "
            }
            else{
                return false
            }
        }
        else if textField == txtPhone {
            let str = (textField.text! + string)
            if str.count <= 14 {
                textField.text = Singleton.shared.formattedNumber(number: textField.text!)
                return true
            }
            else{
                return false
            }
        }
        else {
            return true
        }
    }
    
    func textViewDidBeginEditing(_ textView: UITextView) {
        if txtMsg.text == "Message" {
            txtMsg.text = ""
            txtMsg.tag = 101
            txtMsg.textColor = UIColor.init(named: "Color_Black")
        }
    }

    func textViewDidEndEditing(_ textView: UITextView) {
        if txtMsg.text == "" {
            txtMsg.text = "Message"
            txtMsg.tag = 102
            txtMsg.textColor = UIColor.lightGray
        }
    }
    
}

extension ContactUsVC{
    func contactUsAPI() {
        var paramer: [String: Any] = [:]

        paramer["user_id"] = Global.kretriveUserData().User_Id!
        paramer["name"] = self.txtName.text!
        paramer["email"] = self.txtMail.text!
        paramer["phone"] = self.txtPhone.text!.digitsOnly()
        paramer["message"] = self.txtMsg.text!
        paramer["sys_time"] = Date().dateTimeString(withFormate: "yyyy-MM-dd HH:mm:ss")

        WebService.call.POST(filePath: APIConstant.Request.contactUs, params: paramer, enableInteraction: false, showLoader: true, viewObj: (Global.appDelegate.window?.rootViewController!.view)!, onSuccess: { (result, success) in
            
            if let Dict = result as? [String:Any] {
                if let str = Dict["code"] as? Int , str == 0{
                    self.txtName.text = ""
                    self.txtMail.text = ""
                    self.txtPhone.text = ""
                    self.txtMsg.text = "Message"
                    self.txtMsg.textColor = UIColor.lightGray
                    AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "KeySuccessSubmit".localize) { (index, title) in }
                    return
                }
            }

        }) {
            print("Error \(self.description)")
        }
    }

}
