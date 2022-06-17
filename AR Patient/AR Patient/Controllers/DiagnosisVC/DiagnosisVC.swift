//
//  DiagnosisVC.swift
//  AR Patient
//
//  Created by Knoxweb on 05/05/20.
//  Copyright © 2020 Knoxweb. All rights reserved.
//

import UIKit
import SwiftyJSON

protocol DiagnosisDelegate {
    func diagnosisData(_ test_id: String?)
}

class DiagnosisVC: UIViewController {

    @IBOutlet weak var btnBack: UIButton!
    @IBOutlet weak var viewHeader: UIView!
    @IBOutlet weak var txtDiagnosis: IPTextField!
    @IBOutlet weak var txtSummary: IPTextView!
    @IBOutlet weak var txtObjective: IPTextView!
    @IBOutlet weak var txtAssessment: IPTextView!
    @IBOutlet weak var txtPlan: IPTextView!
    @IBOutlet weak var btnSubmit: IPButton!
    @IBOutlet weak var imgOverlay: UIImageView!
    @IBOutlet weak var viewOverlay: UIView!
    
    var test_id : String = ""
    var patient_id : String = ""
    var tag : Int = 1 //1 = Submit Exam & 2 = Without Submitting Exam
    var delegate: DiagnosisDelegate?
    var diagnosis : String = ""
    var subjective : String = ""
    var objective : String = ""
    var assessment : String = ""
    var plan : String = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        self.viewHeader.layer.frame = CGRect(x: 0, y: 0, width: UIScreen.main.bounds.size.width, height: 60)
        self.viewHeader.roundCorners([.bottomLeft, .bottomRight], radius: 25)
        
        self.txtDiagnosis.delegate = self
        self.txtSummary.delegate = self
        self.txtObjective.delegate = self
        self.txtAssessment.delegate = self
        self.txtPlan.delegate = self
            
        self.txtDiagnosis.withoutImage(direction: .Left, colorSeparator: UIColor.clear, colorBorder: UIColor.clear)
        
        self.setUpPlaceHolder()
        
        self.txtDiagnosis.text = self.diagnosis
        
        if(self.tag == 1) {
            self.btnBack.isHidden = false
        } else {
            self.btnBack.isHidden = false
        }
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.setUpPlaceHolder()
        
        self.txtDiagnosis.text = self.diagnosis
    }
    
    override func viewDidAppear(_ animated: Bool) {
        self.navigationController?.navigationBar.isHidden = true

        self.txtDiagnosis.layer.cornerRadius = self.txtDiagnosis.frame.height/2
        self.txtDiagnosis.layer.masksToBounds = true
        
        self.setUpPlaceHolder()
        
        self.txtDiagnosis.text = self.diagnosis
    }
    
    @IBAction func btnBack(_ sender: UIButton) {
        self.view.endEditing(true)
        self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func btnSubmit(_ sender: UIButton) {
        self.view.endEditing(true)
        self.validationInfo()
    }
    
    @IBAction func btnInfo(_ sender: UIButton) {
        self.imgOverlay.isHidden = false
        self.viewOverlay.isHidden = false
    }
    
    @IBAction func btnClose(_ sender: UIButton) {
        self.imgOverlay.isHidden = true
        self.viewOverlay.isHidden = true
    }
    
    func validationInfo() {
        self.view.endEditing(true)
        
        let strDiagnosis = self.txtDiagnosis.text!.trimmingCharacters(in: .whitespaces)
        let strSummary = txtSummary.text!.trimmingCharacters(in: .whitespaces)
        
        guard strDiagnosis.count > 0 else{
            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "Please enter diagnosis.") { (index, title) in }
            return
        }
        
        guard strSummary.count > 0 && strSummary != "Subjective Notes" else{
            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "Please enter Subjective notes.") { (index, title) in }
            return
        }
        
        self.testDiagnosisAPI()
    }
    
    func setUpPlaceHolder() {
        if(self.subjective == "") {
            self.txtSummary.text = "Subjective Notes"
            self.txtSummary.tag = 102
        } else {
            self.txtSummary.text = self.subjective
            txtSummary.tag = 101
            txtSummary.textColor = UIColor.init(named: "Color_Black")
        }
        
        if(self.objective == "") {
            self.txtObjective.text = "Objective Notes"
            self.txtObjective.tag = 102
        } else {
            self.txtObjective.text = self.objective
            txtObjective.tag = 101
            txtObjective.textColor = UIColor.init(named: "Color_Black")
        }
        
        if(self.assessment == "") {
            self.txtAssessment.text = "Assessment Notes"
            self.txtAssessment.tag = 102
        } else {
            self.txtAssessment.text = self.assessment
            txtAssessment.tag = 101
            txtAssessment.textColor = UIColor.init(named: "Color_Black")
        }
        
        if(self.plan == "") {
            self.txtPlan.text = "Plan Notes"
            self.txtPlan.tag = 102
        } else {
            self.txtPlan.text = self.plan
            txtPlan.tag = 101
            txtPlan.textColor = UIColor.init(named: "Color_Black")
        }
    }
}

extension DiagnosisVC : UITextFieldDelegate, UITextViewDelegate {
    
    func textView(_ textView: UITextView, shouldChangeTextIn range: NSRange, replacementText text: String) -> Bool {
        return txtSummary.text.count + (text.count - range.length) <= 300
    }
    
    func textViewDidBeginEditing(_ textView: UITextView) {
        if txtSummary.text == "Subjective Notes" {
            txtSummary.text = ""
            txtSummary.tag = 101
            txtSummary.textColor = UIColor.init(named: "Color_Black")
        } else if txtObjective.text == "Objective Notes" {
            txtObjective.text = ""
            txtObjective.tag = 101
            txtObjective.textColor = UIColor.init(named: "Color_Black")
        } else if txtAssessment.text == "Assessment Notes" {
            txtAssessment.text = ""
            txtAssessment.tag = 101
            txtAssessment.textColor = UIColor.init(named: "Color_Black")
        } else if txtPlan.text == "Plan Notes" {
            txtPlan.text = ""
            txtPlan.tag = 101
            txtPlan.textColor = UIColor.init(named: "Color_Black")
        }
    }

    func textViewDidEndEditing(_ textView: UITextView) {
        if txtSummary.text == "" {
            txtSummary.text = "Subjective Notes"
            txtSummary.tag = 102
            txtSummary.textColor = UIColor.lightGray
        } else if txtObjective.text == "" {
            txtObjective.text = "Objective Notes"
            txtObjective.tag = 102
            txtObjective.textColor = UIColor.lightGray
        } else if txtAssessment.text == "" {
            txtAssessment.text = "Assessment Notes"
            txtAssessment.tag = 102
            txtAssessment.textColor = UIColor.lightGray
        } else if txtPlan.text == "" {
            txtPlan.text = "Plan Notes"
            txtPlan.tag = 102
            txtPlan.textColor = UIColor.lightGray
        }
    }
    
    
    
}

extension DiagnosisVC{
    func testDiagnosisAPI() {
        var paramer: [String: Any] = [:]

        paramer["user_id"] = Global.kretriveUserData().User_Id!
        paramer["test_id"] = self.test_id
        paramer["diagnosis"] = self.txtDiagnosis.text!
        paramer["subjective"] = self.txtSummary.text!
        paramer["objective"] = self.txtObjective.text!
        paramer["assessment"] = self.txtAssessment.text!
        paramer["plan"] = self.txtPlan.text!
        paramer["patient_id"] = self.patient_id
        paramer["sys_time"] = Date().dateTimeString(withFormate: "yyyy-MM-dd HH:mm:ss")

        WebService.call.POST(filePath: APIConstant.Request.testDiagnosis, params: paramer, enableInteraction: false, showLoader: true, viewObj: (Global.appDelegate.window?.rootViewController!.view)!, onSuccess: { (result, success) in
            
            if let Dict = result as? [String:Any] {
                if let str = Dict["code"] as? Int , str == 0{
                    if(self.tag == 1) {
                        let vc = UIStoryboard.init(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "TestResultsVC") as? TestResultsVC
                        vc?.test_id = self.test_id
                        vc?.isFromExam = true
                        self.navigationController?.pushViewController(vc!, animated: true)
                    } else {
                        let test_id = JSON(Dict["test_id"]!).stringValue
                        self.delegate?.diagnosisData(test_id)
                        self.btnBack(self.btnBack)
                    }
                }
            }

        }) {
            print("Error \(self.description)")
        }
    }

}
