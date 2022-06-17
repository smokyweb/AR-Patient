//
//  RegistrationVC.swift
//  AR Patient
//
//  Created by Knoxweb on 27/04/20.
//  Copyright © 2020 Knoxweb. All rights reserved.
//

import UIKit

class RegistrationVC: UIViewController {
    
    //MARK:- IBOUTLET VARIABLE
    @IBOutlet weak var imgAvatar: UIImageView!
    @IBOutlet weak var txtName: IPTextField!
    @IBOutlet weak var txtEmail: IPTextField!
    @IBOutlet weak var txtPassword: passwordTextField!
    @IBOutlet weak var txtConfirmPassword: passwordTextField!
    @IBOutlet weak var lblTermsCondition: IPAutoScalingLabel!
    @IBOutlet weak var btnSignUp: IPButton!
    @IBOutlet weak var btnLogin: IPButton!
    @IBOutlet weak var checkToSeeProfile: CheckBox!
    
    let tcText = "KeylblAcceptTC".localize
    var imgValidation = false
    
    var isPublic = "0"
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        
        self.checkToSeeProfile(self.checkToSeeProfile)
        self.checkToSeeProfile.isChecked = true
        
        self.imgAvatar.layer.cornerRadius = self.imgAvatar.frame.height / 2
        self.imgAvatar.layer.masksToBounds = true
        self.imgAvatar.layer.borderWidth = 2
        self.imgAvatar.layer.borderColor = UIColor.init(named: "Color_White")!.cgColor
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.txtPassword.layer.cornerRadius = self.txtPassword.frame.height/2
        self.txtPassword.layer.masksToBounds = true
        self.txtConfirmPassword.layer.cornerRadius = self.txtPassword.frame.height/2
        self.txtConfirmPassword.layer.masksToBounds = true
        
        self.imgAvatar.layer.cornerRadius = self.imgAvatar.frame.height / 2
        self.imgAvatar.layer.masksToBounds = true
        self.imgAvatar.layer.borderWidth = 2
        self.imgAvatar.layer.borderColor = UIColor.init(named: "Color_White")!.cgColor
        
        self.setLanguageText()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        self.txtPassword.layer.cornerRadius = self.txtPassword.frame.height/2
        self.txtPassword.layer.masksToBounds = true
        self.txtConfirmPassword.layer.cornerRadius = self.txtPassword.frame.height/2
        self.txtConfirmPassword.layer.masksToBounds = true
        
        self.imgAvatar.layer.cornerRadius = self.imgAvatar.frame.height / 2
        self.imgAvatar.layer.masksToBounds = true
        self.imgAvatar.layer.borderWidth = 2
        self.imgAvatar.layer.borderColor = UIColor.init(named: "Color_White")!.cgColor
        
        self.txtPassword.addRightView(img: #imageLiteral(resourceName: "HideEye"), selected: #imageLiteral(resourceName: "Eye"))
        self.txtConfirmPassword.addRightView(img: #imageLiteral(resourceName: "HideEye"), selected: #imageLiteral(resourceName: "Eye"))
    }
    
    
    //MARK:- CUSTOM METHODS
    func setLanguageText() -> Void {
        self.txtName.delegate = self
        self.txtEmail.delegate = self
        self.txtPassword.delegate = self
        self.txtConfirmPassword.delegate = self
        
        self.txtName.placeholder = "KeylblFullName".localize
        self.txtEmail.placeholder = "KeylblEmail".localize
        self.txtPassword.placeholder = "keylblPassword".localize
        self.txtConfirmPassword.placeholder = "KeylblConfirmPassword".localize
        
        self.txtName.withImage(direction: .Left, image: #imageLiteral(resourceName: "Password"), colorSeparator: UIColor.clear, colorBorder: UIColor.clear)
        self.txtEmail.withImage(direction: .Left, image: #imageLiteral(resourceName: "Email"), colorSeparator: UIColor.clear, colorBorder: UIColor.clear)
        self.txtPassword.withImage(direction: .Left, image: #imageLiteral(resourceName: "User"), colorSeparator: UIColor.clear, colorBorder: UIColor.clear)
        self.txtConfirmPassword.withImage(direction: .Left, image: #imageLiteral(resourceName: "User"), colorSeparator: UIColor.clear, colorBorder: UIColor.clear)
        
        self.btnSignUp.setTitle("KeybtnSignUp".localize, for: .normal)
        
        let underlineAttriString = NSMutableAttributedString(string: tcText)
        let range1 = (tcText as NSString).range(of: "Terms & Condition")
        underlineAttriString.addAttribute(NSAttributedString.Key.underlineStyle, value: NSUnderlineStyle.single.rawValue, range: range1)
        underlineAttriString.addAttribute(NSAttributedString.Key.font, value: UIFont.init(name: Theme.Font.SemiBold, size: Theme.Font.size.TxtSize_12)!, range: range1)
        underlineAttriString.addAttribute(NSAttributedString.Key.foregroundColor, value: UIColor.init(named: "Color_White")!, range: range1)
        lblTermsCondition.attributedText = underlineAttriString
        lblTermsCondition.isUserInteractionEnabled = true
        lblTermsCondition.addGestureRecognizer(UITapGestureRecognizer(target:self, action: #selector(tapLabel(gesture:))))
        
        self.txtPassword.placeHolderColor = UIColor.init(named: "Color_White")!
        self.txtConfirmPassword.placeHolderColor = UIColor.init(named: "Color_White")!
        
        self.txtPassword.layer.cornerRadius = self.txtPassword.frame.height/2
        self.txtPassword.layer.masksToBounds = true
        self.txtConfirmPassword.layer.cornerRadius = self.txtPassword.frame.height/2
        self.txtConfirmPassword.layer.masksToBounds = true
    }
    
    func validationInfo() {
        self.view.endEditing(true)
        let strName = txtName.text!.trimmingCharacters(in: .whitespaces)
        let strEmail = txtEmail.text!.trimmingCharacters(in: .whitespaces)
        let strPassword = txtPassword.text!.trimmingCharacters(in: .whitespaces)
        let strConfPassword = txtConfirmPassword.text!.trimmingCharacters(in: .whitespaces)
        
        guard imgValidation == true else {
            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "KeyProfileImageValidation".localize) { (index, title) in }
            return
        }
        guard strName.count > 0 else{
            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "KeyFullNameValidation".localize) { (index, title) in }
            return
        }
        guard strEmail.count > 0 else{
            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "KeyEmailUserValidation".localize) { (index, title) in }
            return
        }
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
        guard strConfPassword.count > 0 else{
            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "keyConfirmPasswordValidation".localize) { (index, title) in }
            return
        }
        guard strConfPassword == strPassword else {
            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "keyPasswordMatchValidation".localize) { (index, title) in }
            return
        }
        
        self.signUpAPICall()
    }
    
    
    // MARK: - 
    // MARK: - BUTTON ACTION METHODS
    @IBAction func checkToSeeProfile(_ sender: UIButton) {
        if(self.checkToSeeProfile.isChecked != true){
            self.isPublic = "1"
        } else{
            self.isPublic = "0"
        }
    }
    
    @IBAction func btnSignUp(_ sender: IPButton) {
        self.view.endEditing(true)
        validationInfo()
    }
    
    @IBAction func btnLogin(_ sender: IPButton) {
        self.view.endEditing(true)
        self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func btnChooseAvtar(_ sender: Any) {
        self.view.endEditing(true)
        
        PhotosUtility.shared.openCameraInControllerWithGallery(self, position: imgAvatar.frame, completionBlock: { (image,originalImage) in
            guard let image = originalImage else { return }
            self.imgAvatar.image = image
            self.imgAvatar.contentMode = .scaleAspectFill
            self.imgAvatar.clipsToBounds = true
            self.imgAvatar.layer.masksToBounds = true
            self.imgValidation = true
        })
    }
    
    @objc func tapLabel(gesture: UITapGestureRecognizer) {
        let termsRange = (tcText as NSString).range(of: "Terms & Condition")
        
        let vc = UIStoryboard.init(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "WebViewController") as? WebViewController
        vc?.cms_id = Singleton.shared.cms_id_terms
        self.navigationController?.pushViewController(vc!, animated: true)
    }
    
    // MARK: - 
    // MARK: - API CALLER METHODS
    func signUpAPICall(){
        var paramer: [String: Any] = [:]
        paramer["is_public"] = isPublic
        paramer["name"] = txtName.text!
        paramer["email"] = txtEmail.text!
        paramer["password"] = txtPassword.text!
        if let avatarImg = self.imgAvatar.image {
            let imgData = avatarImg.compressImageToUpload()
            paramer["avatar"] = imgData.imageData
        }
        paramer["is_public"] = self.isPublic
        
        WebService.call.ImageUpload(filePath: APIConstant.Request.signUp, params: paramer, enableInteraction: false, showLoader: true, viewObj: self.view, onSuccess: { (result, success) in
            
            if let Dict = result as? [String:Any] {
                if let str = Dict["code"] as? Int , str == 0 {
                    AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "Thank you for registering with us.") { (index, title) in
                        self.txtName.text = ""
                        self.txtEmail.text = ""
                        self.txtPassword.text = ""
                        self.txtConfirmPassword.text = ""
                        
                        self.view.endEditing(true)
                        
                        Global.appDelegate.setnavigation(viewController: "LoginVC")
                    }
                }else{
                    AJAlertController.initialization().showAlertWithOkButton(aStrMessage: Dict["message"] as! String) { (index, title) in
                        
                    }
                }
            }
        }) {
            print("Error \(self.description)")
        }
    }
}

extension RegistrationVC : UITextFieldDelegate {
    func textFieldShouldBeginEditing(_ textField: UITextField) -> Bool {
        return true
    }
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        if textField == txtName {
            let str = (textField.text! + string)
            if str.count <= 20 {
                return true//string != " "
            }
            else{
                return false
            }
        } else if textField == txtEmail {
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
        else if textField == txtConfirmPassword {
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
