//
//  SymptomsCell.swift
//  AR Patient
//
//  Created by silicon on 20/10/20.
//  Copyright © 2020 Silicon. All rights reserved.
//

import UIKit

class SymptomsCell: UITableViewCell {

    @IBOutlet weak var lblSymptoms: IPAutoScalingLabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
