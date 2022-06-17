//
//  BodyPartsCell.swift
//  AR Patient
//
//  Created by Knoxweb on 01/07/21.
//  Copyright © 2021 Knoxweb. All rights reserved.
//

import UIKit

class BodyPartsCell: UITableViewCell {

    @IBOutlet weak var img: UIImageView!
    @IBOutlet weak var lblTitle: IPAutoScalingLabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
