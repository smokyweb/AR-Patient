//
//  TCCutomButton.swift

//
//  Created by SAS 
//  Copyright © 2018 MAC7. All rights reserved.
//

import Foundation
import UIKit
/*import DTGradientButton

class TCCutomButton: UIButton {

    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        
        self.layer.borderColor = UIColor.clear.cgColor
        self.titleLabel?.textColor = Global.kAppColor.WhiteColor
        
        self.titleLabel?.font = UIFont.init(name: Global.kFont.Roboto_Medium, size: Global.kFontSize.ButtonSize)
        self.setNeedsDisplay()
        
        if self.tag == 101{
            self.setGradientBackgroundColors([UIColor.hexString("FFA70B"), UIColor.hexString("FFC50A")], direction: .toBottom, for: .normal)
            self.setGradientBackgroundColors([UIColor.hexString("FFC50A"), UIColor.hexString("FFA70B")], direction: .toBottom, for: .highlighted)
            
            self.layer.cornerRadius = 0.0
            self.layer.masksToBounds = true
        }
        else if self.tag == 102{
            self.setGradientBackgroundColors([UIColor.hexString("FFA70B"), UIColor.hexString("FFC50A")], direction: .toBottom, for: .normal)
            self.setGradientBackgroundColors([UIColor.hexString("FFC50A"), UIColor.hexString("FFA70B")], direction: .toBottom, for: .highlighted)
            
            self.layer.cornerRadius = 5.0
            self.layer.masksToBounds = true
        }
        else if self.tag == 103{
            self.setGradientBackgroundColors([UIColor.hexString("374147"), UIColor.hexString("374147")], direction: .toBottom, for: .normal)
            self.setGradientBackgroundColors([UIColor.hexString("374147"), UIColor.hexString("374147")], direction: .toBottom, for: .highlighted)
            
            self.layer.cornerRadius = 5.0
            self.layer.masksToBounds = true
        }
        else if self.tag == 104{
            self.setGradientBackgroundColors([UIColor.hexString("374147"), UIColor.hexString("374147")], direction: .toBottom, for: .normal)
            self.setGradientBackgroundColors([UIColor.hexString("374147"), UIColor.hexString("374147")], direction: .toBottom, for: .highlighted)
            
            self.layer.cornerRadius = 0.0
            self.layer.masksToBounds = true
        }
    }
}

public extension UIButton {
    public func setGradientBackgroundColors(_ colors: [UIColor], direction: DTImageGradientDirection, for state: UIControlState) {
        if colors.count > 1 {
            // Gradient background
            setBackgroundImage(UIImage(size: CGSize(width: 1, height: 1), direction: direction, colors: colors), for: state)
        }
        else {
            if let color = colors.first {
                // Mono color background
                setBackgroundImage(UIImage(color: color, size: CGSize(width: 1, height: 1)), for: state)
            }
            else {
                // Default background color
                setBackgroundImage(nil, for: state)
            }
        }
    }
}
*/
