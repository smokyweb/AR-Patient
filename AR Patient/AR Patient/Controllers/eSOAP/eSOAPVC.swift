//
//  eSOAPVC.swift
//  AR Patient
//
//  Created by Knoxweb on 20/10/20.
//  Copyright © 2020 Knoxweb. All rights reserved.
//

import UIKit

class eSOAPVC: UIViewController {
    
    @IBOutlet weak var lblBasicInfo: IPTextField!
    @IBOutlet weak var lblROS: IPTextField!
    @IBOutlet weak var lblPmhx: IPTextField!
    @IBOutlet weak var lblSurghx: IPTextField!
    @IBOutlet weak var lblMeds: IPTextField!
    @IBOutlet weak var lblALL: IPTextField!
    @IBOutlet weak var lblFamHx: IPTextField!
    @IBOutlet weak var lblSocHx: IPTextField!
    
    @IBOutlet weak var lblVitals: IPTextField!
    @IBOutlet weak var lblBMI: IPTextField!
    @IBOutlet weak var lblBP: IPTextField!
    @IBOutlet weak var lblTemp: IPTextField!
    @IBOutlet weak var lblHR: IPTextField!
    @IBOutlet weak var lblGeneral: IPTextField!
    @IBOutlet weak var lblHeart: IPTextField!
    @IBOutlet weak var lblLung: IPTextField!
    @IBOutlet weak var lblTspine: IPTextField!
    @IBOutlet weak var lblChestWall: IPTextField!
    
    @IBOutlet weak var tableViewA: UITableView!
    
    @IBOutlet weak var tableViewB: UITableView!
    
    @IBOutlet weak var viewHeader: UIView!
    @IBOutlet weak var btnBack: UIButton!
    @IBOutlet weak var btnSubmit: IPButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        self.viewHeader.layer.frame = CGRect(x: 0, y: 0, width: UIScreen.main.bounds.size.width, height: 60)
        self.viewHeader.roundCorners([.bottomLeft, .bottomRight], radius: 25)
            
        self.lblBasicInfo.withoutImage(direction: .Left, colorSeparator: UIColor.clear, colorBorder: UIColor.clear)
        self.lblROS.withoutImage(direction: .Left, colorSeparator: UIColor.clear, colorBorder: UIColor.clear)
        self.lblPmhx.withoutImage(direction: .Left, colorSeparator: UIColor.clear, colorBorder: UIColor.clear)
        self.lblSurghx.withoutImage(direction: .Left, colorSeparator: UIColor.clear, colorBorder: UIColor.clear)
        self.lblMeds.withoutImage(direction: .Left, colorSeparator: UIColor.clear, colorBorder: UIColor.clear)
        self.lblALL.withoutImage(direction: .Left, colorSeparator: UIColor.clear, colorBorder: UIColor.clear)
        self.lblFamHx.withoutImage(direction: .Left, colorSeparator: UIColor.clear, colorBorder: UIColor.clear)
        self.lblSocHx.withoutImage(direction: .Left, colorSeparator: UIColor.clear, colorBorder: UIColor.clear)
        
        self.lblVitals.withoutImage(direction: .Left, colorSeparator: UIColor.clear, colorBorder: UIColor.clear)
        self.lblBMI.withoutImage(direction: .Left, colorSeparator: UIColor.clear, colorBorder: UIColor.clear)
        self.lblBP.withoutImage(direction: .Left, colorSeparator: UIColor.clear, colorBorder: UIColor.clear)
        self.lblTemp.withoutImage(direction: .Left, colorSeparator: UIColor.clear, colorBorder: UIColor.clear)
        self.lblHR.withoutImage(direction: .Left, colorSeparator: UIColor.clear, colorBorder: UIColor.clear)
        self.lblGeneral.withoutImage(direction: .Left, colorSeparator: UIColor.clear, colorBorder: UIColor.clear)
        self.lblHeart.withoutImage(direction: .Left, colorSeparator: UIColor.clear, colorBorder: UIColor.clear)
        self.lblLung.withoutImage(direction: .Left, colorSeparator: UIColor.clear, colorBorder: UIColor.clear)
        self.lblTspine.withoutImage(direction: .Left, colorSeparator: UIColor.clear, colorBorder: UIColor.clear)
        self.lblChestWall.withoutImage(direction: .Left, colorSeparator: UIColor.clear, colorBorder: UIColor.clear)
    }

    @IBAction func btnBack(_ sender: UIButton) {
    }
    
    @IBAction func btnSubmit(_ sender: IPButton) {
    }
}
