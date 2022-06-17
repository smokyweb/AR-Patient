//
//  MyProfileCell.swift
//  AR Patient
//
//  Created by Knoxweb on 06/05/20.
//  Copyright © 2020 Knoxweb. All rights reserved.
//

import UIKit
import SFProgressCircle

class MyProfileCell: UITableViewCell {
    
    @IBOutlet weak var mainView: UIView!
    @IBOutlet weak var imgAvatar: UIImageView!
    @IBOutlet weak var lblName: IPAutoScalingLabel!
    @IBOutlet weak var lblType: IPAutoScalingLabel!
    @IBOutlet weak var arrowView: UIView!
    @IBOutlet weak var bgProgress: SFCircleGradientView!
    
    @IBOutlet weak var lblPercentage: IPAutoScalingLabel!
    @IBOutlet weak var progress: SFCircleGradientView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        mainView.layer.cornerRadius = 13
        imgAvatar.layer.cornerRadius = 10
        arrowView.roundCorners([.bottomLeft, .topRight], radius: 13)
        
        bgProgress.progress = 1
        bgProgress.lineWidth = 3
        bgProgress.startColor = UIColor(named: "Color_LightGray")
        bgProgress.endColor = UIColor(named: "Color_LightGray")
        bgProgress.startAngle = -1.6
        bgProgress.endAngle = 4.7
        
//        progress.progress = 0.85
        progress.lineWidth = 3
        progress.startColor = UIColor(named: "Color_Start")
        progress.endColor = UIColor(named: "Color_End")
        progress.startAngle = -1.5
        progress.endAngle = 4.7
        progress.roundedCorners = true
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
