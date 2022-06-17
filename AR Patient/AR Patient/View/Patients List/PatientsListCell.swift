//
//  PatientsListCell.swift
//  AR Patient
//
//  Created by Knoxweb on 29/04/20.
//  Copyright © 2020 Knoxweb. All rights reserved.
//

import UIKit

class PatientsListCell: UITableViewCell {

    @IBOutlet weak var mainView: UIView!
    @IBOutlet weak var imgAvatar: UIImageView!
    @IBOutlet weak var lblName: IPAutoScalingLabel!
    @IBOutlet weak var lblTitle: IPAutoScalingLabel!
    @IBOutlet weak var lblSymptoms: IPAutoScalingLabel!
    @IBOutlet weak var arrowView: UIView!
    @IBOutlet weak var lblDescription: IPAutoScalingLabel!
    @IBOutlet weak var lblVitalSign: IPAutoScalingLabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        
        mainView.layer.cornerRadius = 13
        imgAvatar.layer.cornerRadius = 10
        arrowView.roundCorners([.bottomLeft, .topRight], radius: 13)
        lblSymptoms.text = "Patient Introduction"
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
