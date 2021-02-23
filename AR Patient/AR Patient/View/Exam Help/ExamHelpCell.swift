//
//  ExamHelpCell.swift
//  AR Patient
//
//  Created by Krupali on 02/05/20.
//  Copyright © 2020 Silicon. All rights reserved.
//

import UIKit

class ExamHelpCell: UITableViewCell {

    @IBOutlet weak var mainView: UIView!
    @IBOutlet weak var arrowView: UIView!
    @IBOutlet weak var lblQuestion: IPAutoScalingLabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        
        mainView.layer.cornerRadius = 13
        arrowView.roundCorners([.bottomLeft, .topRight], radius: 13)
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
