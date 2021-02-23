//
//  IPImageView.swift
//  ShowOff
//
//  Created by silicon on 05/10/19.
//

import Foundation
import UIKit

class IPImageView: UIImageView {
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        
        
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        setUpView()
    }
    override func prepareForInterfaceBuilder() {
        super.prepareForInterfaceBuilder()
        setUpView()
    }
    
    func setUpView() {
        if self.tag == 101{
            self.roundCorners([.bottomLeft, .bottomRight], radius: 25)
        } else if self.tag == 102{
            /*let yourViewBorder = CAShapeLayer()
            yourViewBorder.strokeColor = Theme.color.lightBlueBorder.cgColor
            yourViewBorder.lineDashPattern = [2, 2]
            yourViewBorder.frame = self.bounds
            yourViewBorder.fillColor = nil
            yourViewBorder.cornerRadius = 12
            yourViewBorder.masksToBounds = true
            yourViewBorder.path = UIBezierPath(rect: self.bounds).cgPath
            self.layer.addSublayer(yourViewBorder)*/
            self.layer.cornerRadius = 12
            self.layer.masksToBounds = true
            self.backgroundColor = Theme.color.lightBlueBG
        }
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        self.setUpView()
    }
}
