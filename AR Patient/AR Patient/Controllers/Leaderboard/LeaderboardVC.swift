//
//  LeaderboardVC.swift
//  AR Patient
//
//  Created by Krupali on 05/05/20.
//  Copyright © 2020 Silicon. All rights reserved.
//

import UIKit
import SwiftyJSON

class LeaderboardVC: UIViewController {

    @IBOutlet weak var headerView: UIView!
    @IBOutlet weak var lblTitle: IPAutoScalingLabel!
    @IBOutlet weak var tableview: UITableView!
    
    var arrData = [LeaderboardModel]()
    var isHaveMoreData : Bool = true
    var paginationCnt = 1
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.lblTitle.text = "KeylblLeaderboard".localize
        
        headerView.layer.frame = CGRect(x: 0, y: 0, width: UIScreen.main.bounds.size.width, height: 60)
        headerView.roundCorners([.bottomLeft, .bottomRight], radius: 25)
        
        tableview.register(UINib(nibName: "LeaderboardCell", bundle: nil), forCellReuseIdentifier: "LeaderboardCell")
        tableview.delegate = self
        tableview.dataSource = self
        tableview.separatorStyle = .none

    }
    
    override func viewDidAppear(_ animated: Bool) {
        self.lblTitle.text = "KeylblLeaderboard".localize
        
        headerView.layer.frame = CGRect(x: 0, y: 0, width: UIScreen.main.bounds.size.width, height: 60)
        headerView.roundCorners([.bottomLeft, .bottomRight], radius: 25)
        
        tableview.register(UINib(nibName: "LeaderboardCell", bundle: nil), forCellReuseIdentifier: "LeaderboardCell")
        tableview.delegate = self
        tableview.dataSource = self
        tableview.separatorStyle = .none
        
        self.arrData.removeAll()
        APICall()
    }
    
    // MARK: - 
    // MARK: - API CALLER METHODS
    func APICall() {
        var paramer: [String: Any] = [:]
        
        paramer["user_id"] = Global.kretriveUserData().User_Id!
        paramer["page_no"] = "\(self.paginationCnt)"
        
        WebService.call.POST(filePath: APIConstant.Request.leaderboard, params: paramer, enableInteraction: false, showLoader: true, viewObj: self.view, onSuccess: { (result, success) in
            if let Dict = result as? [String:Any] {
                
                if let str = Dict["code"] as? Int , str == 0{
                    
                    if let arrLeaderboard = Dict["students"] as? NSArray {
                        if(arrLeaderboard.count == 50) {
                            self.isHaveMoreData = true
                            self.paginationCnt = self.paginationCnt + 1
                        } else {
                            self.isHaveMoreData = false
                        }
                        
                        for arrLeaderboardData in arrLeaderboard {
                            if let dictLeaderboard = arrLeaderboardData as? [String : Any]{
                                let LeaderboardData = LeaderboardModel()
                                
                                LeaderboardData.avatar = JSON(dictLeaderboard["avatar"]!).stringValue
                                LeaderboardData.name = JSON(dictLeaderboard["name"]!).stringValue
                                LeaderboardData.rank = JSON(dictLeaderboard["rank"]!).stringValue
                                LeaderboardData.result = JSON(dictLeaderboard["result"]!).stringValue
                                LeaderboardData.user_id = JSON(dictLeaderboard["user_id"]!).stringValue
                                
                                self.arrData.append(LeaderboardData)
                            }
                        }
                        self.tableview.reloadData()
                    }
                    
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

extension LeaderboardVC: UITableViewDelegate, UITableViewDataSource{
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 100
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.arrData.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        var cell = tableView.dequeueReusableCell(withIdentifier: "LeaderboardCell") as! LeaderboardCell
        
        let data = self.arrData[indexPath.row]
        
        if(data.avatar != "") {
            cell.imgAvatar.sd_setImage(with: URL(string: APIConstant.mediaURL + data.avatar), placeholderImage: UIImage(named: "Avatar"))
        }
        cell.lblName.text = data.name
        cell.lblRank.text = data.rank
        cell.lblNursing.text = "Nursing Student"
        let doubleValue = Double(data.result)
        let doubleStr = String(format: "%.2f", doubleValue!)
        cell.lblPercentage.text = "\(doubleStr)%"

        cell.selectionStyle = .none
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let data = self.arrData[indexPath.row]
        
        let vc = UIStoryboard.init(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "MyProfileVC") as? MyProfileVC
        vc?.user_id = data.user_id
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


