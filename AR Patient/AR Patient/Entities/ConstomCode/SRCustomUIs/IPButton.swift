//
//  IPButton.swift

//
//  Created by SAS
//  Copyright © 2018 MAC7. All rights reserved.
//

import Foundation
import UIKit
//import DTGradientButton

class IPButton: UIButton {
    
    var badgeLabel : UILabel?
    
    var badge: String? {
        didSet {
            addbadgetobutton(badge: badge)
        }
    }
    
    public var badgeBackgroundColor = UIColor.red {
        didSet {
            badgeLabel?.backgroundColor = badgeBackgroundColor
        }
    }
    
    public var badgeTextColor = UIColor.white {
        didSet {
            badgeLabel?.textColor = badgeTextColor
        }
    }
    
    public var badgeFont = UIFont.systemFont(ofSize: 12.0) {
        didSet {
            badgeLabel?.font = badgeFont
        }
    }
    
    public var badgeEdgeInsets: UIEdgeInsets? {
        didSet {
            addbadgetobutton(badge: badge)
        }
    }
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        
    }
    
    func addbadgetobutton(badge: String?) {
        if badgeLabel == nil {
            badgeLabel = UILabel()
        }
        badgeLabel!.text = badge
        badgeLabel!.font = UIFont.init(name: Theme.Font.SemiBold, size: 10.0)
        badgeLabel!.textColor = badgeTextColor
        badgeLabel!.backgroundColor = badgeBackgroundColor
        badgeLabel!.font = badgeFont
        badgeLabel!.sizeToFit()
        
        badgeLabel!.textAlignment = .center
        let badgeSize = badgeLabel!.frame.size
        
        let height = max(18, Double(badgeSize.height) + 5.0)
        let width = max(height, Double(badgeSize.width) + 5.0)
        
        var vertical: Double?, horizontal: Double?
        if let badgeInset = self.badgeEdgeInsets {
            vertical = Double(badgeInset.top) - Double(badgeInset.bottom)
            horizontal = Double(badgeInset.left) - Double(badgeInset.right)
            
            let x = (Double(bounds.size.width) + horizontal!)
            let y = -(Double(badgeSize.height) / 2) + vertical!
            badgeLabel!.frame = CGRect(x: x, y: y, width: width, height: height)
        } else {
            let x = (self.frame.origin.x + self.frame.width) - 15  //(self.frame.width - CGFloat((width / 2.0)) - 10)
            let y = self.frame.origin.y - 2 //CGFloat(-((height / 2.0)))
            badgeLabel!.frame = CGRect(x: x, y: y, width: CGFloat(width), height: CGFloat(height))
        }
        
        badgeLabel!.layer.cornerRadius = badgeLabel!.frame.height/2
        badgeLabel!.layer.masksToBounds = true
        badgeLabel!.layer.zPosition = -1
        addSubview(badgeLabel!)
        badgeLabel!.isHidden = badge != nil ? false : true
    }
    
    func removeBadge() {
        if badgeLabel != nil {
            badgeLabel?.removeFromSuperview()
            badgeLabel = nil
        }
    }
    
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        self.setNeedsDisplay()
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        setUpView()
    }
    override func prepareForInterfaceBuilder() {
        super.prepareForInterfaceBuilder()
        setUpView()
    }
    
    //MARK:- FUNC SETUPVIEW.
    func setUpView() {
        self.clipsToBounds = true
        if self.tag == 101 { // LightBlue BG & White Text
            self.layer.cornerRadius = self.frame.height/2
            self.layer.masksToBounds = true
            self.backgroundColor = UIColor.init(named: "Color_LightBlue")
            self.titleLabel?.textColor = UIColor.init(named: "Color_White")
            self.titleLabel?.font = UIFont.init(name: Theme.Font.SemiBold, size: Theme.Font.size.TitleSize)
            self.tintColor = UIColor.init(named: "Color_White")
        }
        else if self.tag == 102 { // clear BG & White Text
            self.layer.cornerRadius = self.frame.height/2
            self.layer.masksToBounds = true
            self.backgroundColor = UIColor.clear
            self.titleLabel?.textColor = UIColor.init(named: "Color_White")
            self.titleLabel?.font = UIFont.init(name: Theme.Font.Medium, size: Theme.Font.size.btnSize)
            self.tintColor = UIColor.init(named: "Color_White")
        }
        else if self.tag == 103 { // DarkBlue BG & White Text(forgot pwd)
            //            self.layer.cornerRadius = self.frame.height/2
            //            self.layer.masksToBounds = true
            self.backgroundColor = UIColor.init(named: "Color_DarkBlue")
            self.titleLabel?.textColor = UIColor.init(named: "Color_White")
            self.titleLabel?.font = UIFont.init(name: Theme.Font.Medium, size: Theme.Font.size.TxtSize)
            self.tintColor = UIColor.init(named: "Color_White")
        }
        else if self.tag == 104 { // LightBlue BG & White Text
           self.backgroundColor = UIColor.init(named: "Color_LightBlue")
            self.titleLabel?.textColor = UIColor.init(named: "Color_White")
            self.titleLabel?.font = UIFont.init(name: Theme.Font.SemiBold, size: Theme.Font.size.TitleSize)
            self.tintColor = UIColor.init(named: "Color_White")
        } else if self.tag == 105 {
            self.layer.cornerRadius = 10
            self.layer.masksToBounds = true
            self.backgroundColor = UIColor.init(named: "Color_White")
            self.titleLabel?.textColor = UIColor.init(named: "Color_DarkBlue")
            self.titleLabel?.font = UIFont.init(name: Theme.Font.SemiBold, size: Theme.Font.size.btnSize)
            self.tintColor = UIColor.init(named: "Color_DarkBlue")
        } else if self.tag == 106 { // Blue BG & White Text
            self.layer.cornerRadius = self.frame.height/2
            self.layer.masksToBounds = true
            self.backgroundColor = UIColor.init(named: "Color_LightBlue")
            self.titleLabel?.textColor = UIColor.init(named: "Color_White")
            self.titleLabel?.font = UIFont.init(name: Theme.Font.Bold, size: Theme.Font.size.TxtSize_11)
            self.tintColor = UIColor.init(named: "Color_White")
        } else if self.tag == 107 { // clear BG & Blue Text
            self.backgroundColor = .clear
            self.titleLabel?.textColor = UIColor.init(named: "Color_LightBlue")
            self.titleLabel?.font = UIFont.init(name: Theme.Font.Bold, size: Theme.Font.size.btnSize)
            self.tintColor = UIColor.init(named: "Color_LightBlue")
        }
        
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        self.setUpView()
    }
}

public extension UIButton {
    /*public func setGradientBackgroundColors(_ colors: [UIColor], direction: DTImageGradientDirection, for state: UIControl.State) {
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
     }*/
}

class TapabbleLabel: UILabel {
    
    let layoutManager = NSLayoutManager()
    let textContainer = NSTextContainer(size: CGSize.zero)
    var textStorage = NSTextStorage() {
        didSet {
            textStorage.addLayoutManager(layoutManager)
        }
    }
    
    var onCharacterTapped: ((_ label: UILabel, _ characterIndex: Int) -> Void)?
    
    let tapGesture = UITapGestureRecognizer()
    
    override var attributedText: NSAttributedString? {
        didSet {
            if let attributedText = attributedText {
                textStorage = NSTextStorage(attributedString: attributedText)
            } else {
                textStorage = NSTextStorage()
            }
        }
    }
    
    override var lineBreakMode: NSLineBreakMode {
        didSet {
            textContainer.lineBreakMode = lineBreakMode
        }
    }
    
    override var numberOfLines: Int {
        didSet {
            textContainer.maximumNumberOfLines = numberOfLines
        }
    }
    
    /**
     Creates a new view with the passed coder.
     
     :param: aDecoder The a decoder
     
     :returns: the created new view.
     */
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        setUp()
    }
    
    /**
     Creates a new view with the passed frame.
     
     :param: frame The frame
     
     :returns: the created new view.
     */
    override init(frame: CGRect) {
        super.init(frame: frame)
        setUp()
    }
    
    /**
     Sets up the view.
     */
    func setUp() {
        isUserInteractionEnabled = true
        layoutManager.addTextContainer(textContainer)
        textContainer.lineFragmentPadding = 0
        textContainer.lineBreakMode = lineBreakMode
        textContainer.maximumNumberOfLines = numberOfLines
        tapGesture.addTarget(self, action: #selector(TapabbleLabel.labelTapped(gesture:)))
        addGestureRecognizer(tapGesture)
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        textContainer.size = bounds.size
    }
    
    @objc func labelTapped(gesture: UITapGestureRecognizer) {
        guard gesture.state == .ended else {
            return
        }
        
        let locationOfTouch = gesture.location(in: gesture.view)
        let textBoundingBox = layoutManager.usedRect(for: textContainer)
        let textContainerOffset = CGPoint(x: (bounds.width - textBoundingBox.width) / 2 - textBoundingBox.minX,
                                          y: (bounds.height - textBoundingBox.height) / 2 - textBoundingBox.minY);
        let locationOfTouchInTextContainer = CGPoint(x: locationOfTouch.x - textContainerOffset.x,
                                                     y: locationOfTouch.y - textContainerOffset.y)
        let indexOfCharacter = layoutManager.characterIndex(for: locationOfTouchInTextContainer,
                                                            in: textContainer,
                                                            fractionOfDistanceBetweenInsertionPoints: nil)
        
        onCharacterTapped?(self, indexOfCharacter)
    }
}

