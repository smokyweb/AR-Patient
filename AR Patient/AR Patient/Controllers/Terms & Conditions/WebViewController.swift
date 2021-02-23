//
//  WebViewController.swift
//  AR Patient
//
//  Created by Krupali on 28/04/20.
//  Copyright © 2020 Silicon. All rights reserved.
//

import UIKit
import WebKit
import SwiftyJSON

class WebViewController: UIViewController {
    
    @IBOutlet weak var viewHeader: UIView!
    @IBOutlet weak var btnBack: UIButton!
    @IBOutlet weak var lblTitle: IPAutoScalingLabel!
    @IBOutlet weak var webView: WKWebView!
    
    var cms_id = ""
    var url : String = ""
    var tag = 1 // 1=CMS, 2=FAQ Details, 3=NewsFeed Details, 4=Physical Exam Help
    var content : String = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()
        viewHeader.layer.frame = CGRect(x: 0, y: 0, width: UIScreen.main.bounds.size.width, height: 60)
        viewHeader.roundCorners([.bottomLeft, .bottomRight], radius: 25)
        
        if(tag == 1) {
            if(cms_id == Singleton.shared.cms_id_terms) {
                lblTitle.text = "KeylblTerms".localize
            } else if(cms_id == Singleton.shared.cms_id_privacy) {
                lblTitle.text = "Privacy Policy"
            } else if(cms_id == Singleton.shared.cms_id_aboutUs) {
                lblTitle.text = "About Us"
            }
        } else {
            if(tag == 2) {
                lblTitle.text = "F.A.Q."
            } else if(tag == 3) {
                lblTitle.text = "News Feed"
            } else if(tag == 4) {
                lblTitle.text = "Physical Exam Help"
            }
        }
    }
    
    override func viewDidAppear(_ animated: Bool) {
        self.navigationController?.navigationBar.isHidden = true
        if(tag == 1) {
            cmsAPICall()
        } else if (tag == 4) {
            self.webView.configuration.userContentController.addUserScript(self.getZoomDisableScript())
            self.webView.loadHTMLString(self.content, baseURL: nil)
        } else {
            let link = URL(string:"\(self.url)")!
            self.webView.configuration.userContentController.addUserScript(self.getZoomDisableScript())
            self.webView.load(URLRequest(url: link))
        }
    }
    
    override func viewWillAppear(_ animated: Bool) {
    }
    
    private func getZoomDisableScript() -> WKUserScript {
        let source: String = "var meta = document.createElement('meta');" +
            "meta.name = 'viewport';" +
            "meta.content = 'width=device-width, initial-scale=1.0, maximum- scale=1.0, user-scalable=no';" +
            "var head = document.getElementsByTagName('head')[0];" + "head.appendChild(meta);"
        return WKUserScript(source: source, injectionTime: .atDocumentEnd, forMainFrameOnly: true)
    }
    
    // MARK: - 
    // MARK: - BUTTON ACTION METHODS
    @IBAction func btnBack(_ sender: Any) {
        self.view.endEditing(true)
        self.navigationController?.popViewController(animated: true)
    }
    
    // MARK: - 
    // MARK: - API CALLER METHODS
    func cmsAPICall() {
        var paramer: [String: Any] = [:]
        
        paramer["cms_id"] = self.cms_id
        
        WebService.call.POST(filePath: APIConstant.Request.CMS, params: paramer, enableInteraction: false, showLoader: true, viewObj: (Global.appDelegate.window?.rootViewController!.view)!, onSuccess: { (result, success) in
            if let Dict = result as? [String:Any] {
                if let str = Dict["code"] as? Int , str == 0 {
                    let url = JSON(Dict["url"]!).stringValue
                    let link = URL(string:"\(url)")!
                    self.webView.configuration.userContentController.addUserScript(self.getZoomDisableScript())
                    self.webView.load(URLRequest(url: link))
                }
            }
        }) {
            print("Error \(self.description)")
        }
    }
}
