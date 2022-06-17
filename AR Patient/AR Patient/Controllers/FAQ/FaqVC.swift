//
//  FaqVC.swift
//  AR Patient
//
//  Created by Knoxweb on 29/04/20.
//  Copyright © 2020 Knoxweb. All rights reserved.
//

import UIKit
import SwiftyJSON

class FaqVC: UIViewController {
    
    @IBOutlet weak var viewHeader: UIView!
    @IBOutlet weak var btnBack: UIButton!
    @IBOutlet weak var tableView: UITableView!
    
    var arrData = [FaqModel]()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        viewHeader.layer.frame = CGRect(x: 0, y: 0, width: UIScreen.main.bounds.size.width, height: 60)
        viewHeader.roundCorners([.bottomLeft, .bottomRight], radius: 25)
        
        tableView.register(UINib(nibName: "FaqCell", bundle: nil), forCellReuseIdentifier: "FaqCell")
        
        tableView.dataSource = self
        tableView.delegate = self
        tableView.separatorStyle = .none
    }
    
    override func viewDidAppear(_ animated: Bool) {
        self.faqAPIcall()
    }

    @IBAction func btnBack(_ sender: UIButton) {
        self.view.endEditing(true)
        self.navigationController?.popViewController(animated: true)
    }
}

extension FaqVC: UITableViewDataSource, UITableViewDelegate {
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 71.0
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.arrData.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "FaqCell") as! FaqCell
        
        let data = self.arrData[indexPath.row]
        
        cell.lblFaq.text = data.title
        
        cell.selectionStyle = .none
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let data = self.arrData[indexPath.row]
        
        let vc = UIStoryboard.init(name: "Main", bundle: Bundle.main).instantiateViewController(withIdentifier: "WebViewController") as? WebViewController
        vc?.tag = 2
        vc?.url = data.url
        self.navigationController?.pushViewController(vc!, animated: true)
    }
}

extension FaqVC {
    func faqAPIcall(){
        var paramer: [String: Any] = [:]

        WebService.call.POST(filePath: APIConstant.Request.faqList, params: paramer, enableInteraction: false, showLoader: true, viewObj: (Global.appDelegate.window?.rootViewController!.view)!, onSuccess: { (result, success) in
            
            if let Dict = result as? [String:Any] {
                if let str = Dict["code"] as? Int , str == 0 {
                    self.arrData.removeAll()
                    
                    if let arrFaqs = Dict["faqs"] as? NSArray {
                        for arrFaqsData in arrFaqs {
                            if let dictFaqs = arrFaqsData as? [String : Any]{
                                let faqsData = FaqModel()
                                
                                faqsData.faq_id = JSON(dictFaqs["faq_id"]!).stringValue
                                faqsData.title = JSON(dictFaqs["title"]!).stringValue
                                faqsData.desc = JSON(dictFaqs["description"]!).stringValue
                                faqsData.url = JSON(dictFaqs["url"]!).stringValue
                                faqsData.image = JSON(dictFaqs["image"]!).stringValue
                                
                                self.arrData.append(faqsData)
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
