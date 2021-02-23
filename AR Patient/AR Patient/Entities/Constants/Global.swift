//
//  Global.swift
//
//  Created by SAS on 6/13/17.
//  Copyright © 2017 Ilesh. All rights reserved.

import UIKit

class Global {
    
    static let DeviceUUID = UIDevice.current.identifierForVendor!.uuidString
    static let PhoneDigitLimit = 11
    static let UserNameDigitLimit = 50
    static let StreetNODigitLimit = 20
    static let StreetNameDigitLimit = 60
    static var IsOffline:Bool = false
    
    struct g_ws {
        static let Device_type: String! = "IOS"
        static let Role: String! = "STUDENT"
        static let Language_type: String! = Locale.current.languageCode
        static let Version_number: String! = Bundle.main.infoDictionary?["CFBundleShortVersionString"] as? String
    }
    
    static let storyboard = UIStoryboard(name: "Main", bundle: nil)
    static let developStoryboard = UIStoryboard(name: "Development", bundle: nil)
    struct kStripe {
        
        // Stripe Live Keys of Spynn app
        //        static let client_id = "ca_ERybHUhnAglV4Z5QJszeiQCP6iBf8m4W"
        //        static let secretKey = "sk_live_2dlRFxmGr1HZlqGMRHKHSgNg"
        //        static let publicKey = "pk_live_MoFRvDfUeuqIKu260h3RB4Ge"
        //        static let redirection_url = "https://web.spynnapp.com/stripe_connect.html"
        
        
        // Stripe Development Keys of ShowOff
        static let client_id = "ca_FxwntjlaZErf4iSw5ypbX4uIsTpZQDlZ"
        static let secretKey = "sk_test_1kXf2tPZE7ZddgZQ89WhPVP200l53rjt4u"
        static let publicKey = "pk_test_lbaKXzRAg8Q9RjqzM6RJ7FGS00zmaufCjQ"
        static let redirection_url = "https://showoff.testplanets.com/stripe_connect.html"
    }
    
    struct AppUtility {
        
        static func lockOrientation(_ orientation: UIInterfaceOrientationMask) {
            // Comment for unused
            //Global.appDelegate.orientationLock = orientation
        }
        
        /// OPTIONAL Added method to adjust lock and rotate to the desired orientation
        static func lockOrientation(_ orientation: UIInterfaceOrientationMask, andRotateTo rotateOrientation:UIInterfaceOrientation) {
            self.lockOrientation(orientation)
            UIDevice.current.setValue(rotateOrientation.rawValue, forKey: "orientation")
        }
    }
    
    //Device Compatibility
    struct is_Device {
        static let _iPhone = (UIDevice.current.model as String).isEqual("iPhone") ? true : false
        static let _iPad = (UIDevice.current.model as String).isEqual("iPad") ? true : false
        static let _iPod = (UIDevice.current.model as String).isEqual("iPod touch") ? true : false
    }
    
    //Display Size Compatibility
    struct is_iPhone {
        static let _XSMax = (UIScreen.main.bounds.size.height == 896.0 ) ? true : false
        static let _XS = (UIScreen.main.bounds.size.height == 812.0 ) ? true : false
        static let _XR = (UIScreen.main.bounds.size.height == 896.0 ) ? true : false
        static let _X = (UIScreen.main.bounds.size.height == 812.0 ) ? true : false
        static let _6p = (UIScreen.main.bounds.size.height >= 736.0 ) ? true : false
        static let _6 = (UIScreen.main.bounds.size.height <= 667.0 && UIScreen.main.bounds.size.height > 568.0) ? true : false
        static let _5 = (UIScreen.main.bounds.size.height <= 568.0 && UIScreen.main.bounds.size.height > 480.0) ? true : false
        static let _4 = (UIScreen.main.bounds.size.height <= 480.0) ? true : false
    }
    
    //IOS Version Compatibility
    struct is_iOS {
        static let _12 = ((Float(UIDevice.current.systemVersion as String))! >= Float(12.0)) ? true : false
        static let _11 = ((Float(UIDevice.current.systemVersion as String))! >= Float(11.0)) ? true : false
        static let _10 = ((Float(UIDevice.current.systemVersion as String))! >= Float(10.0)) ? true : false
        static let _9 = ((Float(UIDevice.current.systemVersion as String))! >= Float(9.0) && (Float(UIDevice.current.systemVersion as String))! < Float(10.0)) ? true : false
        static let _8 = ((Float(UIDevice.current.systemVersion as String))! >= Float(8.0) && (Float(UIDevice.current.systemVersion as String))! < Float(9.0)) ? true : false
    }
    
    // MARK: -  Shared classes
    static let appDelegate: AppDelegate = UIApplication.shared.delegate as! AppDelegate
    static let singleton = Singleton.shared
    
    // MARK: -  Get UIColor from RGB
    public func RGB(r: Float, g: Float, b: Float, a: Float) -> UIColor {
        return UIColor(red: CGFloat(r / 255.0), green: CGFloat(g / 255.0), blue: CGFloat(b / 255.0), alpha: CGFloat(a))
    }
    
    // MARK: -  Dispatch Delay
    func delay(delay: Double, closure: @escaping ()->()) {
        let when = DispatchTime.now() + delay
        DispatchQueue.main.asyncAfter(deadline: when, execute: closure)
    }
    
    struct kCurrency {
        static let symbol = "USD"
    }
    
    struct g_UserDefaultKey {
        static let DeviceToken: String! = "DEVICE_TOKEN"
    }
    
    struct kretriveUserData {
        
        var IsLoggedIn: String? = Global.singleton.retriveFromUserDefaults(key: kUser.IsLoggedIn)
        var User_Id: String? = Global.singleton.retriveFromUserDefaults(key: kUser.User_Id)
        var isParkingSuggested: String? = Global.singleton.retriveFromUserDefaults(key: kUser.isParkingSuggested)
        var suggestion: String? = Global.singleton.retriveFromUserDefaults(key: kUser.suggestion)
        var park_latitude: String? = Global.singleton.retriveFromUserDefaults(key: kUser.park_latitude)
        var park_longitude: String? = Global.singleton.retriveFromUserDefaults(key: kUser.park_longitude)
        var is_speaked: String? = Global.singleton.retriveFromUserDefaults(key: kUser.is_speaked)
        var is_speaked_multiple: String? = Global.singleton.retriveFromUserDefaults(key: kUser.is_speaked_multiple)
        var park_id: String? = Global.singleton.retriveFromUserDefaults(key: kUser.park_id)
        var business_name: String? = Global.singleton.retriveFromUserDefaults(key: kUser.business_name)
        var trackingStatus: String? = Global.singleton.retriveFromUserDefaults(key: kUser.trackingStatus)
        var localNotification: String? = Global.singleton.retriveFromUserDefaults(key: kUser.localNotification)
        var isGifDisplayed: String? = Global.singleton.retriveFromUserDefaults(key: kUser.isGifDisplayed)
        
        var fullName : String? = Global.singleton.retriveFromUserDefaults(key: kUser.fullName)
        var lastname: String? = Global.singleton.retriveFromUserDefaults(key: kUser.lastname)
        var email: String? = Global.singleton.retriveFromUserDefaults(key: kUser.email)
        var username: String? = Global.singleton.retriveFromUserDefaults(key: kUser.username)//vd
        var role: String? = Global.singleton.retriveFromUserDefaults(key: kUser.role)//vd
        var is_gif_displayed: String? = Global.singleton.retriveFromUserDefaults(key: kUser.is_gif_displayed)//vd
        
        var phone: String? = Global.singleton.retriveFromUserDefaults(key: kUser.phone)
        var stripe_customer_id: String? = Global.singleton.retriveFromUserDefaults(key: kUser.stripe_link_id)
        var avatar: String? = Global.singleton.retriveFromUserDefaults(key: kUser.avatar)
        
        var is_verified: String? = Global.singleton.retriveFromUserDefaults(key: kUser.is_verified)
        var address: String? = Global.singleton.retriveFromUserDefaults(key: kUser.address)
        var address_latitude: String? = Global.singleton.retriveFromUserDefaults(key: kUser.address_latitude)
        var address_longitude: String? = Global.singleton.retriveFromUserDefaults(key: kUser.address_longitude)
        
        var address_state: String? = Global.singleton.retriveFromUserDefaults(key: kUser.address_state)
        var rating: String? = Global.singleton.retriveFromUserDefaults(key: kUser.rating)
        var date_of_birth: String? = Global.singleton.retriveFromUserDefaults(key: kUser.date_of_birth)
        var session_id: String? = Global.singleton.retriveFromUserDefaults(key: kUser.session_id)
        
        var bio: String? = Global.singleton.retriveFromUserDefaults(key: kUser.bio)
        var cover_image: String? = Global.singleton.retriveFromUserDefaults(key: kUser.cover_image)
        var car_image: String? = Global.singleton.retriveFromUserDefaults(key: kUser.car_image)
        var car_model: String? = Global.singleton.retriveFromUserDefaults(key: kUser.car_model)
        var category: String? = Global.singleton.retriveFromUserDefaults(key: kUser.category)
        var car_year: String? = Global.singleton.retriveFromUserDefaults(key: kUser.car_year)
        var car_milage: String? = Global.singleton.retriveFromUserDefaults(key: kUser.car_milage)
        var car_plate_number: String? = Global.singleton.retriveFromUserDefaults(key: kUser.car_plate_number)
        var car_vin_number: String? = Global.singleton.retriveFromUserDefaults(key: kUser.car_vin_number)
        
        var service_area: String? = Global.singleton.retriveFromUserDefaults(key: kUser.service_area)
        var approved_ride_type: String? = Global.singleton.retriveFromUserDefaults(key: kUser.approved_ride_type)
        var online_ride_type: String? = Global.singleton.retriveFromUserDefaults(key: kUser.online_ride_type)
        var ssn: String? = Global.singleton.retriveFromUserDefaults(key: kUser.ssn)
        var gender: String? = Global.singleton.retriveFromUserDefaults(key: kUser.gender)
        var license_number: String? = Global.singleton.retriveFromUserDefaults(key: kUser.license_number)
        var license_region: String? = Global.singleton.retriveFromUserDefaults(key: kUser.license_region)
        var license_city: String? = Global.singleton.retriveFromUserDefaults(key: kUser.license_city)
        var is_approved: String? = Global.singleton.retriveFromUserDefaults(key: kUser.is_approved)
        var is_active: String? = Global.singleton.retriveFromUserDefaults(key: kUser.is_active)
        var accurate_bg_check_status: String? = Global.singleton.retriveFromUserDefaults(key: kUser.accurate_bg_check_status)
        var status: String? = Global.singleton.retriveFromUserDefaults(key: kUser.status)
        
        var doc_exp_date: String? = Global.singleton.retriveFromUserDefaults(key: kUser.doc_exp_date)
        
        var driver_wallet: String? = Global.singleton.retriveFromUserDefaults(key: kUser.driver_wallet)
        var fund_transfer_charge: String? = Global.singleton.retriveFromUserDefaults(key: kUser.fund_transfer_charge)
        var ride_id: String? = Global.singleton.retriveFromUserDefaults(key: kUser.ride_id)
        var map_key: String? = Global.singleton.retriveFromUserDefaults(key: kUser.map_key)
        var mapbox_key: String? = Global.singleton.retriveFromUserDefaults(key: kUser.mapbox_key)
        var max_job_images: String? = Global.singleton.retriveFromUserDefaults(key: kUser.max_job_images)
        var request_session_id: String? = Global.singleton.retriveFromUserDefaults(key: kUser.request_session_id)
        
        var subscription_id: String? = Global.singleton.retriveFromUserDefaults(key: kUser.subscription_id)
        var licenseUrl: String? = Global.singleton.retriveFromUserDefaults(key: kUser.licenseUrl)
        var miles: String? = Global.singleton.retriveFromUserDefaults(key: kUser.miles)
        var licenseexperience: String? = Global.singleton.retriveFromUserDefaults(key: kUser.licenseexperience)
         var licenseimage: String? = Global.singleton.retriveFromUserDefaults(key: kUser.license_image)
         var is_online: String? = Global.singleton.retriveFromUserDefaults(key: kUser.is_online)
        
    }
}
struct kStoryboard {
    static let main: String = "Main"
    static let Development: String = "Development"
    static let jigar: String = "Jigar"
    static let message: String = "Message"
}

struct kUser {
    static let IsLoggedIn: String! = "Pref_IsDriverLoggedIn"
    static let avatar = "Pref_avatar"
    static let email = "Pref_email"
    static let fullName = "Pref_fullName"
    static let session_id = "Pref_session_id"
    static let User_Id: String! = "Pref_User_Id"
    static let role = "Pref_role"
    static let is_public = "Pref_is_public"
    static let is_gif_displayed = "Pref_gif"
    
    
    static let app_version: String! = "Pref_app_version"
    static let subscription_period = "Pref_subscription_period"
    static let business_id = "Pref_business_id"
    static let first_name = "Pref_firstName"
    static let last_name = "Pref_lastName"
    static let license_number = "Pref_license_number"
    static let nickname = "Pref_nickname"
    static let phone = "Pref_phone"
    static let isParkingSuggested: String! = "Pref_isParkingSuggested"
    static let suggestion: String! = "Pref_suggestion"
    static let park_latitude: String! = "Pref_park_latitude"
    static let park_longitude: String! = "Pref_park_longitude"
    static let park_id: String! = "Pref_park_id"
    static let park_address: String! = "Pref_address"
    static let park_feedback: String! = "Pref_feedback"
    static let is_speaked: String! = "Pref_is_speaked"
    static let is_speaked_multiple: String! = "Pref_is_speaked_multiple"
    static let business_name: String! = "Pref_business_name"
    static let admin_email: String! = "Pref_admin_email"
    static let admin_phone: String! = "Pref_admin_phone"
    static let consider_nearest_gate_distance: String! = "Pref_consider_nearest_gate_distance"
    static let suggest_nearest_gate_distance: String! = "Pref_suggest_nearest_gate_distance"
    static let trackingStatus: String! = "Pref_trackingStatus"
    static let driver_type: String! = "driver_type"
    static let isGifDisplayed: String! = "isGifDisplayed"
    static let localNotification: String! = "localNotification"
    static let trip_history_month: String! = "trip_history_month"
    
    
    
    
    
    static let force_update: String! = "Pref_force_update"
    static let AccessToken: String! = "Pref_AccessToken"
    static let Completed_Steps: String = "Pref_Completed_Steps"
    static let subscription_amount: String = "Pref_subscription_amount"
    static let Approval_Status: String = "Pref_Approval_Status"
    static let home_Url = "Pref_home_url"
    
    static let lastname = "Pref_lastname"
    static let username = "Pref_username"
    static let brokerage_name = "Pref_brokerage_name"
    static let stripe_link_id = "Pref_stripe_customer_id"
    static let card_no = "Pref_card_no"
    static let is_verified = "Pref_is_verified"
    static let address = "Pref_address"
    static let miles = "Pref_miles"
    static let address_latitude = "Pref_address_latitude"
    static let address_longitude = "Pref_address_longitude"
    static let license_expiration = "Pref_license_expiration"
    static let license_image = "Pref_license_image"
    static let address_state = "Pref_address_state"
    static let rating = "Pref_rating"
    static let date_of_birth = "Pref_date_of_birth"
    static let bio = "Pref_bio"
    static let cover_image = "Pref_cover_image"
    static let car_image = "Pref_car_image"
    static let car_model = "Pref_car_model"
    static let category = "Pref_category"
    static let car_year = "Pref_car_year"
    static let car_milage = "Pref_car_milage"
    static let car_plate_number = "Pref_car_plate_number"
    static let car_vin_number = "Pref_car_vin_number"
    static let service_area = "Pref_service_area"
    static let about = "Pref_about"
    static let value = "Pref_value"
    static let experience = "Pref_experience"
    static let licenseexperience = "Pref_licenseexperience"
    static let knowledge = "Pref_knowledge"
    static let rl_number = "Pref_rl_number"
    static let rl_expirtation = "Pref_rl_expirtation"
    static let rl_active = "Pref_rl_active"
    static let home_sold = "Pref_home_sold"
    static let home_sold_12 = "Pref_home_sold_12"
    static let state_licensed = "Pref_state_licensed"
    static let is_online = "Pref_online"
    static let approved_ride_type = "Pref_approved_ride_type"
    static let online_ride_type = "Pref_online_ride_type"
    static let ssn = "Pref_ssn"
    static let gender = "Pref_gender"
    static let license_region = "Pref_license_region"
    static let license_city = "Pref_license_city"
    static let status = "Pref_status"
    static let is_approved = "Pref_is_approved"
    static let is_active = "Pref_is_active"
    static let accurate_bg_check_status = "Pref_accurate_bg_check_status"
    
    static let doc_exp_date = "Pref_doc_exp_date"
    
    static let driver_wallet = "Pref_driver_wallet"
    static let fund_transfer_charge = "Pref_fund_transfer_charge"
    static let ride_id = "Pref_ride_id"
    
    static let map_key = "Pref_map_key"
    static let mapbox_key = "Pref_mapbox_key"
    static let max_job_images = "Pref_max_job_images"
    static let request_session_id = "Pref_request_session_id"
    
    static let subscription_id = "Pref_subscription_id"
    static let licenseUrl = "Pref_licenseUrl"
}

enum kCurrency : String {
    case doller = "$"
    static let `default` = kCurrency.doller.rawValue
}
