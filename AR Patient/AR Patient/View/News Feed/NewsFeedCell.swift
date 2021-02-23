//
//  NewsFeedCell.swift
//  AR Patient
//
//  Created by Silicon on 29/04/20.
//  Copyright © 2020 Silicon. All rights reserved.
//

import UIKit

class NewsFeedCell: UITableViewCell {

    @IBOutlet weak var imgBackground: IPView!
    @IBOutlet weak var imgNews: UIImageView!
    @IBOutlet weak var lblTitle: IPAutoScalingLabel!
    @IBOutlet weak var lblDescription: IPAutoScalingLabel!
    @IBOutlet weak var viewArrow: UIView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        
        self.viewArrow.roundCorners([.bottomLeft, .topRight], radius: 12)
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
