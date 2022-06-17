//
//  SubscriptionContentCell.swift
//  RealEstateProApp
//
//  Created by Knoxweb on 28/06/21.
//

import UIKit

class SubscriptionContentCell: UITableViewCell {

    @IBOutlet weak var mainView: UIView!
    @IBOutlet weak var icon: UIImageView!
    @IBOutlet weak var lblContent: IPAutoScalingLabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        
        self.mainView.layer.cornerRadius = self.mainView.bounds.height/2
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
