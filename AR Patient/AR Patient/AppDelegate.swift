//
//  AppDelegate.swift
//  AR Patient
//
//  Created by Silicon on 23/04/20.
//  Copyright © 2020 Silicon. All rights reserved.
//

import UIKit
import IQKeyboardManagerSwift

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?
    var navController:UINavigationController = UINavigationController()

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        // Override point for customization after application launch.
        
        IQKeyboardManager.shared.enable = true
        
        //CONFIGURE FIREBASE SETUP
        setUpFireBaseNotification()
        
        if Global.kretriveUserData().role == ""{
            Singleton.shared.saveToUserDefaults(value: "N/A", forKey: kUser.role)
        }
        
        if Global.kretriveUserData().is_gif_displayed == ""{
            Singleton.shared.saveToUserDefaults(value: "No", forKey: kUser.is_gif_displayed)
        }
        
        sleep(3)
        
        if Global.kretriveUserData().User_Id != "0" && Global.kretriveUserData().User_Id != ""{
            self.setnavigation(viewController: "TabBarVC")
        }
        
        return true
    }
    
    func setnavigation(viewController : String) {
        let vc = Global.storyboard.instantiateViewController(withIdentifier: viewController)
        navController = UINavigationController(rootViewController: vc)
        navController.isNavigationBarHidden = true
        self.window?.rootViewController = navController
        self.window?.makeKeyAndVisible()
    }

    // MARK: UISceneSession Lifecycle

    /*func application(_ application: UIApplication, configurationForConnecting connectingSceneSession: UISceneSession, options: UIScene.ConnectionOptions) -> UISceneConfiguration {
        // Called when a new scene session is being created.
        // Use this method to select a configuration to create the new scene with.
        return UISceneConfiguration(name: "Default Configuration", sessionRole: connectingSceneSession.role)
    }

    func application(_ application: UIApplication, didDiscardSceneSessions sceneSessions: Set<UISceneSession>) {
        // Called when the user discards a scene session.
        // If any sessions were discarded while the application was not running, this will be called shortly after application:didFinishLaunchingWithOptions.
        // Use this method to release any resources that were specific to the discarded scenes, as they will not return.
    }*/
    
    func logoutUser() {
        Singleton.shared.saveToUserDefaults(value: "0" , forKey: kUser.IsLoggedIn)
        Singleton.shared.saveToUserDefaults(value: "ARP@T!ENT$" , forKey: kUser.session_id)
        Singleton.shared.saveToUserDefaults(value: "", forKey: kUser.User_Id)
        Singleton.shared.saveToUserDefaults(value: "N/A", forKey: kUser.role)
        Singleton.shared.saveToUserDefaults(value: "No", forKey: kUser.is_gif_displayed)
        
        self.setnavigation(viewController: "LoginVC")
    }


}

