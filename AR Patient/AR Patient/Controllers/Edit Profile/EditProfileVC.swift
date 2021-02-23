//
//  EditProfileVC.swift
//  AR Patient
//
//  Created by Krupali on 01/05/20.
//  Copyright © 2020 Silicon. All rights reserved.
//

import UIKit
import SwiftyJSON

class EditProfileVC: UIViewController {
    
    @IBOutlet weak var viewHeader: UIView!
    @IBOutlet weak var lblTitle: IPAutoScalingLabel!
    @IBOutlet weak var imgAvatar: UIImageView!
    @IBOutlet weak var txtName: IPTextField!
    @IBOutlet weak var txtMail: IPTextField!
    @IBOutlet weak var btnUpdate: IPButton!
    @IBOutlet weak var btnBack: UIButton!
    @IBOutlet weak var checkToSeeProfile: CheckBox!
    
    var imgValidation = false
    var isPublic = "0"
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.checkToSeeProfile.isChecked = true
        
        self.viewHeader.layer.frame = CGRect(x: 0, y: 0, width: UIScreen.main.bounds.size.width, height: 60)
        self.viewHeader.roundCorners([.bottomLeft, .bottomRight], radius: 25)
        
        self.imgAvatar.layer.cornerRadius = 20
        self.imgAvatar.layer.masksToBounds = true
        self.imgAvatar.layer.borderWidth = 2
        self.imgAvatar.layer.borderColor = UIColor.init(named: "Color_White")!.cgColor
        
        let avatar = "\(APIConstant.mediaURL)\(Singleton.shared.retriveFromUserDefaults(key: kUser.avatar)!)"
        self.imgAvatar.sd_setImage(with: URL(string: avatar), placeholderImage: UIImage(named: "Avatar"), options: .refreshCached)
        self.txtName.text = Singleton.shared.retriveFromUserDefaults(key: kUser.fullName)!
        self.txtMail.text = Singleton.shared.retriveFromUserDefaults(key: kUser.email)!
        
        let is_public = Singleton.shared.retriveFromUserDefaults(key: kUser.is_public)!
        self.isPublic = is_public
        if(is_public == "1") {
            self.checkToSeeProfile.isChecked = true
        } else {
            self.checkToSeeProfile.isChecked = false
        }
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.txtName.layer.cornerRadius = self.txtName.frame.height/2
        self.txtName.layer.masksToBounds = true
        self.txtMail.layer.cornerRadius = self.txtMail.frame.height/2
        self.txtMail.layer.masksToBounds = true
        
        self.viewHeader.layer.frame = CGRect(x: 0, y: 0, width: UIScreen.main.bounds.size.width, height: 60)
        self.viewHeader.roundCorners([.bottomLeft, .bottomRight], radius: 25)
        
        self.imgAvatar.layer.cornerRadius = 20
        self.imgAvatar.layer.masksToBounds = true
        self.imgAvatar.layer.borderWidth = 2
        self.imgAvatar.layer.borderColor = UIColor.init(named: "Color_White")!.cgColor
        
        setLanguageText()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        self.navigationController?.navigationBar.isHidden = true
        self.txtName.layer.cornerRadius = self.txtName.frame.height/2
        self.txtName.layer.masksToBounds = true
        self.txtMail.layer.cornerRadius = self.txtMail.frame.height/2
        self.txtMail.layer.masksToBounds = true
        
        self.viewHeader.layer.frame = CGRect(x: 0, y: 0, width: UIScreen.main.bounds.size.width, height: 60)
        self.viewHeader.roundCorners([.bottomLeft, .bottomRight], radius: 25)
        
        self.imgAvatar.layer.cornerRadius = 20
        self.imgAvatar.layer.masksToBounds = true
        self.imgAvatar.layer.borderWidth = 2
        self.imgAvatar.layer.borderColor = UIColor.init(named: "Color_White")!.cgColor
        
        setLanguageText()
    }
    
    // MARK: - 
    // MARK: - BUTTON ACTION METHODS
    @IBAction func checkToSeeProfile(_ sender: Any) {
        if(self.checkToSeeProfile.isChecked != true){
            self.isPublic = "1"
        } else{
            self.isPublic = "0"
        }
    }
    
    @IBAction func btnBack(_ sender: Any) {
        self.view.endEditing(true)
        self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func chooseImg(_ sender: Any) {
        self.view.endEditing(true)
        
        PhotosUtility.shared.openCameraInControllerWithGallery(self, position: imgAvatar.frame, completionBlock: { (image,originalImage) in
            guard let image = originalImage else { return }
            self.imgAvatar.image = image
            self.imgAvatar.contentMode = .scaleAspectFill
            self.imgAvatar.clipsToBounds = true
            self.imgAvatar.layer.masksToBounds = true
            self.imgValidation = true
            
            self.imageAPICall()
        })
    }
    
    @IBAction func btnUpdate(_ sender: Any) {
        self.view.endEditing(true)
        self.validationInfo()
    }
    
    //MARK:- CUSTOM METHODS
    func setLanguageText(){
        lblTitle.text = "KeylblEditProfile".localize
        txtName.placeholder = "KeylblUserName".localize
        txtMail.placeholder = "KeylblEmail".localize
        btnUpdate.setTitle("KeybtnUpdate".localize.uppercased(), for: .normal)
        
        txtName.delegate = self
        txtMail.delegate = self
        
        txtMail.withoutImage(direction: .Left, colorSeparator: UIColor.clear, colorBorder: UIColor.clear)
        txtName.withoutImage(direction: .Left, colorSeparator: UIColor.clear, colorBorder: UIColor.clear)
    }
    
    func validationInfo() {
        self.view.endEditing(true)
        let strEmail = txtMail.text!.trimmingCharacters(in: .whitespaces)
        let strName = txtName.text!.trimmingCharacters(in: .whitespaces)
        
        /*guard imgValidation == true else {
            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "KeyProfileImageValidation".localize) { (index, title) in }
            return
        }*/
        
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
        
        self.profileAPICall()
    }
    
    //MARK: - 
    //MARK:- API CALL METHODS
    func imageAPICall(){
        var paramer: [String: Any] = [:]
        paramer["user_id"] = Global.kretriveUserData().User_Id!
        if let avatarImg = self.imgAvatar.image {
            let imgData = avatarImg.compressImageToUpload()
            paramer["avatar"] = imgData.imageData
        }
        
        WebService.call.ImageUpload(filePath: APIConstant.Request.changeAvatar, params: paramer, enableInteraction: false, showLoader: true, viewObj: self.view, onSuccess: { (result, success) in
            
            if let Dict = result as? [String:Any] {
                if let str = Dict["code"] as? Int , str == 0 {
                    Singleton.shared.saveToUserDefaults(value: JSON(Dict["avatar"]!).stringValue, forKey: kUser.avatar)
                } else {
                    AJAlertController.initialization().showAlertWithOkButton(aStrMessage: Dict["message"] as! String) { (index, title) in }
                }
                
            }
        }) {
            print("Error \(self.description)")
        }
    }
    
    func profileAPICall(){
        var paramer: [String: Any] = [:]
        paramer["user_id"] = Global.kretriveUserData().User_Id!
        paramer["name"] = self.txtName.text!
        paramer["email"] = self.txtMail.text!
        paramer["is_public"] = self.isPublic
        
        WebService.call.POST(filePath: APIConstant.Request.changeProfile, params: paramer, enableInteraction: false, showLoader: true, viewObj: self.view, onSuccess: { (result, success) in
            if let Dict = result as? [String:Any] {
                
                if let str = Dict["code"] as? Int , str == 0{
                    Singleton.shared.saveToUserDefaults(value: self.txtMail.text!, forKey: kUser.email)
                    Singleton.shared.saveToUserDefaults(value: self.txtName.text!, forKey: kUser.fullName)
                    Singleton.shared.saveToUserDefaults(value: self.isPublic, forKey: kUser.is_public)
                    
                    AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "Profile has been updated successfully.") { (index, title) in
                        /*self.view.endEditing(true)
                        self.navigationController?.popViewController(animated: true)*/
                        Global().delay(delay: 0.5, closure: {
                            self.btnBack(self.btnBack)
                        })
                    }
                } else{
                    AJAlertController.initialization().showAlertWithOkButton(aStrMessage: Dict["message"] as? String ?? "") { (index, title) in }
                }
            }
            
        }) {
            print("Error \(self.description)")
        }
    }
}

extension EditProfileVC : UITextFieldDelegate {
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
            if str.count <= 20 {
                return true//string != " "
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
