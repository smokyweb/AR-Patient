//
//  APIConstant.swift
//  StructureApp
//
//  Copyright © 2017 Jigar Patel. All rights reserved.
//

import Foundation

fileprivate enum BuildForServer : String {
    case production
    case developer
    static let `default` = BuildForServer.developer
}

/// This is the Structure for API
struct APIConstant {
    static let headerKey = ""//ezpickup2019
    
    static var url : String {
        if BuildForServer.default == .developer {
            return  developement.APIURL
        }else {
            return live.APIURL
        }
    }
    
    private struct developement {
        static let baseUrl  = "http://arpatient.betaplanets.com"
        private static let appVersion   = "v9"
        static let APIURL  = "\(baseUrl)/wp-json/\(appVersion)"
    }
    
    private struct live {
        static let baseUrl  = "https://foodpon.betaplanets.com"
        private static let appVersion   = "v1"
        static let APIURL  = "\(baseUrl)/wp-json/\(appVersion)"
    }
    
    static var publicImgUrl : String {
        if BuildForServer.default == .developer {
            return  developement.baseUrl
        }else {
            return live.baseUrl
        }
    }

    static let mediaURL = "http://arpatient.betaplanets.com"
    
    
    /// Structure for URL. This will have the API end point for the server.
    // MARK: - Basic Response keys
    
    /// Structure for API Response Keys. This will use to get the data or anything based on the key from the repsonse. Do not directly use the key rather define here and use it.
    struct Response {
        
        /// API.Response.success
        static let success                                  = "success"
        
        /// API.Response.message
        static let message                                  = "message"
        
        /// API.Response.statusCode
        static let statusCode                               = "statusCode"
        
        /// API.Response.data
        static let data                                     = "data"
    }
    
    // MARK: - Success Failure keys
    
    /// Structure for API Response Success or Failure. This will use to check that if API has responded success or failure
    struct Check {
        /// API.Check.success
        static let success                                   = "1"
        
        /// API.Check.failure
        static let failure                                   = "0"
    }
    
    struct Request {
        static let signUp            = "/users/signup"
        static let login             = "/users/login"
        static let forgotPassword    = "/users/forgot_password"
        static let resetPassword     = "/users/reset_password"
        static let changePassword    = "/users/change_password"
        static let logout            = "/users/logout"
        static let delete            = "/users/delete"
        static let faqs_list         = "/faqs/list"
        static let newsfeeds_list    = "/newsfeeds/list"
        static let faqs_detail       = "/faqs/detail"
        static let newsfeeds_detail  = "/newsfeeds/detail"
        static let CMS               = "/init/cms"
        static let faqList           = "/faqs/list"
        static let newsList          = "/newsfeeds/list"
        static let patientList       = "/patients/types"
        static let contactUs         = "/users/contact_us"
        static let changeProfile     = "/users/change_profile"
        static let changeAvatar      = "/users/change_avatar"
        static let voiceExam         = "/tests/ve_definition"
        static let voiceExamSubmit   = "/tests/ve_submit"
        static let testDiagnosis     = "/tests/diagnosis"
        static let leaderboard       = "/tests/leaderboard"
        static let testResult        = "/tests/result"
        static let myProfile         = "/users/profile"
        static let practExam         = "/tests/pe_definition"
        static let practExamSubmit   = "/tests/pe_submit"

        
        
//        static let sync_master       = "/init/sync_master"
//        static let searchBusiness    = "/init/search_business"
//        static let setIdentity       = "/trucker/set_identity"
//        static let sync_user         = "/init/sync_user"
//        static let onlineStatus      = "/trucker/online_status"
//        static let tripCreate        = "/trip/create"
//        static let acceptTracking    = "/trip/accept_tracking"
//        static let deniedTracking    = "/trip/denied_tracking"
//        static let stopTracking      = "/trip/stop_tracking"
//        static let changeLocation    = "/trucker/change_location"
//        static let arrivedPickup     = "/trip/arrived_at_pickup"
//        static let pickup            = "/trip/pickup"
//        static let arrivedDropoff    = "/trip/arrived_at_dropoff"
//        static let dropoff           = "/trip/dropoff"
//        static let CMS               = "/init/cms"
//        static let contactUs         = "/users/contact_us"
//        static let myProfile         = "/users/myprofile"
//        static let editProfile       = "/trucker/edit_profile"
//        static let tripHistory       = "/trip/history"
//        static let tripDetail        = "/trip/detail"
//        static let notifications     = "/users/notifications"
//        static let blogsList         = "/blogs/list"
//        static let blogsView         = "/blogs/view"
//        static let searchTrucker     = "/business/search_trucker"
//        static let chatRoom          = "/chat/rooms"
//        static let sendChat          = "/chat/send"
//        static let chatConversation  = "/chat/conversation"
//        static let chatDeleteRoom    = "/chat/delete_room"
//        static let chatDeleteConversation    = "/chat/delete_conversation"
//        static let tripCancel        = "/trip/cancel"
//        static let deleteNotifications       = "/users/delete_notification"
//        static let deleteAllNotifications    = "/users/delete_all_notifications"
//        static let selectParking     = "/trip/select_parking"
    }
}
