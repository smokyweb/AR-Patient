//
//  NewsFeedVC.swift
//  AR Patient
//
//  Created by Knoxweb on 29/04/20.
//  Copyright © 2020 Knoxweb. All rights reserved.
//

import UIKit
import SwiftyJSON
import SDWebImage

class NewsFeedVC: UIViewController {

    @IBOutlet weak var viewHeader: UIView!
    @IBOutlet weak var btnBack: UIButton!
    @IBOutlet weak var tableView: UITableView!
    
    var arrData = [NewsFeedModel]()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        viewHeader.layer.frame = CGRect(x: 0, y: 0, width: UIScreen.main.bounds.size.width, height: 60)
        viewHeader.roundCorners([.bottomLeft, .bottomRight], radius: 25)
        
        tableView.register(UINib(nibName: "NewsFeedCell", bundle: nil), forCellReuseIdentifier: "NewsFeedCell")
        
        tableView.dataSource = self
        tableView.delegate = self
        tableView.separatorStyle = .none
    }
    
    override func viewDidAppear(_ animated: Bool) {
        self.newsFeedAPIcall()
    }
    
    @IBAction func btnBack(_ sender: UIButton) {
        self.view.endEditing(true)
        self.navigationController?.popViewController(animated: true)
    }
}

extension NewsFeedVC: UITableViewDataSource, UITableViewDelegate {
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 106.0
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.arrData.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "NewsFeedCell") as! NewsFeedCell
        
        let data = self.arrData[indexPath.row]
        
        if(data.image != "") {
            cell.imgNews.sd_setImage(with: URL(string: data.image))
        }
        cell.lblTitle.text = data.title
        cell.lblDescription.text = data.desc
        
        cell.selectionStyle = .none
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let data = self.arrData[indexPath.row]
        
        let vc = UIStoryboard.init(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "WebViewController") as? WebViewController
        vc?.tag = 3
        vc?.url = data.url
        self.navigationController?.pushViewController(vc!, animated: true)
    }
}

extension NewsFeedVC {
    func newsFeedAPIcall(){
        var paramer: [String: Any] = [:]

        WebService.call.POST(filePath: APIConstant.Request.newsList, params: paramer, enableInteraction: false, showLoader: true, viewObj: (Global.appDelegate.window?.rootViewController!.view)!, onSuccess: { (result, success) in
            
            if let Dict = result as? [String:Any] {
                if let str = Dict["code"] as? Int , str == 0 {
                    self.arrData.removeAll()
                    
                    if let arrNewsFeeds = Dict["newsfeeds"] as? NSArray {
                        for arrNewsData in arrNewsFeeds {
                            if let dictNews = arrNewsData as? [String : Any]{
                                let newsData = NewsFeedModel()
                                
                                newsData.news_id = JSON(dictNews["news_id"]!).stringValue
                                newsData.title = JSON(dictNews["title"]!).stringValue
                                newsData.desc = JSON(dictNews["description"]!).stringValue
                                newsData.url = JSON(dictNews["url"]!).stringValue
                                newsData.image = JSON(dictNews["image"]!).stringValue
                                
                                self.arrData.append(newsData)
                            }
                        }
                        
                        self.tableView.reloadData()
                    }
                }
            }

        }) {
            print("Error \(self.description)")
        }
    }
}
