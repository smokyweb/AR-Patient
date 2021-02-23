//
//  LeaderboardCell.swift
//  AR Patient
//
//  Created by Krupali on 05/05/20.
//  Copyright © 2020 Silicon. All rights reserved.
//

import UIKit

class LeaderboardCell: UITableViewCell {

    @IBOutlet weak var mainView: UIView!
    @IBOutlet weak var rankView: UIView!
    @IBOutlet weak var lblRank: IPAutoScalingLabel!
    @IBOutlet weak var imgAvatar: UIImageView!
    @IBOutlet weak var lblName: IPAutoScalingLabel!
    @IBOutlet weak var lblNursing: IPAutoScalingLabel!
    @IBOutlet weak var arrowView: UIView!
    @IBOutlet weak var lblPercentage: IPAutoScalingLabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        
        rankView.layer.cornerRadius = rankView.bounds.width/2
        mainView.layer.cornerRadius = 13
        imgAvatar.layer.cornerRadius = 10
        arrowView.roundCorners([.bottomLeft, .topRight], radius: 13)
        lblNursing.text = "Nursing Student"
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
