//
//  TestResultsVC.swift
//  AR Patient
//
//  Created by Krupali on 04/05/20.
//  Copyright © 2020 Silicon. All rights reserved.
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
    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var btnBack: UIButton!
    
    @IBOutlet weak var tableview1: UITableView!
    @IBOutlet weak var tableview2: UITableView!
    
    @IBOutlet weak var tableview1Height: NSLayoutConstraint!
    @IBOutlet weak var tableview2Height: NSLayoutConstraint!
    
    @IBOutlet weak var lblDidWell: IPAutoScalingLabel!
    @IBOutlet weak var lblNeedImprove: IPAutoScalingLabel!
    
    var test_id : String = ""
    var arrDidWell = [TestResultModel]()
    var arrImprovement = [TestResultModel]()
    var isFromExam : Bool = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.tabBarController?.tabBar.isHidden = true
        
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
        
        tableview1.reloadData()
        tableview2.reloadData()
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
        
        self.testResultAPI()
        tableview1.reloadData()
        tableview2.reloadData()
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        self.tabBarController?.tabBar.isHidden = false
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
}

extension TestResultsVC: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 45
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if(tableView == tableview1){
            return self.arrDidWell.count
        } else {
            return self.arrImprovement.count
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        var cell = tableView.dequeueReusableCell(withIdentifier: "TestResultsCell") as! TestResultsCell
        
        if(tableView == tableview1){
            
            let data1 = self.arrDidWell[indexPath.row]
            
            cell.circleView.backgroundColor = UIColor.green
            cell.lblExample.text = "\(data1.name)"
        } else if(tableView == tableview2){
            let data2 = self.arrImprovement[indexPath.row]
            
            cell.circleView.backgroundColor = UIColor.systemOrange
            cell.lblExample.text = "\(data2.name)"
        }
        cell.selectionStyle = .none
        return cell
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
                    
                    let average_score_limit = JSON(Dict["average_score_limit"]!).stringValue
                    
                    if let patient = Dict["patient"] as? [String:Any] {
                        let name = JSON(patient["name"]!).stringValue
                        let type = JSON(patient["type"]!).stringValue
                        let symptoms = JSON(patient["symptoms"]!).stringValue
                    }
                    
                    if let test = Dict["test"] as? [String:Any] {
                        let diagnosis = JSON(test["diagnosis"]!).stringValue
                        let notes = JSON(test["notes"]!).stringValue
                        let result = JSON(test["result"]!).stringValue
                        
                        self.lblDiagnosis.text = diagnosis
                        self.lblSummary.text = notes
                        let doubleValue = Double(result)
                        self.progress.progress = CGFloat(doubleValue!/100)
                        let doubleStr = String(format: "%.2f", doubleValue!)
                        self.lblPercentage.text = "\(doubleStr)%"
                        
                        if(average_score_limit > result){
                            self.lblPerformance.text = "Needs Improvement"
                        } else{
                            self.lblPerformance.text = "Good Job"
                        }
                    }
                    
                    if let arrDidWell = Dict["did_well"] as? NSArray {
                        for arrDidWellData in arrDidWell {
                            if let dictDidWell = arrDidWellData as? [String : Any]{
                                let didWellData = TestResultModel()
                                
                                didWellData.name = JSON(dictDidWell["name"]!).stringValue
                                didWellData.result = JSON(dictDidWell["result"]!).stringValue
                                
                                self.arrDidWell.append(didWellData)
                            }
                        }
                    }
                    
                    if let arrImprovement = Dict["need_improvement"] as? NSArray {
                        for arrImprovementData in arrImprovement {
                            if let dictImprovement = arrImprovementData as? [String : Any]{
                                let improvementData = TestResultModel()
                                
                                improvementData.name = JSON(dictImprovement["name"]!).stringValue
                                improvementData.result = JSON(dictImprovement["result"]!).stringValue
                                
                                self.arrImprovement.append(improvementData)
                            }
                        }
                    }
                    
                    if(self.arrDidWell.count > 0) {
                        self.lblDidWell.text = ""
                        self.tableview1.isHidden = false
                        self.tableview1Height.constant = CGFloat(self.arrDidWell.count) * 45
                        self.tableview1.reloadData()
                    } else{
                        self.lblDidWell.text = "Your result isn't good. Better luck next time."
                        self.tableview1.isHidden = true
                        self.tableview1.reloadData()
                    }
                    
                    if(self.arrImprovement.count > 0) {
                        self.lblNeedImprove.text = ""
                        self.tableview2.isHidden = false
                        self.tableview2Height.constant = CGFloat(self.arrImprovement.count) * 45
                        self.tableview2.reloadData()
                    } else{
                        self.lblNeedImprove.text = "Great job! You did very well in every aspect. Cheers!"
                        self.tableview2.isHidden = true
                        self.tableview2.reloadData()
                    }
                    
                    self.scrollView.isHidden = false
                }
            }
            
        }) {
            print("Error \(self.description)")
        }
    }
}
