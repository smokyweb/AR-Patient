//
//  MyProfileVC.swift
//  AR Patient
//
//  Created by Krupali on 06/05/20.
//  Copyright © 2020 Silicon. All rights reserved.
//

import UIKit
import SwiftyJSON

class MyProfileVC: UIViewController {

    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var imgAvatar: UIImageView!
    @IBOutlet weak var mainView: UIView!
    @IBOutlet weak var btnEdit: UIButton!
    @IBOutlet weak var btnBack: UIButton!
    @IBOutlet weak var lblTitle: IPAutoScalingLabel!
    
    @IBOutlet weak var lblName: IPAutoScalingLabel!
    @IBOutlet weak var lblNursing: IPAutoScalingLabel!
    @IBOutlet weak var lblRecentExams: IPAutoScalingLabel!
    @IBOutlet weak var capsule1View: UIView!
    @IBOutlet weak var capsule2View: UIView!
    
    @IBOutlet weak var lblPercentage: IPAutoScalingLabel!
    @IBOutlet weak var lblPerformance: IPAutoScalingLabel!
    @IBOutlet weak var tableview: UITableView!
    @IBOutlet weak var lblRank: IPAutoScalingLabel!
    @IBOutlet weak var lblRankTitle: IPAutoScalingLabel!
    @IBOutlet weak var tableViewHeight: NSLayoutConstraint!
    
    @IBOutlet weak var lblNoResults: IPAutoScalingLabel!
    var arrData = [MyProfileModel]()
    var isFromTab : Bool = false
    var user_id : String = Global.kretriveUserData().User_Id!
    var isHaveMoreData : Bool = true
    var paginationCnt = 1
    
    override func viewDidLoad() {
        super.viewDidLoad()
        mainView.roundCorners([.topLeft, .topRight], radius: 25)
        imgAvatar.layer.cornerRadius = 25
        capsule1View.layer.cornerRadius = 35
        capsule2View.layer.cornerRadius = 35
        
        tableview.register(UINib(nibName: "MyProfileCell", bundle: nil), forCellReuseIdentifier: "MyProfileCell")
        tableview.delegate = self
        tableview.dataSource = self
        tableview.separatorStyle = .none
        
        if(self.user_id == Global.kretriveUserData().User_Id!) {
            self.btnEdit.isHidden = false
            lblTitle.text = "KeylblProfile".localize
        } else {
            self.btnEdit.isHidden = true
            lblTitle.text = "Profile"
        }
        
        if(isFromTab) {
            self.btnBack.isHidden = true
        } else {
            self.btnBack.isHidden = false
        }

    }
    
    override func viewDidAppear(_ animated: Bool) {
        lblTitle.text = "KeylblProfile".localize
        mainView.roundCorners([.topLeft, .topRight], radius: 25)
        imgAvatar.layer.cornerRadius = 25
        capsule1View.layer.cornerRadius = 35
        capsule2View.layer.cornerRadius = 35
        
        tableview.register(UINib(nibName: "MyProfileCell", bundle: nil), forCellReuseIdentifier: "MyProfileCell")
        tableview.delegate = self
        tableview.dataSource = self
        tableview.separatorStyle = .none
        
        if(self.user_id == Global.kretriveUserData().User_Id!) {
            self.btnEdit.isHidden = false
            lblTitle.text = "KeylblProfile".localize
        } else {
            self.btnEdit.isHidden = true
            lblTitle.text = "Profile"
        }
        
        if(isFromTab) {
            self.btnBack.isHidden = true
        } else {
            self.btnBack.isHidden = false
        }
        
        self.arrData.removeAll()
        APICall()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.paginationCnt = 1
        self.scrollView.isHidden = true
    }
    
    override func viewWillLayoutSubviews() {
        mainView.roundCorners([.topLeft, .topRight], radius: 25)
        imgAvatar.layer.cornerRadius = 25
        capsule1View.layer.cornerRadius = 35
        capsule2View.layer.cornerRadius = 35
    }
    
    // MARK: - 
    // MARK: - BUTTON ACTION METHODS
    @IBAction func btnEdit(_ sender: Any) {
        let vc = UIStoryboard.init(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "EditProfileVC") as? EditProfileVC
        self.navigationController?.pushViewController(vc!, animated: true)
    }
    
    @IBAction func btnBack(_ sender: Any) {
        self.view.endEditing(true)
        self.navigationController?.popViewController(animated: true)
    }
    
    // MARK: - 
    // MARK: - API CALLER METHODS
    func APICall() {
        var paramer: [String: Any] = [:]
        
        if(isFromTab) {
            paramer["user_id"] = Global.kretriveUserData().User_Id!
        } else {
            paramer["user_id"] = self.user_id
        }
        paramer["page_no"] = "\(self.paginationCnt)"
        
        WebService.call.POST(filePath: APIConstant.Request.myProfile, params: paramer, enableInteraction: false, showLoader: true, viewObj: self.view, onSuccess: { (result, success) in
            if let Dict = result as? [String:Any] {
                
                if let str = Dict["code"] as? Int , str == 0{
                    
                    self.imgAvatar.sd_setImage(with: URL(string: APIConstant.mediaURL + JSON(Dict["avatar"]!).stringValue), placeholderImage: UIImage(named: "Avatar"), options: .refreshCached)
                    self.lblName.text = JSON(Dict["name"]!).stringValue
                    let result = JSON(Dict["result"]!).stringValue
                    let doubleValue = Double(result)
                    let doubleStr = String(format: "%.2f", doubleValue!)
                    self.lblPercentage.text = "\(doubleStr)%"
                    self.lblPerformance.text = "Overall Rating"
                    self.lblRank.text = JSON(Dict["rank"]!).stringValue
                    self.lblRankTitle.text = "Rank"
                    
                    if let arrMyProfile = Dict["tests"] as? NSArray {
                        if(arrMyProfile.count == 50) {
                            self.isHaveMoreData = true
                            self.paginationCnt = self.paginationCnt + 1
                        } else {
                            self.isHaveMoreData = false
                        }
                        
                        for arrMyProfileData in arrMyProfile {
                            if let dictMyProfile = arrMyProfileData as? [String : Any]{
                                let MyProfileData = MyProfileModel()
                                
                                MyProfileData.patient_avatar = JSON(dictMyProfile["patient_avatar"]!).stringValue
                                MyProfileData.patient_name = JSON(dictMyProfile["patient_name"]!).stringValue
                                MyProfileData.patient_type = JSON(dictMyProfile["patient_type"]!).stringValue
                                MyProfileData.result = JSON(dictMyProfile["result"]!).stringValue
                                MyProfileData.test_id = JSON(dictMyProfile["test_id"]!).stringValue
                                
                                self.arrData.append(MyProfileData)
                            }
                        }
                        if(self.arrData.count > 0) {
                            self.tableview.reloadData()
                        } else{
                            self.lblNoResults.text = "Sorry, you don't have any recent exams."
                            self.tableview.isHidden = true
                            self.tableview.reloadData()
                        }
                        self.tableview.reloadData()
                        self.scrollView.isHidden = false
                    }
                    
                    self.mainView.isHidden = false
                    self.imgAvatar.isHidden = false
                    
                    self.tableViewHeight.constant = CGFloat(100 * self.arrData.count)
                    
                } else{
                    AJAlertController.initialization().showAlertWithOkButton(aStrMessage: Dict["message"] as? String ?? "") { (index, title) in
                        
                    }
                }
            }
            
        }) {
            print("Error \(self.description)")
        }
    }
}

extension MyProfileVC: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.arrData.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        var cell = tableView.dequeueReusableCell(withIdentifier: "MyProfileCell") as! MyProfileCell
        
        let data = self.arrData[indexPath.row]
        
        if(data.patient_avatar != "") {
            cell.imgAvatar.sd_setImage(with: URL(string: APIConstant.mediaURL + data.patient_avatar))
        }
        cell.lblName.text = data.patient_name
        cell.lblType.text = data.patient_type
        let doubleValue = Double(data.result)
        cell.progress.progress = CGFloat(doubleValue!/100)
        let doubleStr = String(format: "%.2f", doubleValue!)
        cell.lblPercentage.text = "\(doubleStr)%"
        cell.selectionStyle = .none
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 100
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let data = self.arrData[indexPath.row]
        
        let vc = UIStoryboard.init(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "TestResultsVC") as? TestResultsVC
        vc?.test_id = data.test_id
        self.navigationController?.pushViewController(vc!, animated: true)
    }
    
    func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {
        let lastElement = self.arrData.count - 1
        if indexPath.row == lastElement {
            if(self.isHaveMoreData) {
                self.APICall()
            }
        }
    }
}
