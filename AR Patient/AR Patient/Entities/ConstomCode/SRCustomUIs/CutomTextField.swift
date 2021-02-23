//
//  SRCutomTextField.swift


//  Created by SAS

//  Copyright © 2018 MAC7. All rights reserved.
//

import Foundation
import UIKit

class CutomTextField: UITextField {
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        self.textColor = Theme.color.primaryTheme
//        self.placeHolderColor = Global.kAppColor.DarkGray
        //self.font = UIFont.init(name: Global.kFont.Roboto_Regular, size: Global.kFontSize.TextFieldSize)
        self.tintColor = Theme.color.primaryTheme
        self.layer.cornerRadius = 5.0
        self.layer.masksToBounds = true
        
        self.leftView?.frame = CGRect(x: 0, y: 0, width: 35, height:35)
        //self.leftViewMode = UITextFieldViewMode.always
        self.leftViewMode = .always
        self.leftView?.contentMode = .scaleAspectFit
    }
    
    override func layoutSubviews() {
       // Singleton.sharedSingleton.setShadow(to: self)
        super.layoutSubviews()
    }
}


extension UITextField
{
    enum Direction
    {
        case Left
        case Right
    }
    
    func AddImage(direction:Direction,imageName:String,Frame:CGRect,backgroundColor:UIColor)
    {
        let View = UIView(frame: Frame)
        View.backgroundColor = backgroundColor
        
        let imageView = UIImageView(frame: Frame)
        imageView.image = UIImage(named: imageName)
        
        View.addSubview(imageView)
        
        if Direction.Left == direction
        {
            self.leftViewMode = .always
            self.leftView = View
        }
        else
        {
            self.rightViewMode = .always
            self.rightView = View
        }
    }
    
    func withImage(direction: Direction, image: UIImage, colorSeparator: UIColor, colorBorder: UIColor){
        let mainView = UIView(frame: CGRect(x: 0, y: 0, width: 60, height: 45))
//        mainView.layer.cornerRadius = 5
        let view = UIView(frame: CGRect(x: 0, y: 0, width: 60, height: 45))
        view.backgroundColor = .clear
        view.clipsToBounds = true
//        view.layer.cornerRadius = 5
//        view.layer.borderWidth = CGFloat(0.5)
//        view.layer.borderColor = colorBorder.cgColor
        //view.layer.addBorder(edge: .right, color: UIColor.init(named: "Color_White")!, thickness: 1.0)
        mainView.addSubview(view)
        
        let imageView = UIImageView(image: image)
        imageView.contentMode = .scaleAspectFit
        imageView.frame = CGRect(x: 20, y: (view.frame.size.height/2) - 12.5  , width: 20.0, height: 20.0)
        view.addSubview(imageView)
        
//        imageView.image = imageView.image!.withRenderingMode(.alwaysTemplate)
//        imageView.tintColor = #colorLiteral(red: 1, green: 0.6549019608, blue: 0.0431372549, alpha: 1)
        
        let seperatorView = UIView()
        
        seperatorView.backgroundColor = colorSeparator
        mainView.addSubview(seperatorView)
        
        if(Direction.Left == direction){ // image left
            seperatorView.frame = CGRect(x: 47.5, y: 3.5, width: 1, height: 38)
            self.leftViewMode = .always
            self.leftView = mainView
        } else { // image right
            seperatorView.frame = CGRect(x: 0, y: 3.5, width: 1, height: 38)
            self.rightViewMode = .always
            self.rightView = mainView
        }
    }
    
    func withoutImage(direction: Direction, colorSeparator: UIColor, colorBorder: UIColor){
        let mainView = UIView(frame: CGRect(x: 0, y: 0, width: 16, height: 45))
        //        mainView.layer.cornerRadius = 5
        let view = UIView(frame: CGRect(x: 0, y: 0, width: 16, height: 45))
        view.backgroundColor = .clear
        view.clipsToBounds = true
        //        view.layer.cornerRadius = 5
        //        view.layer.borderWidth = CGFloat(0.5)
        //        view.layer.borderColor = colorBorder.cgColor
        mainView.addSubview(view)
        
        let seperatorView = UIView()
        seperatorView.backgroundColor = colorSeparator
        mainView.addSubview(seperatorView)
        
        if(Direction.Left == direction){ // image left
            seperatorView.frame = CGRect(x: 49, y: 0, width: 1, height: 45)
            self.leftViewMode = .always
            self.leftView = mainView
        } else { // image right
            seperatorView.frame = CGRect(x: 0, y: 0, width: 5, height: 45)
            self.rightViewMode = .always
            self.rightView = mainView
        }
    }
}
