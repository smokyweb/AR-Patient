//
//  IPView.swift
//  EZPickup_Rider

//

import UIKit

class IPView: UIView {
    private var shadowLayer: CAShapeLayer!
    
    override func layoutSubviews() {
        super.layoutSubviews()
        if shadowLayer == nil {
            shadowLayer = CAShapeLayer()
            if self.tag == 301 {
                shadowLayer.path = UIBezierPath(roundedRect: bounds, cornerRadius: 12).cgPath
                shadowLayer.fillColor = (self.backgroundColor ?? UIColor.white).cgColor
                shadowLayer.shadowColor = UIColor.darkGray.cgColor
                shadowLayer.shadowPath = shadowLayer.path
                shadowLayer.shadowOffset = CGSize(width: 1.0, height: 1.0)
                shadowLayer.shadowOpacity = 0.4
                shadowLayer.shadowRadius = 2
            }
            else if self.tag == 302 {
                shadowLayer.path = UIBezierPath(roundedRect: bounds, cornerRadius: 0).cgPath
                shadowLayer.fillColor = (self.backgroundColor ?? UIColor.white).cgColor
                shadowLayer.shadowColor = UIColor.darkGray.cgColor
                shadowLayer.shadowPath = shadowLayer.path
                shadowLayer.shadowOffset = CGSize(width: 1.0, height: 1.0)
                shadowLayer.shadowOpacity = 0.4
                shadowLayer.shadowRadius = 2
            }
            else if self.tag == 303 {
                shadowLayer.path = UIBezierPath(roundedRect: bounds, cornerRadius: 12).cgPath
                shadowLayer.fillColor = (self.backgroundColor ?? UIColor.white).cgColor
                shadowLayer.shadowColor = UIColor.darkGray.cgColor
                shadowLayer.shadowPath = shadowLayer.path
                shadowLayer.shadowOffset = CGSize(width: 2.0, height: 2.0)
                shadowLayer.shadowOpacity = 0.8
                shadowLayer.shadowRadius = 8
                self.layer.cornerRadius = 8
                self.layer.masksToBounds = true
            }
            else if self.tag == 304 {
                shadowLayer.path = UIBezierPath(roundedRect: bounds, cornerRadius: 12).cgPath
                shadowLayer.fillColor = (self.backgroundColor ?? UIColor.white).cgColor
                shadowLayer.shadowColor = UIColor.darkGray.cgColor
                shadowLayer.shadowPath = shadowLayer.path
                shadowLayer.shadowOffset = CGSize(width: 2.0, height: 2.0)
                shadowLayer.shadowOpacity = 0.8
                shadowLayer.shadowRadius = 10
                self.layer.cornerRadius = 10
                self.layer.masksToBounds = true
            }
            else if self.tag == 305 {
                shadowLayer.path = UIBezierPath(roundedRect: bounds, cornerRadius: 12).cgPath
                shadowLayer.fillColor = (self.backgroundColor ?? UIColor.white).cgColor
                shadowLayer.shadowColor = UIColor.darkGray.cgColor
                shadowLayer.shadowPath = shadowLayer.path
                shadowLayer.shadowOffset = CGSize(width: 1.0, height: 1.0)
                shadowLayer.shadowOpacity = 0.4
                shadowLayer.shadowRadius = 2
                shadowLayer.shadowOpacity = 0.8
                shadowLayer.shadowRadius = 8
                self.layer.cornerRadius = 8
                self.layer.masksToBounds = true
            }
            layer.insertSublayer(shadowLayer, at: 0)
        }
    }
}


