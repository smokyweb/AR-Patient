//
//  SOAPNotesVC.swift
//  AR Patient
//
//  Created by Knoxweb on 30/06/21.
//  Copyright © 2021 Knoxweb. All rights reserved.
//

import UIKit

class SOAPNotesVC: UIViewController {

    @IBOutlet weak var viewHeader: UIView!
    @IBOutlet weak var btnBack: UIButton!
    @IBOutlet weak var lblSubjectiveNotes: IPAutoScalingLabel!
    @IBOutlet weak var lblObjectiveNotes: IPAutoScalingLabel!
    @IBOutlet weak var lblAssessmentNotes: IPAutoScalingLabel!
    @IBOutlet weak var lblPlanNotes: IPAutoScalingLabel!
    
    var subjective_notes = ""
    var objective_notes = ""
    var assessment_notes = ""
    var plan_notes = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        self.tabBarController?.tabBar.isHidden = true
        
        self.lblSubjectiveNotes.text = self.subjective_notes
        self.lblObjectiveNotes.text = self.objective_notes
        self.lblAssessmentNotes.text = self.assessment_notes
        self.lblPlanNotes.text = self.plan_notes
        
        self.viewHeader.layer.frame = CGRect(x: 0, y: 0, width: UIScreen.main.bounds.size.width, height: 60)
        self.viewHeader.roundCorners([.bottomLeft, .bottomRight], radius: 25)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.tabBarController?.tabBar.isHidden = true
    }

    @IBAction func btnBack(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
