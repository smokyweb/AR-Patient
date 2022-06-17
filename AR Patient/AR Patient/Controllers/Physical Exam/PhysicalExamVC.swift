//
//  PhysicalExamVC.swift
//  AR Patient
//
//  Created by Knoxweb on 19/05/20.
//  Copyright © 2020 Knoxweb. All rights reserved.
//

import UIKit
import SwiftyJSON
import Speech
import AVKit
import SDWebImage
import IQKeyboardManagerSwift
import Alamofire
import SceneKit

protocol ShareTestDelegate {
    func shareData(_ test_id: String?, _ body_part_id: String?)
}

class PhysicalExamVC: UIViewController {
    
    @IBOutlet weak var btnBack: UIButton!
    @IBOutlet weak var viewHeader: UIView!
    @IBOutlet weak var viewBottom: UIView!
    //@IBOutlet weak var imgAvatar: UIImageView!
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var txtMessage: IPTextField!
    @IBOutlet weak var btnSend: UIButton!
    @IBOutlet weak var btnGiveDiagnosis: IPButton!
    @IBOutlet weak var bottomGiveDiagnosis: NSLayoutConstraint!
    @IBOutlet weak var heightGiveDiagnosis: NSLayoutConstraint!
    @IBOutlet weak var bottomViewSend: NSLayoutConstraint!
    @IBOutlet weak var viewBodyDescription: UIView!
    @IBOutlet weak var lblBodyDescription: IPAutoScalingLabel!
    @IBOutlet weak var scnView: SCNView!
    @IBOutlet weak var imgBody: UIImageView!
    @IBOutlet weak var txtSymptoms: IPAutoScalingLabel!
    
    var test_id = ""
    var patient_id : String = ""
    var patient_name : String = ""
    var avatar : String = ""
    var body_description : String = ""
    var arrData = [VoiceExamModel]()
    var body_part_id : String = ""
    var arrStudentData = [VoiceExamModel]()
    var arrChatData = [ChatModel]()
    var exceptionalWords = [String]()
    var heightBtnGiveDiagnosis : CGFloat = 0.0
    var delegate: ShareTestDelegate?
    var modelUrl : String = ""
    var vital_signs : String = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        viewBodyDescription.cornerRadius = 15
        lblBodyDescription.text = body_description
        
        viewHeader.layer.frame = CGRect(x: 0, y: 0, width: UIScreen.main.bounds.size.width, height: viewHeader.frame.height)
        viewHeader.roundCorners([.bottomLeft, .bottomRight], radius: 25)
        
        viewBottom.layer.frame = CGRect(x: 0, y: 0, width: UIScreen.main.bounds.size.width, height: 60)
        viewBottom.roundCorners([.topLeft, .topRight], radius: 10)
        
        self.heightBtnGiveDiagnosis = self.heightGiveDiagnosis.constant
        self.heightGiveDiagnosis.constant = 0
        self.bottomGiveDiagnosis.constant = 0
        self.btnGiveDiagnosis.isHidden = true
        
        self.txtMessage.delegate = self
        
        txtMessage.withoutImage(direction: .Left, colorSeparator: UIColor.clear, colorBorder: UIColor.clear)
        
        self.tableView.register(UINib(nibName: "VoiceExamMeCell", bundle: nil), forCellReuseIdentifier: "VoiceExamMeCell")
        self.tableView.register(UINib(nibName: "VoiceExamPatientCell", bundle: nil), forCellReuseIdentifier: "VoiceExamPatientCell")
        
        self.tableView.delegate = self
        self.tableView.dataSource = self
        self.tableView.separatorStyle = .none
        self.tableView.estimatedRowHeight = 73.0
        
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillShow), name: UIResponder.keyboardWillShowNotification, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillHide), name: UIResponder.keyboardWillHideNotification, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(changeInputMode), name: UIResponder.keyboardDidShowNotification, object: nil)
        
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(UIInputViewController.dismissKeyboard))
        self.view.addGestureRecognizer(tap)
        
        self.imgBody.sd_setImage(with: URL(string: APIConstant.mediaURL + self.modelUrl))
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.txtSymptoms.text = self.vital_signs
    }
    
    override func viewDidAppear(_ animated: Bool) {
        IQKeyboardManager.shared.enable = false
        IQKeyboardManager.shared.enableAutoToolbar = false
        
        self.voiceExamAPI()
    }
    
    override func viewWillLayoutSubviews() {
        viewHeader.layer.frame = CGRect(x: 0, y: 0, width: UIScreen.main.bounds.size.width, height: viewHeader.frame.height)
        viewHeader.roundCorners([.bottomLeft, .bottomRight], radius: 25)
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        IQKeyboardManager.shared.enable = true
        IQKeyboardManager.shared.enableAutoToolbar = true
    }
    
    @objc func keyboardWillShow(notification: NSNotification) {
        if let keyboardSize = (notification.userInfo?[UIResponder.keyboardFrameBeginUserInfoKey] as? NSValue)?.cgRectValue {
            if self.view.frame.origin.y == 0 {
                self.scrollToBottom()
                self.bottomViewSend.constant = keyboardSize.height
            }
        }
    }
    
    @objc func keyboardWillHide(notification: NSNotification) {
        self.scrollToBottom()
        self.bottomViewSend.constant = 0
        if self.view.frame.origin.y != 0 {
        }
    }
    
    @objc func changeInputMode(notification: NSNotification) {
        if let keyboardSize = (notification.userInfo?[UIResponder.keyboardFrameEndUserInfoKey] as? NSValue)?.cgRectValue {
            if self.view.frame.origin.y == 0 {
                self.scrollToBottom()
                self.bottomViewSend.constant = keyboardSize.height
            }
        }
    }
    
    @objc func dismissKeyboard() {
        self.view.endEditing(true)
    }
    
    @IBAction func btnBack(_ sender: UIButton) {
        self.view.endEditing(true)
        self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func btnSend(_ sender: UIButton) {
        if(NetworkReachabilityManager()!.isReachable) {
            if(self.txtMessage.text! != "") {
                self.view.endEditing(true)
                
                let chatData = ChatModel()
                chatData.isMe = true
                chatData.message = self.txtMessage.text!
                
                self.arrChatData.append(chatData)
                
                self.tableView.reloadData()
                self.scrollToBottom()
                
                self.calculateAnswer(question: self.txtMessage.text!)
                
                self.txtMessage.text = ""
            }
        } else{
            Singleton.shared.showAlertWithSingleButton(strMessage: LocalizeHelper.shared.localizedString(forKey: "ERROR_CALL"))
        }
    }
    
    @IBAction func btnGiveDiagnosis(_ sender: UIButton) {
        self.view.endEditing(true)
        AJAlertController.initialization().showAlert(aStrMessage: "Are you sure you want to submit?", aCancelBtnTitle: "Yes", aOtherBtnTitle: "No", completion: { (index, title) in
            if index == 0 {
                self.submitVoiceExamAPI()
            }
        })
    }
    
    func generateJSON() -> String {
        let originalObjects = self.arrStudentData
        let encoder = JSONEncoder()
        let data = try! encoder.encode(originalObjects)
        if let JSONString = String(data: data, encoding: String.Encoding.utf8) {
            return JSONString
        }
        return ""
    }
    
    func calculateAnswer(question: String) {
        let definitionData = VoiceExamModel()
        var maxCount : Int = 0
        
        var arrAskedQuestion = question.lowercased().replacingOccurrences(of: "?", with: "", options: .literal, range: nil).replacingOccurrences(of: ".", with: "", options: .literal, range: nil).replacingOccurrences(of: "!", with: "", options: .literal, range: nil).split(separator: " ")
        arrAskedQuestion = arrAskedQuestion.filter { $0.count > 2 }
        
        for i in self.arrData {
            let arrQuestion = i.answer.lowercased().replacingOccurrences(of: "?", with: "", options: .literal, range: nil).replacingOccurrences(of: ".", with: "", options: .literal, range: nil).replacingOccurrences(of: "!", with: "", options: .literal, range: nil).split(separator: " ")
            let arrRemoved = arrQuestion.filter { !self.exceptionalWords.contains(String($0))}
            
            let arrComman  = arrAskedQuestion.filter { (string) -> Bool in
                return arrRemoved.contains(string)
            }
            
            if(arrComman.count > 0) {
                if(maxCount <= arrComman.count) {
                    maxCount = arrComman.count
                    
                    definitionData.answer = i.answer
                    definitionData.id = i.id
                    definitionData.question = question
                }
            }
        }
        
        if(maxCount > 0) {
            self.arrStudentData.append(definitionData)
            
            let chatData = ChatModel()
            chatData.isMe = false
            chatData.message = "Match found."//definitionData.answer
            
            self.arrChatData.append(chatData)
            
            self.tableView.reloadData()
            self.scrollToBottom()
        } else {
            let chatData = ChatModel()
            chatData.isMe = false
            chatData.message = "Sorry, no match found."//"Sorry, patient doesn't have any answer for the asked question."
            
            self.arrChatData.append(chatData)
            
            definitionData.answer = ""
            definitionData.id = "0"
            definitionData.question = question
            self.arrStudentData.append(definitionData)
            
            self.tableView.reloadData()
            self.scrollToBottom()
        }
        
        self.heightGiveDiagnosis.constant = self.heightBtnGiveDiagnosis
        self.bottomGiveDiagnosis.constant = 8
        self.btnGiveDiagnosis.isHidden = false
    }
    
    func scrollToBottom(){
        if(self.arrChatData.count > 0) {
            DispatchQueue.main.async {
                let indexPath = IndexPath(row: self.arrChatData.count-1, section: 0)
                self.tableView.scrollToRow(at: indexPath, at: .bottom, animated: false)
            }
        }
    }
    
    func loadModel(url: URL) {
        let url = url
        let scene = try! SCNScene(url: url, options: [.checkConsistency: true])
        
        // Set the scene to the view
        self.scnView.scene = scene
        self.scnView.loops = true
    }
}


extension PhysicalExamVC: UITableViewDataSource, UITableViewDelegate {
    
    func VoiceExamVC(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return UITableView.automaticDimension
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.arrChatData.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let data = self.arrChatData[indexPath.row]
        
        if(data.isMe) {
            let cell = tableView.dequeueReusableCell(withIdentifier: "VoiceExamMeCell") as! VoiceExamMeCell
            
            let avatar = "\(APIConstant.mediaURL)\(Singleton.shared.retriveFromUserDefaults(key: kUser.avatar)!)"
            cell.imgAvatar.contentMode = .scaleAspectFill
            cell.imgAvatar.clipsToBounds = true
            cell.imgAvatar.sd_setImage(with: URL(string: avatar), placeholderImage: UIImage(named: "Avatar"), options: .refreshCached)
            cell.viewBubble.size.width = UIScreen.main.bounds.size.width-88
            
            cell.lblName.text = Singleton.shared.retriveFromUserDefaults(key: kUser.fullName)!
            
            cell.viewBubble.cornerRadius = 13
            cell.viewBubble.layer.maskedCorners = [.layerMaxXMinYCorner, .layerMinXMaxYCorner]
            
            cell.lblMessage.text = data.message
            
            cell.selectionStyle = .none
            return cell
        } else {
            let cell = tableView.dequeueReusableCell(withIdentifier: "VoiceExamPatientCell") as! VoiceExamPatientCell
            
            cell.imgAvatar.contentMode = .scaleAspectFill
            cell.imgAvatar.clipsToBounds = true
            cell.imgAvatar.sd_setImage(with: URL(string: APIConstant.mediaURL + self.avatar))
            cell.viewBubble.size.width = UIScreen.main.bounds.size.width-88
            
            cell.lblName.text = self.patient_name
            
            cell.viewBubble.cornerRadius = 13
            cell.viewBubble.layer.maskedCorners = [.layerMinXMinYCorner, .layerMaxXMaxYCorner]
            
            cell.lblMessage.text = data.message
            
            cell.selectionStyle = .none
            return cell
        }
    }
}

extension PhysicalExamVC : UITextFieldDelegate {
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        if textField == txtMessage {
            self.view.endEditing(true)
        }
        return true
    }
}

extension PhysicalExamVC {
    
    // MARK: - API CALLER METHODS
    func voiceExamAPI() {
        var paramer: [String: Any] = [:]
        
        paramer["user_id"] = Global.kretriveUserData().User_Id!
        paramer["patient_id"] = self.patient_id
        paramer["body_part_id"] = self.body_part_id
        
        WebService.call.POST(filePath: APIConstant.Request.practExam, params: paramer, enableInteraction: false, showLoader: true, viewObj: view, onSuccess: { (result, success) in
            
            if let Dict = result as? [String:Any] {
                if let str = Dict["code"] as? Int , str == 0{
                    self.arrData.removeAll()
                    
                    if let arrDefinition = Dict["definition"] as? NSArray {
                        for arrDefinitionData in arrDefinition {
                            if let dictDefinition = arrDefinitionData as? [String : Any]{
                                let definitionData = VoiceExamModel()
                                
                                definitionData.answer = JSON(dictDefinition["answer"]!).stringValue
                                definitionData.id = JSON(dictDefinition["id"]!).stringValue
                                //definitionData.question = JSON(dictDefinition["question"]!).stringValue
                                
                                self.arrData.append(definitionData)
                            }
                        }
                    }
                    
                    if let arrWords = Dict["exceptional_words"] as? NSArray {
                        self.exceptionalWords.removeAll()
                        
                        for arrWordsData in arrWords {
                            self.exceptionalWords.append(JSON(arrWordsData).stringValue.lowercased())
                        }
                    }
                    
                    //self.get3DModel()
                }
            }
            
        }) {
            print("Error \(self.description)")
        }
    }
    
    func submitVoiceExamAPI() {
        var paramer: [String: Any] = [:]
        
        paramer["user_id"] = Global.kretriveUserData().User_Id!
        paramer["patient_id"] = self.patient_id
        paramer["body_part_id"] = self.body_part_id
        paramer["conversations"] = self.generateJSON()
        paramer["sys_time"] = Date().dateTimeString(withFormate: "yyyy-MM-dd HH:mm:ss")
        paramer["tr_id"] = self.test_id
        
        WebService.call.POST(filePath: APIConstant.Request.practExamSubmit, params: paramer, enableInteraction: false, showLoader: true, viewObj: view, onSuccess: { (result, success) in
            
            if let Dict = result as? [String:Any] {
                if let str = Dict["code"] as? Int , str == 0{
                    let test_id = JSON(Dict["test_id"]!).stringValue
                    
                    self.delegate?.shareData(test_id, self.body_part_id)
                    self.btnBack(self.btnBack)
                }
            }
            
        }) {
            print("Error \(self.description)")
        }
    }
}

extension PhysicalExamVC {
    func get3DModel() {
        var modeurl = URL(string: self.modelUrl)
        var modelName = modeurl!.lastPathComponent
        
        let path = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true)[0] as String
        let url = NSURL(fileURLWithPath: path)
        if let pathComponent = url.appendingPathComponent(modelName) {
            let filePath = pathComponent.path
            let fileManager = FileManager.default
            if fileManager.fileExists(atPath: filePath) {
                print("FILE AVAILABLE")
                self.loadModel(url: URL(string: filePath)!)
            } else {
                print("FILE NOT AVAILABLE")
                self.downloadFile(url: "\(APIConstant.mediaURL)\(self.modelUrl)")
            }
        } else {
            print("FILE PATH NOT AVAILABLE")
        }
    }
    
    func downloadFile(url: String) {
        WebService.call.DOWNLOAD_FILE(url: url, enableInteraction: false, showLoader: true, viewObj: view, onSuccess: {
            (result, success) in
                self.loadModel(url: URL(string: "\(JSON(result).stringValue)")!)
            }) {
                print("Error \(self.description)")
            }
    }
}
