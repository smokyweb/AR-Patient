//
//  PhysicalExam3DVC.swift
//  AR Patient
//
//  Created by Silicon on 12/05/20.
//  Copyright © 2020 Silicon. All rights reserved.
//

import UIKit
import SceneKit
import SFProgressCircle
import Alamofire
import DrawerView
import Speech
import AVKit
import IQKeyboardManagerSwift
import SwiftyJSON
import googleapis
import AVFoundation

let SAMPLE_RATE = 16000

class PhysicalExam3DVC: UIViewController, UIGestureRecognizerDelegate, SFSpeechRecognizerDelegate, AudioControllerDelegate {
    
    //Download View
    @IBOutlet weak var viewDownload: UIView!
    @IBOutlet weak var bgProgress: SFCircleGradientView!
    @IBOutlet weak var progress: SFCircleGradientView!
    @IBOutlet weak var lblPercentage: UILabel!
    @IBOutlet weak var btnQuit: IPButton!
    @IBOutlet weak var btnGiveDiagnosis: IPButton!
    @IBOutlet weak var heightBtnDiagnosis: NSLayoutConstraint!
    @IBOutlet weak var topBtnDiagnosis: NSLayoutConstraint! //12
    @IBOutlet weak var lblHeader1: IPAutoScalingLabel!
    
    
    //Model View
    @IBOutlet weak var viewModel: UIView!
    @IBOutlet weak var btnHelp: UIButton!
    @IBOutlet weak var btnBack: UIButton!
    @IBOutlet weak var sceneView: SCNView!
    @IBOutlet weak var viewTip: UIView!
    @IBOutlet weak var viewTipHeight: NSLayoutConstraint! //92
    @IBOutlet weak var lblHeader: IPAutoScalingLabel!
    
    @IBOutlet weak var viewPopupSymptom: UIView!
    @IBOutlet weak var viewSymptom: UIView!
    @IBOutlet weak var lblSymptoms: UILabel!
    @IBOutlet weak var btnOK: UIButton!
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var constraintTableViewHeight: NSLayoutConstraint!
    @IBOutlet weak var viewHint: UIView!
    @IBOutlet weak var lblHint: IPAutoScalingLabel!
    @IBOutlet weak var btnHintOK: IPButton!
    
    //Drawer View
    @IBOutlet var drawerView: DrawerView!
    @IBOutlet weak var chatTableView: UITableView!
    //@IBOutlet weak var bottomContinue: NSLayoutConstraint!
    //@IBOutlet weak var heightContinue: NSLayoutConstraint!
    @IBOutlet weak var bottomViewSend: NSLayoutConstraint!
    @IBOutlet weak var txtMessage: IPTextField!
    @IBOutlet weak var btnRecord: UIButton!
    @IBOutlet weak var btnSend: UIButton!
    @IBOutlet weak var viewRecording: UIView!
    //@IBOutlet weak var btnContinue: IPButton!
    @IBOutlet weak var imgArrow: UIImageView!
    @IBOutlet weak var viewRotateInfo: UIView!
    
    var modeUrl : String = ""
    var patient_id : String = ""
    var avatar : String = ""
    var arrBodyParts = [BodyPartsModel]()
    var patient_name : String = ""
    var test_id = "0"
    var physical_exam_test_id : String = ""
    var physical_exam_help : String = ""
    var btnHeight : CGFloat = 0.0
    var body_parts = [String]()
    var vital_signs : String = ""
    var tag : Int = 2 //1=Voice Exam & 2=Physical Exam
    var arrSymptoms : [String] = []
    var tapMatchData = [BodyPartsModel]()
    var hint_text : String = ""
    var pt_name : String = ""
    var detectionTimer : Timer?
    
    /* New */
    var isRecording = false
    var speechToText : String = ""
    
    /*let speechRecognizer        = SFSpeechRecognizer(locale: Locale(identifier: "en-US"))
    var recognitionRequest      : SFSpeechAudioBufferRecognitionRequest?
    var recognitionTask         : SFSpeechRecognitionTask?
    let audioEngine             = AVAudioEngine()*/
    
    var arrData = [VoiceExamModel]()
    var arrStudentData = [VoiceExamModel]()
    var arrChatData = [ChatModel]()
    var exceptionalWords = [String]()
    var heightBtnContinue : CGFloat = 0.0
    var timer = Timer()
    var wrongAnswer : Int = 0
    
    var audioData: NSMutableData!
    var siriActivationString = ""
    var isActivated : Bool = false
    @IBOutlet weak var imgSpeaking: UIImageView!
    var isGifLoaded : Bool = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        
        //self.speechRecognizer?.delegate = self
        AudioController.sharedInstance.delegate = self
        //self.heightBtnContinue = self.heightContinue.constant
        //self.txtMessage.delegate = self
        self.txtMessage.withoutImage(direction: .Left, colorSeparator: UIColor.clear, colorBorder: UIColor.clear)
        
        self.drawerView.attachTo(view: self.view)
        self.drawerView.snapPositions = [.collapsed, .partiallyOpen]
        self.drawerView.collapsedHeight = 50
        self.drawerView.partiallyOpenHeight = (UIScreen.main.bounds.size.height / 2) + 50
//        self.drawerView.layer.cornerRadius = 20
//        self.drawerView.layer.maskedCorners = [.layerMinXMinYCorner, .layerMaxXMinYCorner]
        
        self.btnHeight = self.heightBtnDiagnosis.constant
        self.heightBtnDiagnosis.constant = 0
        self.topBtnDiagnosis.constant = 0
        self.btnGiveDiagnosis.isHidden = true
        
        self.get3DModel()
        
        bgProgress.progress = 1
        bgProgress.lineWidth = 12
        bgProgress.startColor = UIColor(named: "Color_LightGray")
        bgProgress.endColor = UIColor(named: "Color_LightGray")
        bgProgress.startAngle = -1.6
        bgProgress.endAngle = 4.7
        
        progress.lineWidth = 12
        progress.startColor = UIColor(named: "Color_Start")
        progress.endColor = UIColor(named: "Color_End")
        progress.startAngle = -1.5
        progress.endAngle = 4.7
        progress.roundedCorners = true
        
        if(self.physical_exam_help != "") {
            self.btnHelp.isHidden = false
        }
        
        self.tableView.register(UINib(nibName: "SymptomsCell", bundle: nil), forCellReuseIdentifier: "SymptomsCell")
        self.tableView.delegate = self
        self.tableView.dataSource = self
        self.tableView.separatorStyle = .none
        self.tableView.rowHeight = UITableView.automaticDimension
        self.tableView.estimatedRowHeight = 36.5
        
        self.chatTableView.register(UINib(nibName: "VoiceExamMeCell", bundle: nil), forCellReuseIdentifier: "VoiceExamMeCell")
        self.chatTableView.register(UINib(nibName: "VoiceExamPatientCell", bundle: nil), forCellReuseIdentifier: "VoiceExamPatientCell")
        
        self.chatTableView.delegate = self
        self.chatTableView.dataSource = self
        self.chatTableView.separatorStyle = .none
        self.chatTableView.estimatedRowHeight = 73.0
        
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillShow), name: UIResponder.keyboardWillShowNotification, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillHide), name: UIResponder.keyboardWillHideNotification, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(changeInputMode), name: UIResponder.keyboardDidShowNotification, object: nil)
        
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(UIInputViewController.dismissKeyboard))
        self.view.addGestureRecognizer(tap)
        
        self.authorizeMic()
        
        self.lblHint.text = self.hint_text
        self.viewHint.layer.cornerRadius = 10
        self.viewHint.layer.masksToBounds = true
        
        self.siriActivationString = "Hey \(self.patient_name)"
        
        self.imgSpeaking.image = UIImage(named: "Siri")!
    }
    
    override func viewWillAppear(_ animated: Bool) {
        if(self.tag == 1) {
            self.btnGiveDiagnosis.setTitle("GIVE DIAGNOSIS  ", for: .normal)
            self.lblHeader.text = "Patient Exam"
            self.lblHeader1.text = "Patient Exam"
        } else {
            self.btnGiveDiagnosis.setTitle("GIVE DIAGNOSIS", for: .normal)
            self.lblHeader.text = "Physical Exam"
            self.lblHeader1.text = "Physical Exam"
        }
    }
    
    override func viewDidAppear(_ animated: Bool) {
        self.scheduledTimerWithTimeInterval()
        
        IQKeyboardManager.shared.enable = false
        IQKeyboardManager.shared.enableAutoToolbar = false
        
        self.tapMatchData.removeAll()
        self.arrSymptoms.removeAll()
        
        let audioSession = AVAudioSession.sharedInstance()
        do {
            try audioSession.setCategory(AVAudioSession.Category.record)
        } catch {

        }
        audioData = NSMutableData()
        _ = AudioController.sharedInstance.prepare(specifiedSampleRate: SAMPLE_RATE)
        SpeechRecognitionService.sharedInstance.sampleRate = SAMPLE_RATE
        _ = AudioController.sharedInstance.start()
        
        print("Siri started")
    }
    
    override func viewDidLayoutSubviews() {
        let height = tableView.contentSize.height
        
        if(height <= 400) {
            //self.tableView.isScrollEnabled = false
            self.constraintTableViewHeight.constant = height
        } else {
            //self.tableView.isScrollEnabled = true
            self.constraintTableViewHeight.constant = 400
        }
        self.tableView.layoutIfNeeded()
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        self.timer.invalidate()
        IQKeyboardManager.shared.enable = true
        IQKeyboardManager.shared.enableAutoToolbar = true
        _ = AudioController.sharedInstance.stop()
        SpeechRecognitionService.sharedInstance.stopStreaming()
    }
    
    @objc func dismissKeyboard() {
        self.view.endEditing(true)
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
        self.bottomViewSend.constant = 60
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
    
    func scheduledTimerWithTimeInterval(){
        timer = Timer.scheduledTimer(timeInterval: 2, target: self, selector: #selector(self.updateCounting), userInfo: nil, repeats: true)
    }
    
    @objc func updateCounting(){
        if(self.drawerView.position == .partiallyOpen) {
            self.imgArrow.image = UIImage(named: "Down Arrow")
        } else {
            self.imgArrow.image = UIImage(named: "Up Arrow")
        }
    }
    
    @IBAction func btnBack(_ sender: UIButton) {
        self.view.endEditing(true)
        
        if(self.arrChatData.count > 0){
            AJAlertController.initialization().showAlert(aStrMessage: "Are you sure want to exit the exam?", aCancelBtnTitle: "Yes", aOtherBtnTitle: "No", completion: { (index, title) in
                if index == 0 {
                    Global().delay(delay: 0.5, closure: {
                        //Global.appDelegate.setnavigation(viewController: "TabBarVC")
                        self.navigationController?.popViewController(animated: true)
                    })
                }
            })
        } else {
            self.navigationController?.popViewController(animated: true)
        }
    }
    
    @IBAction func btnChat(_ sender: UIButton) {
        if(self.drawerView.position == .open) {
            self.drawerView.setPosition(.collapsed, animated: true)
        } else {
            self.drawerView.setPosition(.open, animated: true)
        }
    }
    
    @IBAction func btnOK(_ sender: UIButton) {
        self.viewSymptom.isHidden = true
        self.viewPopupSymptom.isHidden = true
        
        /*let vc = UIStoryboard.init(name: "Development", bundle: Bundle.main).instantiateViewController(withIdentifier: "VoiceExamVC") as? VoiceExamVC
        vc?.patient_id = self.patient_id
        vc?.avatar = self.avatar
        vc?.patient_name = self.patient_name
        vc?.modeUrl = self.modeUrl
        vc?.vital_signs = self.vital_signs
        vc?.delegate = self
        vc!.body_part_id = tapMatchData[0].id
        self.navigationController?.pushViewController(vc!, animated: true)*/
    }
    
    @IBAction func btnGotIt(_ sender: IPButton) {
        Singleton.shared.saveToUserDefaults(value: "Yes", forKey: kUser.is_gif_displayed)
        self.viewPopupSymptom.isHidden = true
        self.viewRotateInfo.isHidden = true
    }
    
    @IBAction func btnHelp(_ sender: UIButton) {
//        let vc = UIStoryboard.init(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "WebViewController") as? WebViewController
//        vc?.tag = 4
//        vc?.content = self.physical_exam_help
//        self.navigationController?.pushViewController(vc!, animated: true)
        let vc = UIStoryboard.init(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "ExamHelpVC") as? ExamHelpVC
        vc?.arrData = self.arrData
        self.navigationController?.pushViewController(vc!, animated: true)
    }
    
    @IBAction func btnSubmitExam(_ sender: IPButton) {
        if(self.arrChatData.count > 0){
            AJAlertController.initialization().showAlert(aStrMessage: "Are you sure want to submit your exam?", aCancelBtnTitle: "Yes", aOtherBtnTitle: "No", completion: { (index, title) in
                if index == 0 {
                    self.submitVoiceExamAPI()
                }
            })
        }
    }
    @IBAction func btnPencil(_ sender: Any) {
        /*if(self.arrChatData.count > 0){
            AJAlertController.initialization().showAlert(aStrMessage: "Are you sure want to submit your exam?", aCancelBtnTitle: "Yes", aOtherBtnTitle: "No", completion: { (index, title) in
                if index == 0 {
                    self.submitVoiceExamAPI()
                }
            })
        } else if(self.arrChatData.count == 0) {
            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "Please conduct your exam before submitting diagnosis.") { (index, title) in }
        }*/
        let vc = UIStoryboard.init(name: "Development", bundle: Bundle.main).instantiateViewController(withIdentifier: "DiagnosisVC") as? DiagnosisVC
        vc?.test_id = test_id
        vc!.tag = 2
        vc?.patient_id = self.patient_id
        vc?.delegate = self
        self.navigationController?.pushViewController(vc!, animated: true)
    }
    
    @IBAction func btnQuit(_ sender: Any) {
        self.btnBack(self.btnBack)
    }
    
    @IBAction func btnCross(_ sender: Any) {
        self.viewTip.isHidden = true
        self.viewTipHeight.constant = 0
    }
    
    @IBAction func btnGiveDiagnosis(_ sender: IPButton) {
        if(self.physical_exam_test_id != "0") {
            /*if(self.tag == 2) {
                let vc = UIStoryboard.init(name: "Development", bundle: Bundle.main).instantiateViewController(withIdentifier: "DiagnosisVC") as? DiagnosisVC
                vc?.test_id = self.physical_exam_test_id
                self.navigationController?.pushViewController(vc!, animated: true)
            } else {
                let vc = UIStoryboard.init(name: "Development", bundle: Bundle.main).instantiateViewController(withIdentifier: "PhysicalExam3DVC") as? PhysicalExam3DVC
                vc?.patient_id = self.patient_id
                vc?.modeUrl = self.modeUrl
                vc?.arrBodyParts = self.arrBodyParts
                vc?.avatar = self.avatar
                vc?.patient_name = self.patient_name
                vc?.test_id = self.test_id
                vc?.physical_exam_help = self.physical_exam_help
                vc?.vital_signs = self.vital_signs
                self.navigationController?.pushViewController(vc!, animated: true)
            }*/
            
            let vc = UIStoryboard.init(name: "Development", bundle: Bundle.main).instantiateViewController(withIdentifier: "DiagnosisVC") as? DiagnosisVC
            vc?.test_id = self.physical_exam_test_id
            self.navigationController?.pushViewController(vc!, animated: true)
        }
    }
    
    @IBAction func btnHintOK(_ sender: IPButton) {
        self.viewHint.isHidden = true
        self.viewPopupSymptom.isHidden = true
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
                
                self.chatTableView.reloadData()
                self.scrollToBottom()
                
                self.calculateAnswer(question: self.txtMessage.text!)
                
                self.txtMessage.text = ""
                //self.btnSend.isHidden = true
                //self.btnRecord.isHidden = false
            }
        } else{
            Singleton.shared.showAlertWithSingleButton(strMessage: LocalizeHelper.shared.localizedString(forKey: "ERROR_CALL"))
        }
        
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
                self.txtMessage.text = ""
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
            
            /*if audioEngine.isRunning {
                self.audioEngine.stop()
                self.recognitionRequest?.endAudio()
            }
            if(NetworkReachabilityManager()!.isReachable) {
                if(self.speechToText != "") {
                    /*let chatData = ChatModel()
                    chatData.isMe = true
                    chatData.message = self.speechToText
                    
                    self.arrChatData.append(chatData)
                    
                    self.tableView.reloadData()
                    self.scrollToBottom()*/
                    
                    //self.calculateAnswer(question: self.speechToText)
                    self.txtMessage.text = self.speechToText
                    self.btnRecord.isHidden = true
                    self.btnSend.isHidden = false
                }
                self.speechToText = ""
            } else {
                self.speechToText = ""
                Singleton.shared.showAlertWithSingleButton(strMessage: LocalizeHelper.shared.localizedString(forKey: "ERROR_CALL"))
            }*/
        }
        
        //MARK: - Recognize Speech
        
        func startRecording() {

            // Clear all previous session data and cancel task
            /*if recognitionTask != nil {
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
                    print("Here here => \((result?.bestTranscription.formattedString)!)")
                    self.speechToText = (result?.bestTranscription.formattedString)!
                    isFinal = (result?.isFinal)!
                }
                
                /*if let timer = self.detectionTimer, timer.isValid {
                    if isFinal {
                        isFinal = false
                        self.detectionTimer!.invalidate()
                    }
                } else {
                    self.detectionTimer = Timer.scheduledTimer(withTimeInterval: 1.5, repeats: false, block: { (timer) in
                        isFinal = true
                        timer.invalidate()
                        
                        self.btnRecord.isUserInteractionEnabled = false
                        self.viewRecording.isHidden = true
                        self.btnRecord.setImage(UIImage.init(named: "Record"), for: .normal)
                        Global().delay(delay: 1.5, closure: {
                            self.cancelRecording()
                            self.isRecording = false
                            self.btnRecord.isUserInteractionEnabled = true
                        })
                    })
                }*/

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
            }*/
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
            if(self.pt_name.lowercased() == "Pediatric Male".lowercased() || self.pt_name.lowercased() == "Adult Male".lowercased()) {
                text.voice = AVSpeechSynthesisVoice(language: "en-GB")
            } else {
                text.voice = AVSpeechSynthesisVoice(language: "en-US")
            }
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
                //self.btnRecord.isEnabled = true
            } else {
                //self.btnRecord.isEnabled = false
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
                
                self.chatTableView.reloadData()
                self.scrollToBottom()
                
                self.drawerView.setPosition(.partiallyOpen, animated: true)
            } else {
                self.wrongAnswer = self.wrongAnswer + 1
                let chatData = ChatModel()
                chatData.isMe = false
                chatData.message = "Sorry, patient doesn't have any answer for the asked question."
                
                self.arrChatData.append(chatData)
                
                definitionData.answer = ""
                definitionData.id = "0"
                definitionData.question = question
                self.arrStudentData.append(definitionData)
                
                self.chatTableView.reloadData()
                self.scrollToBottom()
                
                if(self.wrongAnswer >= 3) {
                    if(self.hint_text != "") {
                        textToSpeach(text : self.hint_text)
                    }
                    self.viewHint.isHidden = false
                    self.viewPopupSymptom.isHidden = false
                    self.wrongAnswer = 0
                } else {
                    textToSpeach(text : "Sorry, patient doesn't have any answer for the asked question.")
                    
                    self.drawerView.setPosition(.partiallyOpen, animated: true)
                }
            }
            
            /*self.heightContinue.constant = self.heightBtnContinue
            self.bottomContinue.constant = 8
            self.btnContinue.isHidden = false*/
        }
        
        func scrollToBottom(){
            if(self.arrChatData.count > 0) {
                DispatchQueue.main.async {
                    let indexPath = IndexPath(row: self.arrChatData.count-1, section: 0)
                    self.chatTableView.scrollToRow(at: indexPath, at: .bottom, animated: false)
                }
            }
        }
    
    @objc func detectNode(_ sender: UITapGestureRecognizer) {
        self.tapMatchData.removeAll()
        self.arrSymptoms.removeAll()
        let sceneView = sender.view as! SCNView
        let location = sender.location(in: sceneView)
        let results = sceneView.hitTest(location, options: [SCNHitTestOption.searchMode : 1])
        
        if(results.count > 0) {
            let body_part_name = results[0].node.name!
            print("Node name => \(body_part_name)")
            
            let matchedData = self.arrBodyParts.filter { $0.name.lowercased() == body_part_name.lowercased() }
            
            if(matchedData.count == 1) {
                //if !self.body_parts.contains(matchedData[0].id) {
                    if(self.tag == 2) {
                        let vc = UIStoryboard.init(name: "Development", bundle: Bundle.main).instantiateViewController(withIdentifier: "PhysicalExamVC") as? PhysicalExamVC
                        vc?.patient_id = self.patient_id
                        vc!.body_part_id = matchedData[0].id
                        vc?.avatar = self.avatar
                        vc?.patient_name = self.patient_name
                        vc?.body_description = matchedData[0].descr
                        vc?.test_id = self.test_id
                        vc?.delegate = self
                        vc?.modelUrl = matchedData[0].model
                        vc?.vital_signs = self.vital_signs
                        self.navigationController?.pushViewController(vc!, animated: true)
                    } else {
                        self.tapMatchData = matchedData
                        self.arrSymptoms = matchedData[0].symptoms
                        
                        self.tableView.reloadData()
                        
                        self.viewPopupSymptom.isHidden = false
                        self.viewSymptom.isHidden = false
                        /*var strSymptoms = ""
                        for i in matchedData[0].symptoms {
                            strSymptoms = "\(strSymptoms), \(i)"
                        }
                        strSymptoms = String(strSymptoms.dropFirst(2))*/
                        
                        self.lblSymptoms.text = "Symptoms for \(body_part_name.capitalized)"
                        
                        /*AJAlertController.initialization().showAlertForLocation(aStrMessage: "\(strSymptoms)", title: "Symptoms for \(body_part_name.capitalized)", completion: { (index, title) in
                            
                            let vc = UIStoryboard.init(name: "Development", bundle: Bundle.main).instantiateViewController(withIdentifier: "VoiceExamVC") as? VoiceExamVC
                            vc?.patient_id = self.patient_id
                            vc?.avatar = self.avatar
                            vc?.patient_name = self.patient_name
                            vc?.modeUrl = self.modeUrl
                            vc?.vital_signs = self.vital_signs
                            vc?.delegate = self
                            vc!.body_part_id = matchedData[0].id
                            self.navigationController?.pushViewController(vc!, animated: true)
                        })*/
                        
                    }
                /*} else {
                    AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "You have already conducted exam for \(matchedData[0].name.lowercased()). Please choose another body part.") { (index, title) in }
                }*/
            } else {
                AJAlertController.initialization().showAlertForLocation(aStrMessage: "It seems all okay! Please choose another body part.", title: body_part_name.capitalized, completion: { (index, title) in })
            }
        }
    }
    
    func loadModel(url: URL) {
        self.voiceExamAPI()
        
        let url = url
        let scene = try! SCNScene(url: url, options: [.checkConsistency: true])
        
        // Set the scene to the view
        self.sceneView.scene = scene
        self.sceneView.loops = true
        
        self.viewTip.layer.cornerRadius = 10
        self.viewTip.layer.masksToBounds = true
        self.viewTip.isHidden = false
        
        self.viewDownload.isHidden = true
        if Global.kretriveUserData().is_gif_displayed == "No"{
            self.viewPopupSymptom.isHidden = false
            self.viewRotateInfo.isHidden = false
        }
        self.viewModel.isHidden = false
        self.drawerView.isHidden = false
        
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(self.detectNode(_:)))
        self.sceneView.addGestureRecognizer(tapGesture)
        
        /*let panRecognizer = UIPanGestureRecognizer(target: self, action: #selector(panGesture))
        panRecognizer.delegate = self
        self.sceneView.addGestureRecognizer(panRecognizer)*/
    }
    
    /*@objc func panGesture(sender: UIPanGestureRecognizer){
        let translation = sender.translation(in: sender.view)
        print(translation.x, translation.y)
        
        self.sceneView.rotate(byAngle: translation.x, ofType: .degrees)
    }*/
    
    func processSampleData(_ data: Data) {
        audioData.append(data)

        // We recommend sending samples in 100ms chunks
        let chunkSize : Int /* bytes/chunk */ = Int(0.1 /* seconds/chunk */
          * Double(SAMPLE_RATE) /* samples/second */
          * 2 /* bytes/sample */);

        if (audioData.length > chunkSize) {
          SpeechRecognitionService.sharedInstance.streamAudioData(audioData,
                                                                  completion:
            { [weak self] (response, error) in
                guard let strongSelf = self else {
                    return
                }
                
                if let error = error {
                    //strongSelf.textView.text = error.localizedDescription
                    _ = AudioController.sharedInstance.stop()
                    SpeechRecognitionService.sharedInstance.stopStreaming()
                    
                    let audioSession = AVAudioSession.sharedInstance()
                    do {
                        try audioSession.setCategory(AVAudioSession.Category.record)
                    } catch {

                    }
                    self!.audioData = NSMutableData()
                    _ = AudioController.sharedInstance.prepare(specifiedSampleRate: SAMPLE_RATE)
                    SpeechRecognitionService.sharedInstance.sampleRate = SAMPLE_RATE
                    _ = AudioController.sharedInstance.start()
                } else if let response = response {
                    var finished = false
                    print(response)
                    for result in response.resultsArray! {
                        if let result = result as? StreamingRecognitionResult {
                            if result.isFinal {
                                finished = true
                            }
                        }
                        
                        if finished {
                            let tmpBestResult = (response.resultsArray.firstObject as! StreamingRecognitionResult)
                            let tmpBestAlternativeOfResult = tmpBestResult.alternativesArray.firstObject as! SpeechRecognitionAlternative
                            let bestTranscript = tmpBestAlternativeOfResult.transcript!
                            
                            let speak = bestTranscript
                            //strongSelf.stopAudio(strongSelf)
                            if(speak.lowercased() == self!.siriActivationString.lowercased() && !self!.isActivated) {
                                self!.isActivated = true
                                if(!self!.isGifLoaded) {
                                    self!.isGifLoaded = true
                                    let gif = UIImage.gifImageWithName("speaking")
                                    self!.imgSpeaking.image = gif
                                }
                            } else if self!.isActivated {
                                var text = ""
                                if(self!.txtMessage.text != "") {
                                    text = "\(self!.txtMessage.text!) \(speak)"
                                } else {
                                    text = "\(speak)"
                                }
                                self!.txtMessage.text = text
                                let newPosition = self!.txtMessage.endOfDocument
                                self!.txtMessage.selectedTextRange = self!.txtMessage.textRange(from: newPosition, to: newPosition)
                            }
                        }
                    }
                    
                    if finished {                        
                        _ = AudioController.sharedInstance.stop()
                        SpeechRecognitionService.sharedInstance.stopStreaming()
                        
                        let audioSession = AVAudioSession.sharedInstance()
                        do {
                            try audioSession.setCategory(AVAudioSession.Category.record)
                        } catch {

                        }
                        self!.audioData = NSMutableData()
                        _ = AudioController.sharedInstance.prepare(specifiedSampleRate: SAMPLE_RATE)
                        SpeechRecognitionService.sharedInstance.sampleRate = SAMPLE_RATE
                        _ = AudioController.sharedInstance.start()
                    }
                }
          })
          self.audioData = NSMutableData()
        }
    }
    
    @IBAction func stopAudio(_ sender: NSObject) {
      _ = AudioController.sharedInstance.stop()
      SpeechRecognitionService.sharedInstance.stopStreaming()
    }
    
}

extension PhysicalExam3DVC : ShareTestDelegate {
    func shareData(_ test_id: String?, _ body_part_id: String?) {
        print("Test ID => \(test_id!)")
        self.physical_exam_test_id = test_id!
        self.heightBtnDiagnosis.constant = self.btnHeight
        self.topBtnDiagnosis.constant = 12
        self.btnGiveDiagnosis.isHidden = false
        self.body_parts.append(body_part_id!)
    }
}

extension PhysicalExam3DVC : VoiceExamDelegate {
    func voiceExamData(_ test_id: String?, _ body_part_id: String?, _ physical_exam_help: String?, _ arrBodyParts: [BodyPartsModel]?) {
        print("Test ID => \(test_id!)")
        self.physical_exam_test_id = test_id!
        self.test_id = test_id!
        self.heightBtnDiagnosis.constant = self.btnHeight
        self.topBtnDiagnosis.constant = 12
        self.btnGiveDiagnosis.isHidden = false
        self.body_parts.append(body_part_id!)
        //self.arrBodyParts = arrBodyParts!
        //self.physical_exam_help = physical_exam_help!
    }
}

extension PhysicalExam3DVC : DiagnosisDelegate {
    func diagnosisData(_ test_id: String?) {
        self.test_id = test_id!
    }
}

extension PhysicalExam3DVC: UITableViewDelegate, UITableViewDataSource{
    /*func tableView(_ tableView: UITableView, estimatedHeightForRowAt indexPath: IndexPath) -> CGFloat {
        return 53.0
    }*/
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return UITableView.automaticDimension
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if(tableView == self.tableView) {
            return self.arrSymptoms.count
        } else {
            return self.arrChatData.count
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if(tableView == self.tableView) {
            let cell = tableView.dequeueReusableCell(withIdentifier: "SymptomsCell") as! SymptomsCell
            
            cell.lblSymptoms.text = self.arrSymptoms[indexPath.row]
            
            cell.selectionStyle = .none
            return cell
        } else {
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
}

extension PhysicalExam3DVC {
    func get3DModel() {
        var modeurl = URL(string: self.modeUrl)
        //        print("===========> \(modeurl)")
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
                self.viewDownload.isHidden = false
                self.viewModel.isHidden = true
                self.viewPopupSymptom.isHidden = true
                self.viewRotateInfo.isHidden = true
                
                self.downloadFile(url: self.modeUrl)
            }
        } else {
            print("FILE PATH NOT AVAILABLE")
        }
    }
    
    func downloadFile(url: String) {
        let destination = DownloadRequest.suggestedDownloadDestination()
        
        let manager = Alamofire.SessionManager.default
        manager.session.configuration.timeoutIntervalForRequest = 300
        
        Alamofire.download(url, to: destination).downloadProgress(closure: { (progress) in
//            print("Progress => \(progress.fractionCompleted)")
            self.progress.progress = CGFloat(progress.fractionCompleted)
            let floatValue = CGFloat(progress.fractionCompleted * 100)
            let intValue = Int(floatValue)
            self.lblPercentage.text = "\(intValue)%"
            
        }).response { response in // method defaults to `.get`
            print("request => ", response.request)
            print("response => ", response.response)
            
            if(response.error == nil) {
                self.loadModel(url: URL(string: "\(response.destinationURL!)")!)
            } else {
                print("error => ", response.error?.localizedDescription)
                self.viewDownload.isHidden = true
                self.viewModel.isHidden = false
                self.drawerView.isHidden = false
                if Global.kretriveUserData().is_gif_displayed == "No"{
                    self.viewPopupSymptom.isHidden = false
                    self.viewRotateInfo.isHidden = false
                }
            }
        }
    }
}

extension PhysicalExam3DVC : UITextFieldDelegate {
    
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

extension PhysicalExam3DVC {

    // MARK: - API CALLER METHODS
    func voiceExamAPI() {
        var paramer: [String: Any] = [:]
        
        paramer["user_id"] = Global.kretriveUserData().User_Id!
        paramer["patient_id"] = self.patient_id
        
        WebService.call.POST(filePath: APIConstant.Request.voiceExam, params: paramer, enableInteraction: false, showLoader: true, viewObj: view, onSuccess: { (result, success) in
            
            if let Dict = result as? [String:Any] {
                if let str = Dict["code"] as? Int , str == 0{
                    /*if Global.kretriveUserData().is_gif_displayed == "No"{
                        let gif = UIImage.gifImageWithName("record")
                        self.imgGIF.image = gif
                        self.imgGIF.layer.cornerRadius = 10.0
                        self.imgGIF.layer.masksToBounds = true
                        self.viewGIF.isHidden = false
                    }*/
                    
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
        paramer["test_id"] = self.test_id
        paramer["sys_time"] = Date().dateTimeString(withFormate: "yyyy-MM-dd HH:mm:ss")
        
        WebService.call.POST(filePath: APIConstant.Request.voiceExamSubmit, params: paramer, enableInteraction: false, showLoader: true, viewObj: view, onSuccess: { (result, success) in
            
            if let Dict = result as? [String:Any] {
                if let str = Dict["code"] as? Int , str == 0{
                    self.arrBodyParts.removeAll()
                    
                    let test_id = JSON(Dict["test_id"]!).stringValue
                    let physical_exam_help = JSON(Dict["physical_exam_help"]!).stringValue
                    
                    let diagnosis = JSON(Dict["diagnosis"]!).stringValue
                    let notes = JSON(Dict["notes"]!).stringValue
                    
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
                    self.test_id = test_id
                    
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
                    
//                    self.delegate?.voiceExamData(test_id, self.body_part_id, physical_exam_help, self.arrBodyParts)
//                    self.btnBack(self.bntBack)
                    
                    let vc = UIStoryboard.init(name: "Development", bundle: Bundle.main).instantiateViewController(withIdentifier: "DiagnosisVC") as? DiagnosisVC
                    vc?.test_id = test_id
                    vc?.diagnosis = diagnosis
                    vc?.notes = notes
                    self.navigationController?.pushViewController(vc!, animated: true)
                }
            }
            
        }) {
            print("Error \(self.description)")
        }
    }
}
