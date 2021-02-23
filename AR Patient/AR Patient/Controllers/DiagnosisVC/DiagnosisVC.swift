//
//  DiagnosisVC.swift
//  AR Patient
//
//  Created by Silicon on 05/05/20.
//  Copyright © 2020 Silicon. All rights reserved.
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
    @IBOutlet weak var btnSubmit: IPButton!
    
    var test_id : String = ""
    var patient_id : String = ""
    var tag : Int = 1 //1 = Submit Exam & 2 = Without Submitting Exam
    var delegate: DiagnosisDelegate?
    var diagnosis : String = ""
    var notes : String = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        self.viewHeader.layer.frame = CGRect(x: 0, y: 0, width: UIScreen.main.bounds.size.width, height: 60)
        self.viewHeader.roundCorners([.bottomLeft, .bottomRight], radius: 25)
        
        self.txtDiagnosis.delegate = self
        self.txtSummary.delegate = self
            
        self.txtDiagnosis.withoutImage(direction: .Left, colorSeparator: UIColor.clear, colorBorder: UIColor.clear)
        
        if(self.notes == "") {
            self.txtSummary.text = "Soap Notes"
            self.txtSummary.tag = 102
        } else {
            self.txtSummary.text = self.notes
            txtSummary.tag = 101
            txtSummary.textColor = UIColor.init(named: "Color_Black")
        }
        
        self.txtDiagnosis.text = self.diagnosis
        
        if(self.tag == 1) {
            self.btnBack.isHidden = false
        } else {
            self.btnBack.isHidden = false
        }
    }
    
    override func viewWillAppear(_ animated: Bool) {
        if(self.notes == "") {
            self.txtSummary.text = "Soap Notes"
            self.txtSummary.tag = 102
        } else {
            self.txtSummary.text = self.notes
            txtSummary.tag = 101
            txtSummary.textColor = UIColor.init(named: "Color_Black")
        }
        
        self.txtDiagnosis.text = self.diagnosis
    }
    
    override func viewDidAppear(_ animated: Bool) {
        self.navigationController?.navigationBar.isHidden = true

        self.txtDiagnosis.layer.cornerRadius = self.txtDiagnosis.frame.height/2
        self.txtDiagnosis.layer.masksToBounds = true
        
        if(self.notes == "") {
            self.txtSummary.text = "Soap Notes"
            self.txtSummary.tag = 102
        } else {
            self.txtSummary.text = self.notes
            txtSummary.tag = 101
            txtSummary.textColor = UIColor.init(named: "Color_Black")
        }
        
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
    
    func validationInfo() {
        self.view.endEditing(true)
        
        let strDiagnosis = self.txtDiagnosis.text!.trimmingCharacters(in: .whitespaces)
        let strSummary = txtSummary.text!.trimmingCharacters(in: .whitespaces)
        
        guard strDiagnosis.count > 0 else{
            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "Please enter diagnosis.") { (index, title) in }
            return
        }
        
        guard strSummary.count > 0 && strSummary != "Soap Notes" else{
            AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "Please enter SOAP notes.") { (index, title) in }
            return
        }
        
        self.testDiagnosisAPI()
    }
}

extension DiagnosisVC : UITextFieldDelegate, UITextViewDelegate {
    
    func textView(_ textView: UITextView, shouldChangeTextIn range: NSRange, replacementText text: String) -> Bool {
        return txtSummary.text.count + (text.count - range.length) <= 300
    }
    
    func textViewDidBeginEditing(_ textView: UITextView) {
        if txtSummary.text == "Soap Notes" {
            txtSummary.text = ""
            txtSummary.tag = 101
            txtSummary.textColor = UIColor.init(named: "Color_Black")
        }
    }

    func textViewDidEndEditing(_ textView: UITextView) {
        if txtSummary.text == "" {
            txtSummary.text = "Soap Notes"
            txtSummary.tag = 102
            txtSummary.textColor = UIColor.lightGray
        }
    }
    
    
    
}

extension DiagnosisVC{
    func testDiagnosisAPI() {
        var paramer: [String: Any] = [:]

        paramer["user_id"] = Global.kretriveUserData().User_Id!
        paramer["test_id"] = self.test_id
        paramer["diagnosis"] = self.txtDiagnosis.text!
        paramer["notes"] = self.txtSummary.text!
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
