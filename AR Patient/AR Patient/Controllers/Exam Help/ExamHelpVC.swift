//
//  ExamHelpVC.swift
//  AR Patient
//
//  Created by Knoxweb on 02/05/20.
//  Copyright © 2020 Knoxweb. All rights reserved.
//

import UIKit

class ExamHelpVC: UIViewController {

    @IBOutlet weak var viewHeader: UIView!
    @IBOutlet weak var lblTitle: IPAutoScalingLabel!
    @IBOutlet weak var tableview: UITableView!
    
    var arrData = [VoiceExamModel]()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        tableview.register(UINib(nibName: "ExamHelpCell", bundle: nil), forCellReuseIdentifier: "ExamHelpCell")
        tableview.delegate = self
        tableview.dataSource = self
        tableview.separatorStyle = .none
        
        viewHeader.layer.frame = CGRect(x: 0, y: 0, width: UIScreen.main.bounds.size.width, height: 60)
        viewHeader.roundCorners([.bottomLeft, .bottomRight], radius: 25)
        
        setLanguageText()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        self.navigationController?.navigationBar.isHidden = true
        viewHeader.layer.frame = CGRect(x: 0, y: 0, width: UIScreen.main.bounds.size.width, height: 60)
        viewHeader.roundCorners([.bottomLeft, .bottomRight], radius: 25)
        
        setLanguageText()
    }

    // MARK: - 
    // MARK: - BUTTON ACTION METHODS
    @IBAction func btnBack(_ sender: Any) {
        self.view.endEditing(true)
        self.navigationController?.popViewController(animated: true)
    }
    
    //MARK:- CUSTOM METHODS
    func setLanguageText(){
        lblTitle.text = "KeylblExamHelp".localize
        
        viewHeader.layer.frame = CGRect(x: 0, y: 0, width: UIScreen.main.bounds.size.width, height: 60)
        viewHeader.roundCorners([.bottomLeft, .bottomRight], radius: 25)
    }
}

extension ExamHelpVC: UITableViewDataSource, UITableViewDelegate{
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 80
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.arrData.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "ExamHelpCell") as! ExamHelpCell
        
        let data = self.arrData[indexPath.row]
        
        cell.lblQuestion.text = data.question
        if(data.is_mandatory == "1") {
            cell.imgStar.isHidden = false
        } else {
            cell.imgStar.isHidden = true
        }
        
        cell.selectionStyle = .none
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        //let data = self.arrData[indexPath.row]
        
        //AJAlertController.initialization().showAlertWithOkButton(aStrMessage: "\(data.answer)") { (index, title) in }
    }
}
