//
//  SRCutomTextField.swift


//  Created by SAS

//  Copyright © 2018 MAC7. All rights reserved.
//

import Foundation
import UIKit

class IPTextField: UITextField {
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        
        self.font = UIFont.init(name: Theme.Font.Regular, size: Theme.Font.size.TxtSize)
        self.leftView?.frame = CGRect(x: 30, y: 0, width: 35, height:35)
        self.leftViewMode = .always
        self.leftView?.contentMode = .scaleAspectFit
        self.layer.masksToBounds = false
        
        if self.tag == 101{ //bg dark blue and text white
            self.layer.cornerRadius = self.frame.height/2
            self.layer.masksToBounds = true
            self.textColor = UIColor.init(named: "Color_White")!
            self.placeHolderColor = UIColor.init(named: "Color_White")!
            self.backgroundColor = UIColor.init(named: "Color_DarkBlue")!
            self.font = UIFont.init(name: Theme.Font.Regular, size: Theme.Font.size.lblSize)
        } else if self.tag == 102{  //bg white and text black
            self.layer.cornerRadius = 12
            self.layer.masksToBounds = true
            self.textColor = UIColor.init(named: "Color_Black")!
            self.placeHolderColor = UIColor.init(named: "Color_Black")!
            self.backgroundColor = UIColor.init(named: "Color_White")!
            self.font = UIFont.init(name: Theme.Font.Regular, size: Theme.Font.size.lblSize)
        } else if self.tag == 103{  //bg white and text black
            self.textColor = UIColor.init(named: "Color_Black")!
            self.placeHolderColor = UIColor.lightGray
            self.backgroundColor = UIColor.init(named: "Color_White")!
            self.font = UIFont.init(name: Theme.Font.Regular, size: Theme.Font.size.lblSize)
        } else if self.tag == 104{  //bg white and text gray
            self.layer.cornerRadius = self.frame.height/2
            self.layer.masksToBounds = true
            self.textColor = UIColor.init(named: "Color_Black")!
            self.placeHolderColor = UIColor.lightGray
            self.backgroundColor = UIColor.init(named: "Color_White")!
            self.font = UIFont.init(name: Theme.Font.Regular, size: Theme.Font.size.lblSize)
        } else if self.tag == 105{ //bg light blue and text white
            self.layer.cornerRadius = self.frame.height/2
            self.layer.masksToBounds = true
            self.textColor = UIColor.init(named: "Color_White")!
            self.placeHolderColor = UIColor.init(named: "Color_White")!
            self.backgroundColor = UIColor.init(named: "Color_11306A")!
            self.font = UIFont.init(name: Theme.Font.Regular, size: Theme.Font.size.lblSize)
        }
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
        
        if self.tag == 101{ //bg dark blue and text white
            self.layer.cornerRadius = self.frame.height/2
            self.layer.masksToBounds = true
            self.textColor = UIColor.init(named: "Color_White")!
            self.placeHolderColor = UIColor.init(named: "Color_White")!
            self.backgroundColor = UIColor.init(named: "Color_DarkBlue")!
            self.font = UIFont.init(name: Theme.Font.Regular, size: Theme.Font.size.lblSize)
        } else if self.tag == 102{  //bg white and text black
            self.layer.cornerRadius = 12
            self.layer.masksToBounds = true
            self.textColor = UIColor.init(named: "Color_Black")!
            self.placeHolderColor = UIColor.init(named: "Color_Black")!
            self.backgroundColor = UIColor.init(named: "Color_White")!
            self.font = UIFont.init(name: Theme.Font.Regular, size: Theme.Font.size.lblSize)
        } else if self.tag == 103{  //bg white and text black
            self.textColor = UIColor.init(named: "Color_Black")!
            self.placeHolderColor = UIColor.lightGray
            self.backgroundColor = UIColor.init(named: "Color_White")!
            self.font = UIFont.init(name: Theme.Font.Regular, size: Theme.Font.size.lblSize)
        } else if self.tag == 104{  //bg white and text gray
            self.layer.cornerRadius = self.frame.height/2
            self.layer.masksToBounds = true
            self.textColor = UIColor.init(named: "Color_Black")!
            self.placeHolderColor = UIColor.lightGray
            self.backgroundColor = UIColor.init(named: "Color_White")!
            self.font = UIFont.init(name: Theme.Font.Regular, size: Theme.Font.size.lblSize)
        } else if self.tag == 105{ //bg light blue and text white
            self.layer.cornerRadius = self.frame.height/2
            self.layer.masksToBounds = true
            self.textColor = UIColor.init(named: "Color_White")!
            self.placeHolderColor = UIColor.init(named: "Color_White")!
            self.backgroundColor = UIColor.init(named: "Color_11306A")!
            self.font = UIFont.init(name: Theme.Font.Regular, size: Theme.Font.size.lblSize)
        }
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        self.setUpView()
    }
    
    
}

class passwordTextField: UITextField {
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        
        if self.tag == 301{
            self.layer.cornerRadius = self.frame.height/2
            self.layer.masksToBounds = true
            self.textColor = UIColor.init(named: "Color_White")!
            self.placeHolderColor = UIColor.init(named: "Color_White")!
            self.backgroundColor = UIColor.init(named: "Color_DarkBlue")!
            self.font = UIFont.init(name: Theme.Font.Regular, size: Theme.Font.size.lblSize)
        } else if self.tag == 302{
            self.layer.cornerRadius = self.frame.height/2
            self.layer.masksToBounds = true
            self.textColor = UIColor.init(named: "Color_Black")!
            self.placeHolderColor = UIColor.init(named: "Color_Black")!
            self.backgroundColor = UIColor.init(named: "Color_White")!
            self.font = UIFont.init(name: Theme.Font.Regular, size: Theme.Font.size.lblSize)
        } else{
            self.font = UIFont.init(name: Theme.Font.Regular, size: Theme.Font.size.TxtSize)
            self.leftView?.frame = CGRect(x: 30, y: 0, width: 35, height:35)
            self.leftViewMode = .always
            self.leftView?.contentMode = .scaleAspectFit
            self.layer.masksToBounds = false
            
            self.layer.cornerRadius = self.frame.height/2
            self.layer.masksToBounds = true
            self.textColor = UIColor.init(named: "Color_White")!
            self.placeHolderColor = UIColor.init(named: "Color_White")!
            self.backgroundColor = UIColor.black//UIColor.init(named: "Color_Black")!
            self.alpha = 0.75
            self.layer.borderColor = UIColor.init(named: "Color_White")!.cgColor
            self.layer.borderWidth = 0.25
            self.font = UIFont.init(name: Theme.Font.Regular, size: Theme.Font.size.lblSize)
        }
    }
    
    override func rightViewRect(forBounds bounds: CGRect) -> CGRect {
        var rect = super.rightViewRect(forBounds: bounds)
        if #available(iOS 13.0, *) {
            rect.origin.x -= 15 // Assume your right margin is 15
        }
        return rect
    }
}
