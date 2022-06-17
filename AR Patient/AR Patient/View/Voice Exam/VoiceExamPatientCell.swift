//
//  VoiceExamPatientCell.swift
//  AR Patient
//
//  Created by Knoxweb on 04/05/20.
//  Copyright © 2020 Knoxweb. All rights reserved.
//

import UIKit

class VoiceExamPatientCell: UITableViewCell {

    @IBOutlet weak var viewBackground: UIView!
    @IBOutlet weak var imgAvatar: UIImageView!
    @IBOutlet weak var lblName: IPAutoScalingLabel!
    @IBOutlet weak var viewBubble: UIView!
    @IBOutlet weak var lblMessage: IPAutoScalingLabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
