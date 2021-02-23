// IleshPanchalNotificationManager.swift
//
//  Created by Self on 6/2/17.
//  Copyright © 2017 Self. All rights reserved.


import UIKit
import SwiftyJSON

class IPNotificationManager: NSObject {
    
    var dicNotification : NSDictionary?
    var intState :Int = 0
    var applicationState: UIApplication.State?
    var isNavigate: Bool = true
    
    private override init() {
        
    }
    
    static let shared: IPNotificationManager = {
        let instance = IPNotificationManager()
        return instance
    }()
    
    //MARK: - ACTIVE STATE NOTIFICATION IN IOS 10
    func GetPushProcessDataWhenActive(dictNoti:NSDictionary){
        intState = UIApplication.shared.applicationState.rawValue
        self.dicNotification = dictNoti
        if (JSON(Global.kretriveUserData().IsLoggedIn!).boolValue) {
            DisplayNotification()
        }
    }
    
    func DisplayNotification() -> Void  {
        if let strMSG = dicNotification?.value(forKeyPath: "aps.alert") as? String {
            let alertView = UIAlertController(title: "", message:strMSG, preferredStyle: .alert)
            let action = UIAlertAction(title: "OK", style: .default, handler: { (alert) in
                
            })
            alertView.addAction(action)
            let vc = Global.appDelegate.window?.rootViewController
            vc?.present(alertView, animated: true, completion: nil)
        }
        else if let strMSG = dicNotification?.value(forKeyPath: "aps.data.message") as? String {
            let alertView = UIAlertController(title: "", message:strMSG, preferredStyle: .alert)
            let action = UIAlertAction(title: "OK", style: .default, handler: { (alert) in
                
            })
            alertView.addAction(action)
            let vc = Global.appDelegate.window?.rootViewController
            vc?.present(alertView, animated: true, completion: nil)
        }
    }
    
    //MARK: - SEND NOTIFICATION FOR INACTIVE AND BACKGROND STATE
    func GetPushProcessData(dictNoti:NSDictionary, applicationState: UIApplication.State, isNavigate: Bool = true){
        intState = UIApplication.shared.applicationState.rawValue
        self.dicNotification = dictNoti
        self.applicationState = applicationState
        self.isNavigate = isNavigate
        if (JSON(Global.kretriveUserData().IsLoggedIn!).boolValue) {
            NSLog("Login --- 1" )
            handlePushNotification()
        }
    }
    
    //MARK: - 1ft NOTIFICATION
    func handlePushNotification() -> Void {
        if Global.appDelegate.navController == nil {
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.3) {
                NSLog("handlePushNotifBasedOnNotifType --- 2" )
                self.handlePushNotifBasedOnNotifType()
            }
        }else{
            NSLog("handlePushNotifBasedOnNotifType --- 2" )
            self.handlePushNotifBasedOnNotifType()
        }
    }
    
    func manageNavigation() {
        
    }
    
    //MARK:- 2nd NOTIFICATION
    func handlePushNotifBasedOnNotifType() -> Void {
    }
}
