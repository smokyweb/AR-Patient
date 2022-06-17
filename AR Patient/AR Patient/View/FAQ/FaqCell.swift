//
//  FaqCell.swift
//  AR Patient
//
//  Created by Knoxweb on 29/04/20.
//  Copyright © 2020 Knoxweb. All rights reserved.
//

import UIKit

class FaqCell: UITableViewCell {

    @IBOutlet weak var viewBackground: IPView!
    @IBOutlet weak var lblFaq: IPAutoScalingLabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
