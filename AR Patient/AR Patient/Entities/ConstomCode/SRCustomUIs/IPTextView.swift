//
//  IPTextView.swift
//  ShowOff
//
//  Created by Knoxweb on 05/10/19.
//

import Foundation
import UIKit

class IPTextView: UITextView {
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        
        self.font = UIFont.init(name: Theme.Font.Regular, size: Theme.Font.size.TxtSize)

        self.layer.masksToBounds = false
        
        if self.tag == 101{
            self.backgroundColor = UIColor.init(named: "Color_White")!
            self.textColor = UIColor.init(named: "Color_Black")!
            self.font = UIFont.init(name: Theme.Font.Regular, size: Theme.Font.size.lblSize)
            self.layer.cornerRadius = 12
            self.layer.masksToBounds = true
            self.textContainerInset = UIEdgeInsets(top: 15, left: 10, bottom: 0, right: 5)
        } else if self.tag == 102{
            self.backgroundColor = UIColor.init(named: "Color_White")!
            self.textColor = UIColor.lightGray
            self.font = UIFont.init(name: Theme.Font.Regular, size: Theme.Font.size.lblSize)
            self.layer.cornerRadius = 12
            self.layer.masksToBounds = true
            self.textContainerInset = UIEdgeInsets(top: 15, left: 10, bottom: 0, right: 5)
        }
        else{
            self.textColor = Theme.color.text
            self.tintColor = Theme.color.text
            self.backgroundColor = Theme.color.textFieldBG
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
        
        if self.tag == 101{
            self.backgroundColor = UIColor.init(named: "Color_White")!
            self.textColor = UIColor.init(named: "Color_Black")!
            self.font = UIFont.init(name: Theme.Font.Regular, size: Theme.Font.size.lblSize)
            self.layer.cornerRadius = 12
            self.layer.masksToBounds = true
            self.textContainerInset = UIEdgeInsets(top: 15, left: 10, bottom: 0, right: 5)
        } else if self.tag == 102{
            self.backgroundColor = UIColor.init(named: "Color_White")!
            self.textColor = UIColor.lightGray
            self.font = UIFont.init(name: Theme.Font.Regular, size: Theme.Font.size.lblSize)
            self.layer.cornerRadius = 12
            self.layer.masksToBounds = true
            self.textContainerInset = UIEdgeInsets(top: 15, left: 10, bottom: 0, right: 5)
        }
        else{
            self.textColor = Theme.color.text
            self.tintColor = Theme.color.text
            self.backgroundColor = Theme.color.textFieldBG
        }
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        self.setUpView()
    }
}
