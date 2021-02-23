//
//  IPLoader.swift
//  FoodPon_User
//
//  Created by Ilesh's  on 13/03/19.
//

import UIKit
import DGActivityIndicatorView

class IPLoader {
    
    static let shared = IPLoader()
    
    var view : UIView?
    
    static let scrWidth = UIScreen.main.bounds.width
    static let scrHeight = UIScreen.main.bounds.height
    
    class func showLoaderWithBG(viewObj: UIView, boolShow: Bool, enableInteraction: Bool) -> UIView? {
        let width : CGFloat = (54 * scrWidth)/320
        let viewSpinnerBg = UIView(frame: CGRect(x: 0, y: 0, width: scrWidth, height: scrHeight))
        viewSpinnerBg.backgroundColor = #colorLiteral(red: 0, green: 0, blue: 0, alpha: 1).withAlphaComponent(0.2)
        viewSpinnerBg.layer.masksToBounds = true
        viewSpinnerBg.layer.cornerRadius = 5.0
        viewObj.addSubview(viewSpinnerBg)
        
        if boolShow {
            viewSpinnerBg.isHidden = false
        }
        else {
            viewSpinnerBg.isHidden = true
        }
        
        if !enableInteraction {
            viewObj.isUserInteractionEnabled = false
        }
        
        let activityIndicatorView : DGActivityIndicatorView = DGActivityIndicatorView.init(type: DGActivityIndicatorAnimationType(rawValue: 9)!, tintColor: UIColor.init(named: "Color_White")!, size:width)
        activityIndicatorView.center = CGPoint(x: -width, y: scrHeight / 2.0)
        
        UIView.animate(withDuration: 0.5, animations: {
            activityIndicatorView.center = CGPoint(x: scrWidth / 2.0, y: scrHeight / 2.0)
        }) { (isTrue) in
            activityIndicatorView.startAnimating()
        }
        
        activityIndicatorView.tag = 999
        viewSpinnerBg.addSubview(activityIndicatorView)
        return viewSpinnerBg
    }
    
    class func hideRemoveLoaderFromView(removableView: UIView, mainView: UIView) {
        let activityIndicatorView = removableView.viewWithTag(999)
        UIView.animate(withDuration: 0.2, animations: {
            activityIndicatorView?.center = CGPoint(x: scrWidth + CGFloat(activityIndicatorView!.frame.size.width) , y: scrHeight / 2.0)
        }) { (isTrue) in
            removableView.isHidden = true
            removableView.removeFromSuperview()
            mainView.isUserInteractionEnabled = true
        }
    }
    
    func showLoader(OnView:UIView!,enableInteraction:Bool,withMoveAnimation:Bool = false) {
        let width : CGFloat = (54 * IPLoader.scrWidth)/320
        IPLoader.shared.view = UIView(frame: CGRect(x: 0, y: 0, width: IPLoader.scrWidth, height: IPLoader.scrHeight))
        IPLoader.shared.view!.backgroundColor = #colorLiteral(red: 0, green: 0, blue: 0, alpha: 1).withAlphaComponent(0.2)
        IPLoader.shared.view!.layer.masksToBounds = true
        IPLoader.shared.view!.layer.cornerRadius = 5.0
        OnView.addSubview(IPLoader.shared.view!)
        
        if !enableInteraction {
            IPLoader.shared.view!.isUserInteractionEnabled = true
        }else{
            IPLoader.shared.view!.isUserInteractionEnabled = false
        }
        
        let activityIndicatorView : DGActivityIndicatorView = DGActivityIndicatorView.init(type: DGActivityIndicatorAnimationType(rawValue: 9)!, tintColor: UIColor.init(named: "Color_White")!, size:width)
        activityIndicatorView.center = CGPoint(x: -width, y: IPLoader.scrHeight / 2.0)
        
        if withMoveAnimation {
            UIView.animate(withDuration: 0.5, animations: {
                activityIndicatorView.center = CGPoint(x: IPLoader.scrWidth / 2.0, y: IPLoader.scrHeight / 2.0)
            }) { (isTrue) in
                activityIndicatorView.startAnimating()
            }
        }else{
            activityIndicatorView.center = CGPoint(x: IPLoader.scrWidth / 2.0, y: IPLoader.scrHeight / 2.0)
            activityIndicatorView.startAnimating()
        }
        
        
        activityIndicatorView.tag = 999
        IPLoader.shared.view!.addSubview(activityIndicatorView)
    }

    func hideLoader(OnView:UIView!,withMoveAnimation:Bool = false) {
        if IPLoader.shared.view != nil {
            let activityIndicatorView = IPLoader.shared.view!.viewWithTag(999)
            if withMoveAnimation {
                UIView.animate(withDuration: 0.2, animations: {
                    activityIndicatorView?.center = CGPoint(x: IPLoader.scrWidth + CGFloat(activityIndicatorView!.frame.size.width) , y: IPLoader.scrHeight / 2.0)
                }) { (isTrue) in
                    IPLoader.shared.view!.isHidden = true
                    IPLoader.shared.view!.removeFromSuperview()
                    OnView.isUserInteractionEnabled = true
                }
            }else{
                activityIndicatorView?.center = CGPoint(x: IPLoader.scrWidth + CGFloat(activityIndicatorView!.frame.size.width) , y: IPLoader.scrHeight / 2.0)
                IPLoader.shared.view!.isHidden = true
                IPLoader.shared.view!.removeFromSuperview()
                OnView.isUserInteractionEnabled = true
            }
        }
    }
}
