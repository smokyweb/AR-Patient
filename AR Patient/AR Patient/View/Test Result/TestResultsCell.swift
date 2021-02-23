//
//  TestResultsCell.swift
//  AR Patient
//
//  Created by Krupali on 06/05/20.
//  Copyright © 2020 Silicon. All rights reserved.
//

import UIKit

class TestResultsCell: UITableViewCell {

    @IBOutlet weak var circleView: UIView!
    @IBOutlet weak var lblExample: IPAutoScalingLabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        
        self.circleView.layer.cornerRadius = self.circleView.bounds.height/2
        self.circleView.layer.cornerRadius = self.circleView.bounds.width/2
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
