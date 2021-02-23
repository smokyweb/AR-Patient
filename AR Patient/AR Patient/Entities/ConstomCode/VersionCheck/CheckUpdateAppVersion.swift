//
//  CheckUpdateAppVersion.swift
//
//  Created by SAS
//  Copyright © 2018 Self. All rights reserved.
//

import UIKit

class CheckUpdateAppVersion: NSObject {

    func versionAndDownloadUrl() -> (version: String, downloadUrl: String)? {
        guard let identifier = Bundle.main.object(forInfoDictionaryKey: "CFBundleIdentifier") as? String, let url = URL(string: "http://itunes.apple.com/lookup?bundleId=\(identifier)") else { return nil
        }
        
        guard
            let data = try? Data(contentsOf: url),
            let json = try? JSONSerialization.jsonObject(with: data, options: .allowFragments) as? [String: Any],
            let results = json?["results"] as? [[String: Any]],
            results.count > 0,
            let version = results[0]["version"] as? String,
            let downLoadUrl = results[0]["trackViewUrl"] as? String
            else {
                return nil
        }
        
        return (version, downLoadUrl)
    }
    
    func isUpdateAvailable() -> Bool {
        guard let data = versionAndDownloadUrl() else { return false }
        let appStoreVewsion = data.version
        
        return compare(appstoreVersion: appStoreVewsion)
    }
    
    func compare(appstoreVersion: String) -> Bool {
        guard let deviceVersion = Bundle.main.object(forInfoDictionaryKey: "CFBundleShortVersionString") as? String else { return false }
        
        if appstoreVersion.compare(deviceVersion, options: .numeric) == .orderedDescending {
            return true
        } else {
            return false
        }
    }
    
    func showUpdateAlert(isForce:Bool = false) {
        guard let applicationName = Bundle.main.object(forInfoDictionaryKey: "CFBundleName") as? String, let data = versionAndDownloadUrl() else { return }
        
        var alert: UIAlertController?
        
        if compare(appstoreVersion: data.version) {
            alert = UIAlertController(title: applicationName, message: "Version \(data.version) is available on the AppStore", preferredStyle: UIAlertController.Style.alert)
            alert?.addAction(UIAlertAction(title: "Update", style: UIAlertAction.Style.destructive, handler: { action in
                guard let url = URL(string: data.downloadUrl) else { return }
                UIApplication.shared.openURL(url)
            }))
            
            if !isForce {
                alert?.addAction(UIAlertAction(title: "Cancel", style: UIAlertAction.Style.cancel, handler: nil))
            }
        } else {
            if !isForce {
                alert = UIAlertController(title: applicationName, message: "Version \(data.version) is the latest version on the AppStore", preferredStyle: UIAlertController.Style.alert)
                alert?.addAction(UIAlertAction(title: "Ok", style: UIAlertAction.Style.cancel, handler: nil))
            }
        }
        
        guard let _alert = alert else { return }
        UIApplication.shared.keyWindow?.rootViewController?.present(_alert, animated: true, completion: {
            
        })
    }
    
    func getAppCurrentVersionCall() {
        /*AFAPIMaster.sharedAPIMaster.getAppCurrentVersionData_Completion(params: nil, showLoader: true, enableInteraction: false, viewObj: (navObj?.topViewController?.view)!, onSuccess: { (returnData: Any) in
            
            let responseData: NSDictionary = returnData as! NSDictionary
            if (Bundle.main.infoDictionary?["CFBundleShortVersionString"] as! String != responseData["response"] as! String) {
                let alert = UIAlertController(title: "", message: LocalizeHelper().localizedString(forKey: "keyAppUpgradeMsg"), preferredStyle: UIAlertControllerStyle.alert)
                alert.addAction(UIAlertAction(title:LocalizeHelper().localizedString(forKey: "keyAppUpgradeUpdate"), style: UIAlertActionStyle.default, handler: { action in
                    UIApplication.shared.openURL(URL(string: "itms-apps://itunes.apple.com/au/app/chilax/id1222577949?mt=8")!)
                }))
                self.navObj?.topViewController?.present(alert, animated: true, completion: nil)
            }
        })*/
    }
    
}
