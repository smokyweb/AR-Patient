//
//  VoiceExamVC.swift
//  AR Patient
//
//  Created by Knoxweb on 02/05/20.
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

protocol VoiceExamDelegate {
    func voiceExamData(_ test_id: String?, _ body_part_id: String?, _ physical_exam_help: String?, _ arrBodyParts : [BodyPartsModel]?)
}

class VoiceExamVC: UIViewController, SFSpeechRecognizerDelegate {

    @IBOutlet weak var bntBack: UIButton!
    @IBOutlet weak var viewHeader: UIView!
    @IBOutlet weak var viewBottom: UIView!
    @IBOutlet weak var btnHelp: UIButton!
    @IBOutlet weak var imgAvatar: UIImageView!
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var txtMessage: IPTextField!
    @IBOutlet weak var btnRecord: UIButton!
    @IBOutlet weak var btnSend: UIButton!
    @IBOutlet weak var btnContinue: IPButton!
    @IBOutlet weak var bottomContinue: NSLayoutConstraint!
    @IBOutlet weak var heightContinue: NSLayoutConstraint!
    @IBOutlet weak var bottomViewSend: NSLayoutConstraint!
    @IBOutlet weak var btnGotIt: IPButton!
    @IBOutlet weak var viewGIF: UIView!
    @IBOutlet weak var imgBgGIF: UIImageView!
    @IBOutlet weak var imgGIF: UIImageView!
    @IBOutlet weak var viewRecording: UIView!
    @IBOutlet weak var sceneView: SCNView!
    
    var patient_id : String = ""
    var patient_name : String = ""
    var modeUrl : String = ""
    var arrData = [VoiceExamModel]()
    var avatar : String = ""
    var arrStudentData = [VoiceExamModel]()
    var arrChatData = [ChatModel]()
    var exceptionalWords = [String]()
    var arrBodyParts = [BodyPartsModel]()
    var heightBtnContinue : CGFloat = 0.0
    var vital_signs : String = ""
    var modelUrl : URL?
    var delegate: VoiceExamDelegate?
    var body_part_id : String = ""
    
    /*let audioEngine = AVAudioEngine()
    //let speechRecognizer: SFSpeechRecognizer? = SFSpeechRecognizer()
    let speechRecognizer = SFSpeechRecognizer(locale: Locale.init(identifier: "en-US"))  //1
    let request = SFSpeechAudioBufferRecognitionRequest()
    var recognitionTask: SFSpeechRecognitionTask?*/
    var isRecording = false
    var speechToText : String = ""
    
    let speechRecognizer        = SFSpeechRecognizer(locale: Locale(identifier: "en-US"))
    var recognitionRequest      : SFSpeechAudioBufferRecognitionRequest?
    var recognitionTask         : SFSpeechRecognitionTask?
    let audioEngine             = AVAudioEngine()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        self.speechRecognizer?.delegate = self
        
        viewHeader.layer.frame = CGRect(x: 0, y: 0, width: UIScreen.main.bounds.size.width, height: viewHeader.frame.height)
        viewHeader.roundCorners([.bottomLeft, .bottomRight], radius: 25)
        
        viewBottom.layer.frame = CGRect(x: 0, y: 0, width: UIScreen.main.bounds.size.width, height: 60)
        viewBottom.roundCorners([.topLeft, .topRight], radius: 10)
        
        self.heightBtnContinue = self.heightContinue.constant
        self.heightContinue.constant = 0
        self.bottomContinue.constant = 0
        self.btnContinue.isHidden = true
        
        self.txtMessage.delegate = self
        
        if(self.avatar != "") {
            self.imgAvatar.sd_setImage(with: URL(string: APIConstant.mediaURL + self.avatar))
        }
        
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
        
        self.authorizeMic()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        IQKeyboardManager.shared.enable = false
        IQKeyboardManager.shared.enableAutoToolbar = false
        
        if(self.modelUrl != nil) {
            self.loadModel(url: self.modelUrl!)
        }
        
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
    
    override func viewWillDisappear(_ animated: Bool) {
        self.removeMemory()
    }
    
    func removeMemory() {
        self.arrData.removeAll()
        self.arrData = []
        self.arrStudentData.removeAll()
        self.arrStudentData = []
        self.arrChatData.removeAll()
        self.arrChatData = []
        self.exceptionalWords.removeAll()
        self.exceptionalWords = []
        self.arrBodyParts.removeAll()
        self.arrBodyParts = []
        
        self.cancelRecording()
        self.isRecording = false
        self.sceneView = nil
        self.recognitionRequest = nil
        self.recognitionTask = nil
    }
    
    @objc func keyboardWillShow(notification: NSNotification) {
        if let keyboardSize = (notification.userInfo?[UIResponder.keyboardFrameBeginUserInfoKey] as? NSValue)?.cgRectValue {
            if self.view.frame.origin.y == 0 {
                self.scrollToBottom()
                self.bottomViewSend.constant = keyboardSize.height
                //self.view.frame.origin.y -= keyboardSize.height
            }
        }
    }

    @objc func keyboardWillHide(notification: NSNotification) {
        self.scrollToBottom()
        self.bottomViewSend.constant = 0
        if self.view.frame.origin.y != 0 {
            //self.view.frame.origin.y = 0
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
    
    func loadModel(url: URL) {
        print("URL => \(url) ")
        let url = url
        let scene = try! SCNScene(url: url, options: [.checkConsistency: true])
        
        // Set the scene to the view
        self.sceneView.scene = scene
        self.sceneView.loops = true
    }
    
    @IBAction func btnBack(_ sender: UIButton) {
        self.view.endEditing(true)
        self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func btnHelp(_ sender: UIButton) {
        self.view.endEditing(true)
        
        let vc = UIStoryboard.init(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "ExamHelpVC") as? ExamHelpVC
        vc?.arrData = self.arrData
        self.navigationController?.pushViewController(vc!, animated: true)
    }
    
    @objc func longPress(gesture: UILongPressGestureRecognizer) {
        if gesture.state == UIGestureRecognizer.State.began {
            self.viewRecording.isHidden = false
            self.view.endEditing(true)
            self.speechToText = ""
            AudioServicesPlayAlertSound(SystemSoundID(kSystemSoundID_Vibrate))
            //self.recordAndRecognizeSpeech()
            self.startRecording()
            isRecording = true
        }
        
        if gesture.state == UIGestureRecognizer.State.ended {
            self.btnRecord.isUserInteractionEnabled = false
            self.viewRecording.isHidden = true
            Global().delay(delay: 1.5, closure: {
                self.cancelRecording()
                self.isRecording = false
                self.btnRecord.isUserInteractionEnabled = true
            })
        }
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
    
    @IBAction func btnContinue(_ sender: UIButton) {
        self.view.endEditing(true)
        AJAlertController.initialization().showAlert(aStrMessage: "Are you sure you want to continue?", aCancelBtnTitle: "Yes", aOtherBtnTitle: "No", completion: { (index, title) in
            if index == 0 {
                self.submitVoiceExamAPI()
            }
        })
    }
    
    @IBAction func btnGotIt(_ sender: UIButton) {
        Singleton.shared.saveToUserDefaults(value: "Yes", forKey: kUser.is_gif_displayed)
        self.viewGIF.isHidden = true
    }
    
    @IBAction func btnRecord(_ sender: UIButton) {
        if(isRecording) {
            self.btnRecord.isUserInteractionEnabled = false
            self.viewRecording.isHidden = true
            self.btnRecord.setImage(UIImage.init(named: "Record"), for: .normal)
            Global().delay(delay: 1.5, closure: {
                self.cancelRecording()
                self.isRecording = false
                self.btnRecord.isUserInteractionEnabled = true
            })
        } else {
            self.viewRecording.isHidden = false
            self.view.endEditing(true)
            self.speechToText = ""
            AudioServicesPlayAlertSound(SystemSoundID(kSystemSoundID_Vibrate))
            self.startRecording()
            isRecording = true
            self.btnRecord.setImage(UIImage.init(named: "Recording Stop"), for: .normal)
        }
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
    
    func cancelRecording() {
//        recognitionTask?.finish()
//        recognitionTask = nil
//
//        // stop audio
//        request.endAudio()
//        audioEngine.stop()
//        audioEngine.inputNode.removeTap(onBus: 0)
        
        if audioEngine.isRunning {
            self.audioEngine.stop()
            self.recognitionRequest?.endAudio()
        }
        if(NetworkReachabilityManager()!.isReachable) {
            if(self.speechToText != "") {
                let chatData = ChatModel()
                chatData.isMe = true
                chatData.message = self.speechToText
                
                self.arrChatData.append(chatData)
                
                self.tableView.reloadData()
                self.scrollToBottom()
                
                self.calculateAnswer(question: self.speechToText)
                
                self.txtMessage.text = ""
            }
            self.speechToText = ""
        } else {
            self.speechToText = ""
            Singleton.shared.showAlertWithSingleButton(strMessage: LocalizeHelper.shared.localizedString(forKey: "ERROR_CALL"))
        }
    }
    
    //MARK: - Recognize Speech
    
    func startRecording() {

        // Clear all previous session data and cancel task
        if recognitionTask != nil {
            recognitionTask?.cancel()
            recognitionTask = nil
        }

        // Create instance of audio session to record voice
        let audioSession = AVAudioSession.sharedInstance()
        do {
            try audioSession.setCategory(AVAudioSession.Category.record, mode: AVAudioSession.Mode.measurement, options: AVAudioSession.CategoryOptions.defaultToSpeaker)
            try audioSession.setActive(true, options: .notifyOthersOnDeactivation)
        } catch {
            print("audioSession properties weren't set because of an error.")
            //AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "audioSession properties weren't set because of an error.") { (index, title) in }
        }

        self.recognitionRequest = SFSpeechAudioBufferRecognitionRequest()

        let inputNode = audioEngine.inputNode

        guard let recognitionRequest = recognitionRequest else {
            self.viewRecording.isHidden = true
            fatalError("Unable to create an SFSpeechAudioBufferRecognitionRequest object")
        }

        recognitionRequest.shouldReportPartialResults = true

        self.recognitionTask = speechRecognizer?.recognitionTask(with: recognitionRequest, resultHandler: { (result, error) in

            var isFinal = false

            if result != nil {

                self.speechToText = (result?.bestTranscription.formattedString)!
                isFinal = (result?.isFinal)!
            }

            if error != nil || isFinal {

                self.audioEngine.stop()
                inputNode.removeTap(onBus: 0)

                self.recognitionRequest = nil
                self.recognitionTask = nil

                self.btnRecord.isEnabled = true
            }
        })

        let recordingFormat = inputNode.outputFormat(forBus: 0)
        inputNode.installTap(onBus: 0, bufferSize: 1024, format: recordingFormat) { (buffer, when) in
            self.recognitionRequest?.append(buffer)
        }

        self.audioEngine.prepare()

        do {
            try self.audioEngine.start()
        } catch {
            self.viewRecording.isHidden = true
            print("audioEngine couldn't start because of an error.")
            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "audioEngine couldn't start because of an error.") { (index, title) in }
        }
    }
    
    /*func recordAndRecognizeSpeech() {
        let node = audioEngine.inputNode
        let recordingFormat = node.outputFormat(forBus: 0)
        node.installTap(onBus: 0, bufferSize: 1024, format: recordingFormat) { buffer, _ in
            self.request.append(buffer)
        }
        audioEngine.prepare()
        do {
            try audioEngine.start()
        } catch {
            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "There has been an audio engine error.") { (index, title) in }
            return print(error)
        }
        guard let myRecognizer = SFSpeechRecognizer() else {
            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "Speech recognition is not supported for your current locale.") { (index, title) in }
            return
        }
        if !myRecognizer.isAvailable {
            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "Speech recognition is not currently available. Check back at a later time.") { (index, title) in }
            // Recognizer is not available right now
            return
        }
        recognitionTask = speechRecognizer?.recognitionTask(with: request, resultHandler: { result, error in
            if let result = result {
                
                let bestString = result.bestTranscription.formattedString
                var lastString: String = ""
                for segment in result.bestTranscription.segments {
                    let indexTo = bestString.index(bestString.startIndex, offsetBy: segment.substringRange.location)
                    lastString = String(bestString[indexTo...])
                }
                self.speechToText = "\(self.speechToText)\(lastString) "
                //self.checkForColorsSaid(resultString: lastString)
            } else if let error = error {
                AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "There has been a speech recognition error.") { (index, title) in }
                print(error)
            }
        })
    }*/
    
    func authorizeMic() {
        SFSpeechRecognizer.requestAuthorization { (authStatus) in  //4
            
            var isButtonEnabled = false
            
            switch authStatus {  //5
            case .authorized:
                isButtonEnabled = true
                
            case .denied:
                self.viewRecording.isHidden = true
                isButtonEnabled = false
                print("User denied access to speech recognition")
                
            case .restricted:
                self.viewRecording.isHidden = true
                isButtonEnabled = false
                print("Speech recognition restricted on this device")
                
            case .notDetermined:
                self.viewRecording.isHidden = true
                isButtonEnabled = false
                print("Speech recognition not yet authorized")
            }
            
            OperationQueue.main.addOperation() {
                self.btnRecord.isEnabled = isButtonEnabled
                
            }
        }
    }
    
    func textToSpeach(text : String){
        let text = AVSpeechUtterance(string: text)
        text.voice = AVSpeechSynthesisVoice(language: "en-US")
        text.rate = 0.5
        
        do {
           try AVAudioSession.sharedInstance().setCategory(.playback)
        } catch(let error) {
            print(error.localizedDescription)
        }

        let siri = AVSpeechSynthesizer()
        siri.speak(text)
    }
    
    func speechRecognizer(_ speechRecognizer: SFSpeechRecognizer, availabilityDidChange available: Bool) {
        if available {
            self.viewRecording.isHidden = true
            self.btnRecord.isEnabled = true
        } else {
            self.btnRecord.isEnabled = false
        }
    }
    
    func calculateAnswer(question: String) {
        let definitionData = VoiceExamModel()
        var maxCount : Int = 0
        
        var arrAskedQuestion = question.lowercased().replacingOccurrences(of: "?", with: "", options: .literal, range: nil).replacingOccurrences(of: ".", with: "", options: .literal, range: nil).replacingOccurrences(of: "!", with: "", options: .literal, range: nil).split(separator: " ")
        arrAskedQuestion = arrAskedQuestion.filter { $0.count > 2 }
        
        for i in self.arrData {
            let arrQuestion = i.question.lowercased().replacingOccurrences(of: "?", with: "", options: .literal, range: nil).replacingOccurrences(of: ".", with: "", options: .literal, range: nil).replacingOccurrences(of: "!", with: "", options: .literal, range: nil).split(separator: " ")
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
            chatData.message = definitionData.answer
            
            textToSpeach(text : definitionData.answer)
            
            self.arrChatData.append(chatData)
            
            self.tableView.reloadData()
            self.scrollToBottom()
        } else {
            let chatData = ChatModel()
            chatData.isMe = false
            chatData.message = "Sorry, patient doesn't have any answer for the asked question."
            
            textToSpeach(text : "Sorry, patient doesn't have any answer for the asked question.")
            
            self.arrChatData.append(chatData)
            
            definitionData.answer = ""
            definitionData.id = "0"
            definitionData.question = question
            self.arrStudentData.append(definitionData)
            
            self.tableView.reloadData()
            self.scrollToBottom()
        }
        
        self.heightContinue.constant = self.heightBtnContinue
        self.bottomContinue.constant = 8
        self.btnContinue.isHidden = false
    }
    
    func scrollToBottom(){
        if(self.arrChatData.count > 0) {
            DispatchQueue.main.async {
                let indexPath = IndexPath(row: self.arrChatData.count-1, section: 0)
                self.tableView.scrollToRow(at: indexPath, at: .bottom, animated: false)
            }
        }
    }
}

extension VoiceExamVC: UITableViewDataSource, UITableViewDelegate {
    
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

extension VoiceExamVC : UITextFieldDelegate {
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        if textField == txtMessage {
            if let text = textField.text,
            let textRange = Range(range, in: text) {
                let updatedText = text.replacingCharacters(in: textRange, with: string)
                if(updatedText == "") {
                    self.btnRecord.isHidden = false
                    self.btnSend.isHidden = true
                } else {
                    self.btnRecord.isHidden = true
                    self.btnSend.isHidden = false
                }
            }
        }
        return true
    }
    
    func textFieldDidBeginEditing(_ textField: UITextField) {
        if textField == txtMessage {
            if(txtMessage.text! == "") {
                self.btnRecord.isHidden = false
                self.btnSend.isHidden = true
            } else {
                self.btnRecord.isHidden = true
                self.btnSend.isHidden = false
            }
        }
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        if textField == txtMessage {
            if(txtMessage.text! == "") {
                self.btnRecord.isHidden = false
                self.btnSend.isHidden = true
            } else {
                self.btnRecord.isHidden = true
                self.btnSend.isHidden = false
            }
        }
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        if textField == txtMessage {
            self.view.endEditing(true)
        }
        return true
    }
}

extension VoiceExamVC {
    
    // MARK: - API CALLER METHODS
    func voiceExamAPI() {
        var paramer: [String: Any] = [:]
        
        paramer["user_id"] = Global.kretriveUserData().User_Id!
        paramer["patient_id"] = self.patient_id
        paramer["body_part_id"] = self.body_part_id
        
        WebService.call.POST(filePath: APIConstant.Request.voiceExam, params: paramer, enableInteraction: false, showLoader: true, viewObj: view, onSuccess: { (result, success) in
            
            if let Dict = result as? [String:Any] {
                if let str = Dict["code"] as? Int , str == 0{
                    if Global.kretriveUserData().is_gif_displayed == "No"{
                        let gif = UIImage.gifImageWithName("record")
                        self.imgGIF.image = gif
                        self.imgGIF.layer.cornerRadius = 10.0
                        self.imgGIF.layer.masksToBounds = true
                        self.viewGIF.isHidden = false
                    }
                    
                    self.arrData.removeAll()
                    
                    if let arrDefinition = Dict["definition"] as? NSArray {
                        for arrDefinitionData in arrDefinition {
                            if let dictDefinition = arrDefinitionData as? [String : Any]{
                                let definitionData = VoiceExamModel()
                                
                                definitionData.answer = JSON(dictDefinition["answer"]!).stringValue
                                definitionData.id = JSON(dictDefinition["id"]!).stringValue
                                definitionData.question = JSON(dictDefinition["question"]!).stringValue
                                
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
                    
                    if(self.arrData.count > 0) {
                        self.btnHelp.isHidden = false
                    } else {
                        self.btnHelp.isHidden = true
                    }
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
        paramer["conversations"] = self.generateJSON()
        paramer["body_part_id"] = self.body_part_id
        paramer["sys_time"] = Date().dateTimeString(withFormate: "yyyy-MM-dd HH:mm:ss")
        
        WebService.call.POST(filePath: APIConstant.Request.voiceExamSubmit, params: paramer, enableInteraction: false, showLoader: true, viewObj: view, onSuccess: { (result, success) in
            
            if let Dict = result as? [String:Any] {
                if let str = Dict["code"] as? Int , str == 0{
                    self.arrBodyParts.removeAll()
                    
                    let test_id = JSON(Dict["test_id"]!).stringValue
                    let physical_exam_help = JSON(Dict["physical_exam_help"]!).stringValue
                    
                    if let arrBodyParts = Dict["body_parts"] as? NSArray {
                        for arrarrBodyPartsData in arrBodyParts {
                            if let dictBodyParts = arrarrBodyPartsData as? [String : Any]{
                                let bodyPartsData = BodyPartsModel()
                                
                                bodyPartsData.id = JSON(dictBodyParts["id"]!).stringValue
                                bodyPartsData.name = JSON(dictBodyParts["name"]!).stringValue
                                bodyPartsData.descr = JSON(dictBodyParts["description"]!).stringValue
                                bodyPartsData.model = JSON(dictBodyParts["model"]!).stringValue
                                
                                self.arrBodyParts.append(bodyPartsData)
                            }
                        }
                    }
                    
                    /*let vc = UIStoryboard.init(name: "Development", bundle: Bundle.main).instantiateViewController(withIdentifier: "PhysicalExam3DVC") as? PhysicalExam3DVC
                    //vc?.patient_id = self.patient_id
                    //vc?.modeUrl = self.modeUrl
                    vc?.arrBodyParts = self.arrBodyParts
                    //vc?.avatar = self.avatar
                    //vc?.patient_name = self.patient_name
                    vc?.test_id = test_id
                    vc?.physical_exam_help = physical_exam_help
                    //vc?.vital_signs = self.vital_signs
                    self.navigationController?.pushViewController(vc!, animated: true)*/
                    
                    self.delegate?.voiceExamData(test_id, self.body_part_id, physical_exam_help, self.arrBodyParts)
                    self.btnBack(self.bntBack)
                }
            }
            
        }) {
            print("Error \(self.description)")
        }
    }
}
