//
//  IPFirebaseNotification.swift
//  FoodPon_User
//
//  Created by Ilesh's  on 14/03/19.
//

import Foundation
import UserNotifications
import Firebase
import SwiftyJSON

extension AppDelegate {
                                                                  
    func setUpFireBaseNotification() {
        FirebaseApp.configure()
        RegistrationForNotification()
        //Fabric.with([Crashlytics.self])
        Messaging.messaging().delegate = self
        
    }
    
    func RegistrationForNotification() -> Void {
        if #available(iOS 10.0, *) {
            let center  = UNUserNotificationCenter.current()
            center.delegate = self
            center.requestAuthorization(options: [.sound, .alert, .badge]) { (granted, error) in
                if error == nil{
                    DispatchQueue.main.async {
                        UIApplication.shared.registerForRemoteNotifications()
                    }
                }
            }
        }
        else {
            UIApplication.shared.registerUserNotificationSettings(UIUserNotificationSettings(types: [.sound, .alert, .badge], categories: nil))
            UIApplication.shared.registerForRemoteNotifications()
        }
    }
    
}

extension AppDelegate : UNUserNotificationCenterDelegate {
    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        let deviceTokenString = deviceToken.reduce("", {$0 + String(format: "%02X", $1)})
        NSLog("DEVICE TOKEN:- %@",deviceTokenString)
    }
    
    func application(_ application: UIApplication, didFailToRegisterForRemoteNotificationsWithError error: Error) {
        NSLog("ERROR GETING DEVICE TOKEN ")
        Singleton.shared.saveToUserDefaults(value:"ERRORGETINGDEVICETOKEN", forKey: Global.g_UserDefaultKey.DeviceToken)
    }
    
    //MARK: - 
    //MARK:- NOTIFICATION RECEVED METHODS
    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable : Any]) {
        print("Here here here 3")
        notificationReceived(notification: userInfo as [NSObject : AnyObject])
        switch application.applicationState {
        case .inactive:
            NSLog("Inactive")
            break
        case .background:
            NSLog("Background")
            break
        case .active:
            NSLog("Active")
            break
        }
        IPNotificationManager.shared.GetPushProcessData(dictNoti: userInfo as NSDictionary, applicationState: application.applicationState)
    }
    
    
    @available(iOS 10.0, *)
    func userNotificationCenter(_ center: UNUserNotificationCenter, willPresent notification: UNNotification, withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
        var room_id = ""
        var action = ""
        if let userInfo = notification.request.content.userInfo as? [String : AnyObject] {
            let dictPushData = userInfo as NSDictionary
            room_id = JSON(dictPushData.object(forKey: "room_id")).stringValue
            switch UIApplication.shared.applicationState {
            case .inactive:
                NSLog("willPresent Inactive")
            case .background:
                NSLog("willPresent Background")
            case .active:
                NSLog("willPresent Active")
            }
            /*let dicPushData = userInfo as NSDictionary
            print("dicPushData => \(dicPushData)")
            action = dicPushData.object(forKey: "ac") as! String*/
            IPNotificationManager.shared.GetPushProcessData(dictNoti: userInfo as NSDictionary, applicationState: UIApplication.shared.applicationState, isNavigate: false)
            print(userInfo)
        }
        NSLog("willPresent - Handle push from foreground" )
        
        /*if (action != "DELELE_CONVERSATION") {
            if(!Singleton.shared.isChatActive && !Singleton.shared.isChatDetailActive) {
                completionHandler([.alert, .badge, .sound])
            } else if (Singleton.shared.isChatDetailActive && Singleton.shared.room_id != room_id) {
                completionHandler([.alert, .badge, .sound])
            }
        }*/
    }
    
    @available(iOS 10.0, *)
    func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse, withCompletionHandler completionHandler: @escaping () -> Void) {
        if let userInfo = response.notification.request.content.userInfo as? [String : AnyObject] {
            switch UIApplication.shared.applicationState {
            case .inactive:
                NSLog("didReceive Inactive")
            case .background:
                NSLog("didReceive Background")
            case .active:
                NSLog("didReceive Active")
            }
            IPNotificationManager.shared.GetPushProcessData(dictNoti: userInfo as NSDictionary, applicationState: UIApplication.shared.applicationState)
            print(userInfo)
        }
        NSLog("didReceive - Handle push from background or closed")
        completionHandler()
    }
    
    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable : Any], fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {
        
        if let userInfo = userInfo as? [String : AnyObject] {
            switch application.applicationState {
            case .inactive:
                NSLog("Inactive")
                completionHandler(.newData)
            case .background:
                NSLog("Background")
                completionHandler(.newData)
            case .active:
                NSLog("Active")
                completionHandler(.newData)
            }
            IPNotificationManager.shared.GetPushProcessData(dictNoti: userInfo as NSDictionary, applicationState: application.applicationState)
        }
    }
    
    func notificationReceived(notification: [NSObject:AnyObject]) {
        NSLog("notificationReceived : - %@",notification)
        //IPNotificationManager.shared.GetPushProcessData(dictNoti: notification as NSDictionary)
    }
}

extension AppDelegate : MessagingDelegate {
    
    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {
        print("Firebase registration token: \(fcmToken!)")
        Singleton.shared.saveToUserDefaults(value: fcmToken!, forKey: Global.g_UserDefaultKey.DeviceToken)
        _ = Messaging.messaging().fcmToken
    }
    
    func application(application: UIApplication,
                     didRegisterForRemoteNotificationsWithDeviceToken deviceToken: NSData) {
        Messaging.messaging().apnsToken = deviceToken as Data
    }
}
