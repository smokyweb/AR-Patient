//
//  IPAutoScalingLabel.swift

//
//  Created by Self on 12/15/17.
//  Copyright © 2017 Self. All rights reserved.
//

import UIKit

class IPAutoScalingLabel: UILabel {
    required init(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)!
        //print("required init")
    }
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        //print("override init")
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        setUpView()
        //print("awakeFromNib")
       // print("\(self.font.pointSize)")
        autoscalingSizeOflable()
        //print("\(self.font.pointSize)")
    }
    
    func autoscalingSizeOflable (){
        self.font = self.font.withSize(((UIScreen.main.bounds.size.width) * self.font.pointSize) / 320)
    }
    
    //MARK:- FUNC SETUPVIEW.
    func setUpView() {
        if self.tag == 101 { // White Text
            self.textColor = UIColor.init(named: "Color_White")
            self.font = UIFont.init(name: Theme.Font.ExtraBold, size: Theme.Font.size.lblSize)
        } else if self.tag == 102 { // White Text
            self.textColor = UIColor.init(named: "Color_White")
            self.font = UIFont.init(name: Theme.Font.Regular, size: Theme.Font.size.TxtSize_10)
        } else if self.tag == 103 { // White Text
            self.textColor = UIColor.init(named: "Color_White")
            self.font = UIFont.init(name: Theme.Font.Medium, size: Theme.Font.size.lblSize)
        } else if self.tag == 104 { // Light Blue Text
            self.textColor = UIColor.init(named: "Color_LightBlue")
            self.font = UIFont.init(name: Theme.Font.Regular, size: Theme.Font.size.TxtSize_13)
        } else if self.tag == 105 { // Black Text
            self.textColor = UIColor.init(named: "Color_Black")
            self.font = UIFont.init(name: Theme.Font.Bold, size: Theme.Font.size.TxtSize)
        } else if self.tag == 106 { // Dark gray Text
            self.textColor = UIColor.gray
            self.font = UIFont.init(name: Theme.Font.Medium, size: Theme.Font.size.TxtSize_11)
        } else if self.tag == 107 { // Light gray Text
            self.textColor = UIColor.darkGray
            self.font = UIFont.init(name: Theme.Font.Regular, size: Theme.Font.size.TxtSize_10)
        } else if self.tag == 108 { // Dark Blue Text
            self.textColor = UIColor.init(named: "Color_LightBlue")
            self.font = UIFont.init(name: Theme.Font.Medium, size: Theme.Font.size.TxtSize_10)
        } else if self.tag == 109 { // Black Text
            self.textColor = UIColor.init(named: "Color_Black")
            self.font = UIFont.init(name: Theme.Font.Regular, size: Theme.Font.size.TxtSize_11)
        } else if self.tag == 110 { // Light Blue Text
            self.textColor = UIColor.init(named: "Color_LightBlue")
            self.font = UIFont.init(name: Theme.Font.Medium, size: Theme.Font.size.TitleSize)
        } else if self.tag == 111 { // Black Text
            self.textColor = UIColor.init(named: "Color_Black")
            self.font = UIFont.init(name: Theme.Font.Bold, size: Theme.Font.size.TxtSize_9)
        } else if self.tag == 112 { // Black Text
            self.textColor = UIColor.init(named: "Color_Black")
            self.font = UIFont.init(name: Theme.Font.Regular, size: Theme.Font.size.TxtSize_12)
        } else if self.tag == 113 { // 11306A Text
            self.textColor = UIColor.init(named: "Color_11306A")
            self.font = UIFont.init(name: Theme.Font.SemiBold, size: Theme.Font.size.TxtSize_28)
        } else if self.tag == 114 { // darkGray Text
            self.textColor = UIColor.darkGray
            self.font = UIFont.init(name: Theme.Font.Medium, size: Theme.Font.size.TxtSize_11)
        } else if self.tag == 115 { // DarkCyan Text
            self.textColor = UIColor.init(named: "Color_DarkCyan")
            self.font = UIFont.init(name: Theme.Font.Medium, size: Theme.Font.size.TxtSize_12)
        } else if self.tag == 116 { // 11306A Text
            self.textColor = UIColor.init(named: "Color_11306A")
            self.font = UIFont.init(name: Theme.Font.Medium, size: Theme.Font.size.TxtSize_14)
        } else if self.tag == 117 { // Black Text
            self.textColor = UIColor.init(named: "Color_Black")
            self.font = UIFont.init(name: Theme.Font.Medium, size: Theme.Font.size.lblSize)
        } else if self.tag == 118 { // White gray Text
            self.textColor = UIColor.init(named: "Color_White")
            self.font = UIFont.init(name: Theme.Font.Bold, size: Theme.Font.size.TxtSize_10)
        } else if self.tag == 119 { // White Text
            self.textColor = UIColor.init(named: "Color_White")
            self.font = UIFont.init(name: Theme.Font.Bold, size: Theme.Font.size.TopHeaderSize)
        } else if self.tag == 120 { // Light Gray gray Text
            self.textColor = UIColor.init(named: "Color_LightGray")
            self.font = UIFont.init(name: Theme.Font.Medium, size: Theme.Font.size.TxtSize_10)
        } else if self.tag == 121 { // Black Text
            self.textColor = UIColor.init(named: "Color_Black")
            self.font = UIFont.init(name: Theme.Font.Medium, size: Theme.Font.size.TxtSmallSize_8)
        } else if self.tag == 122 { // Blue Text
            self.textColor = UIColor.init(named: "Color_0375f3")
            self.font = UIFont.init(name: Theme.Font.Regular, size: Theme.Font.size.TxtSize_12)
        } else if self.tag == 123 { // White gray Text
            self.textColor = UIColor.init(named: "Color_White")
            self.font = UIFont.init(name: Theme.Font.Bold, size: Theme.Font.size.TxtSize_13)
        }
    }
}
