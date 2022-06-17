//
//  TestResultsVC.swift
//  AR Patient
//
//  Created by Knoxweb on 04/05/20.
//  Copyright © 2020 Knoxweb. All rights reserved.
//

import UIKit
import SFProgressCircle
import SwiftyJSON

class TestResultsVC: UIViewController {
    
    @IBOutlet weak var MainView: UIView!
    @IBOutlet weak var bgProgress: SFCircleGradientView!
    @IBOutlet weak var progress: SFCircleGradientView!
    @IBOutlet weak var lblPercentage: IPAutoScalingLabel!
    @IBOutlet weak var lblPerformance: IPAutoScalingLabel!
    @IBOutlet weak var viewHeader: UIView!
    @IBOutlet weak var lblTitle: IPAutoScalingLabel!
    
    @IBOutlet weak var btnDone: IPButton!
    @IBOutlet weak var lblDiagnosis: IPAutoScalingLabel!
    @IBOutlet weak var lblSummary: IPAutoScalingLabel!
    @IBOutlet weak var lblObjective: IPAutoScalingLabel!
    @IBOutlet weak var lblAssessment: IPAutoScalingLabel!
    @IBOutlet weak var lblPlan: IPAutoScalingLabel!
    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var btnBack: UIButton!
    
    @IBOutlet weak var tableview1: UITableView!
    @IBOutlet weak var tableview2: UITableView!
    
    @IBOutlet weak var tableview1Height: NSLayoutConstraint!
    @IBOutlet weak var tableview2Height: NSLayoutConstraint!
    
    @IBOutlet weak var lblDidWell: IPAutoScalingLabel!
    @IBOutlet weak var lblNeedImprove: IPAutoScalingLabel!
    
    @IBOutlet weak var btnSoapNotes: UIButton!
    @IBOutlet weak var lblSubjectiveNotes: IPAutoScalingLabel!
    @IBOutlet weak var lblObjectiveNotes: IPAutoScalingLabel!
    @IBOutlet weak var lblAssessmentNotes: IPAutoScalingLabel!
    @IBOutlet weak var lblPlanNotes: IPAutoScalingLabel!
    
    //Body Parts
    @IBOutlet weak var lblBodyPartsTitle: IPAutoScalingLabel!
    @IBOutlet weak var checkedBodyPartsTableview: UITableView!
    @IBOutlet weak var checkedBodyPartsTableviewHeight: NSLayoutConstraint!
    @IBOutlet weak var uncheckedBodyPartsTableview: UITableView!
    @IBOutlet weak var uncheckedBodyPartsTableviewHeight: NSLayoutConstraint!
    
    
    var test_id : String = ""
    var arrDidWell = [TestResultModel]()
    var arrImprovement = [TestResultModel]()
    var isFromExam : Bool = false
    var subjective_notes = ""
    var objective_notes = ""
    var assessment_notes = ""
    var plan_notes = ""
    var arrResultBodyParts = [BodyPartsModel]()
    var arrCheckedBodyParts = [String]()
    var arrUncheckedBodyParts = [String]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.tabBarController?.tabBar.isHidden = true
        
//        print("Clicked Data =======> \(Singleton.shared.arrClickedBodyParts.count)")
        
        self.viewHeader.layer.frame = CGRect(x: 0, y: 0, width: UIScreen.main.bounds.size.width, height: 60)
        self.viewHeader.roundCorners([.bottomLeft, .bottomRight], radius: 25)
        
        MainView.layer.cornerRadius = MainView.bounds.width/2
        
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
        
        lblTitle.text = "KeylblTestResults".localize
        
        tableview1.register(UINib(nibName: "TestResultsCell", bundle: nil), forCellReuseIdentifier: "TestResultsCell")
        tableview2.register(UINib(nibName: "TestResultsCell", bundle: nil), forCellReuseIdentifier: "TestResultsCell")
        
        tableview1.delegate = self
        tableview1.dataSource = self
        
        tableview2.delegate = self
        tableview2.dataSource = self
        
        self.tableview1.separatorStyle = .none
        self.tableview2.separatorStyle = .none
        
        uncheckedBodyPartsTableview.register(UINib(nibName: "BodyPartsCell", bundle: nil), forCellReuseIdentifier: "BodyPartsCell")
        uncheckedBodyPartsTableview.delegate = self
        uncheckedBodyPartsTableview.dataSource = self
        self.uncheckedBodyPartsTableview.separatorStyle = .none
        
        checkedBodyPartsTableview.register(UINib(nibName: "BodyPartsCell", bundle: nil), forCellReuseIdentifier: "BodyPartsCell")
        checkedBodyPartsTableview.delegate = self
        checkedBodyPartsTableview.dataSource = self
        self.checkedBodyPartsTableview.separatorStyle = .none
        
        tableview1.reloadData()
        tableview2.reloadData()
        uncheckedBodyPartsTableview.reloadData()
        checkedBodyPartsTableview.reloadData()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        MainView.layer.cornerRadius = MainView.bounds.width/2
        
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
        
        lblTitle.text = "KeylblTestResults".localize
        
        tableview1.register(UINib(nibName: "TestResultsCell", bundle: nil), forCellReuseIdentifier: "TestResultsCell")
        tableview2.register(UINib(nibName: "TestResultsCell", bundle: nil), forCellReuseIdentifier: "TestResultsCell")
        
        tableview1.delegate = self
        tableview1.dataSource = self
        
        tableview2.delegate = self
        tableview2.dataSource = self
        
        self.tableview1.separatorStyle = .none
        self.tableview2.separatorStyle = .none
        
        uncheckedBodyPartsTableview.register(UINib(nibName: "BodyPartsCell", bundle: nil), forCellReuseIdentifier: "BodyPartsCell")
        uncheckedBodyPartsTableview.delegate = self
        uncheckedBodyPartsTableview.dataSource = self
        self.uncheckedBodyPartsTableview.separatorStyle = .none
        
        checkedBodyPartsTableview.register(UINib(nibName: "BodyPartsCell", bundle: nil), forCellReuseIdentifier: "BodyPartsCell")
        checkedBodyPartsTableview.delegate = self
        checkedBodyPartsTableview.dataSource = self
        self.checkedBodyPartsTableview.separatorStyle = .none
        
        tableview1.reloadData()
        tableview2.reloadData()
        uncheckedBodyPartsTableview.reloadData()
        checkedBodyPartsTableview.reloadData()
        
        self.testResultAPI()
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        self.tabBarController?.tabBar.isHidden = false
    }
    
    func noData(label: String, tableview: UITableView) {
        var noDataLabel: UILabel = UILabel(frame: CGRect(x: 0, y: 0, width: tableview.bounds.size.width, height: 20))
        noDataLabel.text = label
        noDataLabel.font = UIFont.init(name: Theme.Font.Bold, size: Theme.Font.size.TxtSize_14)
        noDataLabel.textColor = UIColor.init(named: "Color_Blue")
        noDataLabel.textAlignment = NSTextAlignment.center
        tableview.backgroundView = noDataLabel
    }
    
    // MARK: - 
    // MARK: - BUTTON ACTION METHODS
    @IBAction func btnBack(_ sender: Any) {
        self.view.endEditing(true)
        if(isFromExam) {
            for controller in self.navigationController!.viewControllers as Array {
                if controller.isKind(of: TabBarVC.self) {
                    self.navigationController!.popToViewController(controller, animated: true)
                    break
                }
            }
        } else {
            self.navigationController?.popViewController(animated: true)
        }
    }
    
    @IBAction func btnDone(_ sender: Any) {
        self.btnBack(self.btnBack)
    }
    
    @IBAction func btnSoapNotes(_ sender: UIButton) {
        self.tabBarController?.tabBar.isHidden = true
        let vc = UIStoryboard.init(name: "Development", bundle: Bundle.main).instantiateViewController(withIdentifier: "SOAPNotesVC") as? SOAPNotesVC
        vc!.subjective_notes = self.subjective_notes
        vc!.objective_notes = self.objective_notes
        vc!.assessment_notes = self.assessment_notes
        vc!.plan_notes = self.plan_notes
        self.navigationController?.pushViewController(vc!, animated: true)
    }
}

extension TestResultsVC: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 45
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if(tableView == tableview1){
            if(self.arrDidWell.count == 0) {
                self.noData(label: "No data available.", tableview: self.tableview1)
            } else {
                self.tableview1.backgroundView = nil
            }
            return self.arrDidWell.count
        } else if(tableView == self.uncheckedBodyPartsTableview){
            if(self.arrUncheckedBodyParts.count == 0) {
                self.noData(label: "No data available.", tableview: self.uncheckedBodyPartsTableview)
            } else {
                self.uncheckedBodyPartsTableview.backgroundView = nil
            }
            return self.arrUncheckedBodyParts.count
        } else if(tableView == self.checkedBodyPartsTableview){
            if(self.arrCheckedBodyParts.count == 0) {
                self.noData(label: "No data available.", tableview: self.checkedBodyPartsTableview)
            } else {
                self.checkedBodyPartsTableview.backgroundView = nil
            }
            return self.arrCheckedBodyParts.count
        } else {
            if(self.arrImprovement.count == 0) {
                self.noData(label: "No data available.", tableview: self.tableview2)
            } else {
                self.tableview2.backgroundView = nil
            }
            return self.arrImprovement.count
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {        
        if(tableView == tableview1){
            let cell = tableView.dequeueReusableCell(withIdentifier: "TestResultsCell") as! TestResultsCell
            let data1 = self.arrDidWell[indexPath.row]
            
            cell.circleView.backgroundColor = UIColor.green
            cell.lblExample.text = "\(data1.name)"
            cell.selectionStyle = .none
            return cell
        } else if(tableView == tableview2){
            let cell = tableView.dequeueReusableCell(withIdentifier: "TestResultsCell") as! TestResultsCell
            let data2 = self.arrImprovement[indexPath.row]
            
            cell.circleView.backgroundColor = UIColor.systemOrange
            cell.lblExample.text = "\(data2.name)"
            cell.selectionStyle = .none
            return cell
        } else if(tableView == self.uncheckedBodyPartsTableview){
            let cell = tableView.dequeueReusableCell(withIdentifier: "BodyPartsCell") as! BodyPartsCell
            cell.lblTitle.text = "\(self.arrUncheckedBodyParts[indexPath.row].replacingOccurrences(of: "_", with: " ").capitalized)"
            cell.img.image = UIImage(named: "red_cross_icon")
            cell.img.tintColor = UIColor(named: "Color_Red")
            cell.selectionStyle = .none
            return cell
        } else{
            let cell = tableView.dequeueReusableCell(withIdentifier: "BodyPartsCell") as! BodyPartsCell
            cell.lblTitle.text = "\(self.arrCheckedBodyParts[indexPath.row].replacingOccurrences(of: "_", with: " ").capitalized)"
            cell.img.image = UIImage(named: "green_check_icon")
            cell.img.tintColor = UIColor(named: "Color_Green")
            cell.selectionStyle = .none
            return cell
        }
        /*else{
            let cell = tableView.dequeueReusableCell(withIdentifier: "BodyPartsCell") as! BodyPartsCell
            cell.lblTitle.text = "\(self.arrResultBodyParts[indexPath.row].name)"
            if(self.arrResultBodyParts[indexPath.row].is_selected == "0"){
                cell.img.image = UIImage(named: "red_cross_icon")
                cell.img.tintColor = UIColor(named: "Color_Red")
            } else if(self.arrResultBodyParts[indexPath.row].is_selected == "1"){
                cell.img.image = UIImage(named: "green_check_icon")
                cell.img.tintColor = UIColor(named: "Color_Green")
            }
            cell.selectionStyle = .none
            return cell
        }*/
        
    }
}

extension TestResultsVC {
    
    // MARK: - API CALLER METHODS
    func testResultAPI() {
        var paramer: [String: Any] = [:]
        
        paramer["user_id"] = Global.kretriveUserData().User_Id!
        paramer["test_id"] = self.test_id
        
        WebService.call.POST(filePath: APIConstant.Request.testResult, params: paramer, enableInteraction: false, showLoader: true, viewObj: view, onSuccess: { (result, success) in
            
            if let Dict = result as? [String:Any] {
                if let str = Dict["code"] as? Int , str == 0{
                    
                    self.arrDidWell.removeAll()
                    self.arrImprovement.removeAll()
                    self.arrResultBodyParts.removeAll()
                    self.arrUncheckedBodyParts.removeAll()
                    self.arrCheckedBodyParts.removeAll()
                    
                    let average_score_limit = JSON(Dict["average_score_limit"]!).floatValue
                    
                    if let patient = Dict["patient"] as? [String:Any] {
                        let name = JSON(patient["name"]!).stringValue
                        let type = JSON(patient["type"]!).stringValue
                        let symptoms = JSON(patient["symptoms"]!).stringValue
                    }
                    
                    if let arrResult_body_parts = Dict["result_body_parts"] as? NSArray {
                        for arrResult_body_partsData in arrResult_body_parts {
                            if let dictResult_body_parts = arrResult_body_partsData as? [String : Any]{
                                let dictResult_body_partsData = BodyPartsModel()
                                
                                dictResult_body_partsData.id = JSON(dictResult_body_parts["id"]!).stringValue
                                dictResult_body_partsData.is_selected = JSON(dictResult_body_parts["is_selected"]!).stringValue
                                dictResult_body_partsData.name = JSON(dictResult_body_parts["name"]!).stringValue
                                
                                self.arrResultBodyParts.append(dictResult_body_partsData)
                                
//                                if(JSON(dictResult_body_parts["is_selected"]!).stringValue == "0"){
//                                    self.arrUncheckedBodyParts.append(JSON(dictResult_body_parts["name"]!).stringValue)
//                                } else if(JSON(dictResult_body_parts["is_selected"]!).stringValue == "1"){
//                                    self.arrCheckedBodyParts.append(JSON(dictResult_body_parts["name"]!).stringValue)
//                                }
                            }
                        }
                    }
                    
                    if let test = Dict["test"] as? [String:Any] {
                        let diagnosis = JSON(test["diagnosis"]!).stringValue
                        let notes = JSON(test["notes"]!).stringValue
                        let result = JSON(test["result"]!).floatValue
                        
                        if let soap_written_by_learner = test["soap_written_by_learner"] as? [String:Any] {
                            self.lblSubjectiveNotes.text = JSON(soap_written_by_learner["subjective_notes"]!).stringValue
                            self.lblObjectiveNotes.text = JSON(soap_written_by_learner["objective_notes"]!).stringValue
                            self.lblAssessmentNotes.text = JSON(soap_written_by_learner["assessment_notes"]!).stringValue
                            self.lblPlanNotes.text = JSON(soap_written_by_learner["plan_notes"]!).stringValue
                        }
                        
                        if let soap_written_by_me = test["soap_written_by_me"] as? [String:Any] {
                            let subjective = JSON(soap_written_by_me["subjective"]!).stringValue
                            let objective = JSON(soap_written_by_me["objective"]!).stringValue
                            let assessment = JSON(soap_written_by_me["assessment"]!).stringValue
                            let plan = JSON(soap_written_by_me["plan"]!).stringValue
                            
                            self.lblSummary.text = subjective
                            self.lblObjective.text = objective
                            self.lblAssessment.text = assessment
                            self.lblPlan.text = plan
                        }
                        
                        
                        self.lblDiagnosis.text = diagnosis
                        
                        let doubleValue = Double(result)
                        self.progress.progress = CGFloat(doubleValue/100)
                        let doubleStr = String(format: "%.2f", doubleValue)
                        self.lblPercentage.text = "\(doubleStr)%"
                        
                        if(average_score_limit > result){
                            self.lblPerformance.text = "Needs Improvement"
                        } else{
                            self.lblPerformance.text = "Good Job"
                        }
                    }
                    
                    if let arrDidWell = Dict["asked_que"] as? NSArray {
                        for arrDidWellData in arrDidWell {
                            if let dictDidWell = arrDidWellData as? [String : Any]{
                                let didWellData = TestResultModel()
                                
                                didWellData.name = JSON(dictDidWell["name"]!).stringValue
                                //didWellData.result = JSON(dictDidWell["result"]!).stringValue
                                
                                self.arrDidWell.append(didWellData)
                            }
                        }
                    }
                    
                    if let arrImprovement = Dict["not_asked_que"] as? NSArray {
                        for arrImprovementData in arrImprovement {
                            if let dictImprovement = arrImprovementData as? [String : Any]{
                                let improvementData = TestResultModel()
                                
                                improvementData.name = JSON(dictImprovement["name"]!).stringValue
                                //improvementData.result = JSON(dictImprovement["result"]!).stringValue
                                
                                self.arrImprovement.append(improvementData)
                            }
                        }
                    }
                    
                    if let arrAccessed_body_parts = Dict["accessed_body_parts"] as? NSArray {
                        for arrAccessed_body_partsData in arrAccessed_body_parts {
                            if let dictAccessed_body_partsData = arrAccessed_body_partsData as? [String : Any]{
                                self.arrCheckedBodyParts.append(JSON(dictAccessed_body_partsData["name"]!).stringValue)
                            }
                        }
                    }
                    
                    if let arrNot_accessed_body_parts = Dict["not_accessed_body_parts"] as? NSArray {
                        for arrNot_accessed_body_partsData in arrNot_accessed_body_parts {
                            if let dictNot_accessed_body_partsData = arrNot_accessed_body_partsData as? [String : Any]{
                                self.arrUncheckedBodyParts.append(JSON(dictNot_accessed_body_partsData["name"]!).stringValue)
                            }
                        }
                    }
                    
                    if(self.arrDidWell.count > 0) {
                        self.lblDidWell.text = ""
                        self.tableview1.isHidden = false
                        self.tableview1Height.constant = CGFloat(self.arrDidWell.count) * 45
                        self.tableview1.reloadData()
                    } else{
//                        self.lblDidWell.text = "Your result isn't good. Better luck next time."
                        self.lblDidWell.text = ""
                        self.tableview1.isHidden = false
                        self.tableview1Height.constant = 50
                        self.tableview1.reloadData()
                    }
                    
                    if(self.arrImprovement.count > 0) {
                        self.lblNeedImprove.text = ""
                        self.tableview2.isHidden = false
                        self.tableview2Height.constant = CGFloat(self.arrImprovement.count) * 45
                        self.tableview2.reloadData()
                    } else{
//                        self.lblNeedImprove.text = "Great job! You did very well in every aspect. Cheers!"
                        self.lblNeedImprove.text = ""
                        self.tableview2.isHidden = false
                        self.tableview2Height.constant = 50
                        self.tableview2.reloadData()
                    }
                    
                    if(self.arrUncheckedBodyParts.count > 0) {
                        self.uncheckedBodyPartsTableview.isHidden = false
                        self.uncheckedBodyPartsTableviewHeight.constant = CGFloat(self.arrUncheckedBodyParts.count) * 45
                        self.uncheckedBodyPartsTableview.reloadData()
                    } else{
                        self.uncheckedBodyPartsTableview.isHidden = false
                        self.uncheckedBodyPartsTableviewHeight.constant = 50
                        self.uncheckedBodyPartsTableview.reloadData()
                    }
                    
                    if(self.arrCheckedBodyParts.count > 0) {
                        self.checkedBodyPartsTableview.isHidden = false
                        self.checkedBodyPartsTableviewHeight.constant = CGFloat(self.arrCheckedBodyParts.count) * 45
                        self.checkedBodyPartsTableview.reloadData()
                    } else{
                        self.checkedBodyPartsTableview.isHidden = false
                        self.checkedBodyPartsTableviewHeight.constant = 50
                        self.checkedBodyPartsTableview.reloadData()
                    }
                    
                    self.scrollView.isHidden = false
                }
            }
            
        }) {
            print("Error \(self.description)")
        }
    }
}
