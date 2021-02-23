//
//  PatientsListVC.swift
//  AR Patient
//
//  Created by Krupali on 29/04/20.
//  Copyright © 2020 Silicon. All rights reserved.
//

import UIKit
import SwiftyJSON
import SFProgressCircle
import Alamofire

class PatientsListVC: UIViewController {
    
    @IBOutlet weak var lblTitle: IPAutoScalingLabel!
    @IBOutlet weak var lblTitle1: IPAutoScalingLabel!
    @IBOutlet weak var headerView: UIView!
    @IBOutlet weak var viewSearch: UIView!
    @IBOutlet weak var txtSearch: IPTextField!
    @IBOutlet weak var tableview: UITableView!
    
    @IBOutlet weak var viewDownload: UIView!
    @IBOutlet weak var bgProgress: SFCircleGradientView!
    @IBOutlet weak var progress: SFCircleGradientView!
    @IBOutlet weak var lblPercentage: UILabel!
    
    var arrData = [PatientListModel]()
    var filterData = [PatientListModel]()
    var search : String = ""
    var timer = Timer()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.tableview.register(UINib(nibName: "PatientsListCell", bundle: nil), forCellReuseIdentifier: "PatientsListCell")
        self.tableview.delegate = self
        self.tableview.dataSource = self
        self.tableview.separatorStyle = .none
        self.tableview.estimatedRowHeight = 150.0
        
        headerView.roundCorners([.bottomLeft, .bottomRight], radius: 25)
        self.viewSearch.layer.cornerRadius = self.viewSearch.frame.height/2
        self.viewSearch.layer.masksToBounds = true
        setLanguageText()
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        headerView.roundCorners([.bottomLeft, .bottomRight], radius: 25)
        self.viewSearch.layer.cornerRadius = self.viewSearch.frame.height/2
        self.viewSearch.layer.masksToBounds = true
        setLanguageText()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        headerView.roundCorners([.bottomLeft, .bottomRight], radius: 25)
        self.viewSearch.layer.cornerRadius = self.viewSearch.frame.height/2
        self.viewSearch.layer.masksToBounds = true
        setLanguageText()
        call_PatientAPI()
    }
    
    //MARK:- CUSTOM METHODS
    func setLanguageText() -> Void {
        self.txtSearch.delegate = self
        
        txtSearch.placeholder = "KeylblSearchBar".localize
        
        txtSearch.withImage(direction: .Left, image: #imageLiteral(resourceName: "Search"), colorSeparator: UIColor.clear, colorBorder: UIColor.clear)
        headerView.roundCorners([.bottomLeft, .bottomRight], radius: 25)
        self.viewSearch.layer.cornerRadius = self.viewSearch.frame.height/2
        self.viewSearch.layer.masksToBounds = true
        
        lblTitle.text = "KeylblARListTitlte".localize
        lblTitle1.text = "KeylblARListTitlte".localize
        
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
    }
    
    // MARK: - 
    // MARK: - BUTTON ACTION METHODS
    @IBAction func btnFilter(_ sender: Any) {
    }
    
    // MARK: - 
    // MARK: - API CALLER METHODS
    func call_PatientAPI() {
        var paramer: [String: Any] = [:]
        paramer["user_id"] = Global.kretriveUserData().User_Id!
        
        WebService.call.POST(filePath: APIConstant.Request.patientList, params: paramer, enableInteraction: false, showLoader: true, viewObj: view, onSuccess: { (result, success) in
            
            if let Dict = result as? [String:Any] {
                if let str = Dict["code"] as? Int , str == 0{
                    self.arrData.removeAll()
                    
                    if let arrPatient = Dict["types"] as? NSArray {
                        for arrPatientData in arrPatient {
                            if let dictPatient = arrPatientData as? [String : Any]{
                                let patientData = PatientListModel()
                                
                                patientData.avatar = JSON(dictPatient["avatar"]!).stringValue
                                patientData.hint_text = JSON(dictPatient["hint_text"]!).stringValue
                                patientData.model = JSON(dictPatient["model"]!).stringValue
                                patientData.patient_name = JSON(dictPatient["name"]!).stringValue
                                patientData.patient_id = JSON(dictPatient["patient_id"]!).stringValue
                                patientData.pt_name = JSON(dictPatient["pt_name"]!).stringValue
                                patientData.physical_exam_counter = JSON(dictPatient["physical_exam_counter"]!).intValue
                                patientData.voice_exam_counter = JSON(dictPatient["voice_exam_counter"]!).intValue
                                var symptoms = ""
                                if let arrSymptoms = dictPatient["symptoms"] as? NSArray {
                                    for arrPatientData in arrSymptoms {
                                        symptoms = "\(JSON(arrPatientData).stringValue), \(symptoms)"
                                    }
                                }
                                symptoms = String(symptoms.dropLast(2))
                                patientData.symptoms = symptoms
                                
                                if let arrVitalSigns = dictPatient["vital_signs"] as? NSArray {
                                    for arrVitalData in arrVitalSigns {
                                        if let dictVitalSigns = arrVitalData as? [String : Any]{
                                            let vitalData = VitalSignModel()
                                            
                                            vitalData.vital_sign = JSON(dictVitalSigns["vital_sign"]!).stringValue
                                            vitalData.value = JSON(dictVitalSigns["value"]!).stringValue
                                            
                                            patientData.vital_signs.append(vitalData)
                                        }
                                    }
                                }
                                
                                if let arrBodyParts = dictPatient["body_parts"] as? NSArray {
                                    for arrarrBodyPartsData in arrBodyParts {
                                        if let dictBodyParts = arrarrBodyPartsData as? [String : Any]{
                                            let bodyPartsData = BodyPartsModel()
                                            
                                            bodyPartsData.id = JSON(dictBodyParts["id"]!).stringValue
                                            bodyPartsData.name = JSON(dictBodyParts["name"]!).stringValue
                                            
                                            var arr = [String]()
                                            if let arrSymptoms = dictBodyParts["symptoms"] as? NSArray {
                                                for arrSymptomsData in arrSymptoms {
                                                    let symptoms = JSON(arrSymptomsData).stringValue
                                                    arr.append(symptoms)
                                                }
                                            }
                                            bodyPartsData.symptoms = arr
                                            
                                            patientData.arrBodyParts.append(bodyPartsData)
                                        }
                                    }
                                }
                                
                                if(patientData.physical_exam_counter != 0 && patientData.voice_exam_counter != 0) {
                                    self.arrData.append(patientData)
                                }
                            }
                        }
                        self.filterData = self.arrData
                        
                        self.tableview.reloadData()
                    }
                }
            }
            
        }) {
            print("Error \(self.description)")
        }
    }
}

extension PatientsListVC: UITableViewDelegate, UITableViewDataSource{
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return UITableView.automaticDimension
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.filterData.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "PatientsListCell") as! PatientsListCell
        
        cell.lblName.text = ""
        cell.lblTitle.text = ""
        cell.lblDescription.text = ""
        cell.lblVitalSign.text = ""
        
        let data = self.filterData[indexPath.row]
        
        if(data.avatar != "") {
            cell.imgAvatar.sd_setImage(with: URL(string: APIConstant.mediaURL + data.avatar))
        }
        cell.lblName.text = data.patient_name
        cell.lblTitle.text = data.pt_name
        cell.lblSymptoms.text = "Patient Introduction"
        cell.lblDescription.text = data.symptoms
        cell.lblDescription.numberOfLines = 3
        
        let lastRowIndex = tableView.numberOfRows(inSection: tableView.numberOfSections-1)
        
        for vital_signs in data.vital_signs {
            if (indexPath.row == lastRowIndex - 1) {
                cell.lblVitalSign.text = "\(cell.lblVitalSign.text!)\(vital_signs.vital_sign): \(vital_signs.value)\n"
            } else{
                cell.lblVitalSign.text = "\(cell.lblVitalSign.text!)\(vital_signs.vital_sign): \(vital_signs.value)\n"
            }
//            cell.lblVitalSign.text = "\(cell.lblVitalSign.text!)\(vital_signs.vital_sign): \(vital_signs.value)\n"
        }
        
        cell.selectionStyle = .none
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let data = self.filterData[indexPath.row]
        
        let cell = tableView.cellForRow(at: indexPath) as! PatientsListCell
        
        let vc = UIStoryboard.init(name: "Development", bundle: Bundle.main).instantiateViewController(withIdentifier: "PhysicalExam3DVC") as? PhysicalExam3DVC
        vc?.patient_id = data.patient_id
        vc?.avatar = data.avatar
        vc?.patient_name = data.patient_name
        vc?.modeUrl = "\(APIConstant.mediaURL)\(data.model)"
        vc?.vital_signs = cell.lblVitalSign.text!
        vc?.arrBodyParts = data.arrBodyParts
        vc?.hint_text = data.hint_text
        vc?.pt_name = data.pt_name
        vc?.tag = 1
        self.navigationController?.pushViewController(vc!, animated: true)
        
        /*let vc = UIStoryboard.init(name: "Development", bundle: Bundle.main).instantiateViewController(withIdentifier: "eSOAPVC") as? eSOAPVC
        self.navigationController?.pushViewController(vc!, animated: true)*/
        //self.get3DModel(data: data, cell: cell)
        
    }
}

extension PatientsListVC: UITextFieldDelegate{
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        if let text = textField.text,
        let textRange = Range(range, in: text) {
            let updatedText = text.replacingCharacters(in: textRange, with: string)
            if(updatedText == "") {
                self.filterData = self.arrData
                self.tableview.reloadData()
            } else {
                filterData = arrData.filter({$0.patient_name.lowercased().range(of: updatedText.lowercased()) != nil})
                self.tableview.reloadData()
            }
        }
        /*if string.isEmpty {
            search = String(search.characters.dropLast())
        } else{
            search = textField.text! + string
        }
        
        if(search == ""){
            self.filterData = self.arrData
            self.tableview.reloadData()
        } else{
            filterData = arrData.filter({$0.patient_name.lowercased().range(of: search.lowercased()) != nil})
            self.tableview.reloadData()
        }*/
        return true
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        self.view.endEditing(true)
    }
}

extension PatientsListVC {
    func get3DModel(data: PatientListModel, cell: PatientsListCell) {
        var modeurl = URL(string: "\(APIConstant.mediaURL)\(data.model)")
        //        print("===========> \(modeurl)")
        var modelName = modeurl!.lastPathComponent
        
        let path = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true)[0] as String
        let url = NSURL(fileURLWithPath: path)
        if let pathComponent = url.appendingPathComponent(modelName) {
            let filePath = pathComponent.path
            let fileManager = FileManager.default
            if fileManager.fileExists(atPath: filePath) {
                print("FILE AVAILABLE")
                self.moveToVoiceExam(data: data, cell: cell, url: URL(string: filePath)!)
            } else {
                print("FILE NOT AVAILABLE")
                self.viewDownload.isHidden = false
                //self.viewModel.isHidden = true
                
                self.downloadFile(url: "\(APIConstant.mediaURL)\(data.model)", data: data, cell: cell)
            }
        } else {
            print("FILE PATH NOT AVAILABLE")
        }
    }
    
    func downloadFile(url: String, data: PatientListModel, cell: PatientsListCell) {
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
                self.moveToVoiceExam(data: data, cell: cell, url: URL(string: "\(response.destinationURL!)")!)
            } else {
                print("error => ", response.error?.localizedDescription)
                self.viewDownload.isHidden = true
                //self.viewModel.isHidden = false
            }
        }
    }
    
    func moveToVoiceExam(data: PatientListModel, cell: PatientsListCell, url : URL) {
        let vc = UIStoryboard.init(name: "Development", bundle: Bundle.main).instantiateViewController(withIdentifier: "VoiceExamVC") as? VoiceExamVC
        vc?.patient_id = data.patient_id
        vc?.avatar = data.avatar
        vc?.patient_name = data.patient_name
        vc?.modeUrl = "\(APIConstant.mediaURL)\(data.model)"
        vc?.vital_signs = cell.lblVitalSign.text!
        vc?.modelUrl = url
        self.viewDownload.isHidden = true
        self.navigationController?.pushViewController(vc!, animated: true)
    }
}
